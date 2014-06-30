package com.hassSektor.ufkuikikatinacikaranseyler;

import java.util.ArrayList;
import java.util.StringTokenizer;

import android.R.bool;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.net.Uri;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class customAdapter extends BaseAdapter   implements OnClickListener {
    
    /*********** Declare Used Variables *********/
    private Activity activity;
    private ArrayList data;
    private static LayoutInflater inflater=null;
    public Resources res;
    entryModel tempValues=null;
    int i=0;
    int sayfa;
    String nerden;
     
    /*************  CustomAdapter Constructor *****************/
    public customAdapter(Activity a, ArrayList d,Resources resLocal,int c, String str) {
         
           /********** Take passed values **********/
            activity = a;
            data=d;
            res = resLocal;
            sayfa=c;
            nerden=str;
         
            /***********  Layout inflator to call external xml layout () ***********/
             inflater = ( LayoutInflater )activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
         
    }
 
    /******** What is the size of Passed Arraylist Size ************/
    public int getCount() {
         
        if(data.size()<=0)
            return 1;
        return data.size();
    }
 
    public Object getItem(int position) {
        return position;
    }
 
    public long getItemId(int position) {
        return position;
    }
     
    /********* Create a holder Class to contain inflated xml file elements *********/
    public static class ViewHolder{
         
        public TextView entry;
        public TextView suser;
        public TextView zaman;
        public Button paylas;
        public Button favori;
 
    }
 
    /****** Depends upon data size called for each row , Create each ListView row *****/
    public View getView(final int position, View convertView, ViewGroup parent) {
         
        View vi = convertView;
        ViewHolder holder;
         
        if(convertView==null){
             
            /****** Inflate tabitem.xml file for each row ( Defined below ) *******/
            vi = inflater.inflate(R.layout.liste, null);
             
            /****** View Holder Object to contain tabitem.xml file elements ******/

            holder = new ViewHolder();
            holder.entry = (TextView) vi.findViewById(R.id.entry);
            holder.entry.setMovementMethod(LinkMovementMethod.getInstance());
            holder.suser=(TextView)vi.findViewById(R.id.suser);
            holder.zaman=(TextView)vi.findViewById(R.id.zaman);
            holder.paylas=(Button)vi.findViewById(R.id.paylas);
            holder.favori=(Button)vi.findViewById(R.id.favori);
             
           /************  Set holder with LayoutInflater ************/
            vi.setTag( holder );
        }
        else 
            holder=(ViewHolder)vi.getTag();
         
        if(data.size()<=0)
        {
            holder.entry.setText("Entry Yok!");
             
        }
        else
        {
            /***** Get each Model object from Arraylist ********/
            tempValues=null;
            tempValues = ( entryModel ) data.get( position );
             
            /************  Set Model values in Holder elements ***********/

             holder.entry.setText( Html.fromHtml(tempValues.getEntry()));
             holder.suser.setText( tempValues.getSuser() );
              holder.zaman.setText(tempValues.getZaman());
              final String paylasim = tempValues.getEntryId();
              holder.paylas.setOnClickListener(new OnClickListener()  	//paylaþ butonu
              {
            	  @Override
            	  public void onClick(View v)
            	   {
            		  Uri uri = Uri.parse("https://eksisozluk.com/entry/"+paylasim);
            		  Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            		  v.getContext().startActivity(intent);          
            	   }
            	});
              
              
              holder.favori.setOnClickListener(new OnClickListener()   //favori butonu
              {
            	  @Override
            	  public void onClick(View v)
            	   {
            		  
            		  SharedPreferences mSharedPrefs = v.getContext().getSharedPreferences("ufkukatla",0);
            			SharedPreferences.Editor mPrefsEditor = mSharedPrefs.edit();
            			String favoriString = mSharedPrefs.getString("favori", "b");
            			StringTokenizer st = new StringTokenizer(favoriString, ",");
            			int boy = st.countTokens();
            			int yeni =(sayfa-1)*10+position;
            			int temp;
            				
            			boolean var = false;
            			int[] savedList = new int[(favoriString!="b"?boy:0)+1];
            			int i=0;
            			if(favoriString!="b"){
            			for (i = 0; i < (var==true?boy-1:boy); i++) {
            				temp = Integer.parseInt(st.nextToken());
            				if(temp==yeni && nerden.equals("main")){
            					var=true;
            					i--;
            					continue;
            				}
            				else if(nerden.equals("fav") && i==yeni && var==false ){
            					var=true;
            					i--;
            					continue;
            				}
            			    savedList[i] = temp;
            			}
            			}
            			if(!var)
            				savedList[i]=yeni; 
            			
            			
            			int duration = Toast.LENGTH_SHORT;
            			
             			StringBuilder str = new StringBuilder();
            			for (int j = 0; j < (var==true?i:i+1); j++) {
            			    str.append(savedList[j]).append(",");
            			}
            			mPrefsEditor.putString("favori", str.toString());
            			//mPrefsEditor.remove("favori");      //keyi sil
            			mPrefsEditor.commit();

            			if(var==true){
                			Toast toast = Toast.makeText(v.getContext(), "Entry Favorilerden Kaldýrýldý!", duration);
                			toast.show();
            			}else{
            				Toast toast = Toast.makeText(v.getContext(), "Entry Favorilere Eklendi!", duration);
                			toast.show();
            			}
            		  
            	   }
            	});
              
              
              
             vi.setOnClickListener(new OnItemClickListener( position ));
        }
        return vi;
    }
     
    @Override
    public void onClick(View v) {
            Log.v("CustomAdapter", "=====Row button clicked=====");
    }
     
    /********* Called when Item click in ListView ************/
    private class OnItemClickListener  implements OnClickListener{          
        private int mPosition;
         
        OnItemClickListener(int position){
             mPosition = position;
        }
         
        @Override
        public void onClick(View arg0) {

   
          MainActivity sct = (MainActivity)activity;

         /****  Call  onItemClick Method inside CustomListViewAndroidExample Class ( See Below )****/

            //sct.onItemClick(mPosition);
        }              
    }  
}