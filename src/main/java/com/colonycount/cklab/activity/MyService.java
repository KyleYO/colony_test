package com.colonycount.cklab.activity;

/**
 * Created by kyle on 2016/1/29.
 */

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.RectF;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.colonycount.cklab.MyApplication;
import com.colonycount.cklab.activity.base.BaseActivity;
import com.colonycount.cklab.asynctask.CountColonyAsyncTask;
import com.colonycount.cklab.asynctask.SaveImgAsyncTask;
import com.colonycount.cklab.asynctask.base.AsyncTaskCompleteListener;
import com.colonycount.cklab.asynctask.base.AsyncTaskPayload;
import com.colonycount.cklab.libs.crop.HighlightView;
import com.colonycount.cklab.libs.crop.PhotoView2;
import com.colonycount.cklab.model.DataWrapper;
import com.colonycount.cklab.model.ImgInfo;
import com.google.android.gms.common.api.GoogleApiClient;

import edu.ntu.esoe.cklab.colonycountcore.Components.DisplayColony;

public class MyService extends Service implements AsyncTaskCompleteListener<Boolean> {


    private Handler handler = new Handler();
    protected GoogleApiClient mGoogleApiClient;
    public static String TAG = "BGService";
    private String count_photo;
    private Bitmap rawImg;
    private Context context = this;
    private AsyncTaskCompleteListener<Boolean> asyncTaskCompleteListener = this;
    private int n=0;
    private ImgInfo imgInfo;
    private int colonyCount;
    private Bitmap mBitmap;
    private PhotoView2 image;
    static final int ORIGINAL_COLONY = 1;
    private List<HighlightView> colonyList = new ArrayList<HighlightView>();
    private List<HighlightView> colonyRemovedList = new ArrayList<HighlightView>();
    private int num = 0 ;
    private Queue<String> img_queue = new LinkedList<>();


