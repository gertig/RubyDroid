package com.gertig.rubydroid;


import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.HapticFeedbackConstants;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.Toast;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author Andrew Gertig
 */
public class AddEventView extends Activity implements OnClickListener{
	
	private EditText eventNameView; 
	private EditText eventBudgetView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_event_layout);
        
        findViews();
        setClickListeners();

    }
    
	/** Get a handle to all user interface elements */
	private void findViews() {
		eventNameView = (EditText) findViewById(R.id.edit_name_of_event);
		eventBudgetView = (EditText) findViewById(R.id.edit_budget_of_event);
	}
    
    public void setClickListeners() {
    	//BUTTON LISTENERS
        View addEventButton = findViewById(R.id.add_event_button);
        addEventButton.setOnClickListener(this);
    }

	@Override
	public void onClick(View v) {
		//Log.e("onClick","ClickListener is working");			
		switch (v.getId()) {
		
    	case R.id.add_event_button:   		
    		v.performHapticFeedback(HapticFeedbackConstants.FLAG_IGNORE_VIEW_SETTING); //Haptic feedback is cool
    		//Once the text boxes have been filled in we can post the data to the Rails server
    		postEvents();  
    		//Toast.makeText(AddEventView.this, "Clicked A Button!", Toast.LENGTH_SHORT).show();
    		break;
		}
		
	}
	
	private void postEvents()
	{
		DefaultHttpClient client = new DefaultHttpClient();
		
		/** FOR LOCAL DEV   HttpPost post = new HttpPost("http://192.168.0.186:3000/events"); //works with and without "/create" on the end */
		HttpPost post = new HttpPost("http://cold-leaf-59.heroku.com/myevents");
	    JSONObject holder = new JSONObject();
	    JSONObject eventObj = new JSONObject();
	    
	    Double budgetVal = 99.9;
	    budgetVal = Double.parseDouble(eventBudgetView.getText().toString());
	    
	    try {	
	    	eventObj.put("budget", budgetVal);
		    eventObj.put("name", eventNameView.getText().toString());
		    
		    holder.put("myevent", eventObj);
		    
		    Log.e("Event JSON", "Event JSON = "+ holder.toString());
		    
	    	StringEntity se = new StringEntity(holder.toString());
	    	post.setEntity(se);
	    	post.setHeader("Content-Type","application/json");
	 	
	    	
	    } catch (UnsupportedEncodingException e) {
	    	Log.e("Error",""+e);
	        e.printStackTrace();
	    } catch (JSONException js) {
	    	js.printStackTrace();
	    }

	    HttpResponse response = null;
	    
	    try {
	        response = client.execute(post);
	    } catch (ClientProtocolException e) {
	        e.printStackTrace();
	        Log.e("ClientProtocol",""+e);
	    } catch (IOException e) {
	        e.printStackTrace();
	        Log.e("IO",""+e);
	    }

	    HttpEntity entity = response.getEntity();
	    
	    if (entity != null) {
	        try {
	            entity.consumeContent();
	        } catch (IOException e) {
	        	Log.e("IO E",""+e);
	            e.printStackTrace();
	        }
	    }

	    Toast.makeText(this, "Your post was successfully uploaded", Toast.LENGTH_LONG).show();
	  
	}
   
}
