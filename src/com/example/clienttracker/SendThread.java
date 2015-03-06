package com.example.clienttracker;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;

import android.os.Handler;
import android.widget.TextView;
/*---------------------------------------------------------------------------------------
--	Source File: SendThread.java - Send thread for the GPS Android client
--
--	Classes:	SendThread   - public class
--				Socket       - java.net
--				Android.*    - android
--				
--				
--	Methods:	public ServerThread(Context ctx, ServerActivity actv) throws IOException 
--				public void run()
--				private void sendText(final String txt)
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
--	Class for sending data along the TCP connection. Called by MyLocationListener whenever 
--  it recieves a location update. Sends the GPS data passed to it by the MyLocationListener
--  then exits.
---------------------------------------------------------------------------------------*/
public class SendThread extends Thread {

	private Double lat;
	private Double lng;
	private Socket client;
	private TextView status;
	private Handler uiHandler;
	private String date;
	/*------------------------------------------------------------------------------------------------------------------
	--      FUNCTION: SendThread Constructor
	--
	--      DATE: March 1 2014
	--      REVISIONS: none
	--
	--      PROGRAMMER: Ramzi Chennafi
	--
	--      INTERFACE: public SendThread(Double lat, Double lng, Socket clientSocket, ClientActivity actv)
	--					lat - Latitude recieved from the MyLocationListener
	--					lng - Longitude recieved from the MyLocationListener
	--					clientSocket - established connection socket recieved from MyLocationListener
	--					actv - handle to the ClientActivity, used to print out status information
	--
	--      RETURNS: void
	--
	--      NOTES:
	--		Constructor for SendThread. Establishes internal variables.
	----------------------------------------------------------------------------------------------------------------------*/	
	public SendThread(Double lat, Double lng, String date, Socket clientSocket, ClientActivity actv)
	{
		this.date = date;
		this.lat = lat;
		this.lng = lng;
		client = clientSocket;
		uiHandler = new Handler();
		status = (TextView) actv.findViewById(R.id.cstatus_area);
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
	--      Called by the start() method of Thread. When called with start() a new thread is established and this function
	--		sends data along the previously established connection socket through TCP.  Sends longitude, latitude and current time.
	----------------------------------------------------------------------------------------------------------------------*/	
	public void run(){
		
		if(!client.isConnected())
		{
			CtUtils.sendText(status, uiHandler, "Client is not connected.");
			return;
		}
		
		try 
    	{
			OutputStream outToServer = client.getOutputStream();
			DataOutputStream out = new DataOutputStream(outToServer);
			out.writeUTF(String.valueOf(lat) +"~" + String.valueOf(lng) + "~" + date);

			CtUtils.sendText(status, uiHandler, "Current Latitude: " + lat + "\n");
			CtUtils.sendText(status, uiHandler, "Current Longitude: " + lng +"\n");
			CtUtils.sendText(status, uiHandler, "Time: " + date + "\n");
    	}
    	catch(IOException e)
    	{
    		try 
    		{
				client.getOutputStream().close();
				client.close();
			} 
    		catch (IOException e1) 
    		{
    			CtUtils.sendText(status, uiHandler, e.getMessage());
    			return;
			}
    		CtUtils.sendText(status, uiHandler, "IOException during send.\n");
    	}
	}
}
