
package com.com.csuni.pjp.server.authService;

import com.com.csuni.pjp.server.service.JwtService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneOffset;

import static org.assertj.core.api.Assertions.assertThat;

class JwtServiceTest {

    private JwtService jwtService;

    @BeforeEach
    void setup() {
        jwtService = new JwtService();

        ReflectionTestUtils.setField(jwtService, "jwtSecret",
                "TEST_SECRET_TEST_SECRET_TEST_SECRET_TEST_SECRET");

        ReflectionTestUtils.setField(jwtService, "jwtExpirationMs", 60000L);

        jwtService.setClock(Clock.fixed(
                Instant.parse("2025-12-06T00:00:00Z"),
                ZoneOffset.UTC
        ));
    }

    @Test
    @DisplayName("Test for checking if generateToken method generates deterministic token")
    void testGenerateJwtToken() {
        String token = jwtService.generateToken("admin");
        String expected =
                "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhZG1pbiIsImlhdCI6MTc2NDk3OTIwMCwiZXhwIjoxNzY0OTc5MjYwfQ.STephIAZ4RLSG69U4bz1evK3WzsKVcrczsw1NZ39HaQ";

        assertThat(token).isEqualTo(expected);
    }
}
