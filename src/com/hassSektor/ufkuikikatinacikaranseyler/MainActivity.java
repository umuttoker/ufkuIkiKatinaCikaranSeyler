package com.hassSektor.ufkuikikatinacikaranseyler;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.StringTokenizer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;








import android.app.Activity;
import android.app.ActionBar;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.PopupMenu.OnMenuItemClickListener;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.os.Build;
import android.os.StrictMode.VmPolicy;

public class MainActivity extends FragmentActivity{
    ListView list;
    customAdapter adapter;
    public static  MainActivity CustomListView = null;
    public static  ArrayList<entryModel> CustomListViewValuesArr = new ArrayList<entryModel>();
    static Resources res;
    static JSONArray array;
    static DemoCollectionPagerAdapter mDemoCollectionPagerAdapter;
    String jsonFavs;
    Bundle favoriler = new Bundle();
    

    static JSONArray konulmalik = new JSONArray();    //anasayfa ve favoriler için deðiþkenler
    static JSONArray fvs;
    static String nerden= "main";
    static String baslik="Sayfa ";
    static boolean favlar = false;
    static boolean anasayfa = true;
    static int sayfaSayisi=677;
    static Context context;
    static Boolean ayarlar=false;
    
    private DrawerLayout mDrawerLayout;			//sol menü
    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;

    private CharSequence mDrawerTitle;
    private CharSequence mTitle;
    private String[] mPlanetTitles;
    
    
    static Boolean navi;				//tercihler için shared pref deðerleri 
    static Boolean geceModu;
	static int yaziBoyutu;
	static int entrySayisi;
	static int sonSayfa;
	 

