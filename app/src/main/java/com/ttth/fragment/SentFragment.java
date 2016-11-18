package com.ttth.fragment;

import android.app.Dialog;
import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import com.ttth.adapter.SentAdapter;
import com.ttth.database.MyProvider;
import com.ttth.model.SMS;
import com.ttth.smsclassic.R;

import java.util.ArrayList;

/**
 * Created by Thanh Hang on 4/23/2016.
 */
public class SentFragment extends Fragment implements AdapterView.OnItemClickListener{
    private ArrayList<SMS>arrSent;
    private ListView lvListSent;
    private SentAdapter sentAdapter;
    private Dialog sentDialog;
    private TextView tvNameDetail,tvDateDetail,tvTimeDetail,tvPurportDetail;
    private Button btnReply;
    private MyProvider mProvider;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.sent_fragment,container,false);
        arrSent = new ArrayList<SMS>();
        mProvider = new MyProvider(getActivity());
        arrSent.addAll(mProvider.getDataSent());
        lvListSent = (ListView) view.findViewById(R.id.lvListSent);

        sentDialog = new Dialog(getActivity(),android.R.style.Theme_DeviceDefault_Dialog_NoActionBar_MinWidth);
        View viewSent = inflater.inflate(R.layout.sms_detail,null);
        tvNameDetail = (TextView) viewSent.findViewById(R.id.tvNameDetail);
        tvDateDetail = (TextView) viewSent.findViewById(R.id.tvDateDetail);
        tvTimeDetail = (TextView) viewSent.findViewById(R.id.tvTimeDetail);
        tvPurportDetail = (TextView) viewSent.findViewById(R.id.tvPurportDetail);
        btnReply = (Button) viewSent.findViewById(R.id.btnReply);
        btnReply.setVisibility(View.INVISIBLE);
        sentDialog.setContentView(viewSent);
        initView();
        return view;
    }

    private void initView() {
        sentAdapter = new SentAdapter(getActivity(),android.R.layout.simple_list_item_1,arrSent);
        lvListSent.setAdapter(sentAdapter);
        lvListSent.setOnItemClickListener(this);
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
        tvNameDetail.setText(arrSent.get(position).getName());
        tvDateDetail.setText(arrSent.get(position).getDate());
        tvTimeDetail.setText(arrSent.get(position).getTime());
        tvPurportDetail.setText(arrSent.get(position).getPurport());
        sentDialog.show();
    }
}
