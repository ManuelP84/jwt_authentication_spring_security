package com.springsecurity.jwtAuthentication.security.filter;

import com.springsecurity.jwtAuthentication.entity.user.User;
import com.springsecurity.jwtAuthentication.security.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class JwtTokenFilter extends OncePerRequestFilter {

    @Autowired
    private JwtTokenUtil jwtTokenUtil;
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        if(!hasAuthorizationHeader(request)){
            filterChain.doFilter(request, response);
            return;
        }
        String accessToken = getAccessToken(request);

        if(!jwtTokenUtil.validateAccessToken(accessToken)){
            filterChain.doFilter(request, response);
            return;
        }

        setAuthenticationContext(accessToken, request);
        filterChain.doFilter(request, response);
    }

    private void setAuthenticationContext(String accessToken, HttpServletRequest request){
        UserDetails userDetails = getUserDetails(accessToken);

        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, null);
        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    private UserDetails getUserDetails(String accessToken) {
        User userDetails = new User();
        String[] subjectArray = jwtTokenUtil.getSubject(accessToken).split(",");
        userDetails.setId(Integer.parseInt(subjectArray[0]));
        userDetails.setEmail(subjectArray[1]);

        return userDetails;
    }

    private Boolean hasAuthorizationHeader(HttpServletRequest request){
        String auth = request.getHeader("Authorization");
        System.out.println("Authorization header: " + auth);
        if(ObjectUtils.isEmpty(auth) || !auth.startsWith("Bearer")){
            return false;
        }
        return true;
    }

    private String getAccessToken(HttpServletRequest request){
        String auth = request.getHeader("Authorization");
        String token = auth.split(" ")[1].trim();   // Split the header. Example:  Bearer gETgsdGRTY564b
        System.out.println("Access token: " + token);
        return token;
    }
}
