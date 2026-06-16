package com.br.univille.Libranos.controler;
import com.br.univille.Libranos.dto.LoginRecord;
import com.br.univille.Libranos.dto.LoginResponse;
import com.br.univille.Libranos.dto.RegisterRecord;
import com.br.univille.Libranos.models.User;
import com.br.univille.Libranos.service.AuthenticationService;
import com.br.univille.Libranos.service.JwtService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/auth")
@RestController
public class AuthenticationController {
    private final JwtService jwtService;

    private final AuthenticationService authenticationService;

    public AuthenticationController(JwtService jwtService, AuthenticationService authenticationService) {
        this.jwtService = jwtService;
        this.authenticationService = authenticationService;
    }

    @PostMapping("/signup")
    public ResponseEntity<User> register(@RequestBody RegisterRecord registerUserDto) {
        User registeredUser = authenticationService.signup(registerUserDto);

        return ResponseEntity.ok(registeredUser);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> authenticate(@RequestBody LoginRecord loginUserDto) {
        System.out.println("Login chamado: " + loginUserDto.email());
        User authenticatedUser = authenticationService.authenticate(loginUserDto);

        String jwtToken = jwtService.generateToken(authenticatedUser);
        Long expirationTime = jwtService.getExpirationTime();

        LoginResponse loginResponse = new LoginResponse(
                jwtToken,
                expirationTime,
                authenticatedUser.getRole().name()
        );

        return ResponseEntity.ok(loginResponse);
    }
}