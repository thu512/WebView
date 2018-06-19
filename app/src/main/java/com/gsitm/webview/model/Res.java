package com.gsitm.webview.model;

/**
 * Created by lcj on 2018. 6. 19..
 */

public class Res {
    String data;

    public Res(String data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "Res{" +
                "data='" + data + '\'' +
                '}';
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
