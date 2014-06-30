package com.hassSektor.ufkuikikatinacikaranseyler;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.hassSektor.ufkuikikatinacikaranseyler.MainActivity.DemoCollectionPagerAdapter;
import com.hassSektor.ufkuikikatinacikaranseyler.MainActivity.DemoObjectFragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class favoriler extends FragmentActivity{

    ListView list;
    customAdapter adapter;
    public static  favoriler CustomListView = null;
    public static  ArrayList<entryModel> CustomListViewValuesArr = new ArrayList<entryModel>();
    static Resources res;
    static JSONArray array;
    static DemoCollectionPagerAdapter mDemoCollectionPagerAdapter;
    static ViewPager mViewPager;
	static Context context ;

	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		setContentView(R.layout.activity_collection_demo);
		Intent intent = getIntent();
        String jsonArray = intent.getStringExtra("favori");
        context = getBaseContext();

        try {
             array = new JSONArray(jsonArray);
            System.out.println(array.toString(2));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        
        
						// list view için oluþturuluyor
		res =getResources();
		CustomListView = this;
		
		mDemoCollectionPagerAdapter = new DemoCollectionPagerAdapter(getSupportFragmentManager());
		mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mDemoCollectionPagerAdapter);

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
            args.putInt(DemoObjectFragment.ARG_SAYFA, (i+1));
            args.putParcelableArrayList(DemoObjectFragment.ARG_OBJECT,CustomListViewValuesArr); 
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public int getCount() {
        	double uzun = (double)array.length();
        	double sonuc = uzun /10;
        	int i=(int) Math.ceil(sonuc);
            return i;
        }

        @Override
        public CharSequence getPageTitle(int position) {		// sayfa baþlýðý
            return "Favori Sayfa " + (position + 1);
        }
    }
	
	public static class DemoObjectFragment extends Fragment {		// sayfa oluþturuluyor

        public static final String ARG_OBJECT = "object";
        public static final String ARG_SAYFA = "sayfa";

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.listview_with_navigation_buttons, container, false);
            Bundle args = getArguments();
            
            (( ListView )rootView.findViewById( R.id.listem )).setAdapter( new customAdapter( CustomListView, args.getParcelableArrayList(ARG_OBJECT),res,args.getInt(ARG_SAYFA),"fav" ) );  // List defined in XML ( See Below )
            TextView sayfa = (TextView)rootView.findViewById(R.id.sayfa);
            sayfa.setText(args.getInt(ARG_SAYFA)+ "/"+String.valueOf((int)Math.ceil((double)array.length()/10)));
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
          		    mViewPager.setCurrentItem(array.length()/10, true);
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
	
	
}
