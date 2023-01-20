package ru.socialnet.team29.controllers;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.socialnet.team29.responses.dialog_response.*;
import ru.socialnet.team29.services.MessageService;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@Slf4j
public class MessageController {
    private final MessageService messageService;

    @GetMapping(value = "/message")
    public ResponseEntity<List<MessageDto>> getMessageByAuthorId(@RequestParam Long authorId) {
        log.info("got a request for to get a last message from MessageController, from db module. authorId " + authorId);
        return new ResponseEntity<>(messageService.getMessageById(authorId), HttpStatus.OK);
    }

    @PostMapping("/message")
    public ResponseEntity<MessageDto> saveMessage(@RequestBody MessageDto messageDto) {
        log.info("got a request for to save a message to the db from MessageController from db " + messageDto);
        return new ResponseEntity<>(messageService.saveMessage(messageDto), HttpStatus.OK);
    }

    @GetMapping(value = "/countUnread")
    public ResponseEntity <Map<Long, UnreadCount> > getUnreadMessages(@RequestParam Long authorId){
        Map<Long, UnreadCount>  unreadMessages = messageService.getUnreadMessages(authorId);
        log.info(" unreadMessages " + unreadMessages);
        return new ResponseEntity<>(unreadMessages, HttpStatus.OK);
    }

    @GetMapping(value = "/messages")
    List<MessageDto> getFullDialogData(@RequestParam Long id, @RequestParam Long companionId) {
        log.info("got a request for to get some dialogs and messages from getFullDialogData, from db module. authorId " + id);
        return messageService.getFullDialogData(id, companionId);
    }

    @GetMapping(value = "/messages/set_read")
    ResponseEntity<ShortDialogResponse<MessageDatum>> setReadAllStatus(@RequestParam Long id, @RequestParam Long companionId) {
        return new ResponseEntity<>(messageService.setReadAllStatus(id, companionId), HttpStatus.OK);
    }
}
