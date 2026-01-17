package org.example.githubproxy;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.web.client.RestClient;


@Configuration
class GithubClientConfig {

    @Bean
    RestClient githubRestClient(
            RestClient.Builder builder,
            @Value("${github.base-url:https://api.github.com}") String baseUrl
    ) {
        return builder
                .baseUrl(baseUrl)
                // GitHub API recommends using a vendor media type.
                .defaultHeader(HttpHeaders.ACCEPT, "application/vnd.github+json")
                // GitHub API historically requires a User-Agent header.
                .defaultHeader(HttpHeaders.USER_AGENT, "github-proxy")
                .build();
    }
}
