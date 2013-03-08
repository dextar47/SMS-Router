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

public class MainActivity extends Activity {

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
	
		public class SmsListener extends BroadcastReceiver{
		
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
		                        String msgBody = msgs[i].getMessageBody();
		                        Log.d("SMS received",msgBody);
		                    }
		                }catch(Exception e){
		//                            Log.d("Exception caught",e.getMessage());
		                }
		            }
		        }
		    }
		}
	LinearLayout mainLayout;
	ScrollView sc;
	EditText txtBox;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mainLayout = (LinearLayout) findViewById(R.id.mainLayout);
        txtBox = (EditText) findViewById(R.id.editText1);
        
    }

    public void postData(String url) {
    		
        // Create a new HttpClient and Post Header
        HttpClient httpclient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost(url);

        try {
            // Add your data
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
            nameValuePairs.add(new BasicNameValuePair("msg", txtBox.getText().toString()));
            //nameValuePairs.add(new BasicNameValuePair("stringdata", "Hi"));
            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

            // Execute HTTP Post Request
            HttpResponse response = httpclient.execute(httppost);
            //Log.d("Response",response.toString());
            Reader reader = new InputStreamReader(response.getEntity().getContent());
            StringBuffer sb = new StringBuffer();
            {
                int read;
                char[] cbuf = new char[1024];
                while ((read = reader.read(cbuf)) != -1)
                    sb.append(cbuf, 0, read);
            }

            Log.d("Data",sb.toString());

        } catch (ClientProtocolException e) {
            // TODO Auto-generated catch block
        		//Log.d("Data",e.getMessage());
        } catch (IOException e) {
            // TODO Auto-generated catch block
        		Log.d("Data",e.getMessage());
        }
    } 
    public void addLabel(View v) {
    		TextView tv = new TextView(this);
    		tv.setText(txtBox.getText().toString());
    		//Log.d("Hello","World");
    		mainLayout.addView(tv);
    		new SendDataToServer("http://192.168.0.103/mobile/index.php/home/addme").execute();
    		//Log.d("Hello","World");
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
}
