package org.example.githubproxy;

import java.util.List;


//mini fields

record GithubOwner(String login) {
}

record GithubRepo(String name, boolean fork, GithubOwner owner) {
}

record GithubCommit(String sha) {
}

record GithubBranch(String name, GithubCommit commit) {
}

// --- Our API models ---

record BranchInfo(String name, String lastCommitSha) {
}

record RepositoryInfo(String repositoryName, String ownerLogin, List<BranchInfo> branches) {
}

record ErrorResponse(int status, String message) {
}
