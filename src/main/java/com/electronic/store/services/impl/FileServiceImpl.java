package com.electronic.store.services.impl;

import com.electronic.store.exceptions.BadApiRequestException;
import com.electronic.store.helper.AppConstats;
import com.electronic.store.services.FileService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class FileServiceImpl implements FileService {

    private Logger logger= LoggerFactory.getLogger(FileServiceImpl.class);

    /**
     * @implNote This method is for image uploading
     * @param path
     * @param file
     * @return
     * @throws IOException
     */
    @Override
    public String uploadImage(String path, MultipartFile file) throws IOException {
        String originalFilename = file.getOriginalFilename();

        logger.info("filename :{}",originalFilename);
        String filename= UUID.randomUUID().toString();
        String extension=originalFilename.substring(originalFilename.lastIndexOf("."));
        String fileNameWithExtension=filename+extension;
//        String fullPathWithFileName=path+ File.separator +fileNameWithExtension;
        String fullPathWithFileName=path + fileNameWithExtension;

        if (extension.equalsIgnoreCase(".png") || extension.equalsIgnoreCase(".jpg") || extension.equalsIgnoreCase(".jpeg"))
        {
            //File save
            File folder=new File(path);

            if (!folder.exists()){
                //create folder
                folder.mkdirs();
            }
            //upload Image
            Files.copy(file.getInputStream(), Paths.get(fullPathWithFileName));
            logger.info("File full path:{}",fullPathWithFileName);
            logger.info("Completed DAO call for uploading image");
            return fileNameWithExtension;
        }
        else {
        throw new BadApiRequestException(AppConstats.NOT_ALLOWED+extension);
        }

    }

    /**
     * @implNote This method is for serving image
     * @param path
     * @param fileName
     * @return
     * @throws FileNotFoundException
     */
    @Override
    public InputStream getResource(String path, String fileName) throws FileNotFoundException {
        logger.info("Entering DAO call for getting image ");
        String fullPath = path + File.separator + fileName;
        InputStream inputStream = new FileInputStream(fullPath);
        logger.info("Completed DAO call for getting image  ");
        return inputStream;

    }
}
