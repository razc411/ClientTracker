package com.example.clienttracker;
import java.net.*;
import java.io.*;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.widget.TextView;
/*---------------------------------------------------------------------------------------
--	Source File: ServerThread.java - Server thread for the GPS Android client
--
--	Classes:	ServerThread - public class
--				ServerSocket - java.net
--				Android.*    - android
--				
--				
--	Methods:	public ServerThread(Context ctx, ServerActivity actv) throws IOException 
--				public void run()
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
--	This class is called to instantiate the Client Tracker server client.
--	
--	Retrieves the connection data in the constructor and sets up a Listening socket.
--	Once start() is called on this object, the run method is executed. The run method goes
--	into an accept state, waiting for TCP clients. Once a TCP client is found, it connects
--	and recieves the data. All data is passed to the UI thread through the handler to print
--  to the status window.
--
--	The server thread will timeout after 100 seconds and exit. The Stop button will cause
--  the socket of this server to close, and end the thread.
---------------------------------------------------------------------------------------*/
public class ServerThread extends Thread 
{
	private String ServerString;
	private ServerSocket ListeningSocket;
	private TextView status;
	private SharedPreferences sharedpreferences;
	private Handler uiHandler;
	/*------------------------------------------------------------------------------------------------------------------
	--      FUNCTION: ServerThread Constructor
	--
	--      DATE: March 1 2014
	--      REVISIONS: none
	--
	--      PROGRAMMER: Clark Allenby 
	--
	--      INTERFACE: public ServerThread(Context ctx, ServerActivity actv) throws IOException
	--					ctx : Application context of the ServerActivity, used for SharedPreferences
	--					actv: Calling ServerActivity object, used to pass status messages
	--					throws IOException : when a failure occurs in sending or accepting
	--					
	--
	--      RETURNS: void
	--
	--      NOTES:
	--      Constructor for the ServerThread object. Establishes the server settings and the server listening
	--		socket.
	----------------------------------------------------------------------------------------------------------------------*/
	public ServerThread(Context ctx, ServerActivity actv) throws IOException 
	{
		status = (TextView) actv.findViewById(R.id.sstatus_area);
		sharedpreferences = ctx.getSharedPreferences(CtUtils.myPREFERENCES, Context.MODE_PRIVATE);
		int port = sharedpreferences.getInt(CtUtils.serverPort, -1);
		uiHandler = new Handler();
		
		ListeningSocket = new ServerSocket(port);
		actv.serverSocket = ListeningSocket;
		ListeningSocket.setSoTimeout(100000);
		ListeningSocket.setReuseAddress(true);
	}
	/*------------------------------------------------------------------------------------------------------------------
	--      FUNCTION: run
	--
	--      DATE: March 1 2014
	--      REVISIONS: none
	--
	--      PROGRAMMER: Clark Allenby 
	--
	--      INTERFACE: public void run()	
	--
	--      RETURNS: void
	--
	--      NOTES:
	--		Main function of the thread. Called whenever the thread begins execution with the .start() command.
	--		Waits for a client connection. Once a connection is established, processes incoming data until the connection
	--		is terminated.
	--      
	----------------------------------------------------------------------------------------------------------------------*/
	public void run() 
	{
		Socket NewClientSocket = null;
		DataInputStream in = null;
		CtUtils.sendText(status, uiHandler,"Listening on port: " + ListeningSocket.getLocalPort() + "\n");
		
		try 
		{
			NewClientSocket = ListeningSocket.accept();
		} 
		catch	 (IOException e1) 
		{
			return;
		}
		
		CtUtils.sendText(status, uiHandler,"Connection from: " + NewClientSocket.getRemoteSocketAddress() + "\n");
		
		while(true){
			try 
			{
				in = new DataInputStream(NewClientSocket.getInputStream());
				ServerString = in.readUTF();
				String[] tokens = ServerString.split("~");
				CtUtils.sendText(status, uiHandler, "Current Latitude: " + tokens[0] + "\n");
				CtUtils.sendText(status, uiHandler, "Current Longitude: " + tokens[1] +"\n");
				CtUtils.sendText(status, uiHandler, "Time: " + tokens[2] + "\n");
			}
			catch (SocketTimeoutException s)
			{
				CtUtils.sendText(status, uiHandler, s.getMessage());
				break;
			} 
			catch (IOException e) 
			{
				try 
				{
					NewClientSocket.getOutputStream().close();
					NewClientSocket.close();
					ListeningSocket.close();
				} 
				catch (IOException e1) 
				{
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				CtUtils.sendText(status, uiHandler, e.getMessage());
				break;
			}
		}
	}
}