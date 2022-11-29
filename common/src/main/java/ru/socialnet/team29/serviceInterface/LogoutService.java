package ru.socialnet.team29.serviceInterface;

import ru.socialnet.team29.model.LogoutResponse;

import javax.servlet.http.HttpServletRequest;

public interface LogoutService {


    void logout(HttpServletRequest request);
}
