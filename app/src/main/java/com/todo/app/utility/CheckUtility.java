package com.todo.app.utility;
import java.io.File;
import java.io.IOException;
import java.util.Set;

public class CheckUtility {
    
    //数値チェック
    public static boolean isNumeric(String str) {
        try {
            Double.parseDouble(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    //ファイル名重複チェックのメソッド
    public static Boolean checkDupulicates(File directory, Set<String> fileNames, String checkFileName) throws IOException {

    //ファイル存在判断
    File[] files = directory.listFiles();
    if (files == null) {
    return true;
    }

    //全ファイル名取得
    for (File file : files) {
        //ディレクトリかチェック
        if (file.isDirectory()) {
            checkDupulicates(file, fileNames,checkFileName);
        } else {
            //ファイルの場合は、ファイル名取得
            String AddFileName = file.getName();

            //配列の中に、保存済みのファイルかチェック
            if (fileNames.contains(AddFileName)){
                System.out.println("保存済みファイル" + file.getAbsolutePath());
            } else {
                //未保存のファイルのみ、追加する。
                fileNames.add(AddFileName);
            }
        }
    }

    //重複チェック
if (fileNames.contains(checkFileName)){
        System.out.println("重複しているファイル：" + checkFileName);
        return false;
}

return true;
    }//重複チェックエンド

}




