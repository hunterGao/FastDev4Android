package com.huntergao.fastdev4android.designpattern.mvp.model;

/**
 * 简单的用户实体类
 * Created by HunterGao on 16/1/16.
 */
public class User {
    private String name;
    private String password;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
