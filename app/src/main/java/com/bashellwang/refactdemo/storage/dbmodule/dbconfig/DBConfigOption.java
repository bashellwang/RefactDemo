package com.bashellwang.refactdemo.storage.dbmodule.dbconfig;

/**
 * Created by liang.wang on 2019/3/6.
 */
public class DBConfigOption {
    private String mDbName;

    public String getDbName(){
        return mDbName;
    }

    private DBConfigOption(Builder builder) {
        mDbName = builder.dbName;
    }

    public static final class Builder {

        private String dbName;

        public Builder setDbName(String dbName) {
            this.dbName = dbName;
            return this;
        }

        public DBConfigOption build() {
            return new DBConfigOption(this);
        }
    }
}
