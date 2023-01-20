package ru.socialnet.team29.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.socialnet.team29.responses.dialog_response.*;
import ru.socialnet.team29.service.DialogServiceImpl;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/dialogs")
@Slf4j
public class DialogController {
    private final DialogServiceImpl dialogService;

    @PutMapping("/{companionId}")
    public ResponseEntity<ShortDialogResponse<MessageDatum>> getDialogCompanionId(
            @PathVariable(value = "companionId") Long companionId) {
        var dialogResponse = dialogService.setReadAllStatus(companionId);
        return new ResponseEntity<>(dialogResponse, HttpStatus.OK);
    }

    @GetMapping()
    public ResponseEntity<DialogResponse<DialogData>> getDialogs(
            @RequestParam(value = "offset", required = false, defaultValue = "0") Integer offset,// номер страницы с диалогами
            @RequestParam(value = "itemPerPage", required = false, defaultValue = "0") Integer itemPerPage) {// кол-во диалогов на странице
        DialogResponse<DialogData> dialogResponse = dialogService.getDialogResponse(offset, itemPerPage);
        return new ResponseEntity<>(dialogResponse, HttpStatus.OK);
    }

    @GetMapping("/unreaded")
    public ResponseEntity<ShortDialogResponse<UnreadCount>> getUnreadedDialogs() {
        return new ResponseEntity<>(dialogService.getUnreadedDialogResponse(), HttpStatus.OK);
    }

    @GetMapping("/messages")
    public ResponseEntity<DialogResponse<MessageDto>> getMessages(
            @RequestParam(value = "offset", required = false, defaultValue = "0") Integer offset,
            @RequestParam(value = "itemPerPage", required = false, defaultValue = "20") Integer itemPerPage,
            @RequestParam(value = "companionId", required = false, defaultValue = "0L") Long companionId) {
        DialogResponse<MessageDto> dialogResponse = dialogService.getCompanionDialogResponse(offset, itemPerPage, companionId);
        return new ResponseEntity<>(dialogResponse, HttpStatus.OK);
    }
}
