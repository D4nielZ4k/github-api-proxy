package org.example.githubproxy;

class GithubUserNotFoundException extends RuntimeException {

    GithubUserNotFoundException(String username) {
        super("GitHub user not found: " + username);
    }
}
