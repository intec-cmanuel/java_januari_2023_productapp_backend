package be.intecbrussel.productapp.service;

import be.intecbrussel.productapp.model.AppUser;
import be.intecbrussel.productapp.repository.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class RegisterService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public RegisterService(UserRepository userRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    public void createUser(String email, String password) {
        AppUser user = new AppUser(email, bCryptPasswordEncoder.encode(password));
        userRepository.save(user);
    }
}
