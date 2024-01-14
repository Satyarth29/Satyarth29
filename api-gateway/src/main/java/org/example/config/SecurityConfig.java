package org.example.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.stereotype.Component;


@EnableWebFluxSecurity//the api gateway starter dependency is based on springweb flux thats why this
@Component
public class SecurityConfig {

    @Bean
    public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity serverHttpSecurity) {
        System.out.println("this is security!!!!!!!!!🙌🙌🙌🙌🙌🙌🎂🎂🎂🎂🎂🎂🎂🎂🐱‍🚀🐱‍🚀🐱‍👓");
        serverHttpSecurity
                .csrf().disable()
                .authorizeExchange(exchange ->
                        exchange.pathMatchers("/eureka/**")
                                .permitAll()
                                .anyExchange()
                                .authenticated())
                .oauth2ResourceServer(ServerHttpSecurity.OAuth2ResourceServerSpec::jwt);
        //In the context of OAuth 2.0, a resource server is an application that protects resources via OAuth tokens.
        // The job of the resource server is to validate the token before serving a resource to the client.
        return serverHttpSecurity.build();

    }

}
