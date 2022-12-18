package ru.socialnet.team29.serviceInterface.feign.connections;

import java.util.List;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import ru.socialnet.team29.answers.AnswerListFriendsForPerson;
import ru.socialnet.team29.model.Person;
import ru.socialnet.team29.model.PostDto;
import ru.socialnet.team29.serviceInterface.feign.DBConnectionFeignInterface;

@Component
public class FeignAnswerFromDb implements DBConnectionFeignInterface {

    @Override
    public Person savePerson(Person person) {
        return null;
    }

    @Override
    public Person getPersonByEmail(String email) {
        return null;
    }
    @Override
    public Person getPersonByToken(@RequestParam String token) {
        return null;
    };

    @Override
    public Person updatePerson(@RequestBody Person person) {
        return null;
    }

    @Override
    public List<PostDto> getPostDto(@RequestParam String email) {
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
    public AnswerListFriendsForPerson getFriendsByIdPerson(
            Integer id,
            String statusName,
            AnswerListFriendsForPerson.FriendPageable pageable) {
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
}
