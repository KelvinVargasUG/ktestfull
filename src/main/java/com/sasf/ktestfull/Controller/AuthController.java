package com.sasf.ktestfull.Controller;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sasf.ktestfull.Dto.ApiGenericResponse;
import com.sasf.ktestfull.Dto.UserResponseDto;
import com.sasf.ktestfull.Security.JwtTokenProvider;
import com.sasf.ktestfull.Service.IUserService;
import com.sasf.ktestfull.Util.ResponseUtil;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;

    private final JwtTokenProvider tokenProvider;

    private final IUserService userService;

    public AuthController(AuthenticationManager authenticationManager, JwtTokenProvider tokenProvider,
            IUserService userService) {
        this.authenticationManager = authenticationManager;
        this.tokenProvider = tokenProvider;
        this.userService = userService;
    }

    @PostMapping("/login")
    public ResponseEntity<ApiGenericResponse<Map<String, String>>> login(@RequestParam String email,
            @RequestParam String password) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(email, password));
        SecurityContextHolder.getContext().setAuthentication(authentication);

        Long userId = userService.getAuthenticatedUserId(email).getIdUser();
        String token = tokenProvider.generateToken(email, userId);
        return ResponseUtil.ok(null, Map.of("token", token));
    }

    @GetMapping("/me")
    public ResponseEntity<ApiGenericResponse<Map<String, Object>>> getAuthenticatedUserId(
            @RequestHeader("Authorization") String authHeader) {
        String token = authHeader.replace("Bearer ", "");
        String email = tokenProvider.getEmailFromToken(token);
        UserResponseDto user = userService.getAuthenticatedUserId(email);
        Long userId = user.getIdUser();
        List<String> roles = user.getRol();
        return ResponseUtil.ok(null, Map.of("userId", userId, "email", email, "roles", roles));
    }
}