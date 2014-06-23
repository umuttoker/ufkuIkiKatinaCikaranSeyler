package com.hassSektor.ufkuikikatinacikaranseyler;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Connection;
import org.jsoup.Connection.Method;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;




import android.app.Activity;
import android.app.ActionBar;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.os.Build;

public class MainActivity extends FragmentActivity {
    ListView list;
    customAdapter adapter;
    public static  MainActivity CustomListView = null;
    public static  ArrayList<entryModel> CustomListViewValuesArr = new ArrayList<entryModel>();
    static Resources res;
    static JSONArray array;
    DemoCollectionPagerAdapter mDemoCollectionPagerAdapter;

    
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;

    private CharSequence mDrawerTitle;
    private CharSequence mTitle;
    private String[] mPlanetTitles;

    /**
     * The {@link android.support.v4.view.ViewPager} that will display the object collection.
     */
    ViewPager mViewPager;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_collection_demo);
		
        mTitle = mDrawerTitle = getTitle();
        mPlanetTitles = getResources().getStringArray(R.array.MenuList);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);
        mDrawerList.setAdapter(new ArrayAdapter<String>(this,R.layout.drawer_list_item, mPlanetTitles));
        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());
		new yukle().execute();				// assyntask json oblejsinin yuklenmesi için çaðýrýlýyor
		
        
		CustomListView = this;				// list view için oluþturuluyor
		res =getResources();

         
        
		

	}
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        	if(position==0){
        		
        	}
        	else if(position==1){
        		
        	}
        	else if(position==2){
        		
        	}
        	else if(position==3){
        	    startActivity(new Intent("com.hassSektor.ufkuikikatinacikaranseyler.AYARLAR"));
        	    
        	}
        }
    }
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
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
				array = new JSONArray(loadJSONFromAsset());		// asset den çaðýrýlan json objesi json arrayine dönüþtürülüyor
				
			    
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
			mDemoCollectionPagerAdapter = new DemoCollectionPagerAdapter(getSupportFragmentManager());
			mViewPager = (ViewPager) findViewById(R.id.pager);
	        mViewPager.setAdapter(mDemoCollectionPagerAdapter);
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
            for (int i1 = (i*10); i1 < ((i+1)*10) && i1 < array.length(); i1++) {				//her sayfa için 10 entry hazýrlýyor
                JSONObject row;
                final entryModel sched = new entryModel();
                
				try {
					row = array.getJSONObject(i1);
				
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
            args.putParcelableArrayList(DemoObjectFragment.ARG_OBJECT,CustomListViewValuesArr); 
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public int getCount() {
            // For this contrived example, we have a 100-object collection.
            return 676;
        }

        @Override
        public CharSequence getPageTitle(int position) {		// sayfa baþlýðý
            return "Sayfa " + (position + 1);
        }
    }
	
	public static class DemoObjectFragment extends Fragment {		// sayfa oluþturuluyor

        public static final String ARG_OBJECT = "object";

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.listview_with_navigation_buttons, container, false);
            Bundle args = getArguments();
            
            (( ListView )rootView.findViewById( R.id.listem )).setAdapter( new customAdapter( CustomListView, args.getParcelableArrayList(ARG_OBJECT),res ) );  // List defined in XML ( See Below )
	        
            return rootView;
        }
    }
}

