package org.example.githubproxy;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
class GithubService {

    private final GithubClient client;

    GithubService(GithubClient client) {
        this.client = client;
    }

    List<RepositoryInfo> listNonForkRepositoriesWithBranches(String username) {
        var repos = client.getUserRepos(username);

        return repos.stream()
                .filter(repo -> !repo.fork())
                .map(repo -> {
                    var ownerLogin = repo.owner().login();
                    var branches = client.getRepoBranches(ownerLogin, repo.name());

                    var branchInfos = branches.stream()
                            .map(b -> new BranchInfo(b.name(), b.commit().sha()))
                            .toList();

                    return new RepositoryInfo(repo.name(), ownerLogin, branchInfos);
                })
                .toList();
    }
}
