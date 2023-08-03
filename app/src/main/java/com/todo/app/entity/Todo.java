package com.todo.app.entity;

public class Todo {
    
private long id;
    private String title;
    private int done_flg;
    private String time_limit;
    private double urgency_level;
    private int del_flg;
    private String status;
    
    public long getId() {
        return id;
    }
    
    public void setId(long id) {
        this.id = id;
    }
    
    public String getTitle() {
        return title;
    }
    
    public void setTitle(String title) {
        this.title = title;
    }
    
    public int getDone_flg() {
        return done_flg;
    }
    
    public void setDone_flg(int done_flg) {
        this.done_flg = done_flg;
    }
    
    public String getTime_limit() {
        return time_limit;
    }
    
    public void setTime_limit(String time_limit) {
        this.time_limit = time_limit;
    }

    public double getUrgency_level() {
    return urgency_level;
    }

    public void setUrgency_level(double urgency_level) {
        this.urgency_level = urgency_level;
    }

    public int getDel_Flg() {
        return del_flg;
    }

    public void setDel_flg(int del_flg) {
        this.del_flg = del_flg;
    }

    public String getStatus() {
    return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}