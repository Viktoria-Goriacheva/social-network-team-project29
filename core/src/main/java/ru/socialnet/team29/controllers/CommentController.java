package ru.socialnet.team29.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.socialnet.team29.answers.PagePostResponseForComment;
import ru.socialnet.team29.dto.CommentDto;
import ru.socialnet.team29.service.CommentService;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/post/{id}/comment")
public class CommentController {
    private final CommentService commentService;

    @PostMapping
    public ResponseEntity<CommentDto> saveComment(@PathVariable(value = "id") Integer postId,
                                                  @RequestBody CommentDto commentDto) {
        return new ResponseEntity<>(commentService.addComment(postId, commentDto.getCommentText(), commentDto.getParentId()), HttpStatus.CREATED);
    }
    @PutMapping("/{comment_id}")
    public ResponseEntity<CommentDto> editComment(
            @PathVariable(value = "id") Integer id,
            @PathVariable(value = "comment_id") Integer commentId,
            @RequestBody CommentDto commentDto) {
        return new ResponseEntity<>(commentService.editComment(id,
                commentId, commentDto.getCommentText()), HttpStatus.OK);
    }
    @DeleteMapping("/{comment_id}")
    public ResponseEntity<Boolean> deleteComment(
            @PathVariable(value = "id") Integer id,
            @PathVariable(value = "comment_id") Integer commentId) {
        return new ResponseEntity<>(commentService.deleteCommentById(id, commentId), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<PagePostResponseForComment> getAllCommentsFromPost(@PathVariable(value = "id") Integer id,
                                                             @RequestParam(value = "page", defaultValue = "1") Integer page,
                                                             @RequestParam(value = "size", required = false, defaultValue = "1") Integer size,
                                                             String sort) {
        return new ResponseEntity<>(commentService.getAllComment(id, size, page, sort), HttpStatus.OK);
    }

    @GetMapping("/{comment_id}/subcomment")
    public ResponseEntity<PagePostResponseForComment> subComment(
            @PathVariable(value = "id") Integer id,
            @PathVariable(value = "comment_id") Integer commentId,
            @RequestParam(value = "page", defaultValue = "1") Integer page,
            @RequestParam(value = "size", required = false, defaultValue = "1") Integer size) {
        return new ResponseEntity<>(commentService.getSubcomment(id, commentId, page, size), HttpStatus.OK);
    }
}