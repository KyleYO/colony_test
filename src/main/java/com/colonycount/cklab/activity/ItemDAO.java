package com.colonycount.cklab.activity;

/**
 * Created by kyle on 2016/1/29.
 */

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import android.content.ClipData;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;
import android.util.Log;

// 資料功能類別
public class ItemDAO {
    // 表格名稱
    public static final String TABLE_NAME = "colony_list";

    // 編號表格欄位名稱，固定不變
    public static final String KEY_ID = "_id";

    // 其它表格欄位名稱
    public static final String PhotoName_COLUMN = "photoName";
    public static final String TakenDate_COLUMN = "takenDate";
    public static final String Finished = "finished";
    public static final String CountTime_COLUMN = "countTime";
    public String TAG = "Error";


    // 使用上面宣告的變數建立表格的SQL指令
    public static final String CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + " (" +
                    KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    PhotoName_COLUMN + " VARCHAR NOT NULL, " +
                    TakenDate_COLUMN + " DATE NOT NULL, " +
                    Finished + " VARCHAR NOT NULL, " +
                    CountTime_COLUMN + " INTEGER)";

    // 資料庫物件
    private SQLiteDatabase db;

    // 建構子，一般的應用都不需要修改
    public ItemDAO(Context context) {
        Log.d(TAG, "Item_Con");
        db = MyDBHelper.getDatabase(context);
    }

    // 關閉資料庫，一般的應用都不需要修改
    public void close() {
        Log.d("DB", "Item_close");
        db.close();
    }

    // 新增參數指定的物件
    /*
    public Item insert(Item item) {
        // 建立準備新增資料的ContentValues物件
        ContentValues cv = new ContentValues();

        // 加入ContentValues物件包裝的新增資料
        // 第一個參數是欄位名稱， 第二個參數是欄位的資料
        cv.put(DATETIME_COLUMN, item.getDatetime());
        //cv.put(COLOR_COLUMN, item.getColor().parseColor());
        cv.put(TITLE_COLUMN, item.getTitle());


        // 新增一筆資料並取得編號
        // 第一個參數是表格名稱
        // 第二個參數是沒有指定欄位值的預設值
        // 第三個參數是包裝新增資料的ContentValues物件
        long id = db.insert(TABLE_NAME, null, cv);

        // 設定編號
        item.setId(id);
        // 回傳結果
        return item;
    }*/

    // 修改參數指定的物件
    //標記DB中已經算完的colony data
    public boolean countFinished(String img_name) {
        // 建立準備修改資料的ContentValues物件
        ContentValues cv = new ContentValues();

        // 加入ContentValues物件包裝的修改資料
        // 第一個參數是欄位名稱， 第二個參數是欄位的資料
        cv.put(Finished, "'True'");
        // cv.put(COLOR_COLUMN, item.getColor().parseColor());
        //cv.put(TITLE_COLUMN, item.getTitle());


        // 設定修改資料的條件為編號
        // 格式為「欄位名稱＝資料」
        String where = PhotoName_COLUMN + " = '" + img_name + "'";

        // 執行修改資料並回傳修改的資料數量是否成功
        return db.update(TABLE_NAME, cv, where, null) > 0;
    }
/*
    // 刪除參數指定編號的資料
    public boolean delete(long id){
        // 設定條件為編號，格式為「欄位名稱=資料」
        String where = KEY_ID + "=" + id;
        // 刪除指定編號資料並回傳刪除是否成功
        return db.delete(TABLE_NAME, where, null) > 0;
    }

    // 讀取所有記事資料
    public List<Item> getAll() {
        List<Item> result = new ArrayList<>();
        Cursor cursor = db.query(
                TABLE_NAME, null, null, null, null, null, null, null);

        while (cursor.moveToNext()) {
            result.add(getRecord(cursor));
        }

        cursor.close();
        return result;
    }

    // 取得指定編號的資料物件
    public Item get(long id) {
        // 準備回傳結果用的物件
        Item item = null;
        // 使用編號為查詢條件
        String where = KEY_ID + "=" + id;
        // 執行查詢
        Cursor result = db.query(
                TABLE_NAME, null, where, null, null, null, null, null);

        // 如果有查詢結果
        if (result.moveToFirst()) {
            // 讀取包裝一筆資料的物件
            item = getRecord(result);
        }

        // 關閉Cursor物件
        result.close();
        // 回傳結果
        return item;
    }

    // 把Cursor目前的資料包裝為物件
    public Item getRecord(Cursor cursor) {
        // 準備回傳結果用的物件
        Item result = new Item();

        result.setId(cursor.getLong(0));
        result.setDatetime(cursor.getLong(1));
        // result.setColor(ItemActivity.getColors(cursor.getInt(2)));
        result.setTitle(cursor.getString(3));
        result.setContent(cursor.getString(4));
        result.setFileName(cursor.getString(5));
        result.setLatitude(cursor.getDouble(6));
        result.setLongitude(cursor.getDouble(7));
        result.setLastModify(cursor.getLong(8));

        // 回傳結果
        return result;
    }
*/
    // 取得資料數量
    public int getCount() {
        Log.d("DB", "Item_getCount");
        int result = 0;
        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM " + TABLE_NAME, null);
        cursor.moveToFirst();
        Log.d("DB", cursor.getString(0));
        return Integer.valueOf(cursor.getString(0));
    }

    public void insert(String photoName){
        ContentValues values = new ContentValues();
        values.put(PhotoName_COLUMN, photoName);
        values.put(TakenDate_COLUMN,new Date().toString());
        values.put(Finished,"False");


        Log.d("DB", "Item_insert : " + photoName);
        db.insert(ItemDAO.TABLE_NAME, null, values);
    }
    public ArrayList<String> show(String q){

        Log.d("DB", "Item_show");
        ArrayList<String> result = new ArrayList<String>(){};
        Cursor cursor = db.rawQuery(q,null);
        //result = String.valueOf(getCount());1

        while (cursor.moveToNext()) {
            result.add(cursor.getString(0));
        }
        cursor.close();

        return result;
    }

    // 建立範例資料
    /*
    public void sample() {
        Item item = new Item(0, new Date().getTime(), "關於Android Tutorial的事情.", "Hello content", "", 0, 0, 0);
        Item item2 = new Item(0, new Date().getTime(), "一隻非常可愛的小狗狗!", "她的名字叫「大熱狗」，又叫\n作「奶嘴」，是一隻非常可愛\n的小狗。", "", 25.04719, 121.516981, 0);
        Item item3 = new Item(0, new Date().getTime(), "一首非常好聽的音樂！", "Hello content", "", 0, 0, 0);
        Item item4 = new Item(0, new Date().getTime(), "儲存在資料庫的資料", "Hello content", "", 0, 0, 0);

        insert(item);
        insert(item2);
        insert(item3);
        insert(item4);
    }*/

}
