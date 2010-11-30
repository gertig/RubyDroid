package com.gertig.rubydroid;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.w3c.dom.CharacterData;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import android.app.ListActivity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

/** 
 * @author Andrew Gertig
 */
public class RubyMain extends ListActivity
{
	
	private ArrayList<myEvents> eventsArrayList = null;
	private EventsAdapter m_adapter;

    /** Called when the activity is first created. */
	  @Override
	  public void onCreate(Bundle savedInstanceState)
	  {
	    super.onCreate(savedInstanceState);
	    eventsArrayList = new ArrayList<myEvents>();
	    retreiveProjects();
	    
	    this.m_adapter = new EventsAdapter(this, R.layout.row, eventsArrayList);
        setListAdapter(this.m_adapter);     

	  }
	  
	  

private void retreiveProjects()
{
  HttpClient httpClient = new DefaultHttpClient();
  
  String xmlResponse;
  
  try
  {
    /** FOR LOCAL DEV String url = "http://192.168.0.186:3000/events?format=xml"; */
	String url = "http://cold-leaf-59.heroku.com/myevents?format=xml";
    Log.d( "gertigable", "performing get " + url );

    HttpGet method = new HttpGet( new URI(url) );
    HttpResponse response = httpClient.execute(method);
    if ( response != null )
    {
    	xmlResponse = getResponse(response.getEntity());
        //Log.i( "Gertig", "received " + xmlResponse);
    	eventsArrayList = parseXMLString(xmlResponse);
    }
    else
    {
      Log.i( "Gertig", "got a null response" );
    }
  } catch (IOException e) {
    Log.e( "Error", "IOException " + e.getMessage() );
  } catch (URISyntaxException e) {
    Log.e( "Error", "URISyntaxException " + e.getMessage() );
  }
  
}

private String getResponse( HttpEntity entity )
{
  String response = "";

  try
  {
    int length = ( int ) entity.getContentLength();
    StringBuffer sb = new StringBuffer( length );
    InputStreamReader isr = new InputStreamReader( entity.getContent(), "UTF-8" );
    char buff[] = new char[length];
    int cnt;
    while ( ( cnt = isr.read( buff, 0, length - 1 ) ) > 0 )
    {
      sb.append( buff, 0, cnt );
    }

      response = sb.toString();
      isr.close();
  } catch ( IOException ioe ) {
    ioe.printStackTrace();
  }

  return response;
}

/*
 * parseXMLString() by Jonathan Gertig
 * edited by Andrew Gertig
 */
private ArrayList<myEvents> parseXMLString(String xmlString) {
	
	
	try {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = dbf.newDocumentBuilder();
        InputSource is = new InputSource();
        is.setCharacterStream(new StringReader(xmlString));

        Document doc = db.parse(is);
        NodeList nodes = doc.getElementsByTagName("myevent");

        eventsArrayList = new ArrayList<myEvents>(); //Gertig
        	  
		//Log.e("Gertig","There are "+nodes.getLength()+" events in this list");
               
        //Iterate the events
        for (int i = 0; i < nodes.getLength(); i++) {
        	
           Element element = (Element) nodes.item(i);          
           eventsArrayList.add(new myEvents());
           
           NodeList eventIDNum = element.getElementsByTagName("id");
           Element line = (Element) eventIDNum.item(0);
           eventsArrayList.get(i).eventID = Integer.parseInt(getCharacterDataFromElement(line));	           		  

           NodeList eventName = element.getElementsByTagName("name");
           line = (Element) eventName.item(0);           
           eventsArrayList.get(i).name = getCharacterDataFromElement(line).trim();
           
           
           NodeList eventBudget = element.getElementsByTagName("budget");
           line = (Element) eventBudget.item(0);
           eventsArrayList.get(i).budget = Double.parseDouble(getCharacterDataFromElement(line)); 
           
        }
        
    }
    catch (Exception e) {
        e.printStackTrace();
    }
    
    return eventsArrayList;   
	
}

public static String getCharacterDataFromElement(Element e) {
    Node child = e.getFirstChild();
    if (child instanceof CharacterData) {
       CharacterData cd = (CharacterData) child;
       return cd.getData();
    }
    return "?"; //ListActivity will display a ? if a null value is passed to the Rails server
  }

private class EventsAdapter extends ArrayAdapter<myEvents>{

    private ArrayList<myEvents> items;

    public EventsAdapter(Context context, int textViewResourceId, ArrayList<myEvents> items) {
            super(context, textViewResourceId, items);
            this.items = items;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
            View v = convertView;
            if (v == null) {
                LayoutInflater vi = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                v = vi.inflate(R.layout.row, null);
            }
            myEvents o = items.get(position);
            if (o != null) {
                    TextView tt = (TextView) v.findViewById(R.id.toptext);
                    TextView mt = (TextView) v.findViewById(R.id.middletext);
                    TextView bt = (TextView) v.findViewById(R.id.bottomtext);
                    
                    if (tt != null) {
                          tt.setText("Name: "+ o.getEventName());                            
                    }
                    if(mt != null){
                        mt.setText("Budget: "+ o.getEventBudget());
                    }
                    if(bt != null){
                          bt.setText("ID: "+ o.getEventID());
                    }
                    
            }
            return v;
    }
}



}