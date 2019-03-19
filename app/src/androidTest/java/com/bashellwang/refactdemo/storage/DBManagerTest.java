package com.bashellwang.refactdemo.storage;

import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.util.Log;

import com.bashellwang.refactdemo.business.Note;
import com.bashellwang.refactdemo.storage.dbmodule.DBManager;
import com.bashellwang.refactdemo.storage.dbmodule.dbconfig.DBConfigOption;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by liang.wang on 2019/3/6.
 */
@RunWith(AndroidJUnit4.class)
public class DBManagerTest {
    public static final String TAG = "DBManagerTest";

    @Before
    public void setUp() throws Exception {
        Log.i(TAG, "init...");
        DBConfigOption configOption = new DBConfigOption.Builder().setDbName("testUnit.db").build();
        DBManager.getInstance().init(InstrumentationRegistry.getTargetContext(), null, configOption);
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void getInstance() {
    }

    @Test
    public void init() {
    }

    @Test
    public void getmDataBase() {
        Log.i(TAG, "queryAll...");
        List<Note> list = DBManager.getInstance().getmDataBase().queryAll(Note.class);
        Log.i(TAG, getListStr("queryAll :", list));

        ArrayList<Note> datas = new ArrayList<>();
        for (long i =0;i<5;i++){
            Note note = new Note();
            note.setId(i);
            note.setComment("评论"+i);
            note.setText("text"+i);
            datas.add(note);
        }
        DBManager.getInstance().getmDataBase().insertOrUpdate(datas);

        Log.i(TAG, "插入数据...");
        list = DBManager.getInstance().getmDataBase().queryAll(Note.class);
        Log.i(TAG, getListStr("queryAll :", list));

        for (int i =0 ;i<list.size();i++){
            list.get(i).setText("文本修改"+i);
        }


        Log.i(TAG, "修改数据...");
        list = DBManager.getInstance().getmDataBase().queryAll(Note.class);
        Log.i(TAG, getListStr("queryAll :", list));
    }

    public String getListStr(String str, List<Note> datas) {
        if (datas == null) {
            return str + " 数据空";
        } else {
            return str + "数量："+datas.size()+"==="+datas.toString();
        }
    }
}