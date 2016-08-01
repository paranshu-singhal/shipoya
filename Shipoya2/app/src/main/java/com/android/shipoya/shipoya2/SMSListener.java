package com.android.shipoya.shipoya2;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.provider.Telephony;
import android.telephony.SmsMessage;
import android.util.Log;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SMSListener extends BroadcastReceiver {

    private static final String logTag = "logTag";
    public OtpReceive mCallback;

    public SMSListener(Context ctx) {
        this.mCallback = (OtpReceive)ctx;
    }

    @Override
    public void onReceive(Context context, Intent intent) {

        if (Telephony.Sms.Intents.SMS_RECEIVED_ACTION.equals(intent.getAction())) {
            for (SmsMessage smsMessage : Telephony.Sms.Intents.getMessagesFromIntent(intent)) {
                String messageBody = smsMessage.getMessageBody();
                String sender = smsMessage.getOriginatingAddress();

                if (sender.contains("SHPOYA")) {
                    Pattern pt = Pattern.compile(StaticData.otp_regex);
                    Matcher mch = pt.matcher(messageBody);

                    if (mch.find()) {
                        String otp = mch.toMatchResult().group(0);
                        mCallback.onOtpReceive(otp);
                        Log.d(logTag, otp);
                    }
                }
            }
        }
    }

    public interface OtpReceive {
        void onOtpReceive(String otp);
    }
}
