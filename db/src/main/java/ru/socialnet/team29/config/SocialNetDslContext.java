package ru.socialnet.team29.config;

import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.jooq.impl.DSL;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class SocialNetDslContext {
    private final JdbcConnections connections;

    @Bean
    public DSLContext dsl() {
        return DSL.using(connections.getUrl(), connections.getUsername(), connections.getPassword());
    }
}
