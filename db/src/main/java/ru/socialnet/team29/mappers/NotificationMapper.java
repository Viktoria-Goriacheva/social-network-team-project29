package ru.socialnet.team29.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.socialnet.team29.answers.AddNewNotification;
import ru.socialnet.team29.answers.NotificationForFront;

import ru.socialnet.team29.model.NotificationCommon;
import ru.socialnet.team29.model.Person;
import ru.socialnet.team29.model.enums.NotificationType;
import ru.socialnet.team29.payloads.AddNotificationPayload;

import java.time.LocalDateTime;

@Mapper(componentModel = "spring", imports = {LocalDateTime.class, NotificationType.class})

public interface NotificationMapper {

    @Mapping(source = "notificationCommon.sentTime", target = "time")
    @Mapping(source = "notificationCommon.personId", target = "userId")
    @Mapping(target = "content", constant = "")
    @Mapping(target = "isStatusSent", constant = "true")
    @Mapping(source = "payload.authorId", target = "authorId")
    @Mapping(source = "payload.notificationType", target = "notificationType")
    AddNewNotification notificationCommonToAddNewNotification(NotificationCommon notificationCommon, AddNotificationPayload payload);


    @Mapping(target = "sentTime", expression = "java(LocalDateTime.now())")
    @Mapping(source = "payload.userId", target = "personId")
    @Mapping(target = "typeId", expression = "java(NotificationType.valueOf(payload.getNotificationType()).getNumber())")
    @Mapping(target = "contact", source = "contact")
    NotificationCommon addNotificationPayloadToNotificationCommon(AddNotificationPayload payload, String contact);

    @Mapping(target = "content", constant = "")
    @Mapping(source = "notificationCommon.id", target = "id")
    @Mapping(target = "notificationType", expression = "java(NotificationType.getTypeEnum(notificationCommon.getTypeId()))")
    NotificationForFront notificationCommonToNotificationForFront(NotificationCommon notificationCommon, Person person);



}
