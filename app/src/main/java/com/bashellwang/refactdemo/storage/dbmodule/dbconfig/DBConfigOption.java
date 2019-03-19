package com.bashellwang.refactdemo.storage.dbmodule.dbconfig;

/**
 * Created by liang.wang on 2019/3/6.
 */
public class DBConfigOption {
    public String mDbName;

    public static final class Builder {

        public String dbName;

        public Builder setDbName(String dbName) {
            this.dbName = dbName;
            return this;
        }

        public DBConfigOption build() {
            DBConfigOption configOption = new DBConfigOption();
            configOption.mDbName = dbName;
            return configOption;
        }
    }
}
