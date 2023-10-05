package com.lcwd.electronic.store.services.impl;

import com.lcwd.electronic.store.exceptions.BadRequest;
import com.lcwd.electronic.store.services.FileService;
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


    Logger logger = LoggerFactory.getLogger(FileService.class);
    @Override
    public String uploadFile(MultipartFile multipartFile, String path) throws IOException {

        String originalFilename = multipartFile.getOriginalFilename();
        logger.info("fileName : {}" , originalFilename);

        String  fileName = UUID.randomUUID().toString();

        String fileExtension = originalFilename.substring(originalFilename.lastIndexOf("."));

        logger.info("extension : {}" , fileExtension);

        String fileNameWithExtension = fileName+fileExtension;
        String fullPathWithFileName = path+ fileNameWithExtension;

        if(fileExtension.equalsIgnoreCase(".png") ||
                fileExtension.equalsIgnoreCase(".jpg")
                || fileExtension.equalsIgnoreCase(".jpeg")
        ){
            // create a floder
            File folder = new File(path);
            if(!folder.exists()){
               folder.mkdirs();
            }

            Files.copy(multipartFile.getInputStream() , Paths.get(fullPathWithFileName));
            System.gc();

        }
        else{
            throw  new BadRequest("file not allow with this extension " + fileExtension + "!!");
        }
        return fileNameWithExtension    ;
    }

    @Override
    public InputStream getResource(String path, String fileName) throws FileNotFoundException {
        String fullPath = path+fileName;
        InputStream inputStream = new FileInputStream(fullPath);
        logger.info("inputStrem : {}" , inputStream);
        return inputStream;
    }

    @Override
    public Boolean deleteFile(String path, String fileName) throws FileNotFoundException {
        String fullPath = path + fileName;
        File f = new File(fullPath);
        if(f.delete()){
            return true;
        }
        else{
            return false;
        }
    }


}
