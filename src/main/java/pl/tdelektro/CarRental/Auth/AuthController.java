package pl.tdelektro.CarRental.Auth;


import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
class AuthController {

    private final AuthenticateService authenticateService;

    @PostMapping("/register")
    ResponseEntity<AuthenticationResponse> register(@RequestBody RegisterRequest request) {
        return new ResponseEntity<AuthenticationResponse>(authenticateService.register(request), HttpStatus.OK);
    }

    @PostMapping("/register/admin")
    ResponseEntity<AuthenticationResponse> registerAdmin(@RequestBody RegisterRequest request) {
        return new ResponseEntity<AuthenticationResponse>(authenticateService.registerAdmin(request), HttpStatus.OK);
    }


    @PostMapping("/authenticate")
    ResponseEntity<AuthenticationResponse> authenticate(@RequestBody AuthenticationRequest request) {
        return new ResponseEntity<AuthenticationResponse>(authenticateService.authenticate(request), HttpStatus.OK);
    }




}
