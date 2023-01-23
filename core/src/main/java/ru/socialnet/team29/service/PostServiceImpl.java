package ru.socialnet.team29.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import ru.socialnet.team29.answers.PagePostResponse;
import ru.socialnet.team29.dto.PostLikeDto;
import ru.socialnet.team29.model.PageableObject;
import ru.socialnet.team29.model.PostDto;
import ru.socialnet.team29.model.Sort;
import ru.socialnet.team29.model.enums.NotificationType;
import ru.socialnet.team29.model.enums.PostType;
import ru.socialnet.team29.payloads.AddNotificationPayload;
import ru.socialnet.team29.payloads.PostPayload;
import ru.socialnet.team29.serviceInterface.NotificationService;
import ru.socialnet.team29.serviceInterface.PersonService;
import ru.socialnet.team29.serviceInterface.PostService;
import ru.socialnet.team29.serviceInterface.feign.DBConnectionFeignInterface;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {

    private final DBConnectionFeignInterface feignInterface;
    private final PersonService personService;
    private final NotificationService notificationService;
    private final FriendServiceImpl friendService;
    private static int newPage;

    @Override
    public Boolean addPost(PostPayload postPayload) {
        log.info("Запрос от фронта - добавить пост title={}", postPayload.getTitle());
        int authorId = personService.getMyId();
        PostDto postDto = PostDto.builder()
                .authorId(authorId)
                .title(postPayload.getTitle())
                .postText(postPayload.getPostText())
                .tags(postPayload.getTags())
                .imagePath(postPayload.getImagePath())
                .time(OffsetDateTime.now())
                .timeChanged(OffsetDateTime.now())
                .publishDate(postPayload.getPublishDate())
                .type(postPayload.getPublishDate() == null ? PostType.POSTED : PostType.QUEUED)
                .isBlocked(false)
                .isDelete(false)
                .commentsCount(0)
                .likeAmount(0)
                .myLike(false)
                .build();
        addNewNotificationToAllFriends();
        return feignInterface.savePost(postDto);
    }

    private void addNewNotificationToAllFriends() {
        Integer idCurrentUser = personService.getIdPersonFromSecurityContext();
        List<Integer> listFriends = friendService.getAllFriendsId(idCurrentUser);
        for (Integer element : listFriends) {
            AddNotificationPayload payload = AddNotificationPayload.builder()
                    .authorId(String.valueOf(idCurrentUser))
                    .userId(String.valueOf(element))
                    .notificationType(NotificationType.POST.getValue())
                    .content("Я создал новый пост.")
                    .NotificationTypeId(NotificationType.POST.getNumber())
                    .build();
            notificationService.addNewNotification(payload);
        }
    }

    @Override
    public PostDto findPostById(Integer id, String email) {
        log.info("Запрос от фронта - найти пост id={}", id);
        return feignInterface.getPostById(id, email);
    }

    @Override
    public Boolean updatePost(Integer postId, PostPayload postPayload) {
        log.info("Запрос от фронта - обновить пост postId={}", postId);
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        PostDto oldPost = findPostById(postId, email);
        PostDto newPost = PostDto.builder()
                .id(postId)
                .authorId(oldPost.getAuthorId())
                .title(postPayload.getTitle())
                .postText(postPayload.getPostText())
                .tags(postPayload.getTags())
                .imagePath(postPayload.getImagePath())
                .time(oldPost.getTime())
                .timeChanged(OffsetDateTime.now())
                .publishDate(oldPost.getPublishDate())
                .type(oldPost.getType())
                .isBlocked(oldPost.getIsBlocked())
                .isDelete(oldPost.getIsDelete())
                .commentsCount(oldPost.getCommentsCount())
                .likeAmount(oldPost.getLikeAmount())
                .myLike(oldPost.isMyLike())
                .build();

        return feignInterface.updatePost(newPost);

    }

    @Override
    public Boolean deletePost(Integer id) {
        log.info("Запрос от фронта - удалить пост id={}", id);
        return feignInterface.deletePost(id);
    }

    @Override
    public PagePostResponse getPosts(String tags, long dateTo, long dateFrom, String author, String text, boolean withFriends, String sort, boolean isDelete,
                                     int size, Integer accountIds, int page) {
        Sort sorter = Sort.builder().ascending(false).descending(true).direction("DESC")
                .ignoreCase(false).nullHandling("NATIVE").property("time").build();
        List<Sort> sortList = new ArrayList<>();
        sortList.add(sorter);
        PageableObject pageableObject = PageableObject.builder()
                .unpaged(false)
                .paged(true)
                .sort(sortList)
                .pageNumber(getPageNumber(page, accountIds))
                .pageSize(size)
                .offset(getOffset(page, size, accountIds))
                .build();
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        List<PostDto> postsDto = feignInterface.getPostDto(email, accountIds, tags, dateTo, dateFrom, author, text);
        Integer totalElements = postsDto.size();
        Integer totalPage = getTotalPage(totalElements, size);
        postsDto = getCollectionsByOffsetLimit(page, size, postsDto, totalPage, accountIds);

        PagePostResponse pagePostResponse = PagePostResponse.builder()
                .content(postsDto)
                .totalElements(totalElements)
                .empty(setEmpty(totalElements))
                .totalPages(getResultTotalPage(totalPage, accountIds, dateTo))
                .number(getPageNumber(page, accountIds))
                .numberOfElements(postsDto.size())
                .last(setLast(totalPage, page, accountIds))
                .first(setFirst(page, accountIds))
                .size(size)
                .sort(sortList)
                .pageableObject(pageableObject)
                .build();

        return pagePostResponse;
    }

    @Override
    public HttpStatus addLikeToPost(Integer id) {
        if (id != null) {
            log.info("Like to post id=" + id.toString());
            Integer personId = personService.getIdPersonFromSecurityContext();
            PostLikeDto postLikeDto = PostLikeDto.builder()
                    .postId(id)
                    .personId(personId)
                    .build();
            boolean isAppendLike = feignInterface.addLikeToPost(postLikeDto);
            if (!isAppendLike) {
                log.info("The post is not liked");
                return HttpStatus.BAD_REQUEST;
            }
        } else {
            log.info("The post id=null");
            return HttpStatus.BAD_REQUEST;
        }
        return HttpStatus.CREATED;
    }

    @Override
    public HttpStatus deleteLikeFromPost(Integer id) {
        if (id != null) {
            log.info("Delete like from post id=" + id.toString());
            int personId = personService.getIdPersonFromSecurityContext();
            PostLikeDto postLikeDto = PostLikeDto.builder()
                    .postId(id)
                    .personId(personId)
                    .build();
            boolean isRemovedLike = feignInterface.deleteLikeFromPost(postLikeDto);
            if (!isRemovedLike) {
                log.info("Like is not deleted from the post");
                return HttpStatus.BAD_REQUEST;
            }
        } else {
            log.info("The post id=null");
            return HttpStatus.BAD_REQUEST;
        }
        return HttpStatus.CREATED;
    }


    @Override
    public HttpStatus addLikeToPostComment(Integer id, Integer commentId) {
        if (id != null && commentId != null) {
            log.info("Like to post id=" + id.toString() + ", comment id=" + commentId.toString());
            int personId = personService.getIdPersonFromSecurityContext();
            PostLikeDto postLikeDto = PostLikeDto.builder()
                    .postId(id)
                    .personId(personId)
                    .commentId(commentId)
                    .build();
            boolean isAppendLike = feignInterface.addLikeToPostComment(postLikeDto);
            if (!isAppendLike) {
                log.info("The comment is not liked");
                return HttpStatus.BAD_REQUEST;
            }
        } else {
            log.info("The post id=null or comment id=null");
            return HttpStatus.BAD_REQUEST;
        }
        return HttpStatus.CREATED;
    }

    @Override
    public HttpStatus deleteLikeFromPostComment(Integer id, Integer commentId) {
        if (id != null) {
            log.info("Delete like from post id=" + id.toString() + ", comment id=" + commentId.toString());
            int personId = personService.getIdPersonFromSecurityContext();
            PostLikeDto postLikeDto = PostLikeDto.builder()
                    .postId(id)
                    .personId(personId)
                    .commentId(commentId)
                    .build();
            boolean isRemovedLike = feignInterface.deleteLikeFromPostComment(postLikeDto);
            if (!isRemovedLike) {
                log.info("Like is not deleted from the comment");
                return HttpStatus.BAD_REQUEST;
            }
        } else {
            log.info("The post id=null or comment id=null");
            return HttpStatus.BAD_REQUEST;
        }
        return HttpStatus.CREATED;
    }

    private int getPageNumber(int page, Integer accountIds) {
        if (accountIds == 0 && page == -1) {
            return 1;
        }
        return page;
    }

    private int getOffset(int page, int size, Integer accountIds) {
        if (accountIds == 0 && page == -1) {
            return 0;
        }
        return page * size - size;
    }

    private boolean setLast(int totalPage, int page, Integer accountIds) {
        if (accountIds == 0 && page == -1 && totalPage == 1) {
            return true;
        }
        if (totalPage == 0) {
            return true;
        }
        if (accountIds != 0 && page == 1 && page == totalPage) {
            return true;
        }
        if (page == totalPage) {
            return true;
        }
        return false;
    }

    private boolean setFirst(int page, Integer accountIds) {
        if (accountIds == 0 && page == -1) {
            return true;
        }
        if (accountIds != 0 && page == 1) {
            return true;
        }
        return false;
    }

    private boolean setEmpty(int totalElement) {
        if (totalElement == 0) {
            return true;
        }
        return false;
    }

    private int getTotalPage(int totalElement, int size) {
        if (totalElement % size != 0) {
            return (totalElement / size) + 1;
        }
        return totalElement / size;
    }

    private int getResultTotalPage(int totalPage, Integer accountIds, long dateTo) {
        if (accountIds != 0 && totalPage == 0) {
            return 2;
        }
        if (dateTo != 0) {
            return totalPage;
        }
        return totalPage + 1;
    }

    private List<PostDto> getCollectionsByOffsetLimit(int oldPage, int size,
                                                      List<PostDto> postDtoList, int totalPage, Integer accountIds) {
        int offset = getOffset(oldPage, size, accountIds);
        newPage = oldPage;
        if (accountIds == 0 && oldPage == -1) {
            newPage = 1;
        }
        if (postDtoList.size() <= size) {
            postDtoList = postDtoList.subList(0, postDtoList.size());
        } else {
            if (newPage == 1) {
                postDtoList = postDtoList.subList(0, size);
            } else if (newPage == totalPage) {
                postDtoList = postDtoList.subList(offset, postDtoList.size());
            } else if (newPage > 1 && newPage != totalPage) {
                int rightBorder = offset + size;
                postDtoList = postDtoList.subList(offset, rightBorder);
            }
        }
        return postDtoList;
    }
}