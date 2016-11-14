package com.itchunyang.provider;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.text.TextUtils;

/**
 * Created by lcy on 2016/11/11.
 */

public class DatabaseProvider extends ContentProvider {

    private DBOpenHelper dbOpenHelper;

    @Override
    public boolean onCreate() {
        dbOpenHelper = new DBOpenHelper(getContext(), ContentData.DATABASE_NAME);
        return true;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
        Cursor cursor = null;

        switch (ContentData.MATCHER.match(uri)){
            case ContentData.GIRLS:
                cursor = db.query(ContentData.Table.TABLE_NAME,projection,selection,selectionArgs,null,null,sortOrder);
                break;

            case ContentData.GIRL:
                long id = ContentUris.parseId(uri);
                String whereClause = "id=" + String.valueOf(id);
                if (!TextUtils.isEmpty(selection))
                    whereClause = whereClause + " and " + selection;
                cursor = db.query(ContentData.Table.TABLE_NAME,projection,whereClause,selectionArgs,null,null,sortOrder);
                break;
        }
        return cursor;
    }

    /**
     * 如果我们在getType方法中返回一个null或者是返回一个自定义的android不能识别的MIME类型，那么当我们在query方法中返回Cursor的时候，
     * 系统要对Cursor进行分析，进而得出结论，知道该Cursor只有一条数据还是有多条数据，但是如果我们按照Google的建议，手动的返回了相应的MIME，
     * 那么系统就不会自己去分析了，这样可以提高一丢点的系统性能
     *
     * 如果操作的数据属于集合类型，那么MIME类型字符串应该以vnd.android.cursor.dir/开头
     * * 如果要操作的数据属于单一数据，那么MIME类型字符串应该以vnd.android.cursor.item/开头
     */
    @Nullable
    @Override
    public String getType(Uri uri) {
        System.out.println("---->");
        switch (ContentData.MATCHER.match(uri)){
            case ContentData.GIRL:
                // 查询多条数据
                return "vnd.android.cursor.dir/multi";
            case ContentData.GIRLS:
                // 根据id或者姓名查询一条数据
                return "vnd.android.cursor.item/single";
        }
        return null;
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        SQLiteDatabase db = dbOpenHelper.getWritableDatabase();

        //返回新添记录的行号，与主键id无关
        long id = db.insert(ContentData.Table.TABLE_NAME, null, values);
        getContext().getContentResolver().notifyChange(uri,null);
        return ContentUris.withAppendedId(uri, id);
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        int effectRows = -1;
        SQLiteDatabase db = dbOpenHelper.getWritableDatabase();

        switch (ContentData.MATCHER.match(uri)) {
            case ContentData.GIRLS:
                effectRows = db.delete(ContentData.Table.TABLE_NAME, selection, selectionArgs);
                getContext().getContentResolver().notifyChange(uri,null);
                break;
            case ContentData.GIRL:
                long id = ContentUris.parseId(uri);
                String whereClause = "id=" + String.valueOf(id);
                if (!TextUtils.isEmpty(selection))
                    whereClause = whereClause + " and " + selection;

                effectRows = db.delete(ContentData.Table.TABLE_NAME, whereClause, selectionArgs);
                getContext().getContentResolver().notifyChange(uri,null);
                break;
        }

        db.close();
        return effectRows;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        SQLiteDatabase db = dbOpenHelper.getWritableDatabase();

        int effectRows = 0;
        switch (ContentData.MATCHER.match(uri)){
            case ContentData.GIRLS:
                effectRows = db.update(ContentData.Table.TABLE_NAME,values,selection,selectionArgs);
                getContext().getContentResolver().notifyChange(uri,null);
                break;

            case ContentData.GIRL:
                long id = ContentUris.parseId(uri);
                String whereClause = "id="+String.valueOf(id);
                if(!TextUtils.isEmpty(selection)){
                    whereClause = whereClause +" and " + selection;
                }
                effectRows = db.update(ContentData.Table.TABLE_NAME,values,whereClause,selectionArgs);
                getContext().getContentResolver().notifyChange(uri,null);
                break;
        }
        db.close();
        return effectRows;
    }

    //对应的是resolver.bulkInsert 批量插入的方法
    @Override
    public int bulkInsert(Uri uri, ContentValues[] values) {

        SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
        db.beginTransaction();
        try {
            int count = values.length;
            for (int i = 0; i < count; i++) {
                if (db.insert(ContentData.Table.TABLE_NAME, null, values[i]) < 0) {
                    return 0;
                }
            }

            db.setTransactionSuccessful();
            getContext().getContentResolver().notifyChange(uri,null);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
            db.close();
        }

        //he number of values that were inserted.
        //return super.bulkInsert(uri, values);

        return values.length;
    }
}
