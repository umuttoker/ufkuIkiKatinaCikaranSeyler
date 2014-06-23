package com.hassSektor.ufkuikikatinacikaranseyler;

import android.widget.TextView;

public class Globals {
		   private static Globals instance;
		 
		   // Global variable
		   private TextView yazi;
		 
		   // Restrict the constructor from being instantiated
		   private Globals(){}
		 
		 
		   public TextView getYazi() {
			return yazi;
		}


		public void setYazi(TextView yazi) {
			this.yazi = yazi;
		}


		public static synchronized Globals getInstance(){
		     if(instance==null){
		       instance=new Globals();
		     }
		     return instance;
		   }
		
}
