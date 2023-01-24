package ru.socialnet.team29.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.socialnet.team29.dto.CommentDto;
import ru.socialnet.team29.services.CommentsService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
public class CommentController {

    private final CommentsService commentService;

    @PostMapping(value = "/saveComment")
    public CommentDto saveComment(@RequestBody CommentDto commentDto) {
        log.info("Получили запрос от core - добавить комментарий поста: {}", commentDto.getCommentText());
        return commentService.addNewComment(commentDto);
    }

    @PutMapping(value = "/update")
    public CommentDto updateComments(@RequestBody CommentDto commentDto) {
        return commentService.updateComment(commentDto);
    }

    @DeleteMapping(value = "/delete")
    public Boolean deleteCommentById(@RequestBody Integer commentId) {
        log.info("Получили запрос от core - убрать комментарий поста c id=" + commentId);
        return commentService.deleteById(commentId);
    }

    @GetMapping(value = "/oneComment")
    public CommentDto findCommentById(@RequestParam Integer commentId) {
        return commentService.findByCommentId(commentId);
    }
    @GetMapping(value = "/getCommentIdByPostId")
    public List<Integer> getCommentByPostId(@RequestParam Integer postId) {
        return commentService.getCommentIdByPostId(postId);
    }

    @GetMapping("/comment")
    public List<CommentDto> getCommentsById(@RequestParam Integer postId, @RequestParam Integer personId) {
        log.info("Получили запрос на выдачу всех коментариев по id поста {}", postId);
        return commentService.getCommentByPostId(postId, personId);
    }


}
