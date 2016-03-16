/*******************************************************************************
 * Copyright 2011, 2012 Chris Banes.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/
package com.colonycount.cklab.activity;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Region;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.Toast;

import com.colonycount.cklab.asynctask.CountColonyAsyncTask;
import com.colonycount.cklab.asynctask.base.AsyncTaskCompleteListener;
import com.colonycount.cklab.asynctask.base.AsyncTaskPayload;
import com.colonycount.cklab.libs.crop.PhotoView;
import com.colonycount.cklab.libs.crop.PhotoViewAttacher;
import com.colonycount.cklab.libs.crop.PhotoViewAttacher.OnMatrixChangedListener;
import com.colonycount.cklab.model.DataWrapper;

import edu.ntu.esoe.cklab.colonycountcore.Config.Global;
import edu.ntu.esoe.cklab.colonycountcore.Components.DisplayColony;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import com.crashlytics.android.Crashlytics;
import io.fabric.sdk.android.Fabric;

public class CropPhotoActivity extends Activity implements View.OnClickListener, AsyncTaskCompleteListener<Boolean>{
    private PhotoViewAttacher mAttacher;

    private PhotoView mImageView;
    
    // my code
    private Bitmap mBitmap;
    private Bitmap croppedImage;
    
    private ImageButton btnCount;
    
    private ImageButton btnClose;
    
    private int screenWidth;
    
    private int screenHeight;

    private RectF displayRect;

    private AsyncTaskCompleteListener<Boolean> asyncTaskCompleteListener;
    
    
//    private int colonyNum;
    
    @SuppressWarnings("deprecation")
	@Override
    public void onCreate(Bundle savedInstanceState) {
		//從手機選照片後
		Log.d("Step", "CropPhotoActivity-onCreate");
		super.onCreate(savedInstanceState);

		Fabric.with(this, new Crashlytics());

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.layout_crop_photo);
        
        mImageView = (PhotoView) findViewById(R.id.iv_photo);
        btnCount = (ImageButton) findViewById(R.id.btn_count);
        btnClose = (ImageButton) findViewById(R.id.btn_close);
        asyncTaskCompleteListener = this;
        
        Uri photoUri = getIntent().getData();
        /* get colony number to send to next asynctask */
//        String photoPath = getRealPathFromURI(photoUri);
//        colonyNum = Integer.parseInt(photoPath.substring(photoPath.lastIndexOf("(")+1, photoPath.lastIndexOf(")")));
        
        
        mBitmap = decodeSampledBitmapFromResource(getContentResolver(), photoUri, Global.CROP_REQUEST_WIDTH, Global.CROP_REQUEST_HEIGHT);
        
        Display display = getWindowManager().getDefaultDisplay(); 
        screenWidth = display.getWidth();    // deprecated
        screenHeight = display.getHeight();  // deprecated
        
        // my code
        // TODO: get photo rotation to display
        mImageView.setImageBitmap(mBitmap);
