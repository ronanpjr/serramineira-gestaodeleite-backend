package serramineira.sistemas.leite.service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import serramineira.sistemas.leite.model.Usuario;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Instant;

@Service
public class TokenService {

    @Value("${api.security.token.secret}")
    private String secret;

    @Value("${api.security.token.expiration}")
    private long expiration;

    public String gerarToken(Usuario usuario) {
        SecretKey secretKey = getSecretKey();
        return Jwts.builder()
                .subject(usuario.getEmail())
                .claim("userId", usuario.getId())
                .issuer("API Serra Mineira Leite")
                .issuedAt(java.sql.Timestamp.from(Instant.now()))
                .expiration(java.sql.Timestamp.from(Instant.now().plusMillis(expiration)))
                .signWith(secretKey)
                .compact();
    }

    public String getSubject(String tokenJWT) {
        try {
            SecretKey secretKey = getSecretKey();
            return Jwts.parser()
                    .verifyWith(secretKey)
                    .build()
                    .parseSignedClaims(tokenJWT)
                    .getPayload()
                    .getSubject();
        } catch (Exception e) {
            throw new RuntimeException("Token JWT inválido ou expirado!");
        }
    }

    private SecretKey getSecretKey() {
        return Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }
}