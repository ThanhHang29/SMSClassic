package com.ttth.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;

import com.ttth.smsclassic.MainActivity;

/**
 * Created by Thanh Hang on 4/26/2016.
 */
public class MyReceiver extends BroadcastReceiver {
    private static final String ACTION_RECEIVED_SMS="android.provider.Telephony.SMS_RECEIVED";
    private static final String MESSAGE_KEY = "pdus";

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (action.equals(ACTION_RECEIVED_SMS)){
            Bundle bundle = intent.getExtras();
            SmsMessage[] smsReceived = null;
            String smsAddress;
            if (bundle!=null){
                    try {
                        Object[] message = (Object[]) bundle.get(MESSAGE_KEY);
                        smsReceived = new SmsMessage[message.length];
                        for (int i = 0; i< smsReceived.length ; i++){
                            smsReceived[i] = SmsMessage.createFromPdu((byte[]) message[i]);
                            smsAddress = smsReceived[i].getOriginatingAddress();
                            String smsBody = smsReceived[i].getMessageBody().toString();
                            Log.e("Broadcast","==========="+smsAddress+"\n"+smsBody);
                            Intent intentMain = new Intent(context, MainActivity.class);
                            intentMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            context.startActivity(intentMain);
                            Intent broadcastIntent = new Intent();
                            broadcastIntent.setAction("ACTION_RECIEVED_SMS");
                            broadcastIntent.putExtra("sms",smsAddress);
                            context.sendBroadcast(broadcastIntent);
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                }
            }
        }
    }
}
