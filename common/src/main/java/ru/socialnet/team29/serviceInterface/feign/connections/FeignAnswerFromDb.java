package ru.socialnet.team29.serviceInterface.feign.connections;

import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestParam;
import ru.socialnet.team29.answers.AnswerListFriendsForPerson;
import ru.socialnet.team29.dto.FriendSearchDto;
import ru.socialnet.team29.dto.PersonSearchDto;
import ru.socialnet.team29.dto.PostLikeDto;
import ru.socialnet.team29.dto.RecommendationFriendsDto;
import ru.socialnet.team29.model.FriendForFront;
import ru.socialnet.team29.model.PostDto;
import ru.socialnet.team29.serviceInterface.feign.DBConnectionFeignInterface;

import java.util.List;

@Component
public class FeignAnswerFromDb implements DBConnectionFeignInterface {
    @Override
    public List<PostDto> getPostDto(@RequestParam String email,@RequestParam Integer accountIds) {
        return null;
    }

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

    @Override
    public Boolean savePost(PostDto postDto) {
        return null;
    }

    @Override
    public PostDto getPostById(Integer id) {
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
}
