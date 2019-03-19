package com.bashellwang.refactdemo.business;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.Generated;

import java.io.Serializable;

/**
 * Created by liang.wang on 2019/3/10.
 */
@Entity
public class User implements Serializable {
    private static final long serialVersionUID = 8065647366383923077L;
    @Id
    private Long id;

    @NotNull
    private String name;
    private int age;
    private String desc;

    @Generated(hash = 1902667901)
    public User(Long id, @NotNull String name, int age, String desc) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.desc = desc;
    }

    @Generated(hash = 586692638)
    public User() {
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return this.age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getDesc() {
        return this.desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
