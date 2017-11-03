/*
 *  Copyright 2017 Curity AB
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package io.curity.identityserver.plugin.sms.tele2;

import io.curity.identityserver.plugin.sms.tele2.config.Tele2SmsPluginConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import se.curity.identityserver.sdk.errors.ErrorCode;
import se.curity.identityserver.sdk.http.HttpResponse;
import se.curity.identityserver.sdk.service.ExceptionFactory;
import se.curity.identityserver.sdk.service.HttpClient.HttpClientException;
import se.curity.identityserver.sdk.service.WebServiceClient;
import se.curity.identityserver.sdk.service.sms.SmsSender;

import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class Tele2SmsSender implements SmsSender
{
    private static Logger _logger = LoggerFactory.getLogger(Tele2SmsSender.class);
    private final ExceptionFactory _exceptionFactory;
    private final WebServiceClient _webServiceClient;
    private final Tele2SmsPluginConfiguration _configuration;

    private static final String unexpectedErrorMessage = "An unexpected error has occurred";

    public Tele2SmsSender(Tele2SmsPluginConfiguration configuration)
    {
        _exceptionFactory = configuration.getExceptionFactory();
        _webServiceClient = configuration.getWebServiceClient();
        _configuration = configuration;
    }

    @Override
    public boolean sendSms(String toNumber, String msg)
    {
        _logger.debug("Sending SMS to number = {} using tele2 provider", toNumber);

        Map<String, Collection<String>> message = getQueryParametersMap(toNumber, msg);

        return executeRequest(message);
    }

    private Map<String, Collection<String>> getQueryParametersMap(String toNumber, String incomingMessage)
    {
        Map<String, Collection<String>> queryParameters = new HashMap<>();
        queryParameters.put("to", Collections.singleton(toNumber));
        String message = replaceMessage(incomingMessage);
        queryParameters.put("message", Collections.singleton(message));
        queryParameters.put("username", Collections.singleton(_configuration.getUserName()));
        queryParameters.put("password", Collections.singleton(_configuration.getPassword()));
        queryParameters.put("from", Collections.singleton(_configuration.getFromNumber()));
        queryParameters.put("charset", Collections.singleton(StandardCharsets.UTF_8.displayName()));
        if (_configuration.isSendFlashSms())
        {
            queryParameters.put("type", Collections.singleton("flash"));
        }

        return queryParameters;
    }

    private String replaceMessage(String incomingMessage)
    {
        String otpOrLink = Tele2SmsUtil.extractOtpOrLink(incomingMessage);
        String replacedMessage = _configuration.getMessageTemplate().replace("{}", otpOrLink);
        _logger.trace("Replaced message: {}", replacedMessage);
        return replacedMessage;
    }

    private boolean executeRequest(Map<String, Collection<String>> message)
    {
        try
        {
            HttpResponse response = _webServiceClient.withQueries(message)
                    .request()
                    .get()
                    .response();

            int httpStatusCode = response.statusCode();
            String responseBody = response.body(HttpResponse.asString());

            switch (httpStatusCode)
            {
                case 202:
                    _logger.debug("Sms was successfully sent. Message: {}", responseBody);
                    return true;
                case 403:
                    _logger.info("Failed to send sms. Invalid credentials. Message: {}", responseBody);
                    break;
                case 409:
                    _logger.warn("Failed to send sms. The Tele2 rate limit has been reached. Message: {}", responseBody);
                    break;
                default:
                    _logger.debug("Failed to send sms. Got error from Tele2 service. Code: {}, Message: {}",
                            httpStatusCode, responseBody);
                    break;
            }

            throw _exceptionFactory.internalServerException(ErrorCode.EXTERNAL_SERVICE_ERROR);
        }
        catch (HttpClientException e)
        {
            _logger.warn("Error when communicating with tele2 backend: {}", e.getMessage());
            throw _exceptionFactory.internalServerException(
                    ErrorCode.EXTERNAL_SERVICE_ERROR,
                    unexpectedErrorMessage);

        }
    }
}
