package com.colonycount.cklab.activity;

import android.annotation.TargetApi;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.colonycount.cklab.MyApplication;
import com.colonycount.cklab.activity.base.GPlusClientActivity;
import com.colonycount.cklab.asynctask.GetImgAsyncTask;
import com.colonycount.cklab.asynctask.base.AsyncTaskCompleteListener;
import com.colonycount.cklab.asynctask.base.AsyncTaskPayload;
import com.colonycount.cklab.callback.BitmapDecodedListener;
import com.colonycount.cklab.model.ImgSearchFilter;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.plus.Plus;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import org.json.JSONArray;
import org.json.JSONObject;
import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.LoaderCallbackInterface;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class HomeActivity extends GPlusClientActivity implements AsyncTaskCompleteListener<Boolean>, BitmapDecodedListener {
	private DrawerLayout layDrawer;
    private ListView lstDrawer;
    
    private ActionBarDrawerToggle drawerToggle;
    private CharSequence mDrawerTitle;
    private CharSequence mTitle;
    
    private int nowPage;
    
    private FragmentHome    fragmentHome;
    private FragmentSetting fragmentSettings;
    
    // gridview data
    private String[] date;
	private String[] colony_num;
	private String[] img_urls;
	private String[] img_ids;
	private List<Bitmap> colony_thumbnails = new ArrayList<Bitmap>();
	
	private String homeMsgStr = null;
	private boolean haveData = false;
    
//    private String[] date = {"2015-02-05","2015-02-05","2015-02-05","2015-02-05","2015-02-05","2015-02-05","2015-02-05","2015-02-05","2015-02-05","2015-02-05",
//    		"2015-02-05","2015-02-05","2015-02-05","2015-02-05","2015-02-05","2015-02-05","2015-02-05","2015-02-05","2015-02-05","2015-02-05",
//    		"2015-02-05","2015-02-05","2015-02-05","2015-02-05","2015-02-05","2015-02-05","2015-02-05","2015-02-05","2015-02-05","2015-02-05",
//    		"2015-02-05","2015-02-05","2015-02-05","2015-02-05","2015-02-05","2015-02-05","2015-02-05","2015-02-05","2015-02-05","2015-02-05",
//    		"2015-02-05","2015-02-05","2015-02-05","2015-02-05","2015-02-05","2015-02-05","2015-02-05","2015-02-05","2015-02-05","2015-02-05",
//    		"2015-02-05","2015-02-05","2015-02-05","2015-02-05","2015-02-05","2015-02-05","2015-02-05","2015-02-05","2015-02-05","2015-02-05",
//    		"2015-02-05","2015-02-05","2015-02-05","2015-02-05","2015-02-05","2015-02-05","2015-02-05","2015-02-05","2015-02-05","2015-02-05",
//    		"2015-02-05","2015-02-05","2015-02-05","2015-02-05","2015-02-05","2015-02-05","2015-02-05","2015-02-05","2015-02-05","2015-02-05",
//    		"2015-02-05","2015-02-05","2015-02-05","2015-02-05","2015-02-05","2015-02-05","2015-02-05","2015-02-05","2015-02-05","2015-02-05",
//    		"2015-02-05","2015-02-05","2015-02-05","2015-02-05","2015-02-05","2015-02-05","2015-02-05","2015-02-05","2015-02-05","2015-02-05"};
//	private String[] colony_num = {"123","123","123","123","123","123","123","123","123","123",
//			"123","123","123","123","123","123","123","123","123","123",
//			"123","123","123","123","123","123","123","123","123","123",
//			"123","123","123","123","123","123","123","123","123","123",
//			"123","123","123","123","123","123","123","123","123","123",
//			"123","123","123","123","123","123","123","123","123","123",
//			"123","123","123","123","123","123","123","123","123","123",
//			"123","123","123","123","123","123","123","123","123","123",
//			"123","123","123","123","123","123","123","123","123","123",
//			"123","123","123","123","123","123","123","123","123","123"};
//	private String[] img_urls;
//	private String[] img_ids = {"123","123","123","123","123","123","123","123","123","123",
//			"123","123","123","123","123","123","123","123","123","123",
//			"123","123","123","123","123","123","123","123","123","123",
//			"123","123","123","123","123","123","123","123","123","123",
//			"123","123","123","123","123","123","123","123","123","123",
//			"123","123","123","123","123","123","123","123","123","123",
//			"123","123","123","123","123","123","123","123","123","123",
//			"123","123","123","123","123","123","123","123","123","123",
//			"123","123","123","123","123","123","123","123","123","123",
//			"123","123","123","123","123","123","123","123","123","123"};
    
    private BaseLoaderCallback mOpenCVCallBack = new BaseLoaderCallback(this) {  
        @Override  
        public void onManagerConnected(int status) {  
            switch (status) {  
                case LoaderCallbackInterface.SUCCESS:
                	break;  
                default:
                    super.onManagerConnected(status);  
                    break;  
            }  
        }  
    };  
    
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// Log.d("test4", "HomeActivity onCreate");
		
//		img_urls = new String[100];
//		for(int i = 0; i < 100; i++){
//			img_urls[i] = "http://140.112.26.221/~master11360/colony%20count/test/" + (i+1) + ".jpg";
//		}
		
		initActionBar();
        initDrawer();
        initDrawerList();
		initUniversalImageLoader();
        
        
        /*int wantedPosition = 0; // Whatever position you're looking for
        int firstPosition = lstDrawer.getFirstVisiblePosition() - lstDrawer.getHeaderViewsCount(); // This is the same as child #0
        int wantedChild = wantedPosition - firstPosition;
        // Say, first visible position is 8, you want position 10, wantedChild will now be 2
        // So that means your view is child #2 in the ViewGroup:
        if (wantedChild < 0 || wantedChild >= lstDrawer.getChildCount()) {
          Log.w(TAG, "Unable to get view for desired position, because it's not being displayed on screen.");
          return;
        }
        // Could also check if wantedPosition is between listView.getFirstVisiblePosition() and listView.getLastVisiblePosition() instead.
        View wantedView = lstDrawer.getChildAt(wantedChild);*/
	}

	public void initUniversalImageLoader(){
		// Create global configuration and initialize ImageLoader with this config
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(this).build();
		ImageLoader.getInstance().init(config);
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		
//		if (!OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_2_4_9, this, mOpenCVCallBack)){
//	        // Log.d("debug", "Cannot connect to OpenCV Manager");
//	    }
//
//		// Log.d("test4", "HomeActivity onResume");
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		
		// Log.d("test4", "HomeActivity onPause");
	}
	
	@Override
	protected void onStart() {
		super.onStart();
		
		//mGoogleApiClient.connect();
		
		// create fragmentHome
		fragmentHome = new FragmentHome(loadPrefStringData(USER_ID), this);
		// Log.d("test4", "HomeActivity: new fragmentHome");
    	
		getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, fragmentHome).commit();
		String title = getResources().getStringArray(R.array.drawer_menu)[0];
		mTitle = title;
	    getActionBar().setTitle(title);
	    nowPage = 0;
		
		new GetImgAsyncTask(this, "", "", this, GetImgAsyncTask.class, false, loadPrefStringData(USER_ID)).execute();
	}

	@Override
	protected void onStop() {
		Log.d("Step", "Home_onStop");
		super.onStop();



		if (mGoogleApiClient.isConnected()) {
			mGoogleApiClient.disconnect();
		}
		
		// Log.d("test4", "HomeActivity onStop");
		
//		// release memory
//		for(int i = 0; i < colony_thumbnails.size(); i++){
//			Bitmap colony_thumbnail = colony_thumbnails.get(i);
//			if(colony_thumbnail != null && !colony_thumbnail.isRecycled()){
//				colony_thumbnail.recycle();
//				colony_thumbnail = null;
//			}
//		}
	}

	
	@TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
	private void initActionBar(){
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);
    }
	
	private void initDrawer(){
        setContentView(R.layout.drawer);

        layDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        lstDrawer = (ListView) findViewById(R.id.left_drawer);
        // TODO: add shadow
//        layDrawer.setDrawerShadow(R.drawable.shadow, GravityCompat.START);
        
        mTitle = mDrawerTitle = getTitle();
        drawerToggle = new ActionBarDrawerToggle(
                this, 
                layDrawer,
                R.drawable.drawer_menu,
                R.string.drawer_open,
                R.string.drawer_close) {

            @Override
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                getActionBar().setTitle(mTitle);
                fragmentHome.setMenuVisible(true);
                
                supportInvalidateOptionsMenu(); 
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                getActionBar().setTitle(mDrawerTitle);
                fragmentHome.setMenuVisible(false);
                
                supportInvalidateOptionsMenu();
            }
        };
        drawerToggle.syncState();

        layDrawer.setDrawerListener(drawerToggle);
    }
	
	private void initDrawerList(){
        String[] drawer_menu = this.getResources().getStringArray(R.array.drawer_menu);
//        CustomArrayAdapter adapter = new CustomArrayAdapter(this, R.layout.drawer_list_item, drawer_menu);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.drawer_list_item, drawer_menu);
        lstDrawer.setAdapter(adapter);
        lstDrawer.setOnItemClickListener(new DrawerItemClickListener());
    }
	
	private class DrawerItemClickListener implements ListView.OnItemClickListener {
	    @Override
	    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
	        selectItem(position);
	    }
	}
	
	public boolean onOptionsItemSelected(MenuItem item) {
	    //home
	    if (drawerToggle.onOptionsItemSelected(item)) {
	        return true;
	    }

	    return super.onOptionsItemSelected(item);
	}
	
	
	private void selectItem(int position) {
		FragmentManager fragmentManager = getSupportFragmentManager();
		FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
		String title = getResources().getStringArray(R.array.drawer_menu)[position];
		
//		TextView tv = (TextView) lstDrawer.getChildAt(position);
//		tv.setTypeface(tv.getTypeface(), Typeface.BOLD);
		
	    switch (position) {
	    case 0:
	    	if(nowPage != 0){
    			fragmentHome = new FragmentHome(loadPrefStringData(USER_ID), this);
    			
    			fragmentTransaction.replace(R.id.content_frame, fragmentHome);
//			    fragmentTransaction.addToBackStack("home");
			    fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
			    fragmentTransaction.commit();

			    lstDrawer.setItemChecked(position, true);
			    mTitle = title;
			    getActionBar().setTitle(title);
			    layDrawer.closeDrawer(lstDrawer);
			    
			    setPage(0);
			    // Log.d("test4", "HomeActivity selected");
			    fragmentHome.setSelected(true);
			    if(haveData)
			    	fragmentHome.setHaveData(haveData);
			    else
	    			fragmentHome.setHomeMsgStr(homeMsgStr);
	    	}
	    	break;
	    case 1:
	    	if(nowPage != 1){
    			fragmentSettings = new FragmentSetting();
		        
			    fragmentTransaction.replace(R.id.content_frame, fragmentSettings);
//			    fragmentTransaction.addToBackStack("home");
			    fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
			    fragmentTransaction.commit();

			    lstDrawer.setItemChecked(position, true);
			    mTitle = title;
			    getActionBar().setTitle(title);
			    layDrawer.closeDrawer(lstDrawer);
			    
			    setPage(1);
	    	}
	    	break;
	    case 2:
	    	Resources resources = getResources();
	    	new FragmentDialog(resources.getString(R.string.drawer_logout_title),
	    					   resources.getString(R.string.drawer_logout_msg),
	    					   0, 
	    					   resources.getString(R.string.btn_ok), 
	    					   resources.getString(R.string.btn_cancel), 
	    					   new DialogInterface.OnClickListener() {
								
									@Override
									public void onClick(DialogInterface dialog, int which) {
										if (mGoogleApiClient.isConnected()) {
											MyApplication.getInstance().loginState = Boolean.FALSE;
											savePrefData(LOGGED_IN, false);
											removePrefData(USER_ACCOUNT);
											removePrefData(USER_ID);
											
											Plus.AccountApi.clearDefaultAccount(mGoogleApiClient);
											mGoogleApiClient.disconnect();
											mGoogleApiClient.connect();
										}
									}
								}, 
	    					   null).show(getSupportFragmentManager(), "dialog");
	    	break;
	    default:
	        return;
	    }
	}

	
	@Override
	public void onConnectionFailed(ConnectionResult result) {
		/* Logout */
		// Log.d(TAG, "onConnectionFailed");
		
		Intent i = new Intent(this, LoginActivity.class);
		startActivity(i);
		finish();
		overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
	}
	
	private void setPage(int page){
		nowPage = page;
	}

	
	public Set<String> loadPrefStringSetData(String key){
		return super.loadPrefStringSetData(key);
	}
	
	public void searchColony(ImgSearchFilter searchFilter){
		AsyncTaskPayload payload = new AsyncTaskPayload();
		payload.putValue("search_colony", "true");
		payload.setImgSearchFilter(searchFilter);
		new GetImgAsyncTask(this, "", "", this, GetImgAsyncTask.class, false, loadPrefStringData(USER_ID)).execute(payload);
	}
	
	@Override
	public void onTaskComplete(AsyncTaskPayload result, String taskName) {
		String resultStr = result.getValue("result");
		// Log.d("test4", "resultStr = " + resultStr);
		if(taskName.equals("GetImgAsyncTask")){
			setGridViewData(result.getImageInfoList());
			// initial
			if(result.getValue("search_colony") == null){
				// Log.d("test4", "initial");
				// Log.d("test4", "resultStr = " + resultStr);
				if(resultStr.equals("success")){
					setGridView();
					haveData = true;
				} else {
					homeMsgStr = getString(R.string.home_info_message);
					setHomeMsg(homeMsgStr);
					haveData = false;
				}
			// search colony
			} else {
				// Log.d("test4", "search colony");
				if(resultStr.equals("success")){
					setGridView();
					haveData = true;
				} else {
					homeMsgStr = getString(R.string.search_colony_result_msg);
					setHomeMsg(homeMsgStr);
					haveData = false;
				}
			}
		}
	}
	
	public void setGridViewData(JSONArray jsonArray){
		// Log.d("test4", "setGridViewData");
		// http://proxy.colonycounter.cklab.org     ====  http://140.112.26.221/~master11360/colony%20count
		String photoUrlPrefix = "http://proxy.colonycounter.cklab.org";
		try {
			int len = jsonArray.length();
			date = new String[len];
			colony_num = new String[len];
			img_urls = new String[len];
			img_ids = new String[len];
			
			// Log.d("test4", "jsonArray len = " + jsonArray.length());
			for(int i = 0; i < jsonArray.length(); i++){
				JSONObject image = jsonArray.getJSONObject(i);
				String img_id = image.getString("img_id");
				String img_url = image.getString("img_url");
				String img_colony_num = image.getString("img_colony_num");
				String img_upload_time = image.getString("img_upload_time");
				img_url = photoUrlPrefix + img_url.substring(img_url.indexOf("/"));
				
//				// Log.d("test4", "id = " + img_id + ", url = " + img_url + ", num = " + img_colony_num + ", time = " + img_upload_time);

				date[i] = img_upload_time;
				colony_num[i] =  img_colony_num;
				img_urls[i] = img_url;
				img_ids[i] = img_id;
			}
		} catch(Exception e) {
			
		}
	}
	
	public void setGridView(){
		fragmentHome.setGridView(date, colony_num, img_urls, img_ids, this);
	}
	
	public void setHomeMsg(String msg){
		fragmentHome.setHomeMsg(msg);

	}
	
	@Override
	public void onDecoded(Bitmap bitmap, String url) {
//		if(!colony_thumbnails.contains(bitmap)){
//			colony_thumbnails.add(bitmap);
//			// Log.d("test4", "colony_thumbnails size = " + colony_thumbnails.size() + ", url = " + url);
//		}
	}

	
//	class CustomArrayAdapter extends ArrayAdapter<String> {
//
//		public CustomArrayAdapter(Context context, int resource, String[] objects) {
//			super(context, resource, objects);
//		}
//
//		@Override
//		public View getView(int position, View convertView, ViewGroup parent) {
//			// TODO Auto-generated method stub
//			LayoutInflater inflater = getLayoutInflater();
//			View row = inflater.inflate(R.layout.row, parent, false);
//			TextView label = (TextView) row.findViewById(R.id.weekofday);
//			
//			return tv;
//		}
//	}
}
