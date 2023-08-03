package com.todo.app.entity;

import org.springframework.web.multipart.MultipartFile;

public class ImageForm {

    private MultipartFile imageFile;

    public MultipartFile getImageFile() {
        return imageFile;
    }

    public void setImageFile(MultipartFile imageFile) {
        this.imageFile = imageFile;
    }
}

