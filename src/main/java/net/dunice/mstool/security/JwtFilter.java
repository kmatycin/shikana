package net.dunice.mstool.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import net.dunice.mstool.constants.ConstantFields;
import net.dunice.mstool.constants.ValidationConstants;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    private final UserDetailsService userDetailsService;

    private final JwtService jwtService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String jwtToken = getJwtFromRequest(request);
        if (jwtToken != null && !jwtToken.equals(ValidationConstants.TOKEN_NOT_PROVIDED) &&
                jwtService.validateToken(jwtToken)) {
            String username = jwtService.getUsernameFromToken(jwtToken);
            CustomUserDetails userDetails = (CustomUserDetails) userDetailsService.loadUserByUsername(username);
            UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
        filterChain.doFilter(request, response);
    }

    private String getJwtFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader(ConstantFields.AUTHORIZATION);
        System.out.println("Полученный заголовок: " + bearerToken); // Отладка
        if (bearerToken != null && bearerToken.startsWith(ConstantFields.BEARER)) {
            String token = bearerToken.substring(7).trim(); // Убираем пробелы
            System.out.println("Извлечённый токен: " + token); // Отладка
            return token;
        }
        return ValidationConstants.TOKEN_NOT_PROVIDED;
    }
}