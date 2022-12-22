package ru.socialnet.team29.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.socialnet.team29.responses.dialog_response.*;
import ru.socialnet.team29.repository.MessageRepository;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class MessageService {

    private final MessageRepository messageRepository;

    public MessageDto saveMessage(MessageDto messageDto) {
        log.info("function *saveMessage* has been executed. from MessageService, from db module");
        return messageDto;
    }

    public List<MessageDto> getMessageById(Long id) {
        List<MessageDto> lastMessage = messageRepository.getLastMessage(id);
        log.info("function *getMessageById* has been executed. from MessageService, from db module. Got a List " + lastMessage);
        return lastMessage;
    }

    public Map<Long, UnreadCount> getUnreadMessages(Long id) {
        Map<Long, UnreadCount>  unreadMessages = messageRepository.getUnreadMessages(id);
        log.info("function *getMessageById* has been executed. from MessageService, from db module. Got a List " + unreadMessages);
        return unreadMessages;
    }

    public List<MessageDto> getFullDialogData(Long id, Long companionId) {
        List<MessageDto> dialogs = messageRepository.getFullDialogData(id, companionId);
        log.info("function *getFullDialogData* has been executed. from MessageService. id={0} companionId={1}".formatted(id, companionId));
        return dialogs;
    }

    public ShortDialogResponse<MessageDatum> setReadAllStatus(Long id, Long companionId) {
        log.info("function *setReadAllStatus* has been executed. from MessageService. id={0} companionId={1}".formatted(id, companionId));
        messageRepository.setReadAllStatus(id, companionId);
        return  ShortDialogResponse.<MessageDatum>builder()
                .timestamp(OffsetDateTime.now().toInstant().toEpochMilli())
                .data(MessageDatum.builder().message("Ok").build())
                .build();
    }
}