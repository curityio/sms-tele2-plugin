package io.curity.identityserver.plugin.sms.tele2;

import io.curity.identityserver.plugin.sms.tele2.config.Tele2SmsPluginConfiguration;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.mockito.Mockito;

import java.util.Arrays;
import java.util.Collection;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;

@RunWith(Parameterized.class)
public class ExtractLinkTest
{
    private String testString;
    private String expected;

    public ExtractLinkTest(String testString, String expected)
    {
        this.testString = testString;
        this.expected = expected;
    }

    @Test
    public void parseMessage() throws Exception
    {

        Tele2SmsSender sender = new Tele2SmsSender(Mockito.mock(Tele2SmsPluginConfiguration.class));
        Assert.assertThat(expected, is(equalTo(Tele2SmsUtil.extractOtpOrLink(testString))));
    }

    @Parameterized.Parameters
    public static Collection<Object[]> data()
    {
        return Arrays.asList(new Object[][]{
                {"Click link: https://sms.curity.io?query=parameter", "https://sms.curity.io?query=parameter"},
                {"https://sms.curity.io/token link first", "https://sms.curity.io/token"},
                {"With port https://sms.curity.io:443/", "https://sms.curity.io:443/"},
                {"Message without link", "Message without link"},
                {"74945", "74945"}
        });
    }
}
