package com.hassSektor.ufkuikikatinacikaranseyler;

import java.util.ArrayList;
import java.util.StringTokenizer;

import android.R.bool;
import android.R.drawable;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.net.Uri;
import android.provider.ContactsContract.CommonDataKinds.Im;
import android.text.Html;
import android.text.Spannable;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
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
    int yaziBoyutu;
    String nerden;
    boolean gece;
     
    /*************  CustomAdapter Constructor *****************/
    public customAdapter(Activity a, ArrayList d,Resources resLocal,int c, String str,int boyut, boolean geceModu) {
         
           /********** Take passed values **********/
            activity = a;
            data=d;
            res = resLocal;
            sayfa=c;
            nerden=str;
            yaziBoyutu=boyut;
            gece=geceModu;
         
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
        public ImageButton paylas;
        public ImageButton favori;
 
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
            holder.paylas=(ImageButton)vi.findViewById(R.id.paylas);
            holder.favori=(ImageButton)vi.findViewById(R.id.favori);
             
           /************  Set holder with LayoutInflater ************/
            vi.setTag( holder );
        }
        else 
            holder=(ViewHolder)vi.getTag();
         
        if(data.size()<=0)
        {
            holder.entry.setText("Henüz Hiç Bir Favori Eklemediniz!");
             
        }
        else
        {
            /***** Get each Model object from Arraylist ********/
            tempValues=null;
            tempValues = ( entryModel ) data.get( position );
             
            /************  Set Model values in Holder elements ***********/

             holder.entry.setText( Html.fromHtml(tempValues.getEntry()));
             holder.entry.setTextSize(yaziBoyutu);
             holder.suser.setText( tempValues.getSuser() );
              holder.zaman.setText(tempValues.getZaman());
              final String paylasim = tempValues.getEntryId();
              final String biri = tempValues.getSuser();
              if(gece){
            	  ((RelativeLayout)vi.findViewById(R.id.altPanel)).setBackgroundColor(0XFF333333);
            	  holder.paylas.setImageResource(R.drawable.ic_action_share_white);
            	  holder.favori.setImageResource(R.drawable.ic_action_not_important_white);
            	  holder.entry.setTextColor(0xFFC7C7C7);
            	  holder.entry.setBackgroundColor(0xFF000000);
            	  holder.suser.setTextColor(0xFFA2A2A2);
            	  holder.zaman.setTextColor(0xFFA2A2A2);
              }
              holder.paylas.setOnClickListener(new OnClickListener()  	//paylaþ butonu
              {
            	  @Override
            	  public void onClick(View v)
            	   {
 //           		  Uri uri = Uri.parse("https://eksisozluk.com/entry/"+paylasim);
//            		  Intent intent = new Intent(Intent.ACTION_VIEW, uri);
//            		  v.getContext().startActivity(intent);         
            		  
            		  Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND); 
            		    sharingIntent.setType("text/plain");
            		    String shareBody ="https://eksisozluk.com/entry/"+paylasim;
            		    sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Ufkunu Katla Aracýlýðýyla");
            		    sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
            		    v.getContext().startActivity(Intent.createChooser(sharingIntent, "Share via"));
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
            			int boyut = mSharedPrefs.getInt("entrySayisi", 10);
            			int entrySayisi;
            			switch (boyut) {
            			case 0:entrySayisi=10;
            				break;
            			case 1:entrySayisi=25;
            			break;
            			case 2:entrySayisi=50;
            			break;
            			case 3:entrySayisi=100;
            			break;
            			default:entrySayisi=10;
            				break;
            			}
            			StringTokenizer st = new StringTokenizer(favoriString, ",");
            			int boy = st.countTokens();
            			int yeni =(sayfa-1)*entrySayisi+position;
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
              
              holder.suser.setOnClickListener(new OnClickListener()  	//susera git
              {
            	  @Override
            	  public void onClick(View v)
            	   {
            		  Uri uri = Uri.parse("https://eksisozluk.com/biri/"+biri);
            		  Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            		  v.getContext().startActivity(intent);          
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