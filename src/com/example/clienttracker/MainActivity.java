package com.example.clienttracker;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.View;
/*---------------------------------------------------------------------------------------
--	Source File: MainActivity.java - Entry activity for the GPS Android client
--
--	Classes:	ServerThread - public class
--				ServerSocket - java.net
--				Android.*    - android
--				
--				
--	Methods:	protected void onCreate(Bundle savedInstanceState)  
--				public void start_client(View view) 
--				public void start_server(View view) 
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
--	Entry menu for the program. Allows the user to select between client and server mode.
--	Upon making a choice, the user is redirected to the proper activity.
---------------------------------------------------------------------------------------*/
public class MainActivity extends Activity 
{
	/*------------------------------------------------------------------------------------------------------------------
	--      FUNCTION: onCreate
	--
	--      DATE: March 1 2014
	--      REVISIONS: none
	--
	--      PROGRAMMER: Clark Allenby
	--
	--      INTERFACE: protected void onCreate(Bundle savedInstanceState)
	--					savedInstanceState - for android OS purposes
	--
	--      RETURNS: void
	--
	--      NOTES:
	--		Sets up the basic UI.  
	----------------------------------------------------------------------------------------------------------------------*/	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	}
	/*------------------------------------------------------------------------------------------------------------------
	--      FUNCTION: 
	--
	--      DATE: March 1 2014
	--      REVISIONS: none
	--
	--      PROGRAMMER: Clark Allenby
	--
	--      INTERFACE: public void start_client(View view)
	--					view - unused
	--
	--      RETURNS: void
	--
	--      NOTES:
	--		Activates the ClientActivity when the client button is pressed.      
	----------------------------------------------------------------------------------------------------------------------*/
	public void start_client(View view) 
	{
		Intent myIntent = new Intent(this, ClientActivity.class);
		startActivity(myIntent);
	}
	/*------------------------------------------------------------------------------------------------------------------
	--      FUNCTION: 
	--
	--      DATE: March 1 2014
	--      REVISIONS: none
	--
	--      PROGRAMMER: Clark Allenby
	--
	--      INTERFACE: public void start_server(View view)
	--					view - unused
	--
	--      RETURNS: void
	--
	--      NOTES:
	--      Activates the ServerActivity when the server button is pressed.
	----------------------------------------------------------------------------------------------------------------------*/
	public void start_server(View view) 
	{
		Intent myIntent = new Intent(this, ServerActivity.class);
		startActivity(myIntent);
	}
}
