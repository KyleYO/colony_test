package com.colonycount.cklab.asynctask;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.util.Log;

import com.colonycount.cklab.asynctask.base.AsyncTaskCompleteListener;
import com.colonycount.cklab.asynctask.base.AsyncTaskPayload;
import com.colonycount.cklab.asynctask.base.BaseAsyncTask;

public class LoginAsyncTask extends BaseAsyncTask {
	private String postUrl = "http://proxy.colonycounter.cklab.org/php/db_connect.php";
	private Map<String,String> request;
	
	public LoginAsyncTask(Context context, String progressDialogTitle, String progressDialogMsg, AsyncTaskCompleteListener<Boolean> listener, Class<?> cls) {
		super(context, progressDialogTitle, progressDialogMsg, listener, cls, true);
	}
	
	@Override
	protected AsyncTaskPayload doInBackground(AsyncTaskPayload... params) {
		HttpPost httpRequest = new HttpPost(postUrl);
		List<NameValuePair> reqParams = new ArrayList<NameValuePair>();
		addRequest(reqParams);
		String strResult = null;
		
		try {
			httpRequest.setEntity(new UrlEncodedFormEntity(reqParams, HTTP.UTF_8));
			HttpResponse httpResponse = new DefaultHttpClient().execute(httpRequest);
			
			// Log.d("test4", "http status code = " + (httpResponse.getStatusLine().getStatusCode()));
			if(httpResponse.getStatusLine().getStatusCode() == 200){
				strResult = EntityUtils.toString(httpResponse.getEntity());
				// Log.d("test4", "strResult = " + strResult);
			}
		} catch(Exception e) {
			// Log.d("test4", "exception: " + e.toString());
		}

		AsyncTaskPayload result = new AsyncTaskPayload();
		// testing code
//		result.putValue("status", "success");
//		result.putValue("msg", "success");
		
		try {
			JSONObject jsonObject = new JSONObject(strResult);
			String status = jsonObject.getString("status");
			String msg = jsonObject.getString("msg");
			result.putValue("status", status);
			result.putValue("msg", msg);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		return result;
	}

	public void setRequest(Map<String,String> request){
		this.request = request;
	}
	
	public void addRequest(List<NameValuePair> reqParams){
		Set<String> keys = request.keySet();
		Iterator<String> it = keys.iterator();
		while(it.hasNext()){
			String key = it.next();
			reqParams.add(new BasicNameValuePair(key, request.get(key)));
		}
	}
}
