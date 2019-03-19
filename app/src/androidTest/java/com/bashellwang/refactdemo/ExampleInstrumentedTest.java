package com.bashellwang.refactdemo;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.util.Log;

import com.bashellwang.refactdemo.business.Note;
import com.bashellwang.refactdemo.business.User;
import com.bashellwang.refactdemo.storage.dbmodule.dbconfig.DBConfigOption;
import com.bashellwang.refactdemo.storage.dbmodule.DBManager;
import com.bashellwang.refactdemo.storage.spmodule.SPHelper;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

import static org.junit.Assert.*;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {
    private static final String TAG = "ExampleInstrumentedTest";
    @Test
    public void useAppContext() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();

        assertEquals("com.bashellwang.refactdemo", appContext.getPackageName());
    }


    @Test
    public void testDB(){

        DBConfigOption configOption = new DBConfigOption.Builder().setDbName("testUnit.db").build();

        DBManager.getInstance().init(InstrumentationRegistry.getTargetContext(),null,configOption);
        List<Note> list = DBManager.getInstance().getmDataBase().queryAll(Note.class);

        Log.e(TAG,"空："+(list==null));
        System.out.println(TAG+"空："+(list==null));
    }


    @Test
    public void testSPHelper(){
        SPHelper.with(InstrumentationRegistry.getTargetContext(),"testSp").saveInt("int_value",100);
        SPHelper.with(InstrumentationRegistry.getTargetContext(),"testSp").saveFloat("float_value",200f);
        SPHelper.with(InstrumentationRegistry.getTargetContext(),"testSp").saveLong("long_value",300l);
        SPHelper.with(InstrumentationRegistry.getTargetContext(),"testSp").saveBoolean("boolean_value",true);
        SPHelper.with(InstrumentationRegistry.getTargetContext(),"testSp").saveString("string_value","hello,sp");

        User user = new User();
        user.setId(2l);
        user.setAge(18);
        user.setDesc("我是一个学生");
        user.setName("张三");
        try {
            SPHelper.with(InstrumentationRegistry.getTargetContext(), "testSp").saveSerializableObj("obj_value", user);

        } catch (Exception e) {

        }



        Log.e(TAG,"int: "+SPHelper.with(InstrumentationRegistry.getTargetContext(),"testSp").getInt("int_value",-1));
        Log.e(TAG,"float: "+SPHelper.with(InstrumentationRegistry.getTargetContext(),"testSp").getFloat("float_value",-2f));
        Log.e(TAG,"long: "+SPHelper.with(InstrumentationRegistry.getTargetContext(),"testSp").getLong("long_value",-3l));
        Log.e(TAG,"boolean: "+SPHelper.with(InstrumentationRegistry.getTargetContext(),"testSp").getBoolean("boolean_value",false));
        Log.e(TAG,"string: "+SPHelper.with(InstrumentationRegistry.getTargetContext(),"testSp").getString("string_value","null"));
        Object obj = SPHelper.with(InstrumentationRegistry.getTargetContext(),"testSp").getObjBykey("obj_value");
        if (obj instanceof User){
            User user1 = (User) obj;
            Log.e(TAG,"user: "+user1.getId()+" "+user1.getName()+" "+user1.getAge()+" "+user1.getDesc());
        }
    }
}
