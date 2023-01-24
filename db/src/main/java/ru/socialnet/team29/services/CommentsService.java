package ru.socialnet.team29.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.socialnet.team29.dto.CommentDto;
import ru.socialnet.team29.mappers.CommentMapper;
import ru.socialnet.team29.repository.CommentRepository;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class CommentsService {
    private final CommentRepository commentRepository;
    private final CommentMapper commentMapper;

    public Integer getCountCommentsByPostId(int id) {
        return commentRepository.getCountCommentsByPostId(id);
    }

    public CommentDto addNewComment(CommentDto commentDto){
        log.info("Добавлен новый комментарий {}", commentDto.getCommentText());
        return commentMapper.postCommentRecordToCommentDto( commentRepository.insert(commentMapper.CommentDtoToPostCommentRecord(commentDto)));
    }

    public CommentDto updateComment(CommentDto commentDto) {
        log.info("Обновление комментария с id = {}", commentDto.getId());
        return commentMapper.postCommentRecordToCommentDto( commentRepository.update(commentMapper.CommentDtoToPostCommentRecord(commentDto)));
    }

    public Boolean deleteById(Integer id) {
        log.info("Удаление комментария с id - {}", id);
        return commentRepository.deleteById(id);
    }

    public CommentDto getCommentById(Integer commentId) {
        log.info("Получение комментария с id = {}", commentId);
        CommentDto comment = commentMapper.postCommentRecordToCommentDto(commentRepository.findById(commentId));
        return comment;
    }

    public CommentDto findByCommentId(Integer commentId){
        log.info("Получение комментария с ID = {}", commentId);
        CommentDto commentDto = commentMapper.postCommentRecordToCommentDto(commentRepository.findById(commentId));
        return commentDto;
    }

    public List<CommentDto> getCommentByPostId(Integer postId) {
         List<CommentDto> comments = new ArrayList<>();
        List<Integer> posts;
        if (postId != 0) {
            posts = commentRepository.findCommentByPostId(postId);
            posts.forEach(id -> comments.add(getCommentById(id)));
            return comments;
        } return null;
    }

    public List<Integer> getCommentIdByPostId(Integer postId) {
        List<Integer> comment = commentRepository.getCommentIdByPostId(postId);
        return comment;
    }

}