    /**
     * The {@link android.support.v4.view.ViewPager} that will display the ob
     * ject collection.
     */
    static ViewPager mViewPager;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_collection_demo);
		
        mTitle = mDrawerTitle = getTitle();
        mPlanetTitles = getResources().getStringArray(R.array.MenuList);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);
        
        mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
        
        mDrawerList.setAdapter(new ArrayAdapter<String>(this,R.layout.drawer_list_item, mPlanetTitles));
        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);
        setTitle(mPlanetTitles[0]);
        
        
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
        mDrawerList.setItemChecked(0, true);
        
        context = getApplicationContext();
        getPref();
		new yukle().execute();				// assyntask json oblejsinin yuklenmesi için çaðýrýlýyor
        
		CustomListView = this;				// list view için oluþturuluyor
		res =getResources();

         
        
		

	}
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
//		getMenuInflater().inflate(R.menu.sayfa, menu);
//		return true;
        
      
        //SubMenu menu2 = menu.addSubMenu(Menu.NONE, 999, 2,"Sayfaya Git");
		if(sayfaSayisi!=menu.size()){
		menu.removeGroup(1);
			for (int i=1;i<=sayfaSayisi;i++){
	        	menu.add(1, i, i, String.valueOf(i));
	        }
	        menu.setGroupCheckable(1,true,true);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.sayfa, menu);
		}
		return true;
	}
	
	

	
	
    @Override
	protected boolean onPrepareOptionsPanel(View view, Menu menu) {
		// TODO Auto-generated method stub
    	if(sayfaSayisi!=menu.size()){
    	menu.removeGroup(1);
		for (int i=1;i<=sayfaSayisi;i++){
        	menu.add(1, i, i, String.valueOf(i));
        }
        menu.setGroupCheckable(1,true,true);
    MenuInflater inflater = getMenuInflater();
    inflater.inflate(R.menu.sayfa, menu);
    	}
		return super.onPrepareOptionsPanel(view, menu);
	}
    
    
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggls
        mDrawerToggle.onConfigurationChanged(newConfig);
    }





	private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        	if(position==0){
                mDrawerList.setItemChecked(position, true);
                setTitle(mPlanetTitles[position]);
        		myTurn(anasayfa);
        		loadPage();
    	        mDrawerLayout.closeDrawers();
        		
        	}
        	else if(position==1){
        		getFavs();
    			sonSayfa=mViewPager.getCurrentItem();
    	        mDrawerList.setItemChecked(position, true);
    	        setTitle(mPlanetTitles[position]);
        		myTurn(favlar);
    			loadPage();
    	        mDrawerLayout.closeDrawers();
        	}
        	else if(position==2){
        		sonSayfa=mViewPager.getCurrentItem();
                mDrawerList.setItemChecked(position, true);
                setTitle(mPlanetTitles[position]);
        		mDrawerLayout.closeDrawers();
        		ayarlar=true;
        	    startActivity(new Intent("com.hassSektor.ufkuikikatinacikaranseyler.AYARLAR"));
        	    
        	}
        }
    }
	
	public void getFavs(){
		SharedPreferences mSharedPrefs = getBaseContext().getSharedPreferences("ufkukatla",0);
		String favoriString = mSharedPrefs.getString("favori", "b");
		StringTokenizer st = new StringTokenizer(favoriString, ",");
		int boy = st.countTokens();
		int[] savedList = new int[(favoriString!="b"?boy:0)];
		int i=0;
		if(favoriString!="b"){
		for (i = 0; i < boy; i++) {
		    savedList[i] = Integer.parseInt(st.nextToken());
		}
		}
		fvs = new JSONArray();
		for (int j=0;j<savedList.length;j++){
			JSONObject yeni= new JSONObject();
			try {
				yeni = array.getJSONObject(savedList[j]);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			fvs.put(yeni);
		}
	}
	
	
	@Override
    public void setTitle(CharSequence title) {
        mTitle = title;
        getActionBar().setTitle(mTitle);
    }
	
	@Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
//		int id = item.getItemId();
//		if (id == R.id.action_settings) {
//			return true;
//		}
		 if (mDrawerToggle.onOptionsItemSelected(item)) {
	            return true;
	        }
		if (item.isChecked()) item.setChecked(false);
        else item.setChecked(true);
		if(item.getItemId()!=999)
			mViewPager.setCurrentItem(item.getItemId()-1, true);
		
		return super.onOptionsItemSelected(item);
	}


	
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		
			if(nerden=="fav"){
				myTurn(anasayfa);
	    		loadPage();
			}
			else{
				SharedPreferences mSharedPrefs = context.getSharedPreferences("ufkukatla",0);
    			SharedPreferences.Editor mPrefsEditor = mSharedPrefs.edit();
    			mPrefsEditor.putInt("sonSayfa", mViewPager.getCurrentItem());
    			mPrefsEditor.commit();
				super.onBackPressed();
			}
	}

	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment {

		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_main, container,
					false);
			return rootView;
		}
	}
	public class yukle extends AsyncTask<Void, Void, Void>{
		private ProgressDialog yuklen = new ProgressDialog(MainActivity.this);
		
		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub
			
			try {
				jsonFavs = loadJSONFromAsset();
				array = new JSONArray(jsonFavs);		// asset den çaðýrýlan json objesi json arrayine dönüþtürülüyor
				
			    
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			//publishProgress(); 
			return null;
		}

		@Override
		protected void onProgressUpdate(Void... values) {
			// TODO Auto-generated method stub
			super.onProgressUpdate(values);
		}

		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub      

			myTurn(anasayfa);
			loadPage();
			yuklen.dismiss();
			super.onPostExecute(result);
		}

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			yuklen.setMessage("Yuklen");
			yuklen.show();
			super.onPreExecute();
		}
		
		
	}
	public String loadJSONFromAsset() {				//assets klasorundeki json opjesini yüklüyor
	    String json = null;
	    try {

	        InputStream is = getAssets().open("ogrenildiginde.json");

	        int size = is.available();

	        byte[] buffer = new byte[size];

	        is.read(buffer);

	        is.close();

	        json = new String(buffer, "UTF-8");


	    } catch (IOException ex) {
	        ex.printStackTrace();
	        return null;
	    }
	    return json;

	}
	public static class DemoCollectionPagerAdapter extends FragmentStatePagerAdapter {

        public DemoCollectionPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int i) {
            Fragment fragment = new DemoObjectFragment();
            CustomListViewValuesArr = new ArrayList<entryModel>();    // toplam entry sayýsý 6761
            for (int i1 = (i*entrySayisi); i1 < ((i+1)*entrySayisi) && i1 < konulmalik.length(); i1++) {				//her sayfa için 10 entry hazýrlýyor
                JSONObject row;
                final entryModel sched = new entryModel();
                
				try {
					row = konulmalik.getJSONObject(i1);
				
                  /******* Firstly take data in model object ******/
                   sched.setEntry(row.getString("entry"));
                   sched.setSuser(row.getString("suser"));
                   sched.setZaman(row.getString("zaman"));
                   sched.setEntryId(row.getString("id"));
                    
                /******** Take Model Object in ArrayList **********/
                CustomListViewValuesArr.add( sched );
                } catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
            }
            Bundle args = new Bundle();
            args.putInt(DemoObjectFragment.ARG_SAYFA, (i+1));
            args.putParcelableArrayList(DemoObjectFragment.ARG_OBJECT,CustomListViewValuesArr); 
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public int getCount() {
            // For this contrived example, we have a 100-object collection.
     
        		return sayfaSayisi;
        }

        @Override
        public CharSequence getPageTitle(int position) {		// sayfa baþlýðý
        		return baslik + (position + 1);
        }
    }
	
	public void getPref(){
		
		SharedPreferences mSharedPrefs = context.getSharedPreferences("ufkukatla",0);
		geceModu = mSharedPrefs.getBoolean("gece", false);
		navi = mSharedPrefs.getBoolean("navigasyon", true);
		yaziBoyutu = mSharedPrefs.getInt("yaziBoyutu", 15);
		int key = mSharedPrefs.getInt("entrySayisi", 10);
		sonSayfa = mSharedPrefs.getInt("sonSayfa", 0);
		 
		switch (key) {
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
	}
	
	
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		//Toast.makeText(context, "geldik", Toast.LENGTH_SHORT).show();
		SharedPreferences mSharedPrefs = getApplicationContext().getSharedPreferences("ufkukatla",0);
		String tiklanan = mSharedPrefs.getString("ayarlardaMenu", "duz");
		if(ayarlar){
			ayarlar=false;
			getPref();
			if(!tiklanan.equals("duz")){
			if(tiklanan.equals("anasayfa")){
				myTurn(anasayfa);
				setTitle(mPlanetTitles[0]);
				mDrawerList.setItemChecked(0, true);
			}
			else if(tiklanan.equals("favoriler")){
				myTurn(favlar);
				setTitle(mPlanetTitles[1]);
				mDrawerList.setItemChecked(1, true);
			}
			}
			else if(nerden.equals("main")){
				setTitle(mPlanetTitles[0]);
				mDrawerList.setItemChecked(0, true);
			}
			else if(nerden.equals("fav")){
				setTitle(mPlanetTitles[1]);
				mDrawerList.setItemChecked(1, true);
			}
			
			sayfaSayisi = (int)Math.ceil((double)konulmalik.length()/entrySayisi);
			loadPage();
		}

		super.onResume();
	}

	public static class DemoObjectFragment extends Fragment {		// sayfa oluþturuluyor

        public static final String ARG_OBJECT = "object";
        public static final String ARG_SAYFA = "sayfa";

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.listview_with_navigation_buttons, container, false);
            if(geceModu)
            	((LinearLayout)rootView.findViewById(R.id.enarka)).setBackgroundColor(0xFF525252);
            Bundle args = getArguments();

            
    		if(!navi){
    		RelativeLayout tv = (RelativeLayout)rootView.findViewById(R.id.navigasyon);
    		tv.setVisibility(View.GONE);
    		}
    	
            
            (( ListView )rootView.findViewById( R.id.listem )).setAdapter( new customAdapter( CustomListView, args.getParcelableArrayList(ARG_OBJECT),res,args.getInt(ARG_SAYFA),nerden,yaziBoyutu ,geceModu) );  // List defined in XML ( See Below )
            TextView sayfa = (TextView)rootView.findViewById(R.id.sayfa);
            	sayfa.setText(args.getInt(ARG_SAYFA)+ "/" +String.valueOf(sayfaSayisi));
            sayfa.setClickable(true);
            
            
            ((Button)rootView.findViewById(R.id.ileri)).setOnClickListener(new OnClickListener()  	
              {
            	  @Override
            	  public void onClick(View v)
            	   {
            		  int currentPage = mViewPager.getCurrentItem();
            		    int totalPages = mViewPager.getAdapter().getCount();

            		    int nextPage = currentPage+1;
            		    if (nextPage >= totalPages) {
            		        // We can't go forward anymore.
            		        // Loop to the first page. If you don't want looping just
            		        // return here.
            		        nextPage = 0;
            		    }

            		    mViewPager.setCurrentItem(nextPage, true);
            	   }
            	});
	        
	        ((Button)rootView.findViewById(R.id.son)).setOnClickListener(new OnClickListener()  	
            {
          	  @Override
          	  public void onClick(View v)
          	   {
          		    mViewPager.setCurrentItem(sayfaSayisi, true);
          	   }
          	});
	        
	        ((Button)rootView.findViewById(R.id.geri)).setOnClickListener(new OnClickListener()  	
            {
          	  @Override
          	  public void onClick(View v)
          	   {
          		int currentPage = mViewPager.getCurrentItem();
          	    int totalPages = mViewPager.getAdapter().getCount();

          	    int previousPage = currentPage-1;
          	    if (previousPage < 0) {
          	        previousPage = totalPages - 1;
          	    }

          	  mViewPager.setCurrentItem(previousPage, true);
          	   }
          	});
	        
	        ((Button)rootView.findViewById(R.id.ilk)).setOnClickListener(new OnClickListener()  	
            {
          	  @Override
          	  public void onClick(View v)
          	   {
          		    mViewPager.setCurrentItem(0, true);
          	   }
          	});
	        
	        
            return rootView;
        }
    }
	
	public void loadPage(){
		mDemoCollectionPagerAdapter = new DemoCollectionPagerAdapter(getSupportFragmentManager());
		mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mDemoCollectionPagerAdapter);
        if(nerden!="fav")
		mViewPager.setCurrentItem(sonSayfa);
	}
	
	public void myTurn(boolean kim){
		if(kim==true){
			konulmalik = array;
			nerden = "main";
			baslik ="Sayfa ";
		}else{
			if(fvs==null)
				getFavs();
			konulmalik = fvs;
			nerden = "fav";
			baslik ="Favori Sayfa ";
		}
		sayfaSayisi = (int)Math.ceil((double)konulmalik.length()/entrySayisi);
		if(sayfaSayisi==0)
			sayfaSayisi=1;
	}
	
	public  void showPopup(View v) {
		 openOptionsMenu();
	}
	

	
}

