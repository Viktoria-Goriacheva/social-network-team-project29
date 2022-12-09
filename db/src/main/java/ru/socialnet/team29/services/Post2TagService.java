package ru.socialnet.team29.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.socialnet.team29.mappers.Post2tagMapper;
import ru.socialnet.team29.model.Post2tag;
import ru.socialnet.team29.model.PostDto;
import ru.socialnet.team29.repository.Post2TagRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class Post2TagService {

    private final TagService tagService;

    private final Post2tagMapper post2tagMapper;
    private final Post2TagRepository post2TagRepository;

    /**
     * Создает запись Post2tag<br/>
     * Если тег новый, то он добавляется в базу данных
     * @param post2tag DTO Post2tag.
     */
    private void addPost2tag(Post2tag post2tag) {
        post2TagRepository.insert(post2tagMapper.Post2tagToRecord(post2tag));
    }

    public void addPost2tags(PostDto postDto) {

        postDto.getTags().forEach(tag -> addPost2tag(buildPost2tag(tag, postDto.getId())));
    }

    public void updateTags(PostDto postDto) {
        int postId = postDto.getId();
        List<String> updatedTags = postDto.getTags();
        List<String> oldTags = tagService.findAllTagsByPostId(postId);
        updatedTags.forEach(tag -> {     //перебираем новые теги
            if (oldTags.contains(tag))  // если такой тег уже был
                oldTags.remove(tag);    // убираем его из списка старых тегов
            else                        // если тег новый
                addPost2tag(buildPost2tag(tag, postId));         // добавляем тег и запись post2tag
        });
        // перебираем старые удаленные теги
        oldTags.forEach(tag -> deletePost2tag(tag, postId));        // удаляем записи post2tag, а тег оставляем
    }

    /**
     * Удаляет запись Post2tag<br/>
     * Тег остаётся в базе! без всяких проверок, возможно следует проверить остался ли он в каком либо посте.
     * Но это вероятно создаст нагрузку на бд.
     * @param tag имя тега
     * @param postId идентификатор поста
     */
    private void deletePost2tag(String tag, int postId) {
        int tagId = tagService.getTagId(tag);
        int post2tagId = post2TagRepository.findByRelation(postId, tagId);
        post2TagRepository.delete(post2tagId);
    }

    private Post2tag buildPost2tag(String tag, int postId) {
        int tagId = tagService.getTagId(tag); // если нет такого тега, то создается новый, смотри JavaDoc
        return Post2tag
                .builder()
                .postId(postId)
                .tagId(tagId)
                .build();
    }

}
