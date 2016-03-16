package com.colonycount.cklab.asynctask;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Environment;
import android.util.Log;

import com.colonycount.cklab.asynctask.base.AsyncTaskCompleteListener;
import com.colonycount.cklab.asynctask.base.AsyncTaskPayload;
import com.colonycount.cklab.asynctask.base.BaseAsyncTask;

import org.opencv.android.Utils;
import org.opencv.core.Mat;

import edu.ntu.esoe.cklab.colonycountcore.LocalAdaptiveRegionGrowing;
import edu.ntu.esoe.cklab.colonycountcore.Config.Global;
import edu.ntu.esoe.cklab.colonycountcore.Components.DisplayColony;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

public class CountColonyAsyncTask extends BaseAsyncTask {
	private Context context;

	static {
		System.loadLibrary("opencv_java3");
	}

	public CountColonyAsyncTask(Context context, String progressDialogTitle, String progressDialogMsg, AsyncTaskCompleteListener<Boolean> listener, Class<?> cls, boolean showDialog) {

        super(context, progressDialogTitle, progressDialogMsg, listener, cls, showDialog);
		//Log.d("BGService", "CountAsy :  context:" + context + "  title:" + progressDialogTitle + " msg:" + progressDialogMsg + " listener:" + listener + " cls:" + cls + " dialog:" + showDialog);
		this.context = context;
	}

	/**
	 * 
	 */
	@Override
	protected AsyncTaskPayload doInBackground(AsyncTaskPayload... params) {
		AsyncTaskPayload resultPayload = new AsyncTaskPayload();
		// output image with size 1024x1024
		Bitmap croppedImage = params[0].getRawImg();
		
		// create down-scale image with size 512x512 for counting colony
		Bitmap countImage = Bitmap.createScaledBitmap(croppedImage, Global.COUNT_IMAGE_WIDTH, Global.COUNT_IMAGE_HEIGHT, false);

		// save file to pass to another activity
		try {
			String filename = "bitmap.png";
		    FileOutputStream stream = context.openFileOutput(filename, Context.MODE_PRIVATE);
		    croppedImage.compress(Bitmap.CompressFormat.PNG, 100, stream);
		    stream.close();
		    
		    if(croppedImage != null && !croppedImage.isRecycled()){
		    	croppedImage.recycle();
		    	croppedImage = null;
		    }
		    
		    resultPayload.putValue("image", filename);
		} catch (Exception e) {
			resultPayload.putValue("result", "error");
			resultPayload.putValue("msg", e.toString());
			return resultPayload;
		}

        // Log.d("test", "algorithm start");

		Mat matImage = new Mat();
		Utils.bitmapToMat(countImage, matImage);

		LocalAdaptiveRegionGrowing algorithm = new LocalAdaptiveRegionGrowing(matImage);
		List<DisplayColony> components = algorithm.count();
        // Log.d("test", "algorithm end");
		resultPayload.putValue("result", "success");
		resultPayload.setDisplayColony(components);
//		resultPayload.setComponents(components);
		
//		// test
//		resultPayload.setRawImg(algorithm.getResult());
		
		return resultPayload;
		
		
		
		// to get test data
		/*AsyncTaskPayload resultPayload = new AsyncTaskPayload();
		// output image with size 1024x1024
		Bitmap croppedImage = params[0].getRawImg();
		int colonyNum = params[0].getColonyNum();
		// create down-scale image with size 512x512 for counting colony
		Bitmap countImage = Bitmap.createScaledBitmap(croppedImage, Config.COUNT_IMAGE_WIDTH, Config.COUNT_IMAGE_HEIGHT, false);
		// to get test data 
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		countImage.compress(Bitmap.CompressFormat.PNG, 100, stream);
		byte[] byteArray = stream.toByteArray();
		saveColonyImage(byteArray, colonyNum);
		
		return resultPayload;*/
	}
	
	private boolean saveColonyImage(byte[] data, int colonyNum){
		File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "ColonyCount");
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                // Log.d(TAG, "failed to create directory");
                return false;
            }
        }
        
        int fileNumbers = mediaStorageDir.listFiles().length;
        
        // Create a media file name
//        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.TAIWAN).format(new Date());
        File pictureFile = null;
        
        String filePath = mediaStorageDir.getPath() + File.separator + (fileNumbers+1) + "_" + colonyNum + ".jpg";
    	pictureFile = new File(filePath);
        
        if(pictureFile == null){
        	// Log.d(TAG, "Error creating media file, check storage permissions: ");
        	return false;
        } 
        
        try {
            FileOutputStream fos = new FileOutputStream(pictureFile);
            fos.write(data);
            fos.close();
        } catch (FileNotFoundException e) {
            // Log.d(TAG, "File not found: " + e.getMessage());
            return false;
        } catch (IOException e) {
            // Log.d(TAG, "Error accessing file: " + e.getMessage());
            return false;
        }
        
        // Log.d(TAG, "save colony image successfully!");
        return true;
	}
}
