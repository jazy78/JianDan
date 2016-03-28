package com.example.hp.jiandan.SQLite.Mode;

/**
 * Created by hp on 2016/3/18.
 */
public class Entiy {

    public  static String Skey="key";
    public static  String Sresult="resule";
    public static String Spage="page";
    public  static String Stime="time";

    private int key;
    private String result;
    private int page;
    private Long time;


    public Entiy() {
        super();
    }

    public Entiy(int key, String result, int page, Long time) {
        this.key=key;
        this.result=result;
        this.page=page;
        this.time=time;
    }

    public int getKey() {
        return key;
    }

    public int getPage() {
        return page;
    }

    public Long getTime() {
        return time;
    }

    public String getResult() {
        return result;
    }

    public void setKey(int key) {
        this.key = key;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public void setTime(Long time) {
        this.time = time;
    }
}