    private Runnable showTime = new Runnable() {
        public void run() {
            // log目前時間

            //Log.d(TAG, new Date().toString());
            // Log.d("DB", DB.show("Select * From " + ItemDAO.TABLE_NAME));
            //Log.d(TAG, MainActivity.DB.show("Select * From "+ ItemDAO.TABLE_NAME));

            if ( MyApplication.getInstance().loginState ) {


                //等待計算的colony照片
                ArrayList<String> count_queue = new ArrayList<String>(MainActivity.DB.show("Select " + ItemDAO.PhotoName_COLUMN + " From " + ItemDAO.TABLE_NAME + " Where " + ItemDAO.Finished + " = 'False'"));//+ " Where " + ItemDAO.FinishDate_COLUMN + " is NULL"));

                if (count_queue.isEmpty()) {
                    //全部照片都算完了
                    Log.d(TAG, "Counting queue is empty!");
                    MyApplication.getInstance().LockOff();

                } else {

                    Log.d(TAG, "Counting start ~ ~ ~!");
                    for (int i = 0; i < count_queue.size(); i++) {
                        //Log.d(TAG, count_queue.get(i));

//                        Log.d(TAG, " While start!");
//                        //確保前一個已經算完再開始算
//                        while(MyApplication.getInstance().getLockState());
//                        MyApplication.getInstance().LockOn();

                        img_queue.add(count_queue.get(i));
                        Log.d(TAG, "i:"+i+" queue:" + (count_queue.get(0) + "  now:" + count_queue.get(i)));
//                        while(img_queue.element()!=count_queue.get(i))
//                            try {
//                                Log.d(TAG, "QQ_Sleeping");
//                                Thread.sleep(2000);
//                            } catch (InterruptedException e) {
//                                e.printStackTrace();
//                            }
                        count_photo = TakePhotoActivity.fileDir + "/" + count_queue.get(i);

                        if (new File(count_photo).exists()) {

                            rawImg = BitmapFactory.decodeFile(count_photo);

                            AsyncTaskPayload asyncTaskPayload = new AsyncTaskPayload();
                            asyncTaskPayload.setRawImg(rawImg);


                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                                Log.d(TAG, "RawImg - " + rawImg + " | Image : " + count_photo + " | context: "+context +" | asynTask : "+asyncTaskCompleteListener+" | countclass : "+CountColonyAsyncTask.class+" | asynThread : "+AsyncTask.THREAD_POOL_EXECUTOR+" | asynPlayload : "+asyncTaskPayload);
                                new CountColonyAsyncTask(context, getString(R.string.system_info), getString(R.string.counting_dialog_msg), asyncTaskCompleteListener, CountColonyAsyncTask.class, true).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, asyncTaskPayload);
                            } else {
                                Log.d(TAG, "rawImg - " + rawImg + " | Image : " + count_photo + " | context: "+context +" | asynTask : "+asyncTaskCompleteListener+" | countclass : "+CountColonyAsyncTask.class+" | asynThread : "+AsyncTask.THREAD_POOL_EXECUTOR+" | asynPlayload : "+asyncTaskPayload);
                                new CountColonyAsyncTask(context, getString(R.string.system_info), getString(R.string.counting_dialog_msg), asyncTaskCompleteListener, CountColonyAsyncTask.class, true).execute(asyncTaskPayload);
                            }
                            if(!MainActivity.DB.countFinished(count_queue.get(i)))
                                Log.d(TAG, count_photo + " DB update error");
                            else
                                Log.d(TAG, count_photo + " count completed!");
                        } else {
                            Log.d(TAG, count_photo + " not found.");
                        }
                    }
                }
            }
            handler.postDelayed(this, 20000);
        }
    };

    @Override
    public IBinder onBind(Intent arg0) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void onCreate() {
        // TODO Auto-generated method stub
        super.onCreate();
        Log.i("服務", "建立");
        Log.d("DB", "建立");

    }

    @Override
    public void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        Log.i("服務", "銷毀");
        Log.d("DB", "銷毀");
        handler.removeCallbacks(showTime);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // TODO Auto-generated method stub
        Log.i("服務", "執行");
        Log.d("DB", "執行");
        handler.postDelayed(showTime, 1000);
        return super.onStartCommand(intent, flags, startId);


    }

    @Override
    public void onTaskComplete(AsyncTaskPayload result, String taskName) {

        num++;
        Log.d(TAG, "onTaskComplete start : "+num);
//        imgInfo = new ImgInfo();
//        imgInfo.setColonyCount(colonyCount);
        String filename = result.getValue("image");
        Log.d(TAG, "get filename : " + count_photo);

        try {
            FileInputStream is = this.openFileInput(filename);
            mBitmap = BitmapFactory.decodeFile(count_photo);
            Log.d(TAG, "mBitmap : " + mBitmap);
            // Log.d("test", "mBitmap height = " + mBitmap.getHeight() + ", width = " + mBitmap.getWidth());
            is.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.d(TAG, "image : " + image);


        //拿到計算後的colony資料結構
        DataWrapper dw = new DataWrapper(result.getDisplayColony());
        List<DisplayColony> components = dw.getParliaments();

//        for(int i = 0; i < components.size(); i++){
//            DisplayColony component = components.get(i);
//            HighlightView hv = getHighlightView((int) component.getCenterX(), (int) component.getCenterY(), component.getRadius(), ORIGINAL_COLONY);
//            image.addColony(hv);
//            //colonyList.add(hv);
//        }
        //colonyCount = image.getColonyList().size();
        colonyCount = components.size();
        //colonyCount = 0;
        Log.d(TAG, "get count : " + colonyCount);

        // set colonyList, colonyRemovedList
//        imgInfo.setColonyList(colonyList);
//        imgInfo.setColonyRemovedList(colonyRemovedList);

        // raw image
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        Log.d(TAG,"Bitmap: "+result.getValue("image")+"  count: "+colonyCount);
        mBitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);//100 is the best quality possibe
        byte[] data_raw = bos.toByteArray();

        Log.d(TAG, "Result: " + result.getValue("image") + "  count: " + colonyCount);
        new SaveImgAsyncTask(context, getString(R.string.system_info), getString(R.string.result_saving_img_msg), null, SaveImgAsyncTask.class, true, data_raw, MyApplication.getInstance().getEmail(), MyApplication.getInstance().getId(), components).execute();
        MyApplication.getInstance().LockOff();
        img_queue.poll();
        /*
        //計算完成後把照片直接存進資料夾
        Log.d(TAG,"Result: "+result.getValue("image"));
        n+=1;
        String input_filename = result.getValue("image");
        new DataWrapper(result.getDisplayColony());
       // String filename = TakePhotoActivity.fileDir + "/"+n+".png";
        FileOutputStream stream = null;

        try {
            FileInputStream is = this.openFileInput(input_filename);
            rawImg = BitmapFactory.decodeStream(is);
            // Log.d("test", "mBitmap height = " + mBitmap.getHeight() + ", width = " + mBitmap.getWidth());
            is.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            //把原圖跟colony資訊傳到server
            stream = new FileOutputStream(filename);
            rawImg.compress(Bitmap.CompressFormat.PNG, 100, stream);// 把數據寫入文件
            stream.close();

        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            try {
                stream.flush();
                stream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        //重新scan檔案，解決實際上存在但在目錄下找不到檔案的問題
        MediaScannerConnection.scanFile(this, new String[]{filename}, null,
                new MediaScannerConnection.OnScanCompletedListener() {
                    @Override
                    public void onScanCompleted(String path, Uri uri) {
                        Log.d("SD", "Scanned " + path + ":");
                        Log.d("SD", "-> uri=" + uri);
                    }
                });
*/
    }
    public HighlightView getHighlightView(int x, int y, int r, int type){
        HighlightView hv = new HighlightView(image);

        int width = mBitmap.getWidth();
        int height = mBitmap.getHeight();

        Rect imageRect = new Rect(0, 0, width, height);
        boolean mCircleCrop = true;

        Matrix mImageMatrix = image.getImageMatrix();

        RectF colonyRect = new RectF(x-r, y-r, x+r, y+r);
        hv.setup(mImageMatrix, imageRect, colonyRect, mCircleCrop, true, type);

        return hv;
    }
/*
    public void onTaskComplete(AsyncTaskPayload result, String taskName) {
        //算完後顯示結果
        Log.d("Step", "onTaskComplete->onPause");
        if(taskName.equals("CountColonyAsyncTask")){
            if(result.getValue("result").equals("success")){
                if(rawImg != null && !rawImg.isRecycled()){
                    rawImg.recycle();
                    rawImg = null;
                }
                //跳到刪減/儲存計算結果的頁面
                Intent intent = new Intent(this, ResultActivity.class);
                intent.putExtra("image", result.getValue("image"));
                intent.putExtra("imageComponent",  new DataWrapper(result.getDisplayColony()));
                startActivity(intent);
                finish();
            } else {
                Toast.makeText(this, "count colony error", Toast.LENGTH_LONG).show();
            }
        }
    }
*/
}

