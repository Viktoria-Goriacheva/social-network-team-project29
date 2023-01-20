package ru.socialnet.team29.responses.dialog_response;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.socialnet.team29.model.Person;

@Data
@AllArgsConstructor
public class DialogData {
   private Long id;
   private Integer unreadCount;
   private Person conversationPartner;
   private MessageDto lastMessage;
}
