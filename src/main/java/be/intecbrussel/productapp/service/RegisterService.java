package be.intecbrussel.productapp.service;

import be.intecbrussel.productapp.model.AppUser;
import be.intecbrussel.productapp.model.dto.LoginRequest;
import be.intecbrussel.productapp.model.dto.LoginResponse;
import be.intecbrussel.productapp.repository.UserRepository;
import be.intecbrussel.productapp.security.JwtUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class RegisterService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;


    public RegisterService(UserRepository userRepository, BCryptPasswordEncoder bCryptPasswordEncoder, AuthenticationManager authenticationManager, JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
    }

    public void createUser(String email, String password) {
        AppUser user = new AppUser(email, bCryptPasswordEncoder.encode(password));
        userRepository.save(user);
    }

    public LoginResponse login(LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));
        String email = authentication.getName();
        AppUser user = new AppUser(email, "");
        String token = jwtUtil.createToken(user);
        LoginResponse loginResponse = new LoginResponse(email, token);

        return loginResponse;
    }
}
