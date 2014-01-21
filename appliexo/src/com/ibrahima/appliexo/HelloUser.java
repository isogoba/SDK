/* Partie Gestion de l'interface utilisateur*/


package com.ibrahima.appliexo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


public class HelloUser extends Activity {
		
	final String EXTRA_Name = "name";
    final String EXTRA_firstName = "firstName";
    
	    @Override
	    public void onCreate(final Bundle savedInstanceState) 
	    {
	    	
	    	super.onCreate(savedInstanceState);
	    	setContentView(R.layout.activity_main_user);
	    	Intent intent = getIntent();
	        final TextView textname = (TextView) findViewById(R.id.textView2);
	        final TextView textfirstname = (TextView) findViewById(R.id.TextView02);
	        if (intent != null) {
	     	   textname.setText(intent.getStringExtra(EXTRA_Name));
	     	  textfirstname.setText(intent.getStringExtra(EXTRA_firstName));
	     	   
	        }
	        
        	final Button button = (Button) findViewById(R.id.button1);
            button.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                	Intent intent = new Intent(HelloUser.this, HelloGoogleMapActivity.class);
                	// transmission des infos user pour pouvoir les conserver en mémoire
                	intent.putExtra(EXTRA_Name, textname.getText().toString());
    				intent.putExtra(EXTRA_firstName, textfirstname.getText().toString());
            		startActivity(intent);
            		finish();
	    }    
});
	    }
	    }
	    








