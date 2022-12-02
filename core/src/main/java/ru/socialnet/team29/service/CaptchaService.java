package ru.socialnet.team29.service;

import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import com.github.cage.GCage;
import lombok.RequiredArgsConstructor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import ru.socialnet.team29.responses.CaptchaResponse;

@Service
@RequiredArgsConstructor
@Slf4j
public class CaptchaService {
    private final static int HOUR = 6000000;

     Map<String, String> mapCache = new HashMap<>();

    public CaptchaResponse getCaptchaCode() {

        CaptchaResponse captchaResponse = new CaptchaResponse();
        GCage captcha = new GCage();
        String codeCaptcha = captcha.getTokenGenerator().next().substring(8);
        String image = getImageBase64(captcha, codeCaptcha);
        String secretCode = generateSecretCode();
        captchaResponse.setImageBase64(image);
        captchaResponse.setSecretCode(secretCode);
        saveCaptchaToDb(codeCaptcha, secretCode);
        return captchaResponse;
    }

    private String getImageBase64(GCage gCage, String code) {
        byte[] fileContent = gCage.draw(code);
        String encodedString = Base64.getEncoder().encodeToString(fileContent);
        return "data:image/png;base64, " + encodedString;
    }

    private String generateSecretCode() {
        return UUID.randomUUID().toString().replaceAll("-", "").toLowerCase();
    }

    private void saveCaptchaToDb(String codeCaptcha, String secretCode) {
        mapCache.put(secretCode, codeCaptcha);
        log.info(mapCache + " mapCache from save ");
    }

    @Scheduled(fixedRate = HOUR)
    private void deleteOldCaptchaCodes() {
        mapCache.clear();
    }

    //добавить в сервис регистрации пользователя(он еще не готов)
   public  boolean checkCaptcha(String secretToken) {

        if (!mapCache.containsKey(secretToken)) {
            log.info(secretToken + " Код с картинки введён неверно");
                return false;
            } else log.info(secretToken + " Код с картинки введён верно");
            return true;
        }
}
