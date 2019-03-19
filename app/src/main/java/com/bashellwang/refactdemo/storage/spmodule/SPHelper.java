package com.bashellwang.refactdemo.storage.spmodule;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Base64;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Map;

/**
 * Created by liang.wang on 2019/3/11.
 */
public class SPHelper {

    private static final String TAG = SPHelper.class.getSimpleName();
    private SharedPreferences mSharedPreferences;
    private static SPHelper mSPHelper;


    /**
     * SharedPreferences helper,default file named packagename + "_preference"
     *
     * @param context context
     */
    public SPHelper(@NonNull Context context) {
        mSharedPreferences = context.getSharedPreferences(context.getPackageName() + "_preference", Context.MODE_PRIVATE);
    }

    /**
     * SharedPreferences helper, with given name
     *
     * @param context context
     * @param spName  stored SharedPreferences file name
     */
    public SPHelper(@NonNull Context context, @NonNull String spName) {
        mSharedPreferences = context.getSharedPreferences(spName, Context.MODE_PRIVATE);
    }

    public static SPHelper with(@NonNull Context context) {
        mSPHelper = new SPHelper(context);
        return mSPHelper;
    }

    public static SPHelper with(@NonNull Context context, @NonNull String spName) {
        mSPHelper = new SPHelper(context, spName);
        return mSPHelper;
    }

    public void remove(String tag) {
        mSharedPreferences.edit().remove(tag).apply();
    }

    public void clear() {
        mSharedPreferences.edit().clear().apply();
    }

    public boolean contains(String key) {
        return mSharedPreferences.contains(key);
    }

    public boolean getBoolean(String key, boolean defaultValue) {
        return mSharedPreferences.getBoolean(key, defaultValue);
    }

    public void saveBoolean(String key, boolean value) {
        mSharedPreferences.edit().putBoolean(key, value).apply();
    }

    public String getString(String key, String defaultValue) {
        return mSharedPreferences.getString(key, defaultValue);
    }

    public void saveString(String key, String value) {
        mSharedPreferences.edit().putString(key, value).apply();
    }

    public float getFloat(String key, float defaultValue) {
        return mSharedPreferences.getFloat(key, defaultValue);
    }

    public void saveFloat(String key, float value) {
        mSharedPreferences.edit().putFloat(key, value).apply();
    }

    public int getInt(String key, int defaultValue) {
        return mSharedPreferences.getInt(key, defaultValue);
    }

    public void saveInt(String key, int value) {
        mSharedPreferences.edit().putInt(key, value).apply();
    }

    public long getLong(String key, long defaultValue) {
        return mSharedPreferences.getLong(key, defaultValue);
    }

    public void saveLong(String key, long value) {
        mSharedPreferences.edit().putLong(key, value).apply();
    }

    public void saveByteArray(String key, byte[] data) {
        String dataStr;
        try {
            dataStr = data == null ? null : Base64.encodeToString(data, Base64.DEFAULT);
        } catch (AssertionError e) {
            dataStr = null;
        }
        saveString(key, dataStr);
    }

    public byte[] getByteArray(String key) {
        String dataString = getString(key, "");
        if (!TextUtils.isEmpty(dataString)) {
            try {
                return Base64.decode(dataString, Base64.DEFAULT);
            } catch (IllegalArgumentException e) {
                return null;
            }
        } else {
            return null;
        }
    }

    /**
     * store object with key
     *
     * @param key stored key
     * @param obj must be Serialiazable object
     * @throws Exception
     */
    public void saveSerializableObj(String key, Object obj) throws Exception {
        if (obj instanceof Serializable) {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream oos = null;
            try {
                oos = new ObjectOutputStream(baos);
                oos.writeObject(obj);//把对象写到流里

                saveByteArray(key, baos.toByteArray());
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    baos.close();
                    if (oos != null) {
                        oos.close();
                    }
                } catch (Exception e) {

                }

            }
        } else {
            throw new Exception("object must implements Serializable");
        }
    }

    /**
     * get object with key
     *
     * @param key stored key
     * @return object
     */
    public Object getObjBykey(String key) {
        byte[] data = getByteArray(key);

        ByteArrayInputStream bais = new ByteArrayInputStream(data);
        ObjectInputStream ois = null;
        Object obj = null;
        try {
            ois = new ObjectInputStream(bais);
            obj = ois.readObject();
        } catch (IOException e) {

        } catch (ClassNotFoundException e1) {

        } finally {
            try {
                bais.close();
                if (ois != null) {
                    ois.close();
                }

            } catch (Exception e) {

            }
        }
        return obj;
    }

    public Map<String, ?> getAll() {
        return mSharedPreferences.getAll();
    }

    /**
     * save datas in map
     *
     * @param datas datas to be stored
     * @throws Exception store object without Serializable
     */
    public void saveAll(Map<String, ?> datas) throws Exception {
        for (Map.Entry<String, ?> entry : datas.entrySet()) {
            Object obj = entry.getValue();
            String key = entry.getKey();
            if (obj instanceof String) {
                saveString(key, (String) obj);
            } else if (obj instanceof Boolean) {
                saveBoolean(key, (boolean) obj);
            } else if (obj instanceof Float) {
                saveFloat(key, (float) obj);
            } else if (obj instanceof Integer) {
                saveInt(key, (int) obj);
            } else if (obj instanceof Long) {
                saveLong(key, (long) obj);
            } else if (obj instanceof byte[]) {
                saveByteArray(key, (byte[]) obj);
            } else if (obj instanceof Serializable) {
                saveSerializableObj(key, obj);
            }
        }
    }
}
