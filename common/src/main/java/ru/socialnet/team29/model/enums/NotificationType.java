package ru.socialnet.team29.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum NotificationType {
    
    POST(1, "POST"),
    POST_COMMENT(2, "POST_COMMENT"),
    COMMENT_COMMENT(3, "COMMENT_COMMENT"),
    FRIEND_REQUEST(4, "FRIEND_REQUEST"),
    MESSAGE(5, "MESSAGE"),
    FRIEND_BIRTHDAY(6, "FRIEND_BIRTHDAY"),
    SEND_EMAIL_MESSAGE(7, "SEND_EMAIL_MESSAGE");
    private int number;
    private String value;

    public static String getTypeEnum(int number){
      NotificationType[] types =  NotificationType.values();
      if (number > types.length) return null;
        for (NotificationType type  : types) {
           if (number == type.getNumber()){
              return type.value;
           }
        }
       return null;
    }


    @Override
    public String toString() {
        return value;
    }
}
