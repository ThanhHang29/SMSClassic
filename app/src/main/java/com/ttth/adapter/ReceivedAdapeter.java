package com.ttth.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import com.ttth.model.SMS;
import com.ttth.smsclassic.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Thanh Hang on 4/22/2016.
 */
public class ReceivedAdapeter extends ArrayAdapter<SMS> {
    private LayoutInflater inflater;
    private ArrayList<SMS>arrReceived;
    public ReceivedAdapeter(Context context, int resource, ArrayList<SMS> data) {
        super(context, resource, data);
        inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        this.arrReceived = data;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        ViewHolder viewHolder;
        if (view==null){
            view = inflater.inflate(R.layout.item_sms,parent,false);
            TextView tvName = (TextView) view.findViewById(R.id.tvName);
            TextView tvPurport = (TextView) view.findViewById(R.id.tvPurport);
            TextView tvDate = (TextView) view.findViewById(R.id.tvDate);
            TextView tvTime = (TextView) view.findViewById(R.id.tvTime);
            viewHolder = new ViewHolder();
            viewHolder.tvName = tvName;
            viewHolder.tvPurport = tvPurport;
            viewHolder.tvDate = tvDate;
            viewHolder.tvTime = tvTime;
            view.setTag(viewHolder);
        }
        viewHolder = (ViewHolder) view.getTag();
        viewHolder.tvName.setText(arrReceived.get(position).getName());
        viewHolder.tvPurport.setText(arrReceived.get(position).getPurport());
        viewHolder.tvDate.setText(arrReceived.get(position).getDate());
        viewHolder.tvTime.setText(arrReceived.get(position).getTime());
        return view;
    }
    class ViewHolder {
        private TextView tvName,tvPurport,tvDate,tvTime;
    }
}
