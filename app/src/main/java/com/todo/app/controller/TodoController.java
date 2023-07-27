package com.todo.app.controller;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.todo.app.entity.DisplayTodo;
import com.todo.app.entity.Todo;
import com.todo.app.mapper.TodoMapper;



@Controller
public class TodoController {

    //日付取得用の桁数
    private static final int DATE_CNT = 9;

    private static final int NEXT_CNT = 1;



    @Autowired
    TodoMapper todoMapper;

    @RequestMapping(value="/")
    public String index(Model model) {

        // List<Todo> list = todoMapper.selectAll();
        
        List<Todo> lists = todoMapper.selectIncomplete();
        List<DisplayTodo> displayTodoList = new ArrayList<>();
        for (Todo list:lists){
            DisplayTodo displayTodo = new DisplayTodo();

            displayTodo.setId(list.getId());
            displayTodo.setTitle(list.getTitle());
            displayTodo.setDone_flg(list.getDone_flg());
            displayTodo.setTime_limit(LocalDate.parse(list.getTime_limit()).format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
            System.out.println(displayTodo.getTime_limit());

                if(LocalDate.parse(displayTodo.getTime_limit()).compareTo(LocalDate.now())<0 ){
                displayTodo.setDate_flg("過去日付");
                }
            displayTodoList.add(displayTodo);
        }


        List<Todo> doneList = todoMapper.selectComplete();
        model.addAttribute("todos",displayTodoList);
        model.addAttribute("doneTodos",doneList);
        System.out.println("///");

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

        //現在日付よりも前の日付で登録しようとしているかチェック
        if(LocalDate.parse(todo.getTime_limit()).compareTo(LocalDate.now())<0){
JOptionPane.showMessageDialog(null, "エラーが発生しました。", "エラー", DATE_CNT, null);
        }

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
        // フォームの送信処理を記述
        // 受け取ったメッセージを処理するなどの処理を行うことができます

        // System.out.println(message);
        // model.addAttribute("AISATSU", "Hello World");

        

        //入力値が、タスク管理の登録内容を書いたものか判断する
        //System.out.println(message.substring(0,6));
        //if(message.substring(0,6).equals("タスク管理,")){
        int tasklength = message.indexOf(",");

        if(tasklength > 1 && tasklength<=200){
            //日付型に変換できるか判断する
            try{
                //System.out.println("日付型に変換可能");
                DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("yyyyMMdd");
                DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                System.out.println(message.substring(tasklength + NEXT_CNT,tasklength + DATE_CNT));
                LocalDate inputDate = LocalDate.parse(message.substring(tasklength + NEXT_CNT,tasklength + DATE_CNT),inputFormatter);
                String FormattedDate = inputDate.format(outputFormatter);
                
                //動作確認用
                //LocalDate outputDate = LocalDate.parse(FormattedDate,outputFormatter);
                //System.out.println(outputDate.format(outputFormatter));

                        Todo todo = new Todo();
                        todo.setTitle(message.substring(0,5));
                        //System.out.println(message.substring(0,5));

                        todo.setTime_limit(FormattedDate);
                        //System.out.println(FormattedDate);
                todoMapper.addInputTask(todo);
                } catch (DateTimeParseException e){
                                //System.out.println("日付に変換不可");
                }

        }

        model.addAttribute("submittedMessage", "入力形式が不正です");


        return "index2"; // リダイレクトして同じ画面を更新
    }

    @GetMapping("/exit")
    public String exitApplication(){
        System.exit(0);

        return "exit";
    }
}

