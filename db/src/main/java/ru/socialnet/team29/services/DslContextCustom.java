package ru.socialnet.team29.services;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.jooq.impl.DSL;
import org.springframework.stereotype.Service;
import ru.socialnet.team29.config.JdbcConnections;

@Service
@RequiredArgsConstructor
@Data
public class DslContextCustom
{

    private final JdbcConnections connections;


    public DSLContext initDslContext(){
        DSLContext dsl = DSL.using(connections.getUrl(), connections.getUsername(), connections.getPassword());
        return dsl;
    }

}
