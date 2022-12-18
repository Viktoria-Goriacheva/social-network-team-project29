package ru.socialnet.team29;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.utility.DockerImageName;
import ru.socialnet.team29.mappers.PostMapper;
import ru.socialnet.team29.testcase.Post;
import ru.socialnet.team29.testcase.PostDto;

import javax.sql.DataSource;

@SpringBootTest(classes = {SocialNetDatabaseApp.class})
class MapperTest {

    static DataSource datasource;

    @Autowired
    private PostMapper postMapperTest;

    @Container
    final static PostgreSQLContainer postgresContainer;

    static {
        postgresContainer = new PostgreSQLContainer(DockerImageName.parse("postgres:latest"));
        postgresContainer.start();

        System.setProperty("spring.datasource.url", postgresContainer.getJdbcUrl());
        System.setProperty("spring.datasource.username", postgresContainer.getUsername());
        System.setProperty("spring.datasource.password", postgresContainer.getPassword());
    }

    @Test
    void shouldProperlyMapModelToDto() {

        Post model = Post.builder()
                .id(11)
                .name("lecture name")
                .build();


        PostDto dto = postMapperTest.postToPostDto(model);

        Assertions.assertNotNull(dto);
        Assertions.assertEquals(model.getId(), dto.getId());
        Assertions.assertEquals(model.getName(), dto.getName());
    }

    @Test
    void shouldProperlyMapDtoToModel() {

        PostDto dto =  PostDto.builder()
                .id(11)
                .name("lecture name")
                .build();


        Post model = postMapperTest.postDtotoPost(dto);

        Assertions.assertNotNull(model);
        Assertions.assertEquals(dto.getId(), model.getId());
        Assertions.assertEquals(dto.getName(), model.getName());
    }
}