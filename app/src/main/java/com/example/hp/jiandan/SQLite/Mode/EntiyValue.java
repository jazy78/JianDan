package com.example.hp.jiandan.SQLite.Mode;

/**
 * Created by hp on 2016/3/18.
 */
public class EntiyValue {
    private int keyVaue;
    private String resultValue;
    private int pageValue;
    private Long timeValue;


    public EntiyValue(int key,String result,int page,Long time) {
        this.keyVaue=key;
        this.resultValue=result;
        this.pageValue=page;
        this.timeValue=time;
    }

    public void setKeyVaue(int keyVaue) {
        this.keyVaue = keyVaue;
    }

    public void setResultValue(String resultValue) {
        this.resultValue = resultValue;
    }

    public void setPageValue(int pageValue) {
        this.pageValue = pageValue;
    }

    public void setTimeValue(Long timeValue) {
        this.timeValue = timeValue;
    }

    public String getResultValue() {
        return resultValue;
    }

    public int getKeyVaue() {
        return keyVaue;
    }

    public int getPageValue() {
        return pageValue;
    }

    public Long getTimeValue() {
        return timeValue;
    }
}
