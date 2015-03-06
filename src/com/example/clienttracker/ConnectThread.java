package com.example.clienttracker;
import java.net.*;
import java.io.*;

import android.content.Context;
import android.content.SharedPreferences;
import android.location.Criteria;
import android.location.LocationManager;
import android.os.Handler;
import android.widget.TextView;
/*---------------------------------------------------------------------------------------
--	Source File: ConnectThread - called to establish a connection and a location listener
--
--	Classes:	ServerThread - public class
--				ServerSocket - java.net
--				Android.*    - android
--				
--				
--	Methods:	public ConnectThread(Context ctx, ClientActivity actv, LocationManager locManager)
--				public void run()
--				private void establishConnection() throws IOException 
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
--	When start() is called on this class, run() is executed in a new thread. Establishes a connection
--  with a server and registers a LocationListener that will call sendThread on location change.
---------------------------------------------------------------------------------------*/
public class ConnectThread extends Thread 
{
	private SharedPreferences sharedpreferences;
	private LocationManager locationManager;
	private ClientActivity actv;
	private String provider;
	private TextView status;
	private Socket client;
	private Handler uiHandler;
	/*------------------------------------------------------------------------------------------------------------------
	--      FUNCTION: ConnectThread
	--
	--      DATE: March 1 2014
	--      REVISIONS: none
	--
	--      PROGRAMMER: Ramzi Chennafi
	--
	--      INTERFACE: public ConnectThread(Context ctx, ClientActivity actv, LocationManager locManager) 
	--					ctx - Context of the calling activity, for getting SharedPreferences
	--					actv - Activity object of the calling activity, used for sending status messages
	--					locManager - LocationManager object, used to instantiate a new location listener
	--
	--      NOTES:
	--      Constructor for ConnectThread. Establishes shared preferences for settings and variables for activity communication.
	----------------------------------------------------------------------------------------------------------------------*/
	public ConnectThread(Context ctx, ClientActivity actv, LocationManager locManager) 
	{
		this.actv = actv;
		locationManager = locManager;
		
		uiHandler = new Handler();
		Criteria criteria = new Criteria();
		status = (TextView) actv.findViewById(R.id.cstatus_area);
		provider = locationManager.getBestProvider(criteria, false);
		sharedpreferences = ctx.getSharedPreferences(CtUtils.myPREFERENCES, Context.MODE_PRIVATE);
	}

	/*------------------------------------------------------------------------------------------------------------------
	--      FUNCTION: run
	--
	--      DATE: March 1 2014
	--      REVISIONS: none
	--
	--      PROGRAMMER: Ramzi Chennafi
	--
	--      INTERFACE: public void run() 
	--					
	--      RETURNS: void
	--
	--      NOTES:
	--      Called by the start() method of Thread. When called with start() a new thread is established and this object calls
	--		run() and establishes a connection with a server. If a connection is established, a new LocationListener is intiated
	--		and monitors for location changes.
	----------------------------------------------------------------------------------------------------------------------*/
	public void run() 
	{
		try 
		{
			establishConnection();
		} 
		catch (IOException e1) 
		{
			CtUtils.sendText(status, uiHandler, "Failure establishing connection.\n");
			return;
		}

		uiHandler.post(new Runnable() { // intiates a the listener on the main thread
			@Override
			public void run() {
				MyLocationListener locListener = new MyLocationListener(client, actv);
				actv.listener = locListener;
				locationManager.requestLocationUpdates(provider, 5, (float) 0.1, locListener);
			}
		});	
	}
	/*------------------------------------------------------------------------------------------------------------------
	--      FUNCTION: establishConnection
	--
	--      DATE: March 1 2014
	--      REVISIONS: none
	--
	--      PROGRAMMER: Ramzi Chennafi
	--
	--      INTERFACE: private void establishConnection() throws IOException 
	--					
	--      RETURNS: void
	--
	--      NOTES:
	--      Establishes a connection with a server. If a connection cannot be established, an IOException is thrown.
	----------------------------------------------------------------------------------------------------------------------*/
	private void establishConnection() throws IOException 
	{
		String serverName = sharedpreferences.getString(CtUtils.hostname, null);
		int port = sharedpreferences.getInt(CtUtils.clientPort, -1);

		CtUtils.sendText(status, uiHandler, "Connecting to " + serverName + " on port " + port + "...\n");
		client = new Socket(serverName, port);
		CtUtils.sendText(status, uiHandler, "Connected to: " + client.getRemoteSocketAddress() + "\n");
		
		actv.clientSocket = client;
	}
}