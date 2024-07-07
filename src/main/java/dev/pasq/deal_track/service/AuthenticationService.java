package dev.pasq.deal_track.service;

import dev.pasq.deal_track.Dto.LoginResponseDto;
import dev.pasq.deal_track.auth.TokenService;
import dev.pasq.deal_track.entity.ApplicationUser;
import dev.pasq.deal_track.entity.Role;
import dev.pasq.deal_track.entity.Token;
import dev.pasq.deal_track.repository.RoleRepository;
import dev.pasq.deal_track.repository.TokenRepository;
import dev.pasq.deal_track.repository.UserRepository;
import dev.pasq.deal_track.utils.EmailTemplate;
import jakarta.mail.MessagingException;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
@Service
@NoArgsConstructor
@AllArgsConstructor
@Transactional
public class AuthenticationService {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private TokenRepository tokenRepository;

    @Autowired
    private EmailService emailService;

    String activationUrl = "http://localhost:8090";


    public ApplicationUser registerUser(String username, String email, String password) throws MessagingException {
        String encodedPassword = passwordEncoder.encode(password);
        Role userRole = roleRepository.findByAuthority("USER").get();
        Set<Role> authorities = new HashSet<>();
        authorities.add(userRole);

        ApplicationUser user = userRepository.save(new ApplicationUser(0L, username, email, encodedPassword, authorities, false,false));
        sendValidationEmail(user);
        return user;
    }

    public void sendValidationEmail(ApplicationUser user) throws MessagingException {
        var newToken= generateAndSaveActivateToken(user);
        //send email
        try {
            emailService.sendEmail(
                    user.getEmail(),
                    user.getUsername(),
                    EmailTemplate.ACTIVATE_ACCOUNT,
                    activationUrl,
                    newToken,
                    "Account Activation"
            );
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }

    public String generateAndSaveActivateToken(ApplicationUser user){
        //generate the token
        String generatedToken = generateActivationToken(6);
        var token = Token.builder()
                .token(generatedToken)
                .createdAt(LocalDateTime.now())
                .expiresAt(LocalDateTime.now().plusMinutes(15))
                .user(user)
                .build();
        tokenRepository.save(token);
        return generatedToken;
    }

    public String generateActivationToken(Integer length){
        String characters = "0123456789";
        StringBuilder codeBuilder = new StringBuilder();
        SecureRandom secureRandom = new SecureRandom();
        for (int i = 0; i<length; i++){
            int randomIndex = secureRandom.nextInt(characters.length());
            codeBuilder.append(characters.charAt(randomIndex));
        }
        return codeBuilder.toString();
    }

    public LoginResponseDto loginUser(String username, String password){

        try{
            Authentication auth = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(username, password)
            );
            //if everything works this generate a token
            String token = tokenService.generateJwt(auth);
            return new LoginResponseDto(userRepository.findByUsername(username).get(), token);
        }catch (AuthenticationException e){
            return new LoginResponseDto(null, "");
        }
    }

    @Transactional
    public void activateAccount(String token) throws MessagingException {
        Token savedToken = tokenRepository.findByToken(token).orElseThrow(()->new RuntimeException("Invalid token"));
        if(LocalDateTime.now().isAfter(savedToken.getExpiresAt())){
            sendValidationEmail(savedToken.getUser());
            throw new RuntimeException("Activation token expired. A new token has been sent to the email address.");

        }

        var user = userRepository.findById(savedToken.getUser().getId()).orElseThrow(()->new UsernameNotFoundException("User not found."));
        user.setEnabled(true);
        userRepository.save(user);
        savedToken.setValidatedAt(LocalDateTime.now());
        tokenRepository.save(savedToken);

    }

}
