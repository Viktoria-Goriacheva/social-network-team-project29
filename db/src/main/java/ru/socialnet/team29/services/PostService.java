package ru.socialnet.team29.services;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.socialnet.team29.domain.tables.records.PostFileRecord;
import ru.socialnet.team29.domain.tables.records.PostTableRecord;
import ru.socialnet.team29.mappers.PostTableMapper;
import ru.socialnet.team29.model.PostDto;
import ru.socialnet.team29.repository.CommentRepository;
import ru.socialnet.team29.repository.PersonRepository;
import ru.socialnet.team29.repository.PostFileRepository;
import ru.socialnet.team29.repository.PostLikeRepository;
import ru.socialnet.team29.repository.PostRepository;
import ru.socialnet.team29.repository.TagRepository;

@Service
@RequiredArgsConstructor
public class PostService {

  private final PostRepository postRepository;
  private final PostTableMapper postTableMapper;
  private final CommentRepository commentRepository;
  private final PostLikeRepository postLikeRepository;
  private final PostFileRepository postFileRepository;
  private final TagRepository tagRepository;
  private final PersonRepository personRepository;

  public List<PostDto> getPosts(String email) {
    Integer personId = personRepository.findPersonIdByEmail(email);
    List<PostTableRecord> posts = postRepository.findAllById(personId);
    List<PostDto> postDtos = postTableMapper.PostTableRecordToPostDto(posts);

    for (PostDto postDto : postDtos) {
      PostFileRecord postFileRecord = postFileRepository.findPathByIdPost(postDto.getId());
      postDto.setCommentsCount(commentRepository.getCountCommentsByPostId(postDto.getId()));
      postDto.setLikeAmount(postLikeRepository.getCountLikeByPostId(postDto.getId()));
      postDto.setMyLike(postLikeRepository.getMyLikeByPostId(postDto.getId(), personId));
      postDto.setImagePath(getPath(postFileRecord));
      postDto.setTags(tagRepository.findAllTagsByPostId(postDto.getId()));
    }
    return postDtos;
  }

  public String getPath(PostFileRecord postFileRecord) {
    if (postFileRecord != null) {
      return postFileRecord.getPath();
    }
    return null;
  }
}
