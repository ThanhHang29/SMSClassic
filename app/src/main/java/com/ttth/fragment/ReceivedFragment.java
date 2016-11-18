package com.ttth.fragment;

import android.app.Dialog;
import android.app.Fragment;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.telephony.SmsManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.ttth.adapter.ReceivedAdapeter;
import com.ttth.database.MyProvider;
import com.ttth.model.SMS;
import com.ttth.smsclassic.MainActivity;
import com.ttth.smsclassic.R;

import java.util.ArrayList;

/**
 * Created by Thanh Hang on 4/23/2016.
 */
public class ReceivedFragment extends Fragment implements AdapterView.OnItemClickListener,View.OnClickListener{
    private static final String KEY_NAME = "key name";
    private static final String SENT = "SMS_SENT";
    private static final String DELIVERED = "SMS_DELIVERED";
    private ArrayList<SMS>arrReceived;
    private ListView lvListReceived;
    private ReceivedAdapeter receivedAdapeter;
    private Dialog receivedDialog,replyDialog;
    private TextView tvNameDetail,tvDateDetail,tvTimeDetail,tvPurportDetail,tvNameReply;
    private EditText edtPurportReply;
    private Button btnReply,btnSendReply;
    private MyProvider mProvider;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.received_fragment,container,false);
        arrReceived = new ArrayList<SMS>();
        mProvider = new MyProvider(getActivity());
        arrReceived.addAll(mProvider.getDataReceived());
        lvListReceived = (ListView) view.findViewById(R.id.lvListReceived);

        receivedDialog = new Dialog(getActivity(),android.R.style.Theme_DeviceDefault_Dialog_NoActionBar_MinWidth);
        View viewReceived = inflater.inflate(R.layout.sms_detail,null);
        tvNameDetail = (TextView) viewReceived.findViewById(R.id.tvNameDetail);
        tvDateDetail = (TextView) viewReceived.findViewById(R.id.tvDateDetail);
        tvTimeDetail = (TextView) viewReceived.findViewById(R.id.tvTimeDetail);
        tvPurportDetail = (TextView) viewReceived.findViewById(R.id.tvPurportDetail);
        btnReply = (Button) viewReceived.findViewById(R.id.btnReply);
        btnReply.setOnClickListener(this);
        receivedDialog.setContentView(viewReceived);

        replyDialog = new Dialog(getActivity(),android.R.style.Theme_DeviceDefault_Dialog_NoActionBar_MinWidth);
        View viewReply = inflater.inflate(R.layout.reply_sms,null);
        tvNameReply = (TextView) viewReply.findViewById(R.id.tvNameReply);
        edtPurportReply = (EditText) viewReply.findViewById(R.id.edtPurportReply);
        btnSendReply = (Button) viewReply.findViewById(R.id.btnSendReply);
        btnSendReply.setOnClickListener(this);
        replyDialog.setContentView(viewReply);
        initVew();
        return view;
    }

    private void initVew() {
        receivedAdapeter = new ReceivedAdapeter(getActivity(),android.R.layout.simple_list_item_1,arrReceived);
        lvListReceived.setAdapter(receivedAdapeter);
        lvListReceived.setOnItemClickListener(this);
    }


    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        tvNameDetail.setText(arrReceived.get(position).getName());
        tvDateDetail.setText(arrReceived.get(position).getDate());
        tvTimeDetail.setText(arrReceived.get(position).getTime());
        tvPurportDetail.setText(arrReceived.get(position).getPurport());
        receivedDialog.show();
        tvNameReply.setText(arrReceived.get(position).getName());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnReply:
                replyDialog.show();
                receivedDialog.dismiss();
                break;
            case R.id.btnSendReply:
                String phoneNumber = tvNameReply.getText().toString();
                String message = edtPurportReply.getText().toString();
                if (message.isEmpty()){
                    Toast.makeText(getActivity(),getResources().getString(R.string.message_empty),Toast.LENGTH_LONG).show();
                }else {
                    sendSMS(phoneNumber,message);
                    replyDialog.dismiss();
                    edtPurportReply.setText("");
                }
                break;
        }
    }
    private void sendSMS(String phoneNumber,String message){
            PendingIntent sentPI = PendingIntent.getBroadcast(getActivity(),0,new Intent(SENT),0);
            getActivity().registerReceiver(new BroadcastReceiver() {
                @Override
                public void onReceive(Context arg0, Intent arg1) {
                    switch (getResultCode()){
                        case MainActivity.RESULT_OK:
                            Toast.makeText(getActivity().getBaseContext(),getResources().getString(R.string.sent),Toast.LENGTH_LONG).show();
                            break;
                        case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
                            Toast.makeText(getActivity().getBaseContext(),getResources().getString(R.string.sent_fail),Toast.LENGTH_LONG).show();
                            break;
                        case SmsManager.RESULT_ERROR_NO_SERVICE:
                            Toast.makeText(getActivity().getBaseContext(),getResources().getString(R.string.no_device),Toast.LENGTH_LONG).show();
                            break;
                        case SmsManager.RESULT_ERROR_NULL_PDU:
                            Toast.makeText(getActivity().getBaseContext(),getResources().getString(R.string.null_pdu),Toast.LENGTH_LONG).show();
                            break;
                        case SmsManager.RESULT_ERROR_RADIO_OFF:
                            Toast.makeText(getActivity().getBaseContext(),getResources().getString(R.string.radio_off),Toast.LENGTH_LONG).show();
                            break;

                    }

                }
            }, new IntentFilter(SENT));
            SmsManager sms = SmsManager.getDefault();
            sms.sendTextMessage(phoneNumber,null,message,sentPI,null);
    }
}
