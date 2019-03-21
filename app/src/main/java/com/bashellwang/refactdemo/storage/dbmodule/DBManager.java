package com.bashellwang.refactdemo.storage.dbmodule;

import android.content.Context;

import com.bashellwang.refactdemo.storage.dbmodule.dbconfig.DBConfigOption;
import com.bashellwang.refactdemo.storage.dbmodule.impl.DaoManager;

import java.util.List;

/**
 * Created by liang.wang on 2019/3/6.
 */
public class DBManager {

    private static volatile DBManager mInstance;
    private IDataBase mDataBase;

    private DBManager() {
    }

    public static DBManager getInstance() {
        if (mInstance == null) {
            synchronized (DBManager.class) {
                if (mInstance == null) {
                    mInstance = new DBManager();
                }
            }
        }
        return mInstance;
    }

    public void init(Context context, IDataBase db, DBConfigOption config) {
        if (db != null) {
            mDataBase = db;
        } else {
            mDataBase = DaoManager.getInstance();
        }
        mDataBase.init(context, config.getDbName());
    }

    public IDataBase getmDataBase() {
        return mDataBase;
    }

    public List<String> getAllTableNames(){
        return mDataBase.getAllTableNames();
    }
}
