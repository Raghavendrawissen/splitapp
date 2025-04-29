package org.example.splitapp.Controller;

import org.example.splitapp.model.LoginRequest;
import org.example.splitapp.model.RegisterRequest;
import org.example.splitapp.model.User;
import org.example.splitapp.Service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<User> register(@RequestBody RegisterRequest request) {
        User user = authService.register(request);
        user.setPasswordHash(null); // Don't expose password hash
        return ResponseEntity.ok(user);
    }

    @PostMapping("/login")
    public ResponseEntity<User> login(@RequestBody LoginRequest request) {
        User user = authService.login(request);
        user.setPasswordHash(null); // Don't expose password hash
        return ResponseEntity.ok(user);
    }
}