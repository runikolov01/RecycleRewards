package com.fcst.student.RecycleRewards.IntegrationTests;

import com.fcst.student.RecycleRewards.RecycleRewardsApplication;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = RecycleRewardsApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class UserControllerIntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    private HttpHeaders headers;

    @BeforeEach
    void setUp() {
        headers = new HttpHeaders();
        authenticate();
    }

    private void authenticate() {
        String url = "http://localhost:" + port + "/login";
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("username", "testuser");
        params.add("password", "testpassword");

        ResponseEntity<String> response = restTemplate.postForEntity(url, params, String.class);

        headers.set("Cookie", response.getHeaders().getFirst(HttpHeaders.SET_COOKIE));
    }

    @Test
    void testHomePage() {
        String url = "http://localhost:" + port + "/home";
        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).contains("Добре дошли в RecycleRewards!");
    }

    @Test
    void testRegisterForm() {
        String url = "http://localhost:" + port + "/register";
        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).contains("Форма за Регистрация");
    }

    @Test
    void testLoginForm() {
        String url = "http://localhost:" + port + "/login";
        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).contains("Вход в профила");
    }


    @Test
    void testShowWinners() {
        String url = "http://localhost:" + port + "/winners";
        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).contains("Печеливши от томболи");
    }

    @Test
    void testAboutPage() {
        String url = "http://localhost:" + port + "/about";
        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).contains("Какво е RecycleRewards?");
    }
}