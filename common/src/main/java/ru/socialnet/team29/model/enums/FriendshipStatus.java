package ru.socialnet.team29.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum FriendshipStatus {
    REQUEST_FROM(1, "REQUEST_FROM"),
    REQUEST_TO(2, "REQUEST_TO"),
    FRIEND(3, "FRIEND"),
    BLOCKED(4, "BLOCKED"),
    REJECTING(5, "REJECTING"),
    SUBSCRIBED(6, "SUBSCRIBED"),
    WATCHING(7, "WATCHING");

    private Integer number;
    private String value;

    public static String getTypeEnum(int number){
        FriendshipStatus[] types =  FriendshipStatus.values();
        if (number > types.length) return null;
        for (FriendshipStatus type  : types) {
            if (number == type.getNumber()){
                return type.value;
            }
        }
        return null;
    }

    public static FriendshipStatus getEnum(String numberString) {
        return FriendshipStatus.getEnum(getTypeEnum(Integer.parseInt(numberString)));
    }

    @Override
    public String toString() {
        return value;
    }
}
