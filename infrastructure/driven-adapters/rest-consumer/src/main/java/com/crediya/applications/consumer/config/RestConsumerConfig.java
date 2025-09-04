package com.crediya.applications.consumer.config;

import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.client.reactive.ClientHttpConnector;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;

import static io.netty.channel.ChannelOption.CONNECT_TIMEOUT_MILLIS;
import static java.util.concurrent.TimeUnit.MILLISECONDS;

@Configuration
@RequiredArgsConstructor
public class RestConsumerConfig {

    private final RestConsumerProperties properties;

    @Bean("crediya-auth")
    public WebClient authWebClient(WebClient.Builder builder) {
        RestConsumerProperties.CrediyaAuthConfig authConfig = properties.getCrediyaAuth();
        return builder
          .baseUrl(authConfig.getUrl())
          .defaultHeader(HttpHeaders.CONTENT_TYPE, "application/json")
          .clientConnector(this.getClientHttpConnector(authConfig.getTimeout()))
          .build();
    }

    private ClientHttpConnector getClientHttpConnector(int timeout) {
        return new ReactorClientHttpConnector(HttpClient.create()
                .compress(true)
                .keepAlive(true)
                .option(CONNECT_TIMEOUT_MILLIS, timeout)
                .doOnConnected(connection -> {
                    connection.addHandlerLast(new ReadTimeoutHandler(timeout, MILLISECONDS));
                    connection.addHandlerLast(new WriteTimeoutHandler(timeout, MILLISECONDS));
                }));
    }

}
