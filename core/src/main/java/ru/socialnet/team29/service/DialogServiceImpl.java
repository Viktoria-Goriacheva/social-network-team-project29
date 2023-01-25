package ru.socialnet.team29.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import ru.socialnet.team29.responses.dialog_response.*;
import ru.socialnet.team29.model.Person;
import ru.socialnet.team29.serviceInterface.feign.DBConnectionFeignInterface;
import ru.socialnet.team29.serviceInterface.feign.DBConnectionFeignInterfacePerson;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class DialogServiceImpl {
    private final DBConnectionFeignInterface feignInterface;
    private final DBConnectionFeignInterfacePerson feignInterfacePerson;
    private final PersonServiceImpl personService;

    public DialogResponse<DialogData> getDialogResponse(Integer offset, Integer itemPerPage) {

        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Long idPerson = Long.valueOf(feignInterfacePerson.getPersonByEmail(email).getId());

        ArrayList<DialogData> dialogData = getDialogData(idPerson);
        return DialogResponse.<DialogData>builder()
                .total(dialogData.size())
                .offset(offset)
                .perPage(itemPerPage)
                .currentUserId(idPerson)
                .data(dialogData)
                .build();
    }

    public ArrayList<DialogData> getDialogData(Long idPerson) {

        List<MessageDto> listMessages = getLastMessageFromBD(idPerson);
        ArrayList<DialogData> listDialogs = new ArrayList<>();
        Map<Long, UnreadCount> unreadMessages = feignInterface.getUnreadMessages(idPerson);

        listMessages
            .forEach(lm -> {
                Long recipientId = lm.getRecipientId();
                Person recipient = feignInterfacePerson.getPersonById(Math.toIntExact(idPerson), Math.toIntExact(recipientId));
                Integer unreadCount = (unreadMessages.containsKey(recipientId)) ? unreadMessages.get(recipientId).getCount() : 0;
                listDialogs.add(new DialogData(lm.getId(), unreadCount, recipient, lm));
            });
        return listDialogs;
    }

    public List<MessageDto> getLastMessageFromBD(Long authorId) {
        List<MessageDto> listLastMessages = feignInterface.getMessageByAuthor(authorId);
        log.info("got a list Last Messages  function *getLastMessageFromBD*  from DialogService, " + listLastMessages);
        return listLastMessages;
    }

    public MessageDto saveMessage(MessageDto messageDto) {
        log.info(messageDto + " MessageTable response from saveMessage from DialogService");
        return feignInterface.saveMessage(messageDto);
    }

    public DialogResponse<MessageDto> getCompanionDialogResponse(Integer offset, Integer itemPerPage, Long companionId) {
        Long id = Long.valueOf(personService.getIdPersonFromSecurityContext());
        List<MessageDto> dialogData = feignInterface.getFullDialogData(id, companionId);
        return DialogResponse.<MessageDto>builder()
                .total(dialogData.size())
                .offset(offset)
                .perPage(itemPerPage)
                .currentUserId(id)
                .data(dialogData)
                .build();
    }

    public ShortDialogResponse<MessageDatum> setReadAllStatus(Long companionId) {
        Long id = Long.valueOf(personService.getIdPersonFromSecurityContext());
        return feignInterface.setReadAllStatus(id, companionId);
    }

    public ShortDialogResponse<UnreadCount> getUnreadedDialogResponse() {
        Long personId = Long.valueOf(personService.getIdPersonFromSecurityContext());
        Map<Long, UnreadCount> unreadMessages = feignInterface.getUnreadMessages(personId);
        Integer count = unreadMessages.values().stream().mapToInt(UnreadCount::getCount).sum();
        return ShortDialogResponse.<UnreadCount>builder()
                .timestamp(OffsetDateTime.now().toInstant().toEpochMilli())
                .data(new UnreadCount(count, 0))
                .build();
    }
}
