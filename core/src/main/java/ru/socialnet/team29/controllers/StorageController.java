package ru.socialnet.team29.controllers;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.socialnet.team29.dto.StorageImageDto;
import ru.socialnet.team29.exception.IoException;
import ru.socialnet.team29.model.Person;
import ru.socialnet.team29.service.StorageService;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/")
public class StorageController {

    private final StorageService storageService;

    @PostMapping(value = "storage", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Person> storageAvatarPhotoResponse(@RequestParam MultipartFile file) {
        return new ResponseEntity<>(storageService.uploadFileToAccount(file), HttpStatus.OK);
    }
    @PostMapping(value = "post/storagePostPhoto", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<StorageImageDto> storagePostPhotoResponse(@RequestParam MultipartFile file) throws IoException {
        return new ResponseEntity<>(storageService.uploadFileToPost(file), HttpStatus.OK);
    }
}
