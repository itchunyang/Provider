package com.itchunyang.providerclient;

import android.content.ContentUris;
import android.content.ContentValues;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void insert(View view) {

        ContentValues values = new ContentValues();

        values.put("name","波多野结衣");
        values.put("age","11");
        values.put("sex","女");
        values.put("address","https://timgsa.baidu.com/timg?image&quality=80&size=b10000_10000&sec=1478860084775&di=9abb9af0944a15ba905b86ba68adcd83&imgtype=0&src=http%3A%2F%2Fpic.yesky.com%2FuploadImages%2F2014%2F349%2F32%2F237899O4I7Y3.png");

        //the row ID of the newly inserted row, or -1 if an error occurred
        Uri uri = getContentResolver().insert(Uri.parse("content://com.itchunyang.girlProvider/girl"), values);

        long id = ContentUris.parseId(uri);
        if(id == -1){
            Toast.makeText(this,"插入失败",Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(this,"插入成功 row ID="+id,Toast.LENGTH_SHORT).show();
        }
    }

    public void insertMany(View view) {

        ContentValues[] values = new ContentValues[5];
        for (int i = 0; i < 5; i++) {
            values[i] = new ContentValues();
            values[i].put("name","波多野结衣"+i);
            values[i].put("age","11");
            values[i].put("sex","女");
            values[i].put("address","深圳");
        }

        int ret = getContentResolver().bulkInsert(Uri.parse("content://com.itchunyang.girlProvider/girl"),values);

        if(ret >= 0)
            Toast.makeText(this,"批量插入成功 cout="+ret,Toast.LENGTH_SHORT).show();
        else
            Toast.makeText(this,"批量插入失败",Toast.LENGTH_SHORT).show();
    }

    public void delete(View view) {
        String where = "name=?";
        String[] selectArgs = new String[]{"波多野结衣2"};

        int count = getContentResolver().delete(Uri.parse("content://com.itchunyang.girlProvider/girl/26"),where,selectArgs);
        Toast.makeText(this,"成功删除了"+count+"个数据",Toast.LENGTH_SHORT).show();
    }

    public void update(View view) {
        ContentValues values = new ContentValues();
        values.put("address","广东");
        values.put("age",99);

        String where = "name=?";
        String[] selectArgs = new String[]{"波多野结衣2"};
        int count = getContentResolver().update(Uri.parse("content://com.itchunyang.girlProvider/girl/18"),values,where,selectArgs);
        Toast.makeText(this,"成功更新了"+count+"个数据",Toast.LENGTH_SHORT).show();
    }

    public void query(View view) {

        //查询所有的
//        Cursor cursor = getContentResolver().query(Uri.parse("content://com.itchunyang.girlProvider/girl"),columns,null,null,null);

        String[] columns = new String[]{"name","id","address"};
        Cursor cursor = getContentResolver().query(Uri.parse("content://com.itchunyang.girlProvider/girl/18"),columns,null,null,null);

        if(cursor != null){
            while (cursor.moveToNext()){
                Toast.makeText(this,cursor.getString(0)+" " + cursor.getString(1)+" " + cursor.getString(2),Toast.LENGTH_SHORT).show();
            }
        }
        cursor.close();
    }

    public void registerObserver(View view) {

        /**
         * notifyForDescendents   为false 表示精确匹配，即只匹配该Uri  为true 表示可以同时匹配其派生的Uri
         * 如果为false，uri必须完全相同，后缀加id也不行!!!!
         *
         * 传递主线程的Handler，否则会有可能会报异常。
         *
         */
        getContentResolver().registerContentObserver(Uri.parse("content://com.itchunyang.girlProvider/girl"),true,new DatabaseObserver(new MyHandler()));
    }

    class DatabaseObserver extends ContentObserver{

        private Handler handler;
        /**
         * Creates a content observer.
         *
         * @param handler The handler to run {@link #onChange} on, or null if none.
         */
        public DatabaseObserver(Handler handler) {
            super(handler);
            this.handler = handler;
        }

        @Override
        public void onChange(boolean selfChange) {
            super.onChange(selfChange);
        }

        /**
         * 当观察到的Uri发生变化时，回调该方法去处理。所有ContentObserver的派生类都需要重载该方法去处理逻辑。
         * 上面的方法也会回调的!
         * @param selfChange
         * @param uri
         */
        @Override
        public void onChange(boolean selfChange, Uri uri) {
            //检测到数据有变化，可以重新查询一下，然后更新ui
            super.onChange(selfChange, uri);
        }

    }

    class MyHandler extends Handler{
        @Override
        public void handleMessage(Message msg) {
        }
    }
}
