package ru.socialnet.team29.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.socialnet.team29.mappers.PostFileMapper;
import ru.socialnet.team29.model.PostDto;
import ru.socialnet.team29.model.PostFile;
import ru.socialnet.team29.repository.PostFileRepository;

@Service
@RequiredArgsConstructor
public class PostFileService {

    private final PostFileMapper postFileMapper;
    private final PostFileRepository postFileRepository;

    public PostFile findByPostId(int postId) {
        return postFileMapper.RecordToPostFile(postFileRepository.findByPostId(postId));
    }

    /**
     * Если новый imagePath из postDto не пустой,<br/>
     * то собирает из параметра ДТО PostFile и добавляет в базу запись
     * @param postDto ДТО создаваемого поста
     */
    public void addImagePath(PostDto postDto) {
        if (!isEmpty(postDto.getImagePath())) {
            PostFile postFile = PostFile
                    .builder()
                    .postId(postDto.getId())
                    .imagePath(postDto.getImagePath())
                    .build();
            postFileRepository.insert(postFileMapper.postFileToRecord(postFile));
        }
    }

    public void updateImagePath(PostDto postDto) {
        int postId = postDto.getId();
        String newImagePath = postDto.getImagePath();
        String oldImagePath = findByPostId(postId).getImagePath();
        PostFile oldPostFile = findByPostId(postId);

        if (oldPostFile.getId() == 0) // если старой записи не найдено
                addImagePath(postDto);// добавляет запись с проверкой imagePath
        else
            if (isEmpty(newImagePath))  // если новый path пустой, а старая запись есть
            postFileRepository.delete(oldPostFile.getId()); // удаляет старую запись

            else                                                // если новый path не пустой, и старая запись есть
                if (!newImagePath.equals(oldImagePath)){        // если не равны старое и новое значение imagePath
                    oldPostFile = PostFile.builder()            // обновляем запись
                            .id(oldPostFile.getId())
                            .postId(postId)
                            .imagePath(postDto.getImagePath())
                            .name(oldPostFile.getName())
                            .build();
                    postFileRepository.update(postFileMapper.postFileToRecord(oldPostFile));
                }
    }

    private boolean isEmpty(String imagePath) {
        return (imagePath == null) || imagePath.isEmpty();
    }

}
