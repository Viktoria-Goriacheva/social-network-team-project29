package ru.socialnet.team29.serviceInterface;

import ru.socialnet.team29.responses.dialog_response.MessageDto;

public interface DialogService {
   MessageDto saveMessage(MessageDto messageDto);

}
