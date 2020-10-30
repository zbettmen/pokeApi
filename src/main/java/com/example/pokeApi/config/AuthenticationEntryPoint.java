package com.example.pokeApi.config;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.www.BasicAuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class AuthenticationEntryPoint extends BasicAuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException authException) throws IOException {
        response.addHeader("WWW-Authenticate", "Basic realm -" + getRealmName());
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); //StatusCode 401 SC_Unauthorized
        var writer = response.getWriter();
        writer.println("Http Status 401: " + authException.getMessage()); //returns why authorization failed
    }

    @Override
    public void afterPropertiesSet() {
        setRealmName("Rijad Omars");
        super.afterPropertiesSet();
    }
}
