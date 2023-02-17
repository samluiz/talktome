package com.saurs.talktome.utils;

import com.saurs.talktome.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

public class ControllerUtils {

    public static UserDetails getAuthenticatedUser() {
       Authentication auth = SecurityContextHolder.getContext().getAuthentication();
       if (auth != null) {
           if (auth.isAuthenticated()) {
               return (UserDetails) auth.getPrincipal();
           }
       }
       throw new AuthenticationCredentialsNotFoundException("User is not authenticated.");
    }
}
