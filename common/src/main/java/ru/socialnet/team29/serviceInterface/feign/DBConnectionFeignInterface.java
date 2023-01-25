package ru.socialnet.team29.serviceInterface.feign;

import java.util.List;
import java.util.Map;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import ru.socialnet.team29.answers.AnswerListFriendsForPerson;
import ru.socialnet.team29.dto.CommentDto;
import ru.socialnet.team29.dto.FriendSearchDto;
import ru.socialnet.team29.dto.PersonSearchDto;
import ru.socialnet.team29.dto.PostLikeDto;
import ru.socialnet.team29.dto.RecommendationFriendsDto;
import ru.socialnet.team29.model.FriendForFront;
import ru.socialnet.team29.model.PostDto;
import ru.socialnet.team29.responses.dialog_response.MessageDatum;
import ru.socialnet.team29.responses.dialog_response.MessageDto;
import ru.socialnet.team29.responses.dialog_response.ShortDialogResponse;
import ru.socialnet.team29.responses.dialog_response.UnreadCount;

@FeignClient(name = "db", url = "${server.db.port}")
public interface DBConnectionFeignInterface {

    @GetMapping(value = "/posts")
    List<PostDto> getPostDto(
        @RequestParam String email,
        @RequestParam Integer accountIds,
        @RequestParam String tags,
        @RequestParam long dateTo,
        @RequestParam long dateFrom,
        @RequestParam String author,
        @RequestParam String text);
    /* MESSAGES */

    @PostMapping(value = "/message")
    MessageDto saveMessage(@RequestBody MessageDto messageDto);

    @GetMapping(value = "/message")
    List<MessageDto> getMessageByAuthor(@RequestParam Long authorId);

    @GetMapping(value = "/countUnread")
    Map<Long, UnreadCount> getUnreadMessages(@RequestParam Long authorId);

    @GetMapping(value = "/messages")
    List<MessageDto> getFullDialogData(@RequestParam Long id, @RequestParam Long companionId);

    @GetMapping(value = "/messages/set_read")
    ShortDialogResponse<MessageDatum> setReadAllStatus(@RequestParam Long id, @RequestParam Long companionId);

    /*  FRIENDS */

    @GetMapping("/friends/request")
    Boolean addFriendRequest(@RequestParam Integer id, @RequestParam Integer friendId);

    @GetMapping("/friends/approve")
    Boolean approveFriendship(@RequestParam Integer id, @RequestParam Integer friendId);

    @PostMapping("/friends")
    AnswerListFriendsForPerson<FriendForFront> getFriendsByIdPerson(
            @RequestBody AnswerListFriendsForPerson<PersonSearchDto> params);

    @DeleteMapping("/friends")
    Boolean deleteFriendship(@RequestParam Integer id, @RequestParam Integer friendId);

    @GetMapping("/friends/exists")
    Boolean friendsByIdExists(@RequestParam Integer id, @RequestParam Integer friendId);

    @GetMapping("/friends/count")
    Integer getCountOfFriends(@RequestParam Integer id);

    @GetMapping("/friends/friendId")
    FriendSearchDto getAllFriendIds(@RequestParam Integer id);

    @PostMapping("/friends/subscribe")
    Boolean toSubscribe(@RequestParam Integer id, @RequestParam Integer friendId);

    @GetMapping("/friends/recommendations")
    List<RecommendationFriendsDto> getRecommendations(@RequestParam Integer id);

    @GetMapping("/friends/block")
    Boolean blockFriend(@RequestParam Integer id, @RequestParam Integer friendId);

    @GetMapping("/friends/blockFriendId")
    FriendSearchDto getIdsBlockedFriends(@RequestParam Integer id);

    @PostMapping(value = "/post")
    Boolean savePost(@RequestBody PostDto postDto);

    @GetMapping(value = "/post")
    PostDto getPostById(@RequestParam Integer id, @RequestParam String email);

    @PutMapping(value = "/post")
    Boolean updatePost(@RequestBody PostDto postDto);

    @DeleteMapping(value = "/post")
    Boolean deletePost(@RequestParam Integer id);

    /* LIKES */

    @PostMapping(value = "/post/like")
    Boolean addLikeToPost(@RequestBody PostLikeDto postLikeDto);

    @DeleteMapping(value = "/post/like")
    Boolean deleteLikeFromPost(@RequestBody PostLikeDto postLikeDto);

    @PostMapping(value = "/post/comment/like")
    Boolean addLikeToPostComment(@RequestBody PostLikeDto postLikeDto);

    @DeleteMapping(value = "/post/comment/like")
    Boolean deleteLikeFromPostComment(@RequestBody PostLikeDto postLikeDto);

    @GetMapping(value = "/friends/integer/id")
    List<Integer> getListIdsAllFriendsCurrentUser(@RequestParam Integer idCurrentUser);

    /* COMMENTS */
    @PostMapping(value = "/saveComment")
    CommentDto saveComment(@RequestBody CommentDto commentDto);

    @PutMapping(value = "/update")
    CommentDto updateComments(@RequestBody CommentDto commentDto);
    @DeleteMapping(value = "/delete")
    Boolean deleteCommentById(@RequestBody Integer commentId);

    @GetMapping(value = "/getCommentIdByPostId")
    Integer getCommentIdByPostId(@RequestParam Integer postId);

    @GetMapping(value = "/oneComment")
    CommentDto getCommentById(@RequestParam Integer commentId);

    @GetMapping(value = "/comment")
    List<CommentDto> getCommentDto(@RequestParam Integer postId, @RequestParam Integer personId);
}