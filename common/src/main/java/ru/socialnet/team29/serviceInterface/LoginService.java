package ru.socialnet.team29.serviceInterface;

import ru.socialnet.team29.answers.MessageAnswer;
import ru.socialnet.team29.dto.PersonLoginDTO;
import ru.socialnet.team29.model.ProfileResponse;

import javax.servlet.http.HttpServletResponse;

public interface LoginService {
   ProfileResponse getProfile(PersonLoginDTO personLoginDTO);

   void setCookieToAnswer(HttpServletResponse response, MessageAnswer answer);
}
