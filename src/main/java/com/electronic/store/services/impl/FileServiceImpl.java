package com.electronic.store.services.impl;

import com.electronic.store.services.FileService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.UUID;

@Service
public class FileServiceImpl implements FileService {

    private Logger loggger= LoggerFactory.getLogger(FileServiceImpl.class);

    @Override
    public String uploadImage(String path, MultipartFile file) {
        String originalFilenameme = file.getOriginalFilename();

        loggger.info("filename :{}",originalFilenameme);
        String filename= UUID.randomUUID().toString();
        String extension=originalFilenameme.substring(originalFilenameme.lastIndexOf("."));
        String fileNameWithExtesion=


        return null;
    }

    @Override
    public InputStream getResource(String path, String fileName) {
        return null;
    }
}
