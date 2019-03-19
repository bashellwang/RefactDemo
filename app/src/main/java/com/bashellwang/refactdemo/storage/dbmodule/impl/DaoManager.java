package com.bashellwang.refactdemo.storage.dbmodule.impl;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.bashellwang.refactdemo.greendao.DaoMaster;
import com.bashellwang.refactdemo.greendao.DaoSession;
import com.bashellwang.refactdemo.storage.dbmodule.IDataBase;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.query.QueryBuilder;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/**
 * Created by liang.wang on 2019/3/5.
 * 这里是使用 GreenDao 的封装，差异化方法，可单独实现，子类调用
 */
public class DaoManager implements IDataBase {

    private String mDbName;//数据库名称
    private volatile static DaoManager mDaoManager;//多线程访问
//    private static DaoMaster.DevOpenHelper mHelper;
    private static DaoOpenHelper mHelper;
    private static DaoMaster mDaoMaster;
    private static DaoSession mDaoSession;
    private Context mContext;

    private static SQLiteDatabase mSqlDb;

    /**
     * 使用单例模式获得操作数据库的对象
     */
    public static DaoManager getInstance() {
        if (mDaoManager == null) {
            synchronized (DaoManager.class) {
                if (mDaoManager == null) {
                    mDaoManager = new DaoManager();
                }
            }
        }
        return mDaoManager;
    }

    @Override
    public List<String> getAllTableNames() {
        ArrayList<String> tableNames = new ArrayList<>();
        Collection<AbstractDao<?, ?>> collection = getDaoSession().getAllDaos();
        AbstractDao dao;
        if (collection != null) {
            Iterator it = collection.iterator();
            while (it.hasNext()) {
                dao = (AbstractDao) it.next();
                tableNames.add(dao.getTablename());
            }
        }
        return tableNames;
    }

    @Override
    public void init(Context context, String dbName) {
        this.mContext = context;
        this.mDbName = dbName;
        getDaoSession();
    }

    /**
     * 判断数据库是否存在，如果不存在则创建
     */
    private DaoMaster getDaoMaster() {
        if (null == mDaoMaster) {
//            mHelper = new DaoMaster.DevOpenHelper(mContext, mDbName);
            mHelper = new DaoOpenHelper(mContext, mDbName);
            mSqlDb = mHelper.getWritableDatabase();
            mDaoMaster = new DaoMaster(mSqlDb);
        }
        return mDaoMaster;
    }

    /**
     * 完成对数据库的增删查找
     */
    private DaoSession getDaoSession() {
        if (null == mDaoSession) {
            if (null == mDaoMaster) {
                mDaoMaster = getDaoMaster();
            }
            mDaoSession = mDaoMaster.newSession();
        }
        return mDaoSession;
    }

    /**
     * 设置debug模式开启或关闭，默认关闭
     */
    public void setDebug(boolean flag) {
        QueryBuilder.LOG_SQL = flag;
        QueryBuilder.LOG_VALUES = flag;
    }

    /**
     * 关闭数据库
     */
    public void closeDataBase() {
        closeHelper();
        closeDaoSession();
    }

    public void closeDaoSession() {
        if (null != mDaoSession) {
            mDaoSession.clear();
            mDaoSession = null;
        }
    }

    public void closeHelper() {
        if (mHelper != null) {
            mHelper.close();
            mHelper = null;
        }
    }

    public AbstractDao getDao(Class<? extends Object> entityClass) {
        return getDaoSession().getDao(entityClass);
    }


    @Override
    public void insert(Object object) {
        Class cls;
        if (object instanceof List) {
            List listObject = (List) object;
            if (listObject.isEmpty()) {
                throw new IllegalArgumentException("List Object Not Allow Empty!");
            }
            cls = listObject.get(0).getClass();
            getDao(cls).insertInTx(listObject);
//            ((AbstractDao<Object, String>) getDao(cls)).insert(listObject.iterator());
        } else {
            cls = object.getClass();
            getDao(cls).insert(object);
        }
    }

    @Override
    public void insertOrUpdate(Object object) {
        Class cls;
        if (object instanceof List) {
            List listObject = (List) object;
            if (listObject.isEmpty()) {
                throw new IllegalArgumentException("List Object Not Allow Empty!");
            }
            cls = listObject.get(0).getClass();
            ((AbstractDao<Object, String>) getDao(cls)).insertOrReplaceInTx(listObject/*.iterator()*/);
        } else {
            cls = object.getClass();
            ((AbstractDao<Object, String>) getDao(cls)).insertOrReplace(object);
        }
    }

    @Override
    public void update(Object object) {
        Class cls;
        if (object instanceof List) {
            List listObject = (List) object;
            if (listObject.isEmpty()) {
                throw new IllegalArgumentException("List Object Not Allow Empty!");
            }
            cls = listObject.get(0).getClass();
            ((AbstractDao<Object, String>) getDao(cls)).updateInTx(listObject/*.iterator()*/);
        } else {
            cls = object.getClass();
            ((AbstractDao<Object, String>) getDao(cls)).update(object);
        }
    }


    @Override
    public void delete(Object object) {
        Class cls;
        if (object instanceof List) {
            List listObject = (List) object;
            if (listObject.isEmpty()) {
                throw new IllegalArgumentException("List Object Not Allow Empty!");
            }
            cls = listObject.get(0).getClass();
            ((AbstractDao<Object, String>) getDao(cls)).deleteInTx(listObject/*.iterator()*/);
        } else {
            cls = object.getClass();
            ((AbstractDao<Object, String>) getDao(cls)).delete(object);
        }
    }


    @Override
    public void deleteByKey(Class<?> classType, String key) {
        ((AbstractDao<Object, String>) getDao(classType)).deleteByKey(key);
    }

    @Override
    public void deleteAll(Class<?> classType) {
        ((AbstractDao<Object, String>) getDao(classType)).deleteAll();
    }

    @Override
    public <T> T queryByKey(Class<T> classType, String key) {
        return ((AbstractDao<T, String>) getDao(classType)).load(key);
    }

    @Override
    public <T> List<T> query(Class<T> classType, String where, String... selectionArg) {
        return ((AbstractDao<T, String>) getDao(classType)).queryRaw(where, selectionArg);
    }

    @Override
    public <T> List<T> queryAll(Class<T> cls) {
        return ((AbstractDao<T, String>) getDao(cls)).loadAll();
    }

//    @Override
//    public Database getDataBase() {
//        if(getDaoSession() == null) {
//            throw new IllegalStateException("DaoSession must be set during content provider is active");
//        }
//        return getDaoSession().getDatabase();
//    }

    @Override
    public SQLiteDatabase getSQLiteDataBase() {
        if (mSqlDb == null){
            throw new IllegalStateException("mHelper must be set during content provider is active");
        }
        return mSqlDb;
    }
}
