package ru.socialnet.team29.services;

import lombok.RequiredArgsConstructor;
import org.hibernate.type.OffsetDateTimeType;
import org.springframework.stereotype.Service;
import ru.socialnet.team29.dto.PostLikeDto;
import ru.socialnet.team29.mappers.PostLikeMapper;
import ru.socialnet.team29.repository.PostLikeRepository;

import java.time.OffsetDateTime;

@Service
@RequiredArgsConstructor
public class PostLikeService {
    private final PostLikeRepository postLikeRepository;
    private final PostLikeMapper postLikeMapper;

    public Integer getCountLikeByPostId(int id) {
        return postLikeRepository.getCountLikeByPostId(id);
    }

    public Integer getCountLikeByCommentId(int id) {
        return postLikeRepository.getCountLikeByCommentId(id);
    }

    /**
     * @param id идентификатор поста
     * @param authorId идентификатор автора поста
     * @return лайк автора своему посту
     */
    public boolean getMyLikeByPostId(Integer id, Integer authorId) {
        return postLikeRepository.getMyLikeByPostId(id, authorId);
    }

    public boolean getMyLikeByCommentId(Integer id, Integer personId) {
        return postLikeRepository.getMyLikeByCommentId(id, personId);
    }

    public Boolean addLikeToPost(PostLikeDto postLikeDto) {
        if (!postLikeRepository.getMyLikeByPostId(postLikeDto.getPostId(), postLikeDto.getPersonId())) {
            return postLikeRepository.insert(postLikeMapper.PostLikeDtoToPostLikeRecord(postLikeDto)) > 0;
        }
        return false;
    }

    public Boolean deleteLikeFromPost(PostLikeDto postLikeDto) {
        if (postLikeRepository.getMyLikeByPostId(postLikeDto.getPostId(), postLikeDto.getPersonId())) {
            return postLikeRepository.deleteLikeFromPost(postLikeMapper.PostLikeDtoToPostLikeRecord(postLikeDto)) > 0;
        }
        return false;
    }

    public Boolean addLikeToPostComment(PostLikeDto postLikeDto) {
        if (!postLikeRepository.getMyLikeByPostId(postLikeDto.getPostId(), postLikeDto.getPersonId(), postLikeDto.getCommentId())) {
            return postLikeRepository.insert(postLikeMapper.PostLikeDtoToPostLikeRecord(postLikeDto)) > 0;
        }
        return false;
    }

    public Boolean deleteLikeFromPostComment(PostLikeDto postLikeDto) {
        if (postLikeRepository.getMyLikeByPostId(postLikeDto.getPostId(), postLikeDto.getPersonId(), postLikeDto.getCommentId())) {
            return postLikeRepository.deleteLikeFromPost(postLikeMapper.PostLikeDtoToPostLikeRecord(postLikeDto)) > 0;
        }
        return false;
    }
}
