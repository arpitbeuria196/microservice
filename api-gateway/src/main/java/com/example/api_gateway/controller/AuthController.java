package com.example.api_gateway.controller;


import com.example.api_gateway.service.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final JwtService jwtService;

    @GetMapping("/token")
    public String issueToken(@RequestParam Long userId,
                             @RequestParam(defaultValue = "USER") String role,
                             @RequestParam(defaultValue = "60") long minutes)
    {
        return jwtService.generateTokenForUser(userId, role, minutes);
    }
}
