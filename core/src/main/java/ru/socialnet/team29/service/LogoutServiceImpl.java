package ru.socialnet.team29.service;

import org.springframework.stereotype.Service;
import ru.socialnet.team29.model.LogoutResponse;
import ru.socialnet.team29.serviceInterface.LogoutService;

import java.time.LocalDateTime;
@Service
public class LogoutServiceImpl implements LogoutService {
    public LogoutResponse getResponse() {
        return LogoutResponse.builder()
                .error("1")
                .timestamp(LocalDateTime.now())
                .data(LocalDateTime.now())
                .build();
    }
}
