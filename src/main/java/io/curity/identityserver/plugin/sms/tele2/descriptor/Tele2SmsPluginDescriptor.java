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

package io.curity.identityserver.plugin.sms.tele2.descriptor;

import io.curity.identityserver.plugin.sms.tele2.Tele2SmsSender;
import io.curity.identityserver.plugin.sms.tele2.config.Tele2SmsPluginConfiguration;
import se.curity.identityserver.sdk.config.Configuration;
import se.curity.identityserver.sdk.plugin.descriptor.SmsPluginDescriptor;
import se.curity.identityserver.sdk.service.sms.SmsSender;

public class Tele2SmsPluginDescriptor implements SmsPluginDescriptor
{
    @Override
    public String getPluginImplementationType()
    {
        return "tele2";
    }

    @Override
    public Class<? extends Configuration> getConfigurationType()
    {
        return Tele2SmsPluginConfiguration.class;
    }

    @Override
    public Class<? extends SmsSender> getSmsSenderType()
    {
        return Tele2SmsSender.class;
    }
}
