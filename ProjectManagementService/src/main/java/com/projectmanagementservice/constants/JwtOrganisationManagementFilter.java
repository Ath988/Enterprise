package com.projectmanagementservice.constants;

import com.projectmanagementservice.utility.JwtManager;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Objects;
import java.util.Optional;


@Slf4j
@AllArgsConstructor
public class JwtOrganisationManagementFilter extends OncePerRequestFilter {
    private JwtManager jwtManager;
    private JwtUserDetail userDetail;
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        final String authHeader=request.getHeader("Authorization");

        log.info("gelen token...: "  + authHeader);
        log.info("Tüm istekler Requirements serviste buraya düşecek ve burada kontrol yapılacak.");
        if (Objects.nonNull(authHeader) && authHeader.startsWith("Bearer ")){
            String token=authHeader.substring(7);
            log.info("Token...: " + token);

            Optional<String> role=jwtManager.getRoleFromToken(token);
            if (role.isPresent()){
                UserDetails userDetails= userDetail.loadUserByUserRole(role.get());
                UsernamePasswordAuthenticationToken authenticationToken=new UsernamePasswordAuthenticationToken(
                        userDetails,null,userDetails.getAuthorities()
                );
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            }
        }
        filterChain.doFilter(request,response);
    }
}
