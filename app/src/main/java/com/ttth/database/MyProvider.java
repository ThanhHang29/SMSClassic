package com.ttth.database;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;
import com.ttth.model.SMS;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;


/**
 * Created by Thanh Hang on 4/23/2016.
 */
public class MyProvider {
    private static final String ID = "_id";
    private static final String ADDRESS = "address";
    private static final String BODY = "body";
    private static final String DATE = "date";
    private Context mContext;
    private ContentResolver mResolver;
    private ArrayList<SMS> arrReceived,arrSent;
    public MyProvider(Context mContext) {
        this.mContext = mContext;
        mResolver = mContext.getContentResolver();
    }
    public ArrayList<SMS> getDataReceived(){
        arrReceived = new ArrayList<SMS>();
        Uri inboxUri = Uri.parse("content://sms/inbox");
        Cursor cursor = mResolver.query(inboxUri,null,null,null,null);
        cursor.moveToFirst();
        int indextID = cursor.getColumnIndexOrThrow(ID);
        int indextName = cursor.getColumnIndexOrThrow(ADDRESS);
        int indextPurport = cursor.getColumnIndexOrThrow(BODY);
        int indextDate = cursor.getColumnIndexOrThrow(DATE);
        while (!cursor.isAfterLast()){
//            for (int i = 0;i < cursor.getColumnCount() - 1;i++){
//                String id = cursor.getColumnName(i);
//                String purport = cursor.getString(i);
//                Log.e("provider","-----------------"+id+"\n"+purport);
//
//            }
            int id = cursor.getInt(indextID);
            String name = cursor.getString(indextName);
            String purport = cursor.getString(indextPurport);
            String date = cursor.getString(indextDate);
//            Date date = new Date( cursor.getLong(indextDate));
//            String formatDate = new SimpleDateFormat("dd/MM/yyyy").format(date);
            Long timestamp = Long.parseLong(date);
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(timestamp);
            Date finaldate = calendar.getTime();
            String smsDate = finaldate.toString();
            Log.e("provider","-----------------"+id+"\n"+name+"\n"+purport+"\n"+smsDate);
            SMS smsReceived = new SMS(id,name,purport,smsDate,"","");
            arrReceived.add(smsReceived);
            cursor.moveToNext();
        }
        return arrReceived;
    }
    public ArrayList<SMS> getDataSent(){
        arrSent = new ArrayList<SMS>();
        Uri sentUri = Uri.parse("content://sms/sent");
        Cursor cursor = mResolver.query(sentUri,null,null,null,null);
        cursor.moveToFirst();
        int indextID = cursor.getColumnIndexOrThrow(ID);
        int indextName = cursor.getColumnIndexOrThrow(ADDRESS);
        int indextPurport = cursor.getColumnIndexOrThrow(BODY);
        int indextDate = cursor.getColumnIndexOrThrow(DATE);
//        Long timestamp = Long.parseLong(indextDate);
        while (!cursor.isAfterLast()){
            for (int i = 0; i < cursor.getColumnCount() -1;i++){
                String id = cursor.getColumnName(i);
                String purport = cursor.getString(i);
                Log.e("provider","-----------------"+id+"\n"+purport);
            }
            int id = cursor.getInt(indextID);
            String name = cursor.getString(indextName);
            String purport = cursor.getString(indextPurport);
            String date = cursor.getString(indextDate);
//            Date date = new Date( cursor.getLong(indextDate));
//            String formatDate = new SimpleDateFormat("dd/MM/yyyy").format(date);
            Long timestamp = Long.parseLong(date);
            Log.e("provider","==============="+timestamp);
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(timestamp);
            Date finaldate = calendar.getTime();
            Log.e("provider","==============="+finaldate);
            String smsDate = finaldate.toString();
            Log.e("provider","-----------------"+id+"\n"+name+"\n"+purport+"\n"+smsDate);
            SMS smsSent = new SMS(id,name,purport,smsDate,"","");
            arrSent.add(smsSent);
            cursor.moveToNext();
        }
        return arrSent;
    }
}
