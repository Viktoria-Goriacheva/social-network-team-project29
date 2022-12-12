package ru.socialnet.team29.repository;

import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;
import ru.socialnet.team29.domain.tables.PostFile;
import ru.socialnet.team29.domain.tables.records.PostFileRecord;

@Repository
@RequiredArgsConstructor
public class PostFileRepository {

  private final DSLContext dsl;

  public PostFileRecord findPathByIdPost(Integer id) {

    return dsl.selectFrom(PostFile.POST_FILE)
        .where(PostFile.POST_FILE.POST_ID.eq(id))
        .fetchAny();
  }

}
