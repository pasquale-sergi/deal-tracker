package dev.pasq.deal_track.controller;

import dev.pasq.deal_track.Dto.LoginDto;
import dev.pasq.deal_track.Dto.LoginResponseDto;
import dev.pasq.deal_track.Dto.RegisterDto;
import dev.pasq.deal_track.entity.ApplicationUser;
import dev.pasq.deal_track.service.AuthenticationService;
import dev.pasq.deal_track.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@CrossOrigin("*")
public class AuthenticationController {

    private UserService userService;
    @Autowired
    private AuthenticationService authenticationService;
    @PostMapping("/register")
    public ApplicationUser registerUser(@RequestBody RegisterDto request){
        System.out.println("in the register controller");
        return authenticationService.registerUser(request.getUsername(), request.getEmail(), request.getPassword());
    }

    @PostMapping("/login")
    public LoginResponseDto loginUser(@RequestBody LoginDto request){
        return authenticationService.loginUser(request.getUsername(), request.getPassword());
    }
}
