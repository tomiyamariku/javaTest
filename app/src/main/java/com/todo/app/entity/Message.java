package com.todo.app.entity;

public class Message {
    private String title;
    private String time_limit;
    private int done_flg;
    private double urgency_level;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTime_limit() {
        return time_limit;
    }

    public void setTime_limit(String time_limit) {
        this.time_limit = time_limit;
    }

    public int getDone_flg() {
        return done_flg;
    }

    public void setDone_flg(int done_flg) {
        this.done_flg = done_flg;
    }

    public double getUrgency_level() {
        return urgency_level;
    }

    public void setUrgency_level(double urgency_level) {
        this.urgency_level = urgency_level;
    }
}

