package org.example.githubproxy;


import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;

import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.hamcrest.Matchers.equalTo;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@WireMockTest(httpPort = 8181)
@AutoConfigureMockMvc
class GithubProxyApplicationTest {

    @Autowired
    private MockMvc mockMvc;

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("github.base-url", () -> "http://localhost:8181");
    }



    @Test
    void shouldReturnNonForkRepositoriesWithBranches() throws Exception {
        // GIVEN
        String username = "adam-poplawski";
        String myRepoName = "original-project";
        String forkRepoName = "forked-library";

        stubFor(WireMock.get(urlPathEqualTo("/users/" + username + "/repos"))
                .willReturn(okJson("""
                        [
                            {
                                "name": "%s",
                                "fork": true,
                                "owner": { "login": "%s" }
                            },
                            {
                                "name": "%s",
                                "fork": false,
                                "owner": { "login": "%s" }
                            }
                        ]
                        """.formatted(forkRepoName, username, myRepoName, username))));


        stubFor(WireMock.get(urlPathEqualTo("/repos/" + username + "/" + myRepoName + "/branches"))
                .willReturn(okJson("""
                        [
                            {
                                "name": "main",
                                "commit": { "sha": "abc-123" }
                            },
                            {
                                "name": "develop",
                                "commit": { "sha": "def-456" }
                            }
                        ]
                        """)));

        // WHEN & THEN
        mockMvc.perform(get("/users/" + username + "/repositories")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].repositoryName").value(myRepoName))
                .andExpect(jsonPath("$[0].ownerLogin").value(username))
                .andExpect(jsonPath("$[0].branches[0].name").value("main"))
                .andExpect(jsonPath("$[0].branches[0].lastCommitSha").value("abc-123"))
                .andExpect(jsonPath("$[0].branches[1].name").value("develop"))
                .andExpect(jsonPath("$[0].branches[1].lastCommitSha").value("def-456"));
    }


    @Test
    void shouldReturn404Json_WhenUserDoesNotExist() throws Exception {
        // GIVEN
        String username = "non-existing-user";

        stubFor(WireMock.get(urlPathEqualTo("/users/" + username + "/repos"))
                .willReturn(aResponse()
                        .withStatus(404)
                        .withHeader("Content-Type", "application/json")
                        .withBody("{ \"message\": \"Not Found\", \"documentation_url\": \"...\" }")));

        // WHEN & THEN
        mockMvc.perform(get("/users/" + username + "/repositories")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())

                .andExpect(status().isNotFound())

                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.message").value("GitHub user not found: " + username));
    }
}