package com.foodexpress.utilities;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
public class FileUploadHelper {
    
    private final static String UPLOAD_DIR = "C:\\Users\\Excel\\Documents\\workspace-spring-tool-suite-4-4.27.0.RELEASE\\FoodExpress\\src\\main\\resources\\static\\images";

    public static List<String> uploadFiles(MultipartFile[] files) {
        List<String> uploadedFileNames = new ArrayList<>();

        try {
            for (MultipartFile file : files) {
                
                String originalFilename = file.getOriginalFilename();
                String fileExtension = originalFilename.substring(originalFilename.lastIndexOf("."));
                String uniqueFilename = UUID.randomUUID().toString() + fileExtension;

                Path filePath = Paths.get(UPLOAD_DIR + File.separator + uniqueFilename);

                Files.copy(file.getInputStream(), filePath);

                uploadedFileNames.add(uniqueFilename);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return uploadedFileNames; 
    }
}
