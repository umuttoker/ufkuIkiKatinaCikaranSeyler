package com.hassSektor.ufkunukatla;


import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class ayarlar extends Activity{

	private SeekBar fontSize = null;
	private TextView boyutluYazi = null;
	private CheckBox panel;
	private CheckBox gece;
	private Spinner entrySayisi;
	private Button kaydet;
	Context context;
	private String array_spinner[];
	
	private Boolean geceModu;
	private Boolean navigasyonBar;
	private int yaziBoyutu;
	private int entryAdet;
	private String geceText;
	private String naviText;
	private Boolean kontrol=false;
	
	SharedPreferences.Editor mPrefsEditor;
	SharedPreferences mSharedPrefs;
	
	private DrawerLayout mDrawerLayout;			//sol menü
    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;

    private CharSequence mDrawerTitle;
    private CharSequence mTitle;
    private String[] mPlanetTitles;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		setContentView(R.layout.ayarlar);
		super.onCreate(savedInstanceState);
		
		
		mTitle = mDrawerTitle = getTitle();
        mPlanetTitles = getResources().getStringArray(R.array.MenuList);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);
        mDrawerList.setAdapter(new ArrayAdapter<String>(this,R.layout.drawer_list_item, mPlanetTitles));
        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);
        setTitle(mPlanetTitles[2]);
        mDrawerList.setItemChecked(2, true);
        
        mDrawerToggle = new ActionBarDrawerToggle(
                this,                  /* host Activity */
                mDrawerLayout,         /* DrawerLayout object */
                R.drawable.ic_drawer,  /* nav drawer image to replace 'Up' caret */
                R.string.drawer_open,  /* "open drawer" description for accessibility */
                R.string.drawer_close  /* "close drawer" description for accessibility */
                ) {
            public void onDrawerClosed(View view) {
                getActionBar().setTitle(mTitle);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            public void onDrawerOpened(View drawerView) {
                getActionBar().setTitle(mDrawerTitle);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };
        mDrawerLayout.setDrawerListener(mDrawerToggle);
		
		
		fontSize = (SeekBar) findViewById(R.id.fontSize);
		boyutluYazi = (TextView)findViewById(R.id.boyutluYazi);
		panel = (CheckBox)findViewById(R.id.panel);
		gece = (CheckBox)findViewById(R.id.gece);
        entrySayisi = (Spinner) findViewById(R.id.entrySayisi);
		
		array_spinner=new String[4];
        array_spinner[0]="10 Entry";
        array_spinner[1]="25 Entry";
        array_spinner[2]="50 Entry";
        array_spinner[3]="100 Entry";
        ArrayAdapter adapter = new ArrayAdapter(this,android.R.layout.simple_spinner_item, array_spinner);
        entrySayisi.setAdapter(adapter);
        mSharedPrefs = getApplicationContext().getSharedPreferences("ufkukatla",0);
		mPrefsEditor = mSharedPrefs.edit();
        setDegerler();
        
//		context = getApplicationContext();
//		LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//
//		View vi = inflater.inflate(R.layout.listview_with_navigation_buttons, null);
//		RelativeLayout tv = (RelativeLayout)vi.findViewById(R.id.navigasyon);
//		tv.setVisibility(View.GONE);
		 
		fontSize.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
			int progressChanged = 0;
			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub
				yaziBoyutu = progressChanged+10;
    			mPrefsEditor.putInt("yaziBoyutu", yaziBoyutu);
				
			}
			
			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
				// TODO Auto-generated method stub
				progressChanged=progress;
				boyutluYazi.setTextSize(progress+10);
			}
		});
		

		
		panel.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				// TODO Auto-generated method stub
				if(isChecked)
					panel.setText("Aktif");
				else
					panel.setText("Pasif");
				navigasyonBar=isChecked;
    			mPrefsEditor.putBoolean("navigasyon", navigasyonBar);
				
			}
		});
		gece.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				// TODO Auto-generated method stub
				if(isChecked)
					gece.setText("Aktif");
				else
					gece.setText("Pasif");
				geceModu=isChecked;
    			mPrefsEditor.putBoolean("gece", geceModu);
			}
		});
		entrySayisi.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
		{
		    @Override
		    public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
		    {
		    	entryAdet=position;
    			mPrefsEditor.putInt("entrySayisi", entryAdet);
		    }

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
				
			}

		});
		
		
		
		}
	
	public int getSayisi(int position){
		switch (position) {
        case 0:  return 10;
        case 1:  return 25;
        case 2:  return 50;
        case 3:  return 100;
        default: return 10;
    }
	}
	
	public void setDegerler(){
		geceModu = mSharedPrefs.getBoolean("gece", false);
		navigasyonBar = mSharedPrefs.getBoolean("navigasyon", true);
		yaziBoyutu = mSharedPrefs.getInt("yaziBoyutu", 15);
		entryAdet = mSharedPrefs.getInt("entrySayisi", 0);
		geceText=geceModu==true?"Aktif":"Pasif";
		naviText=navigasyonBar==true?"Aktif":"Pasif";
		
		entrySayisi.setSelection(entryAdet);
		fontSize.setProgress(yaziBoyutu-10);
		boyutluYazi.setTextSize(yaziBoyutu);
		gece.setChecked(geceModu);
		panel.setChecked(navigasyonBar);
		gece.setText(geceText);
		panel.setText(naviText);
		
	}
	
	private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        	SharedPreferences mSharedPrefs = getApplicationContext().getSharedPreferences("ufkukatla",0);
			SharedPreferences.Editor mPrefsEditor = mSharedPrefs.edit();
        	if(position==0){
        		mPrefsEditor.putString("ayarlardaMenu", "anasayfa");
        		mPrefsEditor.commit();
        		kontrol=true;
        		mDrawerList.setItemChecked(position, true);
    	        setTitle(mPlanetTitles[position]);
        		onBackPressed();
    	        mDrawerLayout.closeDrawers();
        		
        	}
        	else if(position==1){
        		mPrefsEditor.putString("ayarlardaMenu", "favoriler");
        		mPrefsEditor.commit();
        		kontrol=true;
        		mDrawerList.setItemChecked(position, true);
    	        setTitle(mPlanetTitles[position]);
        		onBackPressed();
    	        mDrawerLayout.closeDrawers();
        	}
        	else if(position==2){
        		
        	}
        	else if(position==3){
        		mDrawerList.setItemChecked(position, true);
    	        setTitle(mPlanetTitles[position]);
        		mDrawerLayout.closeDrawers();
        	}
        }
    }
	
	@Override
    public void setTitle(CharSequence title) {
        mTitle = title;
        getActionBar().setTitle(mTitle);
    }

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		if(!kontrol){
		mPrefsEditor.putString("ayarlardaMenu", "duz");
		}
		mPrefsEditor.commit();
		super.onBackPressed();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
//		getMenuInflater().inflate(R.menu.sayfa, menu);
//		return true;
        
      
        //SubMenu menu2 = menu.addSubMenu(Menu.NONE, 999, 2,"Sayfaya Git");
		menu.close();
		return true;
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		 if (mDrawerToggle.onOptionsItemSelected(item)) {
	            return true;
	        }
	
		return super.onOptionsItemSelected(item);
	}

}
