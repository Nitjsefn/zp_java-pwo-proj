


            package com.com.csuni.pjp.server.security;

    import com.com.csuni.pjp.server.service.JwtService;
    import jakarta.servlet.FilterChain;
    import jakarta.servlet.ServletException;
    import jakarta.servlet.http.HttpServletRequest;
    import jakarta.servlet.http.HttpServletResponse;
    import org.springframework.beans.factory.annotation.Value;
    import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
    import org.springframework.security.core.context.SecurityContextHolder;
    import org.springframework.stereotype.Component;
    import org.springframework.web.filter.OncePerRequestFilter;

    import java.io.IOException;
    import java.util.Collections;

    @Component
    public class JwtAuthenticationFilter extends OncePerRequestFilter {

        private final JwtService jwtService;

        public JwtAuthenticationFilter(JwtService jwtService) {
            this.jwtService = jwtService;
        }

        @Override

        protected void doFilterInternal(HttpServletRequest request,
                                        HttpServletResponse response,
                                        FilterChain filterChain) throws ServletException, IOException {

            String path = request.getRequestURI();
            if (path.startsWith("/api/v1/auth/login") || path.startsWith("/api/v1/auth/register") || path.startsWith("/api/v1/auth/home")) {
                filterChain.doFilter(request, response);
                return;
            }

            String authHeader = request.getHeader("Authorization");

            if (authHeader != null && authHeader.startsWith("Bearer ")) {
                String token = authHeader.substring(7);
                String username;

                try {
                    username = jwtService.extractUsername(token);

                    if (jwtService.validateToken(token, username)) {

                        setAuthentication(username);
                    } else {
                        sendUnauthorized(response, "Expired or invalid JWT token");
                        return;
                    }

                } catch (Exception e) {
                    sendUnauthorized(response, "Invalid JWT token");
                    return;
                }
            }

            filterChain.doFilter(request, response);
        }

        private void setAuthentication(String username) {
            UsernamePasswordAuthenticationToken auth =
                    new UsernamePasswordAuthenticationToken(username, null, Collections.emptyList());
            SecurityContextHolder.getContext().setAuthentication(auth);
        }

        private void sendUnauthorized(HttpServletResponse response, String message) throws IOException {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json");
            response.getWriter().write(String.format("""
                {
                  "code": "%s",
                  "message": "%s"
                }
                """, "INVALID_TOKEN", message));
        }
    }