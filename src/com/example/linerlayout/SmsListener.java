package com.example.linerlayout;


import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.telephony.SmsMessage;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;


		public class SmsListener extends BroadcastReceiver{
		
			String msgBody;
			public class SendDataToServer extends AsyncTask<Void, Void, Void> {
				
				String url;
				
				public SendDataToServer(String url2) {
					this.url = url2;
					
				}
				 @Override
				 protected void onPostExecute(Void result) {
				  // TODO Auto-generated method stub
				  //displayRss();

				        
				 }

				 @Override
				 protected void onPreExecute() {
				  // TODO Auto-generated method stub
				  //preReadRss();
				 }

				 @Override
				 protected void onProgressUpdate(Void... values) {
				  // TODO Auto-generated method stub
				  //super.onProgressUpdate(values);
				 }

				 @Override
				 protected Void doInBackground(Void... arg0) {
				  // TODO Auto-generated method stub
					 
					 postData(this.url);
					 
					 return null;
				 }

				}
			public void postData(String url) {
	    		
		        // Create a new HttpClient and Post Header
		        HttpClient httpclient = new DefaultHttpClient();
		        HttpPost httppost = new HttpPost(url);

		        try {
		            // Add your data
		            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
		            nameValuePairs.add(new BasicNameValuePair("msg", this.msgBody));
		            //nameValuePairs.add(new BasicNameValuePair("stringdata", "Hi"));
		            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
		            
		            // Execute HTTP Post Request
		            HttpResponse response = httpclient.execute(httppost);
		            Log.d("data","got it");
		        } catch (ClientProtocolException e) {
		            // TODO Auto-generated catch block
		        		Log.d("Data",e.getMessage());
		        } catch (IOException e) {
		            // TODO Auto-generated catch block
		        		Log.d("Data",e.getMessage());
		        }
		    }
		    private SharedPreferences preferences;
		
		    @Override
		    public void onReceive(Context context, Intent intent) {
		        // TODO Auto-generated method stub
		
		        if(intent.getAction().equals("android.provider.Telephony.SMS_RECEIVED")){
		            Bundle bundle = intent.getExtras();           //---get the SMS message passed in---
		            SmsMessage[] msgs = null;
		            String msg_from;
		            if (bundle != null){
		                //---retrieve the SMS message received---
		                try{
		                    Object[] pdus = (Object[]) bundle.get("pdus");
		                    msgs = new SmsMessage[pdus.length];
		                    for(int i=0; i<msgs.length; i++){
		                        msgs[i] = SmsMessage.createFromPdu((byte[])pdus[i]);
		                        msg_from = msgs[i].getOriginatingAddress();
		                        String msgBody2 = msgs[i].getMessageBody();
		                        this.msgBody = msgBody2;
		                        Log.d("MSG",msgBody2);
		                        new SendDataToServer("http://192.168.0.103/mobile/index.php/home/addme").execute();
		                        //Log.d("SMS received",msgBody);
		                    }
		                }catch(Exception e){
		//                            Log.d("Exception caught",e.getMessage());
		                }
		            }
		        }
		    }
		}