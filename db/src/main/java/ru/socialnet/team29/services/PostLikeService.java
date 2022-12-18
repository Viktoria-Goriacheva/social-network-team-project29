package ru.socialnet.team29.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.socialnet.team29.repository.PostLikeRepository;

@Service
@RequiredArgsConstructor
public class PostLikeService {
    private final PostLikeRepository postLikeRepository;
    public Integer getCountLikeByPostId(int id) {
        return postLikeRepository.getCountLikeByPostId(id);
    }

    /**
     * @param id идентификатор поста
     * @param authorId идентификатор автора поста
     * @return лайк автора своему посту
     */
    public boolean getMyLikeByPostId(int id, Integer authorId) {
        return postLikeRepository.getMyLikeByPostId(id, authorId);
    }
}
