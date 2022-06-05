package it.os.event.handler.config;

import java.io.IOException;
import java.security.interfaces.RSAPublicKey;
import java.util.Calendar;

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
import org.springframework.web.filter.OncePerRequestFilter;

import com.auth0.jwk.Jwk;
import com.auth0.jwk.JwkProvider;
import com.auth0.jwk.UrlJwkProvider;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;

import io.jsonwebtoken.Jwts;
import it.os.event.handler.exception.BusinessException;
import it.os.event.handler.service.impl.UserService;
import lombok.extern.slf4j.Slf4j;

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
            if (!StringUtils.isEmpty(jwt) && validateJwtToken(jwt)) {

                final String username = Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(jwt).getBody()
                        .getSubject();

                final UserDetails userDetails = userService.loadUserByUsername(username);
                final UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                        userDetails, null,
                        userDetails.getAuthorities());
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        } catch (final Exception e) {
            log.error("Cannot set user authentication: {}", e);
        }

        filterChain.doFilter(request, response);
    }

    private boolean validateJwtToken(final String token) {

        boolean isValidJwtToken = true;
        try {
            final DecodedJWT jwt = JWT.decode(token);
            final JwkProvider provider = new UrlJwkProvider("http://localhost:8080");

            final Jwk jwk = provider.get(jwt.getKeyId());
            final Algorithm algorithm = Algorithm.RSA256((RSAPublicKey) jwk.getPublicKey(), null);
            algorithm.verify(jwt);

            if (jwt.getExpiresAt().before(Calendar.getInstance().getTime())) {
                throw new BusinessException("The token is expired!");
            }
        } catch (final Exception e) {
            isValidJwtToken = false;
        }

        return isValidJwtToken;
    }

    private String parseJwt(final HttpServletRequest request) {
        final String headerAuth = request.getHeader("Authorization");

        if (!StringUtils.isEmpty(headerAuth) && headerAuth.startsWith("Bearer ")) {
            return headerAuth.substring(7, headerAuth.length());
        }

        return null;
    }
}