//        mImageView.setRotation(getOrientation(getContentResolver(), photoUri));
        mImageView.setScreenWidth(screenWidth);
        mImageView.setScreenHeight(screenHeight);
        
        // The MAGIC happens here!
        mAttacher = new PhotoViewAttacher(mImageView);
        // my code
        mAttacher.setScreenWidth(screenWidth);
        mAttacher.setScreenHeight(screenHeight);
        
        // Lets attach some listeners, not required though!
        mAttacher.setOnMatrixChangeListener(new MatrixChangeListener());
        
        btnCount.setOnClickListener(this);
        btnClose.setOnClickListener(this);
    }
    
    private String getRealPathFromURI(Uri contentURI) {
        String result;
        Cursor cursor = getContentResolver().query(contentURI, null, null, null, null);
        if (cursor == null) { // Source is Dropbox or other similar local file path
            result = contentURI.getPath();
        } else { 
            cursor.moveToFirst(); 
            int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA); 
            result = cursor.getString(idx);
            cursor.close();
        }
        return result;
    }
    
    
    public static Bitmap decodeSampledBitmapFromResource(ContentResolver contentResolver, Uri resUri, int reqWidth, int reqHeight) {
        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        InputStream imageStream = null;
		try {
			imageStream = contentResolver.openInputStream(resUri);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}  
		
        BitmapFactory.decodeStream(imageStream, null, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        
		try {
			imageStream = contentResolver.openInputStream(resUri);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
        return BitmapFactory.decodeStream(imageStream, null, options);
    }
    
    
    public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
	    // Raw height and width of image
	    final int height = options.outHeight;
	    final int width = options.outWidth;
//	    // Log.d("test4", "width = " + width + ", height = " + height);
	    int inSampleSize = 1;
	
	    if (height > reqHeight || width > reqWidth) {
	
	        final int halfHeight = height / 2;
	        final int halfWidth = width / 2;
	
	        // Calculate the largest inSampleSize value that is a power of 2 and keeps both
	        // height and width larger than the requested height and width.
	        while((halfHeight / inSampleSize) > reqHeight || (halfWidth / inSampleSize) > reqWidth) {
	            inSampleSize *= 2;
	        }
	    }
	
//	    // Log.d("test4", "inSampleSize = " + inSampleSize);
	    return inSampleSize;
	}
    
    
    /**
	 * 
	 * @param contentResolver
	 * @param photoUri
	 * @return
	 */
	public static int getOrientation(ContentResolver contentResolver, Uri photoUri) {
	    Cursor cursor = contentResolver.query(photoUri, new String[] { MediaStore.Images.ImageColumns.ORIENTATION }, null, null, null);
	    
	    if (cursor.getCount() != 1) {
	        return -1;
	    }

	    cursor.moveToFirst();
	    return cursor.getInt(0);
	}
    

    @Override
    public void onDestroy() {
        super.onDestroy();

        // Need to call clean-up
        mAttacher.cleanup();
    }

    
    private class MatrixChangeListener implements OnMatrixChangedListener {
        @Override
        public void onMatrixChanged(RectF rect) {
        	displayRect = new RectF(rect);
//        	float left = displayRect.left;
//        	float top = displayRect.top;
//        	float right = displayRect.right;
//        	float bottom = displayRect.bottom;
//        	// Log.d("test4", "left = " + left + ", top = " + top + ", right = " + right + ", bottom = " + bottom);
        }
    }

	@Override
	public void onClick(View v) {
		if(v.getId() == R.id.btn_count){
			croppedImage = Bitmap.createBitmap(Global.OUTPUT_IMAGE_WIDTH, Global.OUTPUT_IMAGE_HEIGHT, Bitmap.Config.ARGB_8888);
			int imageWidth = mImageView.getWidth();
			int imageHeight = mImageView.getHeight();
			int radius = (Math.min(Math.min(screenWidth, screenHeight), Math.min(imageWidth, imageHeight)) - 20) / 2;
			int paddingX = (imageWidth - radius*2) / 2;
			int paddingY = (imageHeight - radius*2) / 2;
			Canvas canvas = new Canvas(croppedImage);
			
			float dWidth = displayRect.width()-paddingX*2;
			float dHeight = displayRect.height()-paddingY*2;
			float leftPerc = Math.abs(displayRect.left) / dWidth;
			float topPerc = Math.abs(displayRect.top) / dHeight;
			float rightPerc = (Math.abs(displayRect.left)+2*radius) / dWidth;
			float botPerc = (Math.abs(displayRect.top)+2*radius) / dHeight;
			
//			// Log.d("test4", "leftPerc = " + leftPerc + ", topPerc = " + topPerc + ", rightPerc = " + rightPerc + ", botPerc = " + botPerc);
			
			Rect srcRect = new Rect((int)(mBitmap.getWidth()*leftPerc), 
									(int)(mBitmap.getHeight()*topPerc), 
									(int)(mBitmap.getWidth()*rightPerc), 
									(int)(mBitmap.getHeight()*botPerc));
			Rect desRect = new Rect(0, 0, Global.OUTPUT_IMAGE_WIDTH, Global.OUTPUT_IMAGE_HEIGHT);
			canvas.drawBitmap(mBitmap, srcRect, desRect, null);
			
			// release memory
			if(mBitmap != null && !mBitmap.isRecycled()){
				mBitmap.recycle();
				mBitmap = null;
			}
			
			Path p = new Path();
            p.addCircle(Global.OUTPUT_IMAGE_WIDTH / 2F, Global.OUTPUT_IMAGE_HEIGHT / 2F, Global.OUTPUT_IMAGE_WIDTH / 2F, Path.Direction.CW);
            canvas.clipPath(p, Region.Op.DIFFERENCE);
            canvas.drawColor(0x00000000, PorterDuff.Mode.CLEAR);
			
            AsyncTaskPayload asyncTaskPayload = new AsyncTaskPayload();
			asyncTaskPayload.setRawImg(croppedImage);
//			asyncTaskPayload.setColonyNum(colonyNum);
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB){
				new CountColonyAsyncTask(this, getString(R.string.system_info), getString(R.string.counting_dialog_msg), asyncTaskCompleteListener, CountColonyAsyncTask.class, true).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, asyncTaskPayload);
			} else {
				new CountColonyAsyncTask(this, getString(R.string.system_info), getString(R.string.counting_dialog_msg), asyncTaskCompleteListener, CountColonyAsyncTask.class, true).execute(asyncTaskPayload);
			}


//			String filename = "bitmap.png";
//			FileOutputStream stream = null;
//			try {
//				stream = openFileOutput(filename, Context.MODE_PRIVATE);
//				croppedImage.compress(Bitmap.CompressFormat.PNG, 100, stream);
//				stream.close();
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
//			Intent intent = new Intent(this, Test.class);
//			startActivity(intent);
		} else if(v.getId() == R.id.btn_close){
			finish();
		}
	}


	@Override
	public void onTaskComplete(AsyncTaskPayload result, String taskName) {
		// release memory
		if(croppedImage != null && !croppedImage.isRecycled()){
			croppedImage.recycle();
			croppedImage = null;
		}

		if(taskName.equals("CountColonyAsyncTask")){
			if(result.getValue("result").equals("success")){
//				mImageView.setImageBitmap(result.getRawImg());

				Intent intent = new Intent(this, ResultActivity.class);
				intent.putExtra("image", result.getValue("image"));
                List<DisplayColony> components = result.getDisplayColony();
				intent.putExtra("imageComponent", new DataWrapper(components));
				startActivity(intent);
				finish();
			} else {
				Toast.makeText(this, getString(R.string.counting_dialog_error_msg), Toast.LENGTH_LONG).show();
			}
			
//			finish();
		}
	}


	@Override
	protected void onStop() {
		super.onStop();
		
		// Log.d("test4", "CropPhotoActivity onStop");
		
		if(isFinishing() && mBitmap != null && !mBitmap.isRecycled()){
			mBitmap.recycle();
			mBitmap = null;
		}
		
		if(isFinishing() && croppedImage != null && !croppedImage.isRecycled()){
			croppedImage.recycle();
			croppedImage = null;
		}
	}
} 