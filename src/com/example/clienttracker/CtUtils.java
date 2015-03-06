package com.example.clienttracker;

import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.ScrollView;
import android.widget.TextView;
/*---------------------------------------------------------------------------------------
--	Source File: CtUtils - utility functions and definitions for the application
--				
--				
--	Methods:	public static void enableAutoScroll(final ScrollView scrollView1, TextView status)
--				public static String getTime()
--				
--	Date:			March 1 2014
--
--	Revisions:		None
--					
--	Designers:		Ramzi Chennafi and Clark Allenby
--				
--	Programmer:		Ramzi Chennafi and Clark Allenby
--
--	Notes:
--	This class contains global definitions for SharedPreference keys. Also contains some
--  globally used functions.
---------------------------------------------------------------------------------------*/
public class CtUtils 
{
	/* Global SharedPreferences Keys, used to retrieve settings */
	public static final String serverPort = "sportKey";
	public static final String myPREFERENCES = "MyPrefs";
	public static final String hostname = "hostnameKey";
	public static final String clientPort = "cportKey";
	/*------------------------------------------------------------------------------------------------------------------
	--      FUNCTION: enableAutoScroll
	--
	--      DATE: March 1 2014
	--      REVISIONS: none
	--
	--      PROGRAMMER: Ramzi Chennafi
	--
	--      INTERFACE: public static void enableAutoScroll(final ScrollView scrollView1, TextView status)
	--					scrollView1 - ScrollView that contains the textview status
	--					status - status box TextView contained within the scrollview1
	--
	--      RETURNS: void
	--
	--      NOTES:
	--      Enables automatic scrolling when text appears in the status box.
	----------------------------------------------------------------------------------------------------------------------*/
	public static void enableAutoScroll(final ScrollView scrollView1, TextView status){
		
		status.addTextChangedListener(new TextWatcher() {
			public void afterTextChanged(Editable arg0) {
				scrollView1.fullScroll(ScrollView.FOCUS_DOWN);
			}

			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1,
					int arg2, int arg3) {
				// TODO Auto-generated method stub
			}

			@Override
			public void onTextChanged(CharSequence arg0, int arg1, int arg2,
					int arg3) {
				// TODO Auto-generated method stub
			}
		});
	}
	/*------------------------------------------------------------------------------------------------------------------
	--      FUNCTION: 
	--
	--      DATE: March 1 2014
	--      REVISIONS: none
	--
	--      PROGRAMMER: Clark Allenby
	--
	--      INTERFACE: public static void sendText(final TextView status, final String txt)
	--					status - status box handle to print the txt String to
	--					txt    - text to print to the status box
	--
	--      RETURNS: void
	--
	--      NOTES:
	--      Using a Handler, this function posts a new command to the main thread. In this case, it posts a request for the
	--		main thread to write to the specified status box the specified string.
	----------------------------------------------------------------------------------------------------------------------*/
	public static void sendText(final TextView status, Handler uiHandler, final String txt)
	{	
		uiHandler.post(new Runnable() {
		      @Override
		      public void run() {
		    	  status.append(txt);
		      }
		    });
	}
}
