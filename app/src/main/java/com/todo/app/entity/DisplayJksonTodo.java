package com.todo.app.entity;

import com.fasterxml.jackson.databind.ObjectMapper;

public class DisplayJksonTodo extends Todo {
    private String del_flg;

    public String getDel_flg() {
        return del_flg;
    }
    
    public void setDel_flg(String del_flg) {
        this.del_flg = del_flg;
    }

    public void generate_JksonTodo(Todo todo) throws Exception {
        // 自オブジェクトを作成
        DisplayJksonTodo djTodo = new DisplayJksonTodo();

        djTodo.setId(todo.getId());
        djTodo.setTitle(todo.getTitle());
        djTodo.setDone_flg(todo.getDone_flg());
        djTodo.setTime_limit(todo.getTime_limit());
        djTodo.setUrgency_level(todo.getUrgency_level());
        djTodo.setDel_flg(todo.getDel_Flg());
        djTodo.setStatus(todo.getStatus());

        // ObjectMapperを使ってJavaオブジェクトをJSONデータに変換
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonString = objectMapper.writeValueAsString(djTodo);

        System.out.println(jsonString); // 結果：{？？？？？？}
        
    }
}

