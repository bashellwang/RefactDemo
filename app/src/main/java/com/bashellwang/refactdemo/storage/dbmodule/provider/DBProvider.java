package com.bashellwang.refactdemo.storage.dbmodule.provider;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.provider.BaseColumns;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;

import com.bashellwang.refactdemo.App;
import com.bashellwang.refactdemo.storage.dbmodule.DBManager;
import com.bashellwang.refactdemo.storage.dbmodule.dbconfig.DBConfigOption;

import java.util.HashMap;
import java.util.List;

/**
 * Created by liang.wang on 2019/3/7.
 */
public class DBProvider extends ContentProvider {
    private static final String TAG = DBProvider.class.getSimpleName();


    public static final String AUTHORITY = "com.airpay.dbprovider";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);

    public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE;// 获取多行数据
    public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE;// 获取单行数据

    private static final UriMatcher mMatcher;
    //访问表的所有列
    public static final int INCOMING_CONTENT_LIST = 0;
    //访问单独的列
    public static final int INCOMING_CONTENT_SINGLE = 1;

    private static List<String> mTableNames;
    private static HashMap<Integer, String> mTableNameCodePairs = new HashMap<>();

    static {

        DBConfigOption configOption = new DBConfigOption.Builder().setDbName("testUnit.db").build();

        Log.e(TAG, "getApplicationContext : " + App.getContext());
        DBManager.getInstance().init(App.getContext(), null, configOption);
        mTableNames = DBManager.getInstance().getAllTableNames();

        mMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        // 初始化注册
        if (mTableNames != null) {
            for (int i = 0; i < mTableNames.size(); i++) {
                // 注册所有的表。。。每个表均有多行和单行处理
                // 如果 match 成功后则返回注册时对应的匹配码
                mMatcher.addURI(AUTHORITY, mTableNames.get(i), i * 2 + INCOMING_CONTENT_LIST);
                mMatcher.addURI(AUTHORITY, mTableNames.get(i) + "/#", i * 2 + +INCOMING_CONTENT_SINGLE);
                mTableNameCodePairs.put(i, mTableNames.get(i));
            }
        }
    }

    @Override
    public boolean onCreate() {
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        Log.e(TAG, "query : " + uri);
        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();

        int matchCode = mMatcher.match(uri);
        int code = matchCode % 2;
        int index = matchCode / 2;
        switch (code) {
            case INCOMING_CONTENT_LIST:
                queryBuilder.setTables(mTableNameCodePairs.get(index));
                break;
            case INCOMING_CONTENT_SINGLE:
//                long id = ContentUris.parseId(uri);
                String id = uri.getLastPathSegment();
                queryBuilder.setTables(mTableNameCodePairs.get(index));
                queryBuilder.appendWhere(BaseColumns._ID + " = " + id);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }

        SQLiteDatabase db = DBManager.getInstance().getmDataBase().getSQLiteDataBase();
        Cursor cursor = queryBuilder.query(db, projection, selection, selectionArgs, null, null, sortOrder);
        cursor.setNotificationUri(getContext().getContentResolver(), uri);

        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        Log.e(TAG, "getType: " + uri);
        int matchCode = mMatcher.match(uri);

        if (matchCode % 2 == INCOMING_CONTENT_LIST) {
            return CONTENT_TYPE;
        } else if (matchCode % 2 == INCOMING_CONTENT_SINGLE) {
            return CONTENT_ITEM_TYPE;
        } else {
            throw new UnsupportedOperationException("Not yet implemented");
        }

    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        Log.e(TAG, "insert: " + uri);

        int matchCode = mMatcher.match(uri);
        int code = matchCode % 2;
        int index = matchCode / 2;
        SQLiteDatabase db = DBManager.getInstance().getmDataBase().getSQLiteDataBase();
        long id = 0;
//        String path = "";
        Uri newUri = null;

        switch (code) {
            case INCOMING_CONTENT_LIST:
                id = db.insert(mTableNameCodePairs.get(index), null, values);
//                path = BASE_PATH + "/" + id;
                newUri = ContentUris.withAppendedId(uri, id);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);

//        return Uri.parse(path);
        return newUri;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        Log.e(TAG, "delete: " + uri);

        int matchCode = mMatcher.match(uri);
        int code = matchCode % 2;
        int index = matchCode / 2;
        SQLiteDatabase db = DBManager.getInstance().getmDataBase().getSQLiteDataBase();
        int rowsDeleted = 0;
        String id;

        switch (code) {
            case INCOMING_CONTENT_LIST:
                rowsDeleted = db.delete(mTableNameCodePairs.get(index), selection, selectionArgs);
                break;
            case INCOMING_CONTENT_SINGLE:
//                long id = ContentUris.parseId(uri);
                id = uri.getLastPathSegment();
                if (TextUtils.isEmpty(selection)) {
                    rowsDeleted = db.delete(mTableNameCodePairs.get(index), BaseColumns._ID + "=" + id, null);
                } else {
                    rowsDeleted = db.delete(mTableNameCodePairs.get(index), BaseColumns._ID + "=" + id + " and "
                            + selection, selectionArgs);
                }
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);

        return rowsDeleted;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        Log.e(TAG, "update: " + uri);

        int matchCode = mMatcher.match(uri);
        int code = matchCode % 2;
        int index = matchCode / 2;
        SQLiteDatabase db = DBManager.getInstance().getmDataBase().getSQLiteDataBase();
        int rowsUpdated = 0;
        String id;


        switch (code) {
            case INCOMING_CONTENT_LIST:
                rowsUpdated = db.update(mTableNameCodePairs.get(index), values, selection, selectionArgs);
                break;
            case INCOMING_CONTENT_SINGLE:
//                long id = ContentUris.parseId(uri);
                id = uri.getLastPathSegment();
                if (TextUtils.isEmpty(selection)) {
                    rowsUpdated = db.update(mTableNameCodePairs.get(index), values, BaseColumns._ID + "=" + id, null);
                } else {
                    rowsUpdated = db.update(mTableNameCodePairs.get(index), values, BaseColumns._ID + "=" + id
                            + " and " + selection, selectionArgs);
                }
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);

        return rowsUpdated;
    }

}
