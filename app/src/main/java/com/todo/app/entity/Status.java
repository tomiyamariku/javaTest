package com.todo.app.entity;

import java.util.Arrays;

public enum Status {
    Pending(10,"未実行"),
    Ongoing(20, "着手中"),
    Processing(30, "実行中"),
    Completed(40, "完了");

    private final int value;
    private final String name;

    private Status(int value, String name){
        this.value = value;
        this.name = name;
    }

    public int getValue(){
        return value;
    }

    public String getName(){
        return name;
    }

        /**
     * 日本語名称からEnumを取得する（Stream文利用）
     *
     * @param code
     * @return
     */
    public static Status getNameByValue(int code) {
    return Arrays.stream(Status.values())
        .filter(data -> data.getValue()==code)
        .findFirst()
        .orElse(null);
        }

    }


