package com.demo.okta_example.shared;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {

    @GetMapping("/me")
    public String user2(@AuthenticationPrincipal OidcUser user) {
        return "Bienvenido user2, " + user.getFullName() + "!";
    }

    @GetMapping("/profile")
    public String user(@AuthenticationPrincipal OAuth2User user) {
        return "Bienvenido user, " + user.getAttribute("name") + "!";
    }

    @GetMapping("/session")
    public String whoami(Authentication authentication) {
        return authentication.getDetails().toString();
    }
}

