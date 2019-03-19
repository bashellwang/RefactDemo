package com.bashellwang.refactdemo.storage.dbmodule;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import java.util.List;

/**
 * Created by liang.wang on 2019/3/6.
 * <p>
 * 这里接口统一封装，如果有差异化方法，需要在子类实现，利用子类实例来完成。
 * 这里建议尽量不实现差异化方法，否则业务代码会有耦合，后续替换底层数据库会比较麻烦
 */
public interface IDataBase {

    List<String> getAllTableNames();

    void init(Context context, String dbName);

    void insert(Object object);

    void insertOrUpdate(Object object);

    void update(Object object);

    void delete(Object object);

    void deleteByKey(Class<? extends Object> classType, String key);

    void deleteAll(Class<? extends Object> classType);

    <T> T queryByKey(Class<T> classType, String key);

    <T> List<T> query(Class<T> classType, String where, String... selectionArg);

    <T> List<T> queryAll(Class<T> classType);

//    Database getDataBase();

    SQLiteDatabase getSQLiteDataBase();
}
