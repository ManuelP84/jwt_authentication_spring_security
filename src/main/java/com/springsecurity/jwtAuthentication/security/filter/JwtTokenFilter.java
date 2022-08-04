package com.springsecurity.jwtAuthentication.security.filter;

import com.springsecurity.jwtAuthentication.entity.user.Role;
import com.springsecurity.jwtAuthentication.entity.user.User;
import com.springsecurity.jwtAuthentication.security.JwtTokenUtil;
import io.jsonwebtoken.Claims;
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

        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                userDetails,
                null,
                userDetails.getAuthorities());

        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    private UserDetails getUserDetails(String accessToken) {
        User userDetails = new User();

        // Get the claims from the access token
        Claims claims = jwtTokenUtil.parseClaims(accessToken);

        // Get the roles from the claims
        String claimRolesStr = (String) claims.get("roles");
        claimRolesStr = claimRolesStr
                .replace("[", "")
                .replace("]", "")
                .replace(" ", "");
        String[] rolesArray = claimRolesStr.split(",");

        // Include the roles in the user details
        for(String role : rolesArray) {
            userDetails.addRole(new Role(role));
        }

        // Get the subject from the claims
        String subject = (String)claims.get(Claims.SUBJECT);
        String[] subjectArray = subject.split(",");

        // Include the id and the email to the user details
        userDetails.setId(Integer.parseInt(subjectArray[0]));
        userDetails.setEmail(subjectArray[1]);

        return userDetails;
    }

    private Boolean hasAuthorizationHeader(HttpServletRequest request){
        String auth = request.getHeader("Authorization");
        if(ObjectUtils.isEmpty(auth) || !auth.startsWith("Bearer")){
            return false;
        }
        return true;
    }

    private String getAccessToken(HttpServletRequest request){
        String auth = request.getHeader("Authorization");
        String token = auth.split(" ")[1].trim();   // Split the header. Example:  Bearer gETgsdGRTY564b
        //System.out.println("Access token: " + token);
        return token;
    }
}
