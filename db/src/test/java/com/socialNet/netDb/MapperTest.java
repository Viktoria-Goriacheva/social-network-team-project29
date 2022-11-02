package com.socialNet.netDb;

import com.socialNet.netDb.mappers.PostMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.socialnet.team29.testcase.Post;
import ru.socialnet.team29.testcase.PostDto;

@SpringBootTest
class MapperTest {

  @Autowired
  private PostMapper postMapperTest;

  @Test
  void shouldProperlyMapModelToDto() {

    Post model = new Post();
    model.setId(11);
    model.setName("lecture name");

    PostDto dto = postMapperTest.postToPostDto(model);

    Assertions.assertNotNull(dto);
    Assertions.assertEquals(model.getId(), dto.getId());
    Assertions.assertEquals(model.getName(), dto.getName());
  }

  @Test
  void shouldProperlyMapDtoToModel() {

    PostDto dto = new PostDto();
    dto.setId(11);
    dto.setName("lecture name");

    Post model = postMapperTest.postDtotoPost(dto);

    Assertions.assertNotNull(model);
    Assertions.assertEquals(dto.getId(), model.getId());
    Assertions.assertEquals(dto.getName(), model.getName());
  }
}