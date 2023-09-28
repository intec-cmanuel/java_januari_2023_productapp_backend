package be.intecbrussel.productapp.controller;

import be.intecbrussel.productapp.model.AppUser;
import be.intecbrussel.productapp.security.JwtUtil;
import be.intecbrussel.productapp.model.dto.LoginRequest;
import be.intecbrussel.productapp.model.dto.LoginResponse;
import be.intecbrussel.productapp.service.RegisterService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final RegisterService registerService;

    public AuthController(AuthenticationManager authenticationManager, JwtUtil jwtUtil, RegisterService registerService) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.registerService = registerService;
    }

    @PostMapping("/login")
    public ResponseEntity login(@RequestBody LoginRequest loginRequest) {
        try {
            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));
            String email = authentication.getName();
            AppUser user = new AppUser(email, "");
            String token = jwtUtil.createToken(user);
            LoginResponse loginResponse = new LoginResponse(email, token);

            return ResponseEntity.ok(loginResponse);

        } catch (BadCredentialsException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid username or password");
        } catch (Exception e){
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PostMapping("/register")
    public ResponseEntity register(@RequestBody LoginRequest loginRequest) {
        registerService.createUser(loginRequest.getEmail(), loginRequest.getPassword());
        return ResponseEntity.ok().build();
    }
}
