package com.ttth.smsclassic;

import android.app.Dialog;
import android.app.FragmentTransaction;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.*;

import com.ttth.fragment.ReceivedFragment;
import com.ttth.fragment.SentFragment;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    private static final String TAG = "main activity";
    private static final String SENT = "SMS_SENT";
    private static final String DELIVERED = "SMS_DELIVERED";
    private ImageView imgCreateSMS;
    private Button btnReceived,btnSent,btnSendNew;
    private Dialog createSMS;
    private EditText edtNameSend,edtPurportNew;
    private ReceivedFragment receivedFragment;
    private SentFragment sentFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initDialog();
        initFragment();
    }

    private void initFragment() {
        receivedFragment = new ReceivedFragment();
        sentFragment = new SentFragment();
    }

    private void initDialog() {
        createSMS = new Dialog(this,android.R.style.Theme_DeviceDefault_Dialog_NoActionBar_MinWidth);
        LayoutInflater inflater = getLayoutInflater();
        View viewCreateSMS = inflater.inflate(R.layout.new_sms,null);
        edtNameSend = (EditText) viewCreateSMS.findViewById(R.id.edtNameSend);
        edtPurportNew = (EditText) viewCreateSMS.findViewById(R.id.edtPurportNew);
        btnSendNew = (Button) viewCreateSMS.findViewById(R.id.btnSendNew);
        createSMS.setContentView(viewCreateSMS);
        btnSendNew.setOnClickListener(this);

    }

    private void initView() {
        imgCreateSMS = (ImageView) this.findViewById(R.id.imgCreateSMS);
        btnReceived = (Button) this.findViewById(R.id.btnReceived);
        btnSent = (Button) this.findViewById(R.id.btnSent);
        imgCreateSMS.setOnClickListener(this);
        btnReceived.setOnClickListener(this);
        btnSent.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.imgCreateSMS:
                createSMS.show();
                break;
            case R.id.btnReceived:
                btnReceived.setTextColor(Color.BLACK);
                btnSent.setTextColor(Color.WHITE);
                btnSent.setBackgroundColor(Color.parseColor("#8BC34A"));
                btnReceived.setBackgroundResource(R.drawable.shape_btn);
                FragmentTransaction transactionReceived = getFragmentManager().beginTransaction();
                transactionReceived.replace(R.id.flFragment,receivedFragment);
                transactionReceived.commit();
                break;
            case R.id.btnSent:
                btnSent.setTextColor(Color.BLACK);
                btnReceived.setTextColor(Color.WHITE);
                btnReceived.setBackgroundColor(Color.parseColor("#8BC34A"));
                btnSent.setBackgroundResource(R.drawable.shape_btn);
                FragmentTransaction transactionSent = getFragmentManager().beginTransaction();
                transactionSent.replace(R.id.flFragment,sentFragment);
                transactionSent.commit();
                break;
            case R.id.btnSendNew:
                String phoneNumber = edtNameSend.getText().toString();
                String message = edtPurportNew.getText().toString();
                if (phoneNumber.isEmpty()||message.isEmpty()){
                    Toast.makeText(this,getResources().getString(R.string.empty),Toast.LENGTH_LONG).show();
                }else if (phoneNumber.isEmpty()){
                    Toast.makeText(this,getResources().getString(R.string.phone_empty),Toast.LENGTH_LONG).show();
                }else if (message.isEmpty()){
                    Toast.makeText(this,getResources().getString(R.string.message_empty),Toast.LENGTH_LONG).show();
                }else {
                    sendSMS(phoneNumber,message);
                    createSMS.dismiss();
                    edtNameSend.setText("");
                    edtPurportNew.setText("");
                }

                break;
        }
    }
    private void sendSMS(String phoneNumber,String message){

            PendingIntent sentPI = PendingIntent.getBroadcast(this,0,new Intent(SENT),0);
            PendingIntent deliveredPI = PendingIntent.getBroadcast(this,0,new Intent(DELIVERED),0);
            registerReceiver(new BroadcastReceiver() {
                @Override
                public void onReceive(Context arg0, Intent arg1) {
                    switch (getResultCode()){
                        case MainActivity.RESULT_OK:
                            Toast.makeText(getBaseContext(),getResources().getString(R.string.sent),Toast.LENGTH_LONG).show();
                            break;
                        case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
                            Toast.makeText(getBaseContext(),getResources().getString(R.string.sent_fail),Toast.LENGTH_LONG).show();
                            break;
                        case SmsManager.RESULT_ERROR_NO_SERVICE:
                            Toast.makeText(getBaseContext(),getResources().getString(R.string.no_device),Toast.LENGTH_LONG).show();
                            break;
                        case SmsManager.RESULT_ERROR_NULL_PDU:
                            Toast.makeText(getBaseContext(),getResources().getString(R.string.null_pdu),Toast.LENGTH_LONG).show();
                            break;
                        case SmsManager.RESULT_ERROR_RADIO_OFF:
                            Toast.makeText(getBaseContext(),getResources().getString(R.string.radio_off),Toast.LENGTH_LONG).show();
                            break;

                    }

                }
            },new IntentFilter(SENT));
            registerReceiver(new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    switch (getResultCode()){
                        case MainActivity.RESULT_OK:
                            Toast.makeText(getBaseContext(),getResources().getString(R.string.delivered),Toast.LENGTH_LONG).show();
                            break;
                        case MainActivity.RESULT_CANCELED:
                            Toast.makeText(getBaseContext(),getResources().getString(R.string.delivered_fail),Toast.LENGTH_LONG).show();
                            break;
                    }

                }
            }, new IntentFilter(DELIVERED));
            SmsManager sms = SmsManager.getDefault();
            sms.sendTextMessage(phoneNumber,null,message,sentPI,deliveredPI);
    }
}
