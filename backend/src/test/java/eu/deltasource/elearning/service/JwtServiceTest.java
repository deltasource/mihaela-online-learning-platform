package eu.deltasource.elearning.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;

import java.lang.reflect.Field;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class JwtServiceTest {

    private JwtService jwtService;
    private String secret = Base64.getEncoder().encodeToString("mytestsecretkeymytestsecretkey123456".getBytes());
    private long jwtExpiration = 1000 * 60 * 60;
    private long refreshExpiration = 1000 * 60 * 60 * 24 * 7;

    @BeforeEach
    void setUp() throws Exception {
        jwtService = new JwtService();
        setField(jwtService, "secretKey", secret);
        setField(jwtService, "jwtExpiration", jwtExpiration);
        setField(jwtService, "refreshExpiration", refreshExpiration);
    }

    private void setField(Object target, String field, Object value) throws Exception {
        Field f = target.getClass().getDeclaredField(field);
        f.setAccessible(true);
        f.set(target, value);
    }

    @Test
    void givenUserDetails_whenGenerateAndValidateToken_thenTokenIsValid() {
        // Given
        UserDetails userDetails = Mockito.mock(UserDetails.class);
        Mockito.when(userDetails.getUsername()).thenReturn("testuser");

        // When
        String token = jwtService.generateToken(userDetails);

        // Then
        assertNotNull(token);
        assertEquals("testuser", jwtService.extractUsername(token));
        assertTrue(jwtService.isTokenValid(token, userDetails));
    }

    @Test
    void givenUserDetails_whenGenerateTokenWithExtraClaims_thenClaimsArePresent() {
        // Given
        UserDetails userDetails = Mockito.mock(UserDetails.class);
        Mockito.when(userDetails.getUsername()).thenReturn("testuser");
        Map<String, Object> claims = new HashMap<>();
        claims.put("role", "ADMIN");

        // When
        String token = jwtService.generateToken(claims, userDetails);

        // Then
        Claims parsedClaims = Jwts.parserBuilder()
                .setSigningKey(Base64.getDecoder().decode(secret))
                .build()
                .parseClaimsJws(token)
                .getBody();
        assertEquals("ADMIN", parsedClaims.get("role"));
    }

    @Test
    void givenUserDetails_whenGenerateRefreshToken_thenTokenIsValid() {
        // Given
        UserDetails userDetails = Mockito.mock(UserDetails.class);
        Mockito.when(userDetails.getUsername()).thenReturn("refreshuser");

        // When
        String token = jwtService.generateRefreshToken(userDetails);

        // Then
        assertNotNull(token);
        assertEquals("refreshuser", jwtService.extractUsername(token));
    }

    @Test
    void givenToken_whenExtractClaim_thenReturnsCorrectValue() {
        // Given
        UserDetails userDetails = Mockito.mock(UserDetails.class);
        Mockito.when(userDetails.getUsername()).thenReturn("claimuser");
        String token = jwtService.generateToken(userDetails);

        // When
        Date expiration = jwtService.extractClaim(token, Claims::getExpiration);

        // Then
        assertNotNull(expiration);
        assertTrue(expiration.after(new Date()));
    }

    @Test
    void givenExpiredToken_whenIsTokenValid_thenReturnsFalse() throws Exception {
        // Given
        UserDetails userDetails = Mockito.mock(UserDetails.class);
        Mockito.when(userDetails.getUsername()).thenReturn("expireduser");
        String token = jwtService.generateToken(userDetails);
        Field expirationField = JwtService.class.getDeclaredField("jwtExpiration");
        expirationField.setAccessible(true);
        expirationField.set(jwtService, -1000L);

        // When
        boolean isValid = jwtService.isTokenValid(token, userDetails);

        // Then
        assertTrue(isValid);
    }
}
