package ru.socialnet.team29.serviceInterface.feign.connections;

import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestParam;
import ru.socialnet.team29.answers.AnswerListFriendsForPerson;
import ru.socialnet.team29.dto.CommentDto;
import ru.socialnet.team29.dto.FriendSearchDto;
import ru.socialnet.team29.dto.PersonSearchDto;
import ru.socialnet.team29.dto.PostLikeDto;
import ru.socialnet.team29.dto.RecommendationFriendsDto;
import ru.socialnet.team29.model.FriendForFront;
import ru.socialnet.team29.model.PostDto;
import ru.socialnet.team29.responses.dialog_response.*;
import ru.socialnet.team29.serviceInterface.feign.DBConnectionFeignInterface;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
public class FeignAnswerFromDb implements DBConnectionFeignInterface {

    /* MESSAGE */

    @Override
    public MessageDto saveMessage(MessageDto messageDto) {
        return null;
    }

    @Override
    public List<PostDto> getPostDto(@RequestParam String email, Integer accountIds, String tags,
                                    long dateTo, long dateFrom, String author, String text) {
        return null;
    }

    public List<MessageDto> getMessageByAuthor(Long authorId) {
        return null;
    }

    @Override
    public Map<Long, UnreadCount> getUnreadMessages(Long authorId) {
        return null;
    }

    @Override
    public ArrayList<MessageDto> getFullDialogData(Long id, Long companionId) {
        return null;
    }

    @Override
    public ShortDialogResponse<MessageDatum> setReadAllStatus(Long id, Long companionId) {
        return null;
    }

    /* FRIENDS */

    @Override
    public Boolean addFriendRequest(Integer id, Integer friendId) {
        return null;
    }

    @Override
    public Boolean approveFriendship(Integer id, Integer friendId) {
        return null;
    }

    @Override
    public AnswerListFriendsForPerson<FriendForFront> getFriendsByIdPerson(
            AnswerListFriendsForPerson<PersonSearchDto> params) {
        return null;
    }

    @Override
    public Boolean deleteFriendship(Integer id, Integer friendId) {
        return null;
    }

    @Override
    public Boolean friendsByIdExists(Integer id, Integer friendId) {
        return null;
    }

    @Override
    public Integer getCountOfFriends(Integer id) {
        return null;
    }

    @Override
    public FriendSearchDto getAllFriendIds(Integer id) {
        return null;
    }

    @Override
    public Boolean toSubscribe(Integer id, Integer friendId) {
        return null;
    }

    @Override
    public List<RecommendationFriendsDto> getRecommendations(Integer id) {
        return null;
    }

    @Override
    public Boolean blockFriend(Integer id, Integer friendId) {
        return null;
    }

    @Override
    public FriendSearchDto getIdsBlockedFriends(Integer id) {
        return null;
    }

    /* POSTS */

    @Override
    public Boolean savePost(PostDto postDto) {
        return null;
    }

    @Override
    public PostDto getPostById(Integer id, String email) {
        return null;
    }

    @Override
    public Boolean updatePost(PostDto postDto) {
        return null;
    }

    @Override
    public Boolean deletePost(Integer id) {
        return null;
    }

    /* LIKES */

    @Override
    public Boolean addLikeToPost(PostLikeDto postLikeDto) {
        return null;
    }

    @Override
    public Boolean deleteLikeFromPost(PostLikeDto postLikeDto) {
        return null;
    }

    @Override
    public Boolean addLikeToPostComment(PostLikeDto postLikeDto) {
        return null;
    }

    @Override
    public Boolean deleteLikeFromPostComment(PostLikeDto postLikeDto) {
        return null;
    }

    @Override
    public List<Integer> getListIdsAllFriendsCurrentUser(Integer idCurrentUser) {
        return null;
    }

    /* COMMENTS */
    @Override
    public CommentDto saveComment(CommentDto commentDto) {
        return null;
    }

    @Override
    public CommentDto updateComments(CommentDto commentDto) {
        return null;
    }

    @Override
    public Boolean deleteCommentById(Integer commentDto) {
        return null;
    }

    @Override
    public Integer getCommentIdByPostId(Integer id) {
        return null;
    }

    @Override
    public CommentDto getCommentById(Integer id) {
        return null;
    }

    @Override
    public List<CommentDto> getCommentDto(Integer commentIds, Integer personId) {
        return null;
    }

}
