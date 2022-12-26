package ru.socialnet.team29.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.Transformation;
import com.cloudinary.utils.ObjectUtils;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.socialnet.team29.config.CloudinaryConfig;
import ru.socialnet.team29.exception.EntityNotFoundException;
import ru.socialnet.team29.exception.IoException;
import ru.socialnet.team29.model.Person;
import ru.socialnet.team29.serviceInterface.feign.DBConnectionFeignInterface;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class StorageService {
    private final DBConnectionFeignInterface feignInterface;
    private final PersonServiceImpl personService;
    private final CloudinaryConfig cloudinaryConfig;


    @SneakyThrows
    public Person uploadFileToAccount(MultipartFile imageFile) {
        log.info("Попытка загрузить Аватар!");
        File uploadedFile;
        uploadedFile = convertMultiPartToFile(imageFile);
        Map uploadResult = null;
        if (uploadedFile == null) {
            Cloudinary cloudinary = cloudinaryConfig.initCloudinary();
            cloudinary.url().transformation(new Transformation<>().width(100).height(100));
            uploadResult = cloudinaryConfig.initCloudinary().uploader().upload(uploadedFile, ObjectUtils.emptyMap());
        } else {
            log.error("Ошибка загрузки файла!");
            throw new EntityNotFoundException("Файл не найден");
        }

        Person person = personService.getPersonFromSecurityContext();
        person.setPhoto(uploadResult.get("url").toString());
        feignInterface.updatePerson(person);

        return person;
    }

    @SneakyThrows
    public String uploadFileToPost(MultipartFile imageFile) {
        log.info("Попытка загрузить фото в пост!");
        File uploadedFile = null;
        Cloudinary cloudinary = cloudinaryConfig.initCloudinary();
        Map uploadResult;
        if (uploadedFile == null) {
            log.error("Ошибка загрузки файла!");
            throw new EntityNotFoundException("Файл не найден");
        } else {
            try {
                uploadedFile = convertMultiPartToFile(imageFile);
                cloudinary.url().transformation(new Transformation<>().width(100).height(100));
                uploadResult = cloudinaryConfig.initCloudinary().uploader().upload(uploadedFile, ObjectUtils.emptyMap());
            } catch (IOException e) {
                log.error("Ошибка загрузки файла!");
                throw new IoException("Ошибка загрузки файла!");
            }
            log.info("Файл загружен");
            return uploadResult.get("url").toString();

        }
    }

    @SneakyThrows
    private File convertMultiPartToFile(MultipartFile imageFile) {
        File convFile = new File(Objects.requireNonNull(imageFile.getOriginalFilename()));
        FileOutputStream fos = new FileOutputStream(convFile);
        fos.write(imageFile.getBytes());
        fos.close();
        return convFile;
    }

}
