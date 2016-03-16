package com.colonycount.cklab.asynctask;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Context;
import android.util.Log;

import com.colonycount.cklab.activity.R;
import com.colonycount.cklab.asynctask.base.AsyncTaskCompleteListener;
import com.colonycount.cklab.asynctask.base.AsyncTaskPayload;
import com.colonycount.cklab.asynctask.base.BaseAsyncTask;
import com.colonycount.cklab.model.ImgSearchFilter;

public class GetImgAsyncTask extends BaseAsyncTask {
	private String userId;
	
	public GetImgAsyncTask(Context context, String progressDialogTitle, String progressDialogMsg, AsyncTaskCompleteListener<Boolean> listener, Class<?> cls, boolean showDialog, String userId) {
		super(context, progressDialogTitle, progressDialogMsg, listener, cls, showDialog);
		this.userId = userId;
	}
	
	@Override
	protected AsyncTaskPayload doInBackground(AsyncTaskPayload... params) {
		String postUrl = "http://proxy.colonycounter.cklab.org/php/db_connect.php";
		
		HttpPost httpRequest = new HttpPost(postUrl);
		List<NameValuePair> reqParams = new ArrayList<NameValuePair>();
		
		AsyncTaskPayload result = new AsyncTaskPayload();
		
		if(params.length > 0){
			if(params[0].getValue("search_colony").equals("true")){
				addRequest(reqParams, params[0].getImgSearchFilter());
				result.putValue("search_colony", "true");
			}
		} else {
			addRequest(reqParams);
		}
		
		String strResult = null;
		
		try {
			httpRequest.setEntity(new UrlEncodedFormEntity(reqParams, HTTP.UTF_8));
			HttpResponse httpResponse = new DefaultHttpClient().execute(httpRequest);
			
			// Log.d("test4", "status code = " + httpResponse.getStatusLine().getStatusCode());
			if(httpResponse.getStatusLine().getStatusCode() == 200){
				strResult = EntityUtils.toString(httpResponse.getEntity());
				JSONObject jsonObject = new JSONObject(strResult);
				String status = jsonObject.getString("status");
				
				// Log.d("test4", "status = " + status);
				
				try {
					String test = jsonObject.getString("sql");
					// Log.d("test4", "sql = " + test);
				} catch(Exception e){
					// Log.d("test4", "sql = no sql result");
				}
				if(status.equals("success")){
					JSONArray jsonArray = jsonObject.getJSONArray("result");
					result.putValue("result", "success");
					result.setImageInfoList(jsonArray);
				} else if(status.equals("error")){
					result.putValue("result", "error");
				}
			} else {
				result.putValue("result", "error");
				result.putValue("msg", "httpResponse status code = " + httpResponse.getStatusLine().getStatusCode());
			}
		} catch(Exception e) {
			result.putValue("result", "error");
		}
		
		return result;
	}

	public void addRequest(List<NameValuePair> reqParams){
		reqParams.add(new BasicNameValuePair("get_img_data", "true"));
		reqParams.add(new BasicNameValuePair("user_id", userId));
	}
	
	public void addRequest(List<NameValuePair> reqParams, ImgSearchFilter searchFilter){
		List<Date> dates = searchFilter.getDateRange();
		Set<String> types = searchFilter.getColonyType();
		List<Integer> nums = searchFilter.getDilutionNumberRange();
		Set<String> expParams = searchFilter.getExpParam();
		
		// build date string
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		StringBuilder builder = new StringBuilder();
		for(int i = 0; i < dates.size(); i++){
			if(i == dates.size() - 1)
				builder.append(format.format(dates.get(i)));
			else
				builder.append(format.format(dates.get(i)) + ",");
		}
		String dateStr = builder.toString();
		
		// build type string
		builder = new StringBuilder();
		Iterator<String> ite = types.iterator();
		while(ite.hasNext()){
			builder.append(ite.next() + ",");
		}
		builder.deleteCharAt(builder.length()-1);
		String typeStr = builder.toString();
		
		// build nums string
		builder = new StringBuilder();
		for(int i = 0; i < nums.size(); i++){
			if(i == nums.size() - 1)
				builder.append(nums.get(i));
			else
				builder.append(nums.get(i) + ",");
		}
		String numStr = builder.toString();
		
		// build exp param string
		builder = new StringBuilder();
		ite = expParams.iterator();
		while(ite.hasNext()){
			builder.append(ite.next() + ",");
		}
		builder.deleteCharAt(builder.length()-1);
		String expParamStr = builder.toString();
		
		// Log.d("test4", "dateStr = " + dateStr);
		// Log.d("test4", "typeStr = " + typeStr);
		// Log.d("test4", "numStr = " + numStr);
		// Log.d("test4", "expParamStr = " + expParamStr);
		
		reqParams.add(new BasicNameValuePair("get_img_data", "true"));
		reqParams.add(new BasicNameValuePair("user_id", userId));
		reqParams.add(new BasicNameValuePair("search_colony", "true"));
		reqParams.add(new BasicNameValuePair("search_date", dateStr));
		reqParams.add(new BasicNameValuePair("search_type", typeStr));
		reqParams.add(new BasicNameValuePair("search_dilution_num", numStr));
		reqParams.add(new BasicNameValuePair("search_exp_param", expParamStr));
	}
}
