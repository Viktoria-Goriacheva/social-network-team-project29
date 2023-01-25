package ru.socialnet.team29.services;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import java.util.TimeZone;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.socialnet.team29.domain.tables.records.PostFileRecord;
import ru.socialnet.team29.domain.tables.records.PostTableRecord;
import ru.socialnet.team29.mappers.PostTableMapper;
import ru.socialnet.team29.model.PostDto;
import ru.socialnet.team29.model.enums.PostType;
import ru.socialnet.team29.repository.PersonRepository;
import ru.socialnet.team29.repository.PostRepository;

@Service
@RequiredArgsConstructor
@Slf4j
public class PostService {

  private final PostRepository postRepository;
  private final PostTableMapper postTableMapper;
  private final CommentsService commentService;
  private final PostLikeService postLikeService;
  private final PostFileService postFileService;
  private final TagService tagService;
  private final Post2TagService post2TagService;
  private final PersonRepository personRepository;
  private final long startDate = -2208988800L;

  public List<PostDto> getPosts(String email, Integer accountIds, String tags, long dateTo,
      long dateFrom, String author, String text) {
    List<PostDto> posts = new ArrayList<>();
    List<Integer> ids;
    if (dateTo != 0 || tags.length() > 0) {
      OffsetDateTime millisecondsFrom;
      OffsetDateTime millisecondsTo = OffsetDateTime.now();
      if (dateFrom != 0) {
        millisecondsFrom = getTime(dateFrom);
      } else {
        millisecondsFrom = getTime(startDate);
      }
      ids = postRepository.search(text, millisecondsFrom, millisecondsTo, author);

      if (tags != null
          && tags.length() > 0) {
        String[] tagArray = tags.split(",");
        List<Integer> idPostsByTags = new ArrayList<>();
        for (String tagName : tagArray) {
          List<Integer> tag = postRepository.findPostIdsByTag(tagName);
          if (!tag.isEmpty()) {
            idPostsByTags.addAll(tag);
          }
        }
        ids = ids.stream().filter(idPostsByTags::contains).collect(Collectors.toSet()).stream()
            .toList();
        ids.forEach(id -> posts.add(getPostById(id, email)));
        posts.stream().sorted((o1, o2) -> o2.getTime().compareTo(o1.getTime()))
            .collect(Collectors.toList());
        return posts;
      }
      ids.forEach(id -> posts.add(getPostById(id, email)));
      return posts;
    }
    if (accountIds != 0) {
      ids = postRepository.findPostIdsByAuthor(accountIds);
      ids.forEach(id -> posts.add(getPostById(id, email)));
    } else {
      ids = postRepository.findPostIdsByAuthorWithPersons();
      ids.forEach(id -> posts.add(getPostById(id, email)));
    }
    return posts;
  }

  private OffsetDateTime getTime(long time) {
    OffsetDateTime seconds;
    Instant instant = Instant.now();
    ZoneId systemZone = ZoneId.systemDefault();
    ZoneOffset currentOffsetForMyZone = systemZone.getRules().getOffset(instant);
    LocalDateTime triggerTime =
        LocalDateTime.ofInstant(Instant.ofEpochSecond(time), TimeZone
            .getDefault().toZoneId());
    seconds = OffsetDateTime.of(triggerTime, currentOffsetForMyZone);
    return seconds;
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

  public PostDto getPostById(int postId, String email) {
    int authorId = personRepository.findPersonByEmail(email).getId();
    checkPublishDate(postId);
    PostDto post = postTableMapper.PostTableRecordToPostDto(postRepository.findById(postId));
    if (post.getIsDelete()) {
      return null;
    }
    post.setCommentsCount(commentService.getCountCommentsByPostId(postId));
    post.setTags(tagService.findAllTagsByPostId(postId));
    post.setLikeAmount(postLikeService.getCountLikeByPostId(postId));
    post.setMyLike(postLikeService.getMyLikeByPostId(postId, authorId));
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
