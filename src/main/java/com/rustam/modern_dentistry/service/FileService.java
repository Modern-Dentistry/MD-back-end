package com.rustam.modern_dentistry.service;

import com.rustam.modern_dentistry.exception.custom.FileException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Objects;
import java.util.UUID;

@Service
public class FileService {

    public void writeFile(MultipartFile file, String uploadDir, String newFileName) {
        try {
            Path targetLocation = Paths.get(uploadDir).resolve(newFileName);
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new FileException("File yüklənə bilmədi"); // FileOperationException
        }
    }

    public void deleteFile(String fullPath) {
        try {
            Path path = Paths.get(fullPath);
            Files.deleteIfExists(path);
        } catch (IOException e) {
            throw new FileException("Fayl silinə bilmədi: " + fullPath);
        }
    }

    public void updateFile(MultipartFile file, String path, String oldFileName, String newFileName) {
        if (file != null && !file.isEmpty()) {
            if (oldFileName != null) {
                deleteFile(path + "/" + oldFileName);
            }
            writeFile(file, path, newFileName);
        }
    }

    public String getNewFileName(MultipartFile file, String fileNameStart) {
        int dotIndex = Objects.requireNonNull(file.getOriginalFilename()).lastIndexOf('.');
        var fileExtension = file.getOriginalFilename().substring(dotIndex + 1);
        return fileNameStart + UUID.randomUUID() + "." + fileExtension;
    }

    public void checkFileIfExist(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new FileException("Fayl boşdur və ya null-dır.");
        }
    }
}
