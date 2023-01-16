package ru.socialnet.team29.services;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.socialnet.team29.domain.tables.records.PostFileRecord;
import ru.socialnet.team29.domain.tables.records.PostTableRecord;
import ru.socialnet.team29.mappers.PostTableMapper;
import ru.socialnet.team29.model.PostDto;
import ru.socialnet.team29.model.enums.PostType;
import ru.socialnet.team29.repository.PostRepository;

@Service
@RequiredArgsConstructor
@Slf4j
public class PostService {

  private final PostRepository postRepository;
  private final PostTableMapper postTableMapper;
  private final CommentService commentService;
  private final PostLikeService postLikeService;
  private final PostFileService postFileService;
  private final TagService tagService;
  private final Post2TagService post2TagService;

  public List<PostDto> getPostsByAuthorEmail(Integer accountIds) {
    List<PostDto> posts = new ArrayList<>();
    List<Integer> ids;
    if (accountIds != 0) {
      ids = postRepository.findPostIdsByAuthor(accountIds);
      ids.forEach(id -> posts.add(getPostById(id)));
    } else {
      ids = postRepository.findPostIdsByAuthorWithPersons();
      ids.forEach(id -> posts.add(getPostById(id)));
    }
    return posts;
  }

  public Boolean addNewPost(PostDto postDto) {
    postDto.setId(postRepository.insert(postTableMapper.PostDtoToPostTableRecord(postDto)));
    log.info("Добавлен новый пост id = {}", postDto.getId());
    if (postDto.getTags() != null) {
      post2TagService.addPost2tags(postDto);
    }
    postFileService.addImagePath(postDto);
    return postDto.getId() > 0;
  }

  public PostDto getPostById(int postId) {
    checkPublishDate(postId);
    PostDto post = postTableMapper.PostTableRecordToPostDto(postRepository.findById(postId));
    if (post.getIsDelete()) {
      return null;
    }
    post.setCommentsCount(commentService.getCountCommentsByPostId(postId));
    post.setTags(tagService.findAllTagsByPostId(postId));
    post.setLikeAmount(postLikeService.getCountLikeByPostId(postId));
    post.setMyLike(postLikeService.getMyLikeByPostId(postId, post.getAuthorId()));
    post.setImagePath(postFileService.findByPostId(postId).getImagePath());
    return post;
  }

  public boolean updatePost(PostDto postDto) {
    post2TagService.updateTags(postDto);
    postFileService.updateImagePath(postDto);
    return postRepository.update(postTableMapper.PostDtoToPostTableRecord(postDto)) != null;
  }

  public boolean deletePost(Integer id) {
    return postRepository.delete(id);
  }

  /**
   * Проверка даты публикации поста, если публикация отложена. Если дата отложенной публикации в
   * прошлом, то пост помечается как опубликованный
   *
   * @param id идентификатор поста
   */
  public void checkPublishDate(Integer id) {
    PostTableRecord post = postRepository.findById(id);
    if (post.getType().equals(PostType.QUEUED.name())
        && post.getPublishdate().isBefore(OffsetDateTime.now())) {
      post.setType(PostType.POSTED.name());
      post.setPublishdate(null);
      postRepository.update(post);
    }
  }

  @Deprecated
  public String getPath(PostFileRecord postFileRecord) {
    if (postFileRecord != null) {
      return postFileRecord.getPath();
    }
    return null;
  }
}
