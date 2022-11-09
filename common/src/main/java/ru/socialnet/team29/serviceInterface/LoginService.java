package ru.socialnet.team29.serviceInterface;

import ru.socialnet.team29.dto.PersonLoginDTO;
import ru.socialnet.team29.model.ProfileResponse;

public interface LoginService {
   ProfileResponse getProfile(PersonLoginDTO personLoginDTO);
}
