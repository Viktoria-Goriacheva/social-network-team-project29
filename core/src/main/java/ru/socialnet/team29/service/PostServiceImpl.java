package ru.socialnet.team29.service;

import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import ru.socialnet.team29.answers.PagePostResponse;
import ru.socialnet.team29.model.PageableObject;
import ru.socialnet.team29.model.PostDto;
import ru.socialnet.team29.model.Sort;
import ru.socialnet.team29.serviceInterface.PostService;
import ru.socialnet.team29.serviceInterface.feign.DBConnectionFeignInterface;

@Slf4j
@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {

  private final DBConnectionFeignInterface feignInterface;

  @Override
  public PagePostResponse getPosts(boolean withFriends, String sort, boolean isDelete,
      int size, int page) {
    Sort sorter = Sort.builder().ascending(false).descending(true).direction("DESC")
        .ignoreCase(false).nullHandling("NATIVE").property("time").build();
    List<Sort> sortList = new ArrayList<>();
    sortList.add(sorter);
    PageableObject pageableObject = PageableObject.builder()
        .unpaged(false)
        .paged(true)
        .sort(sortList)
        .pageNumber(page)
        .pageSize(size)
        .offset(page * size - 3)
        .build();
    String emailAuth = SecurityContextHolder.getContext().getAuthentication().getName();
    List<PostDto> postsDto = feignInterface.getPostDto(emailAuth);
    Integer totalElements = postsDto.size();
    Integer totalPage = getTotalPage(totalElements, size);
    postsDto = getCollectionsByOffsetLimit(page, size, postsDto, totalPage);

    PagePostResponse pagePostResponse = PagePostResponse.builder()
        .content(postsDto)
        .totalElements(totalElements)
        .empty(setEmpty(totalElements))
        .totalPages(totalPage + 1)
        .number(page)
        .numberOfElements(postsDto.size())
        .last(setLast(totalPage, page))
        .first(setFirst(page))
        .size(size)
        .sort(sortList)
        .pageableObject(pageableObject)
        .build();

    return pagePostResponse;
  }

  private boolean setLast(int totalPage, int page) {
    if (page <= 1 && page == totalPage) {
      return true;
    }
    if ((page + 1) > totalPage) {
      return true;
    }
    return false;
  }

  private boolean setFirst(int page) {
    if (page == 1) {
      return true;
    }
    return false;
  }

  private boolean setEmpty(int totalElement) {
    if (totalElement == 0) {
      return true;
    }
    return false;
  }

  private int getTotalPage(int totalElement, int size) {
    if (totalElement % size != 0) {
      return (totalElement / size) + 1;
    }
    return totalElement / size;
  }


  private List<PostDto> getCollectionsByOffsetLimit(int page, int size,
      List<PostDto> postDtoList, int totalPage) {
    int offset = page * size - 3;
    if (postDtoList.size() <= size) {
      postDtoList = postDtoList.subList(0, postDtoList.size());
    } else {
      if (page <= 1) {
        postDtoList = postDtoList.subList(0, size);
      } else if (page == totalPage) {
        postDtoList = postDtoList.subList(offset, postDtoList.size());
      } else if (page > 1 && page != totalPage) {
        int rightBorder = offset + size;
        postDtoList = postDtoList.subList(offset, rightBorder);
      }
    }
    return postDtoList;
  }
}