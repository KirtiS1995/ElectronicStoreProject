package com.electronic.store.services;

import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;

public interface FileService {


    String uploadImage(String path, MultipartFile file);
    InputStream getResource(String path, String fileName);
}
