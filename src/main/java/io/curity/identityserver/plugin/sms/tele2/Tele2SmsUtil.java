package io.curity.identityserver.plugin.sms.tele2;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Tele2SmsUtil
{
    private static final Pattern URL_PATTERN = Pattern.compile("https?:[^\\s]*");

    /**
     * Helper method to parse the link from the message.
     * <p>
     * The caller gives a message containing either a OTP, or a messages containing a link to click on.
     * Parse the important parts from the messag, to be able to create a personalized message.
     *
     * @param message to extract otp or link from
     * @return If the message contains a http link, only the link will be returned.
     * Otherwise this will most likely be a OTP, so return the full message
     */
    static String extractOtpOrLink(String message)
    {
        Matcher matcher = URL_PATTERN.matcher(message);
        if (matcher.find())
        {
            return matcher.group();
        }
        else
        {
            return message;
        }
    }
}
