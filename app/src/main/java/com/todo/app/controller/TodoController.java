package com.todo.app.controller;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.todo.app.entity.DisplayJksonTodo;
import com.todo.app.entity.DisplayTodo;
import com.todo.app.entity.ImageForm;
import com.todo.app.entity.Message;
import com.todo.app.entity.Status;
import com.todo.app.entity.Todo;
import com.todo.app.mapper.TodoMapper;
import com.todo.app.services.BatExecute;
import com.todo.app.utility.CheckUtility;

@Controller
public class TodoController {

    @Autowired
    TodoMapper todoMapper;

    /**
     * 初期表示のメソッド
     * 1.データベースから未完了・完了済みのタスクをそれぞれ検索して画面に表示
     *
     * @param model データ保持用
     * @return "index" 画面名
     */
    @RequestMapping(value="/")
    public String index(Model model) {

        int defaultNum = 0;


        
        //未完了のタスク取得
        List<Todo> lists = todoMapper.selectIncomplete();
        List<DisplayTodo> displayTodoList = new ArrayList<>();
        List<DisplayJksonTodo> djTodoList = new ArrayList<>();

        for (Todo list:lists){
            DisplayTodo displayTodo = new DisplayTodo();

            displayTodo.setId(list.getId());
            displayTodo.setTitle(list.getTitle());
            displayTodo.setDone_flg(list.getDone_flg());
            displayTodo.setTime_limit(LocalDate.parse(list.getTime_limit()).format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
            displayTodo.setUrgency_level(list.getUrgency_level());

            displayTodo.setStatus(Status.getNameByValue(Integer.parseInt(list.getStatus())).getName());
            System.out.println(displayTodo.getTime_limit());


            try {
                DisplayJksonTodo djTodo = new DisplayJksonTodo();
                djTodo.generate_JksonTodo(list);
            } catch (Exception e) {
                // 例外をキャッチして処理する（例：エラーメッセージを表示する）
                System.err.println("例外が発生しました: " + e.getMessage());
            }

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

    /**
     * 検索表示メソッド
     * タイトルと期限日を指定してタスクを検索して画面に表示
     * @param title タスクのタイトル
     * @param date 期限日
     * @param model データ保持用モデル
     * @return "index" 画面名
     */
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

    /**
     * 登録処理
     * Todoをデータベースに登録する
     *
     * @param todo 登録用obj
     * @return todo 登録用obj
     */
    @RequestMapping(value="/add")
    @ResponseBody
    public Todo add(Todo todo) {
        todoMapper.add(todo);
        return todo;
    }

    /**
     * 更新処理
     * Todoの情報を更新する
     *
     * @param todo 更新用obj
     */
        @RequestMapping(value="/update")
        @ResponseBody
    public void update(Todo todo) {
        todoMapper.update(todo);
        // return "redirect:/";
    }

    /**
     * 削除処理
     * Todoの情報を削除する
     *
     */
    @RequestMapping(value="/delete")
    @ResponseBody
    public void delete() {
    System.out.println("delete");
        todoMapper.delete();
    }


    /**
     * 初期画面表示
     * @return 画面名
     */
    @GetMapping("/index")
    public String index() {
    System.out.println("index");

        return "index";
    }

    /**
     * index2の画面処理
     * @return 画面名
     */
    @GetMapping("/index2")
    public String index2() {
    System.out.println("index2");

        return "index2";
    }

    /**
     * index2の画面処理
     * @return 画面名
     */
    @PostMapping("/index2")
    public String index2post() {
    System.out.println("index2");

        return "index2";
    }

    /**
     * indexの画面処理
     * @return 画面名
     */
    @PostMapping("/index")
    public String index1post() {
    System.out.println("index1");

        return "index";
    }

    
    /**
     * 送信処理
     * 入力された内容をカンマで分割し、タスクを登録する。
     * @param message 画面入力内容
     * @param model 画面表示データ保持用
     * @param model2 未使用
     * @return 画面用
     */
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

    //強制終了（使わない）
    @GetMapping("/exit")
    public String exitApplication(){
        System.exit(0);
        return "exit";
    }

    /**
     * バッチ処理（にするつもりだった）
     * @return
     */
    @RequestMapping(value="/bat")
    // @ResponseBody
    public String bat() {
    BatExecute btExecute = new BatExecute(todoMapper);
    String aaa = btExecute.exec();
    return "index2";
    }
    
    @GetMapping("/upload")
    public String showUploadForm(Model model) {
        model.addAttribute("imageForm", new ImageForm());
        return "upload";
    }

    @PostMapping("/upload")
    public String handleUpload(ImageForm imageForm,@RequestParam("fileName") String InputFileName) throws Exception {
        MultipartFile imageFile = imageForm.getImageFile();

         //<変数>//
         // 最大許容サイズ (100MBを想定)
        long MAX_FILE_SIZE = 100 * 1024 * 1024;
         // 最小許容サイズ (10バイトを想定)
        long MIN_FILE_SIZE = 10;
        //画像ファイルの保存先フォルダパス
        String DIRECTORY_PATH = "/Users/toyamariku/Desktop/画像保存用フォルダ";
        //保存する画像ファイルの名称
        String fileName = InputFileName;
        //拡張子
        String fileExtension = imageForm.getImageFile().getOriginalFilename();
        if (!imageFile.isEmpty()) {
            fileExtension = fileExtension.substring(fileExtension.lastIndexOf("."),fileExtension.length());
        } else {
            return "upload";
        }


        //保存先絶対パス
        String savePath = todoMapper.selectFilePath() + "/" + fileName + fileExtension;


        //<処理>//
        //拡張子チェック
        if (Objects.isNull(fileName)){

        } else {
            switch (fileExtension) {
                case ".png":
                    break;
                case ".jpeg":
                    break;
                case ".jpg":
                    break;
                default:
                    // 処理終了
                    return "redirect:/upload";
            }
        }

        //ディレクトリ存在チェック
        try {
            Set<String> fileNames = new HashSet<>();
            File directory = new File(DIRECTORY_PATH);

            //ディレクトリ内のファイルに重複するものがないかチェック
            if (directory.isDirectory()){
                if(CheckUtility.checkDupulicates(directory, fileNames,fileName + fileExtension)){

                } else {
                //重複ありの場合は、処理を終了する。
                return "upload";
                }
            } else {
                System.out.println("指定されたパスはディレクトリではありません。");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        //ファイルサイズチェック(10byte〜100MB)
        if (imageFile.getSize() > MAX_FILE_SIZE ) {
            throw new Exception("アップロードされたファイルのサイズが大きすぎます。");
        } else if (imageFile.getSize() < MIN_FILE_SIZE){
            throw new Exception("アップロードされたファイルのサイズが小さすぎます。");
        }

        if (!imageFile.isEmpty()) {
            //ファイル名・パス指定
            File file = new File(savePath);
            imageFile.transferTo(file);
        }
        return "redirect:/upload";
    }
}


