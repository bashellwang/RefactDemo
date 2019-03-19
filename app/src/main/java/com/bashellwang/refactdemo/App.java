package com.bashellwang.refactdemo;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.bashellwang.refactdemo.log.ALog;

/**
 * Created by liang.wang on 2019/3/5.
 */
public class App extends Application {

//    private DaoSession daoSession;

    public static Application application = null;

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        application = this;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        // regular SQLite database
//        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(this, "notes-db");
//        Database db = helper.getWritableDb();
//
//        // encrypted SQLCipher database
//        // note: you need to add SQLCipher to your dependencies, check the build.gradle file
//        // DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(this, "notes-db-encrypted");
//        // Database db = helper.getEncryptedWritableDb("encryption-key");
//
//        daoSession = new DaoMaster(db).newSession();

//        DaoManager.getInstance().init(this,"notes-db");
//        application = this;
//        DBConfigOption configOption = new DBConfigOption.Builder().setDbName("notes-db").build();
//        DBManager.getInstance().init(this,null,configOption);
//        daoSession = DaoManager.getInstance().getDaoSession();
        ALog.init();
    }

//    public DaoSession getDaoSession() {
//        return daoSession;
//    }

    public static Application getContext(){
        Log.e("haha","空："+(application == null));
        return application;
    }

}
