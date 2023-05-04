package com.hms.security;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JwtTokenHelper jwtTokenHelper;
    @Autowired
    private  CustomUserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
            String header = request.getHeader("Authorization");
            String username = null;
            String token = null;
            if(header!=null && header.startsWith("Bearer ")){
                token = header.substring(7);
                try {
                    username = this.jwtTokenHelper.extractUsername(token);
                }catch (IllegalArgumentException ex){
                    log.info("Error occurred getting user name from token");
                }catch (ExpiredJwtException ex){
                    log.info("Token expired",ex);
                }catch (SignatureException ex){
                    log.info("Invalid username",ex);
                }catch (MalformedJwtException ex){
                    log.info("invalid jwt",ex);
                }
            }
            else{
                log.warn("Token should start with bearer");
            }

            if(username!=null && SecurityContextHolder.getContext().getAuthentication()==null){
                UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);
                if(this.jwtTokenHelper.validateToken(token,userDetails)){
                    UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(userDetails,null,userDetails.getAuthorities());
                    usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
                }
                else{
                    log.info("Token not validated");
                }
            }
            else{
                log.info("username is null or security context is not null");
            }
            log.info("before filter chain");
            filterChain.doFilter(request,response);
            log.info("after filter chain");
    }
}
