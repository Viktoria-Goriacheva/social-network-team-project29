package ru.socialnet.team29.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import ru.socialnet.team29.answers.PagePostResponseForComment;
import ru.socialnet.team29.dto.CommentDto;
import ru.socialnet.team29.model.PageableObject;
import ru.socialnet.team29.model.PostDto;
import ru.socialnet.team29.model.Sort;
import ru.socialnet.team29.model.enums.CommentType;
import ru.socialnet.team29.serviceInterface.feign.DBConnectionFeignInterface;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Slf4j
public class CommentService {
    private final PersonServiceImpl personService;
    private final PostServiceImpl postService;
    private final DBConnectionFeignInterface feignInterface;

    public CommentDto addComment(Integer postId, String commentText, Integer parentId) {
        Integer author = personService.getIdPersonFromSecurityContext();
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        PostDto post = postService.findPostById(postId,email);

        CommentDto comment = CommentDto.builder()
                .time(OffsetDateTime.now())
                .authorId(author)
                .parentId(parentId == null? 0:parentId)
                .commentText(commentText)
                .postId(post.getId())
                .isBlocked(false)
                .myLike(false)
                .likes(post.getCommentsCount())
                .subComments(new ArrayList<>())
                .build();
        log.info("Сохранение нового комментария {}", comment);
        return feignInterface.saveComment(comment);
    }
    public CommentDto editComment(Integer postId,Integer commentId, String commentText){
        log.info("Запрос от фронта - обновить комментарий commentId = {}", commentId);
        CommentDto oldDto = feignInterface.getCommentById(commentId);
        CommentDto newDto = CommentDto.builder()
                .id(oldDto.getId())
                .commentType("POST")
                .time(oldDto.getTime())
                .authorId(oldDto.getAuthorId())
                .parentId(oldDto.getParentId())
                .commentText(commentText)
                .postId(postId)
                .isBlocked(oldDto.getIsBlocked())
                .myLike(oldDto.getMyLike())
                .likes(oldDto.getLikes())
                .subComments(oldDto.getSubComments())
                .build();

        return feignInterface.updateComments(newDto);
    }

    public Boolean deleteCommentById(int postId, int commentId) {
        log.info("Запрос от фронта - удалить комментарий с id= {}", commentId);
        return feignInterface.deleteCommentById(commentId);
    }

    public PagePostResponseForComment getAllComment(int postId, int size, int page, String sort) {
        Integer meId = personService.getIdPersonFromSecurityContext();
        Sort sorter = Sort.builder().ascending(false).descending(true).direction("DESC")
                .ignoreCase(false).nullHandling("NATIVE").property("time").build();
        List<Sort> sortList = new ArrayList<>();
        sortList.add(sorter);

        PageableObject pageableObject = PageableObject.builder()
                .unpaged(false)
                .paged(true)
                .sort(sortList)
                .pageNumber(getPageNumber(page, postId))
                .pageSize(size)
                .offset(getOffset(page, size, postId))
                .build();
        List<CommentDto> commentDtoList = feignInterface.getCommentDto(postId, meId);

        int totalElements = commentDtoList.size();
        int totalPage = getTotalPage(totalElements, size);
        commentDtoList = getCollectionsByOffsetLimit(page, size, commentDtoList, 1, postId);

        return PagePostResponseForComment.builder()
                .content(commentDtoList)
                .totalElements(totalElements)
                .empty(setEmpty(totalElements))
                .totalPages(1)
                .number(getPageNumber(page, postId))
                .numberOfElements(commentDtoList.size())
                .last(setLast(totalPage, page, postId))
                .first(setFirst(page, postId))
                .size(size)
                .sort(sortList)
                .pageableObject(pageableObject)
                .build();
    }

    public PagePostResponseForComment getSubcomment(Integer postid, Integer commentId, Integer page, Integer size) {
        Integer meId = personService.getIdPersonFromSecurityContext();
        Sort sorter = Sort.builder().ascending(false).descending(true).direction("DESC")
                .ignoreCase(false).nullHandling("NATIVE").property("time").build();
        List<Sort> sortList = new ArrayList<>();
        sortList.add(sorter);

        PageableObject pageableObject = PageableObject.builder()
                .unpaged(false)
                .paged(true)
                .sort(sortList)
                .pageNumber(getPageNumber(page, postid))
                .pageSize(size)
                .offset(getOffset(page, size, postid))
                .build();
        List<CommentDto> commentDtoList = feignInterface.getCommentDto(postid, meId);
        var comments = commentDtoList.stream()
                .filter(i -> Objects.equals(i.getParentId(), commentId))
                .toList();

        int totalElements = comments.size();
        int totalPage = getTotalPage(totalElements, size);
        comments = getCollectionsByOffsetLimit(page, size, comments, 1, postid);

        return PagePostResponseForComment.builder()
                .content(comments)
                .totalElements(totalElements)
                .empty(setEmpty(totalElements))
                .totalPages(1)
                .number(getPageNumber(page, postid))
                .numberOfElements(comments.size())
                .last(setLast(totalPage, page, postid))
                .first(setFirst(page, postid))
                .size(size)
                .sort(sortList)
                .pageableObject(pageableObject)
                .build();
    }

    private int getPageNumber(int page, Integer postId) {
        if (postId == 0 && page == -1) {
            return 1;
        }
        return page;
    }

    private int getOffset(int page, int size, Integer postId) {
        if (postId == 0 && page == -1) {
            return 0;
        }
        return page * size - size;
    }

    private int getTotalPage(int totalElement, int size) {
        if (totalElement % size != 0) {
            return (totalElement / size);
        }
        return totalElement / size;
    }

    private boolean setEmpty(int totalElement) {
        if (totalElement == 0) {
            return true;
        }
        return false;
    }

    private boolean setLast(int totalPage, int page, Integer postId) {
        if (postId == 0 && page == -1 && totalPage == 1) {
            return true;
        }
        if (totalPage == 0) {
            return true;
        }
        if (postId != 0 && page == 1 && page == totalPage) {
            return true;
        }
        if (page == totalPage) {
            return true;
        }
        return false;
    }

    private boolean setFirst(int page, Integer postId) {
        if (postId == 0 && page == -1) {
            return true;
        }
        if (postId != 0 && page == 1) {
            return true;
        }
        return false;
    }

    private List<CommentDto> getCollectionsByOffsetLimit(int oldPage, int size,
                                                         List<CommentDto> commentDtoList, int totalPage, Integer postId) {
        int offset = getOffset(oldPage, size, postId);
        int newComment = oldPage;
        if (postId == 0 && oldPage == -1) {
            newComment = 1;
        }
        if (commentDtoList.size() <= size) {
            commentDtoList = commentDtoList.subList(0, commentDtoList.size());
        } else {
            if (newComment == 1) {
                commentDtoList = commentDtoList.subList(0, size);
            } else if (newComment == totalPage) {
                commentDtoList = commentDtoList.subList(offset, commentDtoList.size());
            } else if (newComment > 1 && newComment != totalPage) {
                int rightBorder = offset + size;
                commentDtoList = commentDtoList.subList(offset, rightBorder);
            }
        }
        return commentDtoList;
    }
}
