package com.bashellwang.refactdemo.storage.dbmodule.impl;

import android.content.Context;

import com.bashellwang.refactdemo.greendao.DaoMaster;
import com.bashellwang.refactdemo.greendao.NoteDao;
import com.bashellwang.refactdemo.greendao.UserDao;
import com.github.yuweiguocn.library.greendao.MigrationHelper;

import org.greenrobot.greendao.database.Database;

/**
 * Created by liang.wang on 2019/3/10.
 */
public class DaoOpenHelper extends DaoMaster.OpenHelper {
    public DaoOpenHelper(Context context, String name) {
        super(context, name);
    }

    @Override
    public void onUpgrade(Database db, int oldVersion, int newVersion) {
        MigrationHelper.migrate(db, new MigrationHelper.ReCreateAllTableListener() {

            @Override
            public void onCreateAllTables(Database db, boolean ifNotExists) {
                DaoMaster.createAllTables(db, ifNotExists);
            }

            @Override
            public void onDropAllTables(Database db, boolean ifExists) {
                DaoMaster.dropAllTables(db, ifExists);
            }
        }, NoteDao.class, UserDao.class);
    }
}