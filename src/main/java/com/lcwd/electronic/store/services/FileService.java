package com.lcwd.electronic.store.services;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;


public interface FileService {

    String uploadFile(MultipartFile multipartFile , String path) throws IOException;

    InputStream getResource(String path , String fileName) throws FileNotFoundException;

    Boolean deleteFile(String path , String fileName) throws  FileNotFoundException;
}
