package pl.kj.bachelors.teams.domain.service.security;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import pl.kj.bachelors.teams.domain.exception.JwtInvalidException;

public interface JwtParser {
    void validateToken(String jwt) throws JwtInvalidException, ExpiredJwtException;
    String getUid(String jwt);
    Claims parseClaims(String jwt);
}
