$(function(){

    //完了済みの個数取得・表示
      let doneCount = $('#donetodes').children("tr").length;
      $('#done_count').text(doneCount);
    
    //更新処理
    $('.todo input').change(function(){
        const todo = $(this).parents('.todo');
        const id = todo.find('input[name="id"]');
        const title = todo.find('input[name="title"]');
        const timeLimit = todo.find('input[name="time_limit"]');
        const isDone = todo.find('input[name="done_flg"]').prop("checked");
        let doneFlg;
        if(isDone == true) {
          doneFlg = 1;
        }else{
          doneFlg = 0;
        }
    
        const params = {
            id : id.val(),
            title : title.val(),
            time_limit : timeLimit.val(),
            done_flg : doneFlg
        }
        $.post("/update",params);
    
        //完了ボタンを押した際の処理
        doneCount =  $('#done_count').text();
    
        if($(this).prop('name') == "done_flg"){
          if(isDone == true){
            $(todo).appendTo('#donetodes');
            todo.find('input[name="title"]').css('text-decoration','line-through')
            todo.find('input[name="time_limit"]').hide();
            doneCount ++;
          }else{
            $(todo).appendTo('#todes');
            todo.find('input[name="title"]').css('text-decoration','none')
            todo.find('input[name="time_limit"]').show()
            doneCount --;
          }
    
          $("#done_count").text(doneCount);
        }
    
    
    })
    

    
    //追加処理
    $('#add').click(function() {
        const params = $('#add_form').serializeArray();
        $.post("/add",params).done(function(json){
            const clone = $('#todes tr:first').clone(true);
            clone.find('input[name="id"]').val(json.id);
            clone.find('input[name="title"]').val(json.title);
            clone.find('span[name="time_limit"]').text(json.time_limit);
            clone.find('span[name="urgency_level"]').text(json.urgency_level);

            console.log(json.time_limit);
            console.log(json.urgency_level);
            // const selectedUrgencyLevel = json.urgency_level;
            // clone.find('input[name="urgency_level"]').filter('[value="' + selectedUrgencyLevel + '"]').prop('checked', true);
            $('#todes').append(clone[0]);
        })
    })
    
    //削除処理
    $('#delete').click(function(){

      // ポップアップメッセージの内容を指定
      var message = "完了済みタスクを削除しますが、よろしいですか？";

      // ポップアップウィンドウを作成
      var popup = window.open("", "削除確認", "width=300,height=200");

      // ポップアップウィンドウにHTMLコンテンツを設定
      var content = "<p>" + message + "</p>";
      content += "<button id='popupYesButton'>はい</button>";
      content += "<button id='popupNoButton'>いいえ</button>"
      popup.document.write(content);

      var yesButton = popup.document.getElementById('popupYesButton');
      var noButton = popup.document.getElementById('popupNoButton');

  // ボタンがクリックされた時の処理を記述
  yesButton.addEventListener("click", function() {
    
    console.log("削除が完了しました。");
    popup.close();
    
        $.post("/delete").done(function(){
            $('#donetodes').empty();
            $('#done_count').text(0);
        })
  });
    
    // ボタンがクリックされた時の処理を記述
    noButton.addEventListener("click", function() {
      console.log("キャンセルしました。");
      popup.close()
    });

    })
    
    //非表示処理
    $('#none').click(function(){
      let button = document.getElementById('none');
      let MytaskDisplay = document.getElementById('MyTask');
  
        if(MytaskDisplay.style.display==''){
          MytaskDisplay.style.display='none';
        }else{
          MytaskDisplay.style.display='';
        }
      
          })
          //完了済みタスク表示/非表示切り替え
    $('.button_for_show').click(function(){
      let showState = $('#done_table').css('display');
      if(showState == "none") {
          $('#done_table').show();
          $(this).css({ transform: ' rotate(225deg)','bottom':'-4px' });
      }else{
          $('#done_table').hide();
          $(this).css({ transform: ' rotate(45deg)','bottom':'4px' });
      }
  })

      //非表示処理
      $('#bat').click(function(){
        $.post("/bat").done(function(){
      })
            })

    })
