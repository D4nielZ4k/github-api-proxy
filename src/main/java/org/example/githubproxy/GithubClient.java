package org.example.githubproxy;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.List;

@Component
class GithubClient {

    private final RestClient restClient;

    GithubClient(RestClient githubRestClient) {
        this.restClient = githubRestClient;
    }

    List<GithubRepo> getUserRepos(String username) {
        var repos = restClient
                .get()
                .uri("/users/{username}/repos", username)
                .retrieve()
                .onStatus(status -> status.value() == 404, (request, response) -> {
                    throw new GithubUserNotFoundException(username);
                })
                .body(new ParameterizedTypeReference<List<GithubRepo>>() {
                });

        return repos == null ? List.of() : repos;
    }

    List<GithubBranch> getRepoBranches(String ownerLogin, String repoName) {
        var branches = restClient
                .get()
                .uri("/repos/{owner}/{repo}/branches", ownerLogin, repoName)
                .retrieve()
                .body(new ParameterizedTypeReference<List<GithubBranch>>() {
                });

        return branches == null ? List.of() : branches;
    }
}
