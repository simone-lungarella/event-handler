package it.os.event.handler.config;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Service;
import org.springframework.web.filter.OncePerRequestFilter;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import it.os.event.handler.service.impl.UserService;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class AuthTokenFilter extends OncePerRequestFilter {

    /*
     * The user details service.
     */
    @Autowired
    private UserService userService;

    @Value("${jwt-token.secret}")
    private String jwtSecret;

    /**
     * Do filter internal.
     *
     * @param request     the request
     * @param response    the response
     * @param filterChain the filter chain
     * @throws ServletException the servlet exception
     * @throws IOException      Signals that an I/O exception has occurred.
     */
    @Override
    protected void doFilterInternal(final HttpServletRequest request, final HttpServletResponse response,
            final FilterChain filterChain)
            throws ServletException, IOException {

        try {
            final String jwt = parseJwt(request);
            if (!StringUtils.isEmpty(jwt) && isValidJwtToken(jwt)) {

                final String username = Jwts.parser()
                        .setSigningKey(Base64.getDecoder().decode(jwtSecret))
                                    .parseClaimsJws(jwt)
                                    .getBody()
                                    .getSubject();
                                    
                log.info("Extracted user with username {} in jwt token", username);
                final UserDetails userDetails = userService.loadUserByUsername(username);
                log.info("Loaded user with username {}", userDetails.getUsername());
                final UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());

                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        } catch (final Exception e) {
            log.error("Cannot set user authentication", e);
        }

        filterChain.doFilter(request, response);
    }

    private boolean isValidJwtToken(final String token) {

        log.info("Validating jwt");
        boolean isValidJwtToken = true;
        try {
            log.info(token);
		    
            final String secret = "asdfSFS34wfsdfsdfSDSD32dfsddDDerQSNCK34SOWEK5354fdgdf4";
            Claims claims = Jwts.parser().setSigningKey(secret.getBytes(StandardCharsets.UTF_8)).parseClaimsJws(token.trim()).getBody();
            isValidJwtToken = claims != null;

        } catch (MalformedJwtException e) {
            log.error("Invalid JWT token: {}", e.getMessage());
        } catch (ExpiredJwtException e) {
            log.error("JWT token is expired: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            log.error("JWT token is unsupported: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            log.error("JWT claims string is empty: {}", e.getMessage());
        } catch (final Exception e) {
            log.warn("Token invalid: {}", e);
            isValidJwtToken = false;
        }

        return isValidJwtToken;
    }

    private String parseJwt(final HttpServletRequest request) {

        log.info("Parsing jwt token");
        final String headerAuth = request.getHeader("Authorization");
        log.info(headerAuth);

        if (!StringUtils.isEmpty(headerAuth) && headerAuth.startsWith("bearer ")) {
            return headerAuth.substring(7, headerAuth.length());
        } else {
            log.info("No token found in request: {}", headerAuth);
            return null;
        }

    }
}
