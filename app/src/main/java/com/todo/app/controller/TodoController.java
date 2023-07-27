package com.todo.app.controller;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.todo.app.entity.DisplayTodo;
import com.todo.app.entity.Message;
import com.todo.app.entity.Todo;
import com.todo.app.mapper.TodoMapper;
import com.todo.app.utility.CheckUtility;


@Controller
public class TodoController {

    @Autowired
    TodoMapper todoMapper;

    @RequestMapping(value="/")
    public String index(Model model) {

        int defaultNum = 0;
        
        //未完了のタスク取得
        List<Todo> lists = todoMapper.selectIncomplete();
        List<DisplayTodo> displayTodoList = new ArrayList<>();
        for (Todo list:lists){
            DisplayTodo displayTodo = new DisplayTodo();

            displayTodo.setId(list.getId());
            displayTodo.setTitle(list.getTitle());
            displayTodo.setDone_flg(list.getDone_flg());
            displayTodo.setTime_limit(LocalDate.parse(list.getTime_limit()).format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
            displayTodo.setUrgency_level(list.getUrgency_level());
            System.out.println(displayTodo.getTime_limit());

                if(LocalDate.parse(displayTodo.getTime_limit()).compareTo(LocalDate.now())<0 ){
                displayTodo.setDate_flg("過去日付");
                }
            displayTodoList.add(displayTodo);
        }

        //完了済みタスク取得
        List<Todo> doneList = todoMapper.selectComplete();

        //取得データ表示
        model.addAttribute("todos",displayTodoList);
        model.addAttribute("doneTodos",doneList);

        //未完了のタスクが存在するか判断
        if(lists.size()>0){
        double urgencyLevel = todoMapper.selectUrgencyAvr();
        model.addAttribute("urgencyLevel",urgencyLevel);
        } else {
        model.addAttribute("urgencyLevel",defaultNum);
        }

        return "index";
    }

        @RequestMapping(value="/search")
        public String search(@RequestParam("title") String title,@RequestParam("date") String date,Model model) {

        // List<Todo> list = todoMapper.selectAll();

        List<Todo> list = todoMapper.search(title,date);
        List<Todo> doneList = todoMapper.search(title,date);
        model.addAttribute("todos",list);
        model.addAttribute("doneTodos",doneList);
    System.out.println("search");
        return "index";
    }

    @RequestMapping(value="/add")
    @ResponseBody
    public Todo add(Todo todo) {
        todoMapper.add(todo);
        return todo;
    }

        @RequestMapping(value="/update")
        @ResponseBody
    public void update(Todo todo) {
        todoMapper.update(todo);
        // return "redirect:/";
    }

    @RequestMapping(value="/delete")
    @ResponseBody
    public void delete() {
    System.out.println("delete");
        todoMapper.delete();
    }

    @GetMapping("/index")
    public String index() {
    System.out.println("index");

        return "index";
    }

    @GetMapping("/index2")
    public String index2() {
    System.out.println("index2");

        return "index2";
    }

    @PostMapping("/index2")
    public String index2post() {
    System.out.println("index2");

        return "index2";
    }

    @PostMapping("/index")
    public String index1post() {
    System.out.println("index1");

        return "index";
    }

    @PostMapping("/submit")
    public String submitForm(@RequestParam("message") String message, Model model,Model model2) {

        //入力された内容をカンマで分割し、Messageオブジェクトの各フィールドに値をセット
        String[] items = message.split(",");

        Message msg = new Message();
        msg.setTitle(items[0]);
        msg.setTime_limit(items[1]);
        msg.setDone_flg(0); //初期登録は、未完了状態で登録する

        //緊急度の数値チェック
        if(CheckUtility.isNumeric(items[2])){
        msg.setUrgency_level(Double.parseDouble(items[2]));
        } else {
        model.addAttribute("submittedMessage", "緊急度は数値で入力してください。");
        return "index2"; // 処理終了
        }
        
        //カンマを除いた文字数をカウント
        int strCnt = message.replace(",", "").length();

        //カンマを除いた文字数が1字以上かつ、200字未満の場合
        if(strCnt > 1 && strCnt<200){
            //日付型に変換できるか判断
            try{
                DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("yyyyMMdd");
                DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

                //日付表示形式変換
                LocalDate inputDate = LocalDate.parse(msg.getTime_limit(),inputFormatter);
                String FormattedDate = inputDate.format(outputFormatter);
                
                //登録処理
                Todo todo = new Todo();
                todo.setTitle(msg.getTitle());
                todo.setTime_limit(FormattedDate);
                todo.setDone_flg(0);
                todo.setUrgency_level(msg.getUrgency_level());

                //実行
                todoMapper.addInputTask(todo);
                
                } catch (DateTimeParseException e){
                System.out.println("日付に変換不可");
                }

        } else {
        model.addAttribute("submittedMessage", "入力形式が不正です");
        }
            return "index2"; // リダイレクトして同じ画面を更新
}

    //強制終了
    @GetMapping("/exit")
    public String exitApplication(){
        System.exit(0);
        return "exit";
    }
    
}


