package com.example.clienttracker;
import java.io.IOException;
import java.net.Socket;
import android.location.LocationManager;
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;
/*---------------------------------------------------------------------------------------
--	Source File: ClientActivity.java - Main activity for the client mode.
--
--	Classes:	ClientActivity - public class
--				Socket        - java.net
--				Android.*    - android
--				
--				
--	Methods:	protected void onCreate(Bundle savedInstanceState)
--				public void startClient(View view)
--				public void stopClient(View view)
--				
--	Date:			March 1 2014
--
--	Revisions:		None
--					
--	Designers:		Ramzi Chennafi and Clark Allenby
--				
--	Programmer:		Ramzi Chennafi
--
--	Notes:
--	This class is called to instantiate the Client Tracker client.
--	
--  Intiates the activity window and the shared settings for the client. Hitting 
--  "Start" will activate the client, once a connection is established location coordinates
--  will be sent every 5 seconds (if the location has changed). "Stop" will close the client
--  connection. If the "Clear" button is pressed, the status box will be cleared.
---------------------------------------------------------------------------------------*/
public class ClientActivity extends Activity {

	public SharedPreferences sharedpreferences;
	public TextView status;
	public Socket clientSocket = null;
	public MyLocationListener listener;
	private EditText port;
	private EditText ipaddr;
	private LocationManager locationManager;
	/*------------------------------------------------------------------------------------------------------------------
	--      FUNCTION: onCreate
	--
	--      DATE: March 1 2014
	--      REVISIONS: none
	--
	--      PROGRAMMER: Ramzi Chennafi
	--
	--      INTERFACE: protected void onCreate(Bundle savedInstanceState)
	--					
	--
	--      RETURNS: void
	--
	--      NOTES:
	--      Establishes client settings, enables auto scrolling for the status box and links a listener to the clear button.
	----------------------------------------------------------------------------------------------------------------------*/
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_client);
	    
		sharedpreferences = getSharedPreferences(CtUtils.myPREFERENCES, Context.MODE_PRIVATE);
		Editor editor = sharedpreferences.edit();
		editor.putInt(CtUtils.clientPort, 3402);
		editor.putString(CtUtils.hostname, "127.0.0.1");
		editor.commit();
		
		status = (TextView) findViewById(R.id.cstatus_area);
		port = (EditText) findViewById(R.id.editPORT);
		ipaddr = (EditText) findViewById(R.id.editIP);
		final ScrollView scrollView1 = (ScrollView) findViewById(R.id.ScrollView01);
		
		port.setText(Integer.toString(sharedpreferences.getInt(CtUtils.clientPort, -1)));
		ipaddr.setText(sharedpreferences.getString(CtUtils.hostname, ""));
		
		locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
		
		CtUtils.enableAutoScroll(scrollView1, status);
		
		// sets the clear button listener, too small to be its own function
		Button buttonOne = (Button) findViewById(R.id.b_clientClear);
		buttonOne.setOnClickListener(new Button.OnClickListener() {
		    public void onClick(View v) {
		            status.setText("");
		    }
		});
	}
	/*------------------------------------------------------------------------------------------------------------------
	--      FUNCTION: startClient
	--
	--      DATE: March 1 2014
	--      REVISIONS: none
	--
	--      PROGRAMMER: Ramzi Chennafi
	--
	--      INTERFACE: public void startClient(View view)
	--					view - unused					
	--
	--      RETURNS: void
	--
	--      NOTES:
	--      Starts the client when the user presses the Start button. Sets the current settings and starts a ConnectThread.
	----------------------------------------------------------------------------------------------------------------------*/
	public void startClient(View view) 
	{
		int currentport = Integer.parseInt(port.getText().toString());
		Editor editor = sharedpreferences.edit();
		editor.putInt(CtUtils.clientPort, currentport);
		editor.putString(CtUtils.hostname, ipaddr.getText().toString());
		editor.commit();
		
		status.append("Client started.\n");
		Thread t = new ConnectThread(getApplicationContext(), this, locationManager);
		t.start();
	}
	/*------------------------------------------------------------------------------------------------------------------
	--      FUNCTION: startClient
	--
	--      DATE: March 1 2014
	--      REVISIONS: none
	--
	--      PROGRAMMER: Ramzi Chennafi
	--
	--      INTERFACE: public void startClient(View view)
	--					view - unused					
	--
	--      RETURNS: void
	--
	--      NOTES:
	--      Stops the client when the user presses the Stop button. Accomplishes this by closing the established client socket
	--		and disabling the MyLocationListener to prevent location updates. If no connection is established, returns an error message.
	----------------------------------------------------------------------------------------------------------------------*/
	public void stopClient(View view) 
	{
		if(clientSocket == null)
		{
			status.append("Client is not running.\n");
			return;
		}
		
		try 
		{
			clientSocket.getOutputStream().close();
			clientSocket.close();
		} 
		catch (IOException e) {
			status.append("Failed to close socket.\n");
			return;
		}	
		
		status.append("Connection Closed.\n");
		locationManager.removeUpdates(listener);
	}
}
