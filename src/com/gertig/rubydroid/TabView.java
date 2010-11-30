package com.gertig.rubydroid;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.widget.TabHost;

/**
 * @author Andrew Gertig
 */
public class TabView extends TabActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE); 

        final TabHost tabHost = getTabHost();

        tabHost.addTab(tabHost.newTabSpec("tab1")
                .setIndicator("Events")
                .setContent(new Intent(this, RubyMain.class)));

        tabHost.addTab(tabHost.newTabSpec("tab2")
                .setIndicator("Add Event")
                .setContent(new Intent(this, AddEventView.class)));
        
        // This tab sets the intent flag so that it is recreated each time
        // the tab is clicked.
        /*
        tabHost.addTab(tabHost.newTabSpec("tab3")
                .setIndicator("destroy")
                .setContent(new Intent(this, Controls2.class)
                        .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)));
        */ 
                 
    }
}
