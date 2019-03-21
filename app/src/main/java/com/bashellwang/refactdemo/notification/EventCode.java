package com.bashellwang.refactdemo.notification;

/**
 * Created by liang.wang on 2019/3/19.
 */
public interface EventCode {

    /**
     * 根据业务模块区分
     */
    interface Login{
        int LOGIN_SUCCESS = 1000;
    }

    interface Pay{
        int LOGIN_SUCCESS = 1100;
    }
}
