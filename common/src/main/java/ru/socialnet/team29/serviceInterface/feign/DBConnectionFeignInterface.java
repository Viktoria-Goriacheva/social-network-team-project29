package ru.socialnet.team29.serviceInterface.feign;

import java.util.List;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import ru.socialnet.team29.answers.AnswerListFriendsForPerson;
import ru.socialnet.team29.dto.FriendSearchDto;
import ru.socialnet.team29.dto.PersonSearchDto;
import ru.socialnet.team29.dto.PostLikeDto;
import ru.socialnet.team29.dto.RecommendationFriendsDto;
import ru.socialnet.team29.model.FriendForFront;
import ru.socialnet.team29.model.PostDto;

@FeignClient(name = "db", url = "${server.db.port}")
public interface DBConnectionFeignInterface {

    @GetMapping(value = "/posts")
    List<PostDto> getPostDto(
        @RequestParam Integer accountIds,
        @RequestParam String tags,
        @RequestParam long dateTo,
        @RequestParam long dateFrom,
        @RequestParam String author);

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
    Boolean savePost (@RequestBody PostDto postDto);

    @GetMapping(value = "/post")
    PostDto getPostById(@RequestParam Integer id);

    @PutMapping(value = "/post")
    Boolean updatePost (@RequestBody PostDto postDto);

    @DeleteMapping(value = "/post")
    Boolean deletePost (@RequestParam Integer id);

    @PostMapping(value = "/post/like")
    Boolean addLikeToPost(@RequestBody PostLikeDto postLikeDto);

    @DeleteMapping(value = "/post/like")
    Boolean deleteLikeFromPost(@RequestBody PostLikeDto postLikeDto);

    @PostMapping(value = "/post/comment/like")
    Boolean addLikeToPostComment(@RequestBody PostLikeDto postLikeDto);

    @DeleteMapping(value = "/post/comment/like")
    Boolean deleteLikeFromPostComment(@RequestBody PostLikeDto postLikeDto);
}