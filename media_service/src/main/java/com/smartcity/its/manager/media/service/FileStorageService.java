package com.smartcity.its.manager.media.service;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import com.its.module.model.exception.BadRequestException;
import org.springframework.web.multipart.MultipartFile;


public class FileStorageService {

    private final Path fileStorageLocation;

    public FileStorageService(String fileStorage) {
        this.fileStorageLocation = Paths.get(fileStorage).toAbsolutePath().normalize();
        System.out.println(fileStorageLocation);
        try {
            Files.createDirectories(this.fileStorageLocation);
            Files.createDirectories(this.fileStorageLocation.resolve("thumbnails"));
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new BadRequestException("Could not create the directory where the uploaded files will be stored.");
        }
    }

    public String storeFile(InputStream inputStream, String fileName) throws IOException {
        if (fileName.contains("..")) {
            throw new BadRequestException("Sorry! Filename contains invalid path sequence " + fileName);
        }

        Path targetLocation = this.fileStorageLocation.resolve(fileName);
        Files.copy(inputStream, targetLocation, StandardCopyOption.REPLACE_EXISTING);
        return fileName;
    }

}
