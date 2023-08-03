package com.todo.app.services;

import java.io.File;
import java.io.IOException;

import org.springframework.web.multipart.MultipartFile;

public class ImageUtil {

    public static void saveImage(MultipartFile imageFile, String savePath) throws IOException {
        File file = new File(savePath);
        imageFile.transferTo(file);
    }
}
