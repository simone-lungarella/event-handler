package it.os.event.handler.config;

import java.io.IOException;
import java.util.Base64;

import javax.crypto.spec.SecretKeySpec;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.impl.crypto.DefaultJwtSignatureValidator;
import it.os.event.handler.service.impl.UserService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@AllArgsConstructor
public class AuthTokenFilter extends OncePerRequestFilter {

    /*
     * The user details service.
     */
    private final UserService userService;

    private final String jwtSecret;

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

                final String[] chunks = jwt.split("\\.");
                final String header = new String(Base64.getUrlDecoder().decode(chunks[0]));
                
                final String algorithm = StringUtils.substringBetween(header, "\"alg\":\"", "\"");
                final SignatureAlgorithm sa = SignatureAlgorithm.valueOf(algorithm);
                
                final SecretKeySpec secretKeySpec = new SecretKeySpec(jwtSecret.getBytes(), sa.getJcaName());

                final String username = Jwts.parser()
                        .setSigningKey(secretKeySpec)
                        .parseClaimsJws(jwt)
                        .getBody()
                        .getSubject();

                log.info("Extracted user with username {} in jwt token", username);
                
                final UserDetails userDetails = userService.loadUserByUsername(username);
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

        log.debug("Validating jwt");
        boolean isValidJwtToken = true;
        try {

            final String[] chunks = token.split("\\.");
            final Base64.Decoder decoder = Base64.getUrlDecoder();

            final String header = new String(decoder.decode(chunks[0]));

            final String tokenWithoutSignature = chunks[0] + "." + chunks[1];
            final String signature = chunks[2];

            final String algorithm = StringUtils.substringBetween(header, "\"alg\":\"", "\"");
            
            final SignatureAlgorithm sa = SignatureAlgorithm.valueOf(algorithm);
            final SecretKeySpec secretKeySpec = new SecretKeySpec(jwtSecret.getBytes(), sa.getJcaName());

            final DefaultJwtSignatureValidator validator = new DefaultJwtSignatureValidator(sa, secretKeySpec);

            log.debug("Proceeding with validation of token");
            if (!validator.isValid(tokenWithoutSignature, signature)) {
                log.warn("Token is not valid");
                isValidJwtToken = false;
            } else {
                log.info("Token validated correctly");
            }

        } catch (final MalformedJwtException e) {
            log.error("Invalid JWT token: {}", e.getMessage());
        } catch (final ExpiredJwtException e) {
            log.error("JWT token is expired: {}", e.getMessage());
        } catch (final UnsupportedJwtException e) {
            log.error("JWT token is unsupported: {}", e.getMessage());
        } catch (final IllegalArgumentException e) {
            log.error("JWT claims string is empty: {}", e.getMessage());
        } catch (final Exception e) {
            log.warn("Token invalid: {}", e);
            isValidJwtToken = false;
        }

        return isValidJwtToken;
    }

    private String parseJwt(final HttpServletRequest request) {

        log.debug("Parsing jwt token");
        final String headerAuth = request.getHeader("Authorization");

        if (!StringUtils.isEmpty(headerAuth) && (headerAuth.startsWith("bearer ") || headerAuth.startsWith("Bearer "))) {
            return headerAuth.substring(7, headerAuth.length());
        } else {
            log.warn("No token found in request: {}", headerAuth);
            return null;
        }

    }
}
