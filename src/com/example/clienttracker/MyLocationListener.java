package com.example.clienttracker;
import java.net.Socket;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
/*---------------------------------------------------------------------------------------
--	Source File: MyLocationListener.java - Listener for location changes.
--
--	Classes:	MyLocationListener - public class
--				Socket        - java.net
--				Android.*    - android			
--				
--	Methods:	public MyLocationListener(Socket client, ClientActivity actv)
--				public void onLocationChanged(Location arg0)
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
--	LocationListener class to listen for location updates. Upon retreiving updates
--  the listener will create a new thread for sending and pass it the latitude, longitude
--  and the currently connected socket.
---------------------------------------------------------------------------------------*/
public class MyLocationListener implements LocationListener 
{
	private Socket clientSocket;
	private ClientActivity actv;
	/*------------------------------------------------------------------------------------------------------------------
	--      FUNCTION: MyLocationListener
	--
	--      DATE: March 1 2014
	--      REVISIONS: none
	--
	--      PROGRAMMER: Ramzi Chennafi
	--
	--      INTERFACE: public MyLocationListener(Socket client, ClientActivity actv)
	--					client - server connected socket currently in use, passed for sending
	--					actv - object of the calling activity, used for sending status messages
	--
	--      RETURNS: void
	--
	--      NOTES:
	--		Constructor for MyLocationListener. Establishes internal variables.    
	----------------------------------------------------------------------------------------------------------------------*/	
	public MyLocationListener(Socket client, ClientActivity actv)
	{
		clientSocket = client;
		this.actv = actv;
	}
	/*------------------------------------------------------------------------------------------------------------------
	--      FUNCTION: 
	--
	--      DATE: March 1 2014
	--      REVISIONS: none
	--
	--      DESIGNER: Ramzi Chennafi
	--      PROGRAMMER: Ramzi Chennafi
	--
	--      INTERFACE: public void onLocationChanged(Location arg0)
	--					arg0 - current location retrieved by the listener
	--
	--      RETURNS: void
	--
	--      NOTES:
	--      Responds when the user changes location. Retrieves the Latitude and Longitude from the location service
	--		then creates a new thread to send the data. Passes the connected socket and a reference to the calling activity.
	----------------------------------------------------------------------------------------------------------------------*/	
	@Override
	public void onLocationChanged(Location arg0) {
		Double lat = arg0.getLatitude();
		Double lng = arg0.getLongitude();
		
		Date date = new Date(arg0.getTime());
		DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
		
		Thread t = new SendThread(lat, lng,  dateFormat.format(date), clientSocket, actv);
		t.start();
	}
	
	/* GENERATED FOR COMPILER - UNUSED */
	@Override
	public void onProviderDisabled(String arg0) {}
	
	/* GENERATED FOR COMPILER - UNUSED */
	@Override
	public void onProviderEnabled(String arg0) {}
	
	/* GENERATED FOR COMPILER - UNUSED */
	@Override
	public void onStatusChanged(String arg0, int arg1, Bundle arg2) {}

}
