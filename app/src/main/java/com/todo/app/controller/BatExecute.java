package com.todo.app.controller;

import org.springframework.beans.factory.annotation.Autowired;

import com.todo.app.mapper.TodoMapper;

public class BatExecute {

    @Autowired
    TodoMapper todoMapper;

    public void exec() {
        // 処理分岐用変数
        int operationAction = 99;

        switch (operationAction) {
            //0:削除していない過去日付のデータに削除フラグをたてる
            case 0:
                todoMapper.deletePastDoneTask();
                break;
            //1:全件削除を解除
            case 1:
                todoMapper.unDelete();
                break;
            //10:10日前になったら、タイトル末尾に「(10日以内)」という文字列を追加
            case 10:
                todoMapper.updateTitle();
                break;
            //0,1,10以外:エラー
            default:
                System.out.println("エラー");
                break;
        }

    }
}