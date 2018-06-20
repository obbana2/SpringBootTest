package com.obbana.myapp.utils;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import java.util.Map;
import java.util.logging.Level;

public class Utils {

    public static boolean isAdmin(Authentication authentication) {
        for(GrantedAuthority auth : authentication.getAuthorities()) {
            if(auth.getAuthority().equals("ADMIN"))
                return true;
        }
        return false;
    }

    public static void isAdminToModel(Map<String, Object> model, Authentication authentication) {
        model.put("__isAdmin", isAdmin(authentication));
    }
}
