package com.todo.app.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.todo.app.mapper.TodoMapper;

@Service
public class BatExecute {

    private final TodoMapper todoMapper;

    @Autowired
    public BatExecute(TodoMapper todoMapper) {
        this.todoMapper = todoMapper;
    }

    public String exec() {
        // 処理分岐用変数
        int operationAction = 10;
        String result = "";

        switch (operationAction) {
            //0:削除していない過去日付のデータに削除フラグをたてる
            case 0:
                todoMapper.deletePastDoneTask();
                result="過去日付のデータ削除";
                break;
            //1:全件削除を解除
            case 1:
                todoMapper.unDelete();
                result="全件削除解除";
                break;
            //10:10日前になったら、タイトル末尾に「(10日以内)」という文字列を追加
            case 10:
                todoMapper.updateTitle();
                result="10日前処理反映";
                break;
            //0,1,10以外:エラー
            default:
                result="エラー";
                break;
        }

        return result;
    }
}
