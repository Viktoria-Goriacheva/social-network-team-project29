package ru.socialnet.team29.controllers;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.socialnet.team29.exception.IoException;
import ru.socialnet.team29.model.Person;
import ru.socialnet.team29.service.StorageService;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/")
@Slf4j
public class StorageController {

    private final StorageService storageService;

    @PostMapping(value = "storage", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Person> storageAvatarPhotoResponse(@RequestParam("imageFile") MultipartFile imageFile) {
        return new ResponseEntity<>(storageService.uploadFileToAccount(imageFile), HttpStatus.OK);
    }
    @PostMapping(value = "post/storagePostPhoto", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> storagePostPhotoResponse(@RequestParam("imageFile") MultipartFile imageFile)  {
        return new ResponseEntity<>(storageService.uploadFileToPost(imageFile), HttpStatus.OK);
    }
}
