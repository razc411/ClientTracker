package com.example.clienttracker;
import java.io.IOException;
import java.net.ServerSocket;

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
--	Source File: ServerActivity.java - Main activity for the server mode.
--
--	Classes:	ServerSocket - java.net
--				android.*   
--				
--				
--	Methods:	protected void onCreate(Bundle savedInstanceState)  
--				public void startServer(View view)
--				public void stopServer(View view)  
--				
--	Date:			March 1 2014
--
--	Revisions:		None
--					
--	Designers:		Ramzi Chennafi and Clark Allenby
--				
--	Programmer:		Clark Allenby
--
--	Notes:
--	The main activity class for the server mode.
--	
--	When a user hits the "Start" button, this activity will call a new ServerThread to begin recieving
--	TCP clients. When the user hits the "Stop" button, this activity will call the stopServer(), 
--  forcing the server to exit. Also manages the server settings input from the user, and sets default
--  values.
--
--  Default Value : Port 3402
---------------------------------------------------------------------------------------*/
public class ServerActivity extends Activity 
{
	private SharedPreferences sharedpreferences;
	private TextView status;
	private EditText port;
	private Thread t = null;
	public ServerSocket serverSocket;
	/*------------------------------------------------------------------------------------------------------------------
	--      FUNCTION: 
	--
	--      DATE: March 1 2014
	--      REVISIONS: none
	--
	--      PROGRAMMER: Clark Allenby
	--
	--      INTERFACE: protected void onCreate(Bundle savedInstanceState)
	--					savedInstanceState - for android OS use
	--
	--      RETURNS: void
	--
	--      NOTES: 
	--		Establishes the ServerActivity. Sets the default values and draws them. Also sets up listeners for the status
	--      box (for auto scrolling) and the clear button (to clear the status screen when pressed).
	--      
	----------------------------------------------------------------------------------------------------------------------*/
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_server);
		
		sharedpreferences = getSharedPreferences(CtUtils.myPREFERENCES, Context.MODE_PRIVATE);
		Editor editor = sharedpreferences.edit();
		editor.putInt(CtUtils.serverPort, 3402);
		editor.commit();

		status = (TextView) findViewById(R.id.sstatus_area);
		port = (EditText) findViewById(R.id.editPORT);
		String text = Integer.toString(sharedpreferences.getInt(CtUtils.serverPort, -1));
		port.setText(text);
		CtUtils.enableAutoScroll((ScrollView) findViewById(R.id.ScrollView01), status);
		
		// sets the clear button listener, too small to be its own function
		Button buttonOne = (Button) findViewById(R.id.b_clear);
		buttonOne.setOnClickListener(new Button.OnClickListener() {
		    public void onClick(View v) {
		            status.setText("");
		    }
		});
	}
	/*------------------------------------------------------------------------------------------------------------------
	--      FUNCTION: startServer
	--
	--      DATE: March 1 2014
	--      REVISIONS: none
	--
	--      PROGRAMMER: Clark Allenby
	--
	--      INTERFACE: public void startServer(View view) 
	--					view - unused
	--
	--      RETURNS: void
	--
	--      NOTES:
	--      Starts the server when the user presses the "Start" button. Will not start if a server is already intiated.
	--		Sets settings before execution of the Server thread.
	----------------------------------------------------------------------------------------------------------------------*/
	public void startServer(View view) 
	{
		if(t != null)
		{
			status.append("Server already instantiated!\n");
			return;
		}
		
		Editor editor = sharedpreferences.edit();
		editor.putInt(CtUtils.serverPort, Integer.parseInt(port.getText().toString()));
		editor.commit();
		
		try 
		{
			t = new ServerThread(getApplicationContext(), this);
			t.start();
		}
		catch (IOException e) 
		{
			status.append(e.getMessage());
			return;
		}
		
		status.append("Server started.\n");
	}
	/*------------------------------------------------------------------------------------------------------------------
	--      FUNCTION: stopServer
	--
	--      DATE: March 1 2014
	--      REVISIONS: none
	--
	--      PROGRAMMER: Clark Allenby
	--
	--      INTERFACE: public void stopServer(View view)
	--					
	--
	--      RETURNS: void
	--
	--      NOTES:
	--      Disconnects the server when the user hits the "Stop" button.
	----------------------------------------------------------------------------------------------------------------------*/
	public void stopServer(View view)
	{
		try 
		{
			serverSocket.close();
		} 
		catch (IOException e) 
		{
			status.append("Failed to close server.\n");
		}
		status.append("Server closed.\n");
		t = null;
	}
}


