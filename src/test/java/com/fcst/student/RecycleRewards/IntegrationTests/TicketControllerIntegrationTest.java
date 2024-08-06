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
class TicketControllerIntegrationTest {

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
    void testOpenHomePage() {
        String url = "http://localhost:" + port + "/index";
        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).contains("Начална страница");
    }

    @Test
    void testOpenStartPage() {
        String url = "http://localhost:" + port + "/start";
        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).contains("Натиснете бутона преди всяко добавяне на нова бутилка:");
    }


    @Test
    void testRegisterTicketPage() {
        String url = "http://localhost:" + port + "/registerTicket";
        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).contains("Регистрация на билет");
    }

    @Test
    void testAddBottle() {
        String url = "http://localhost:" + port + "/start";
        ResponseEntity<String> response = restTemplate.postForEntity(url, null, String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FOUND);
        assertThat(response.getHeaders().getLocation().toString()).contains("/start");
    }
}