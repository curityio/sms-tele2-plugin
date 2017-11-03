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

package io.curity.identityserver.plugin.sms.tele2.config;

import se.curity.identityserver.sdk.config.Configuration;
import se.curity.identityserver.sdk.config.annotation.DefaultBoolean;
import se.curity.identityserver.sdk.config.annotation.DefaultString;
import se.curity.identityserver.sdk.config.annotation.Description;
import se.curity.identityserver.sdk.config.annotation.PatternConstraint;
import se.curity.identityserver.sdk.config.annotation.SizeConstraint;
import se.curity.identityserver.sdk.service.ExceptionFactory;
import se.curity.identityserver.sdk.service.WebServiceClient;


public interface Tele2SmsPluginConfiguration extends Configuration
{
    @Description("The username at the tele2 service")
    String getUserName();

    @Description("The password at the tele2 service")
    String getPassword();

    @Description("The message to send. {} will be replaced with the otp or link.")
    @DefaultString("{}")
    String getMessageTemplate();

    ExceptionFactory getExceptionFactory();

    WebServiceClient getWebServiceClient();

    @Description("Alpha numeric string to use as sender. Example: 46701234567 or MySender")
    @SizeConstraint(min = 1, max = 11)
    @PatternConstraint("[a-zA-Z0-9]*")
    String getFromNumber();

    @Description("If enabled, the sms sent will be of flash type")
    @DefaultBoolean(false)
    boolean isSendFlashSms();
}
