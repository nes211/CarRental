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

    private final AuthenticateService service;

    @PostMapping("register")
    ResponseEntity<AuthenticationResponse> register(@RequestBody RegisterRequest request) {
        return new ResponseEntity<AuthenticationResponse>(service.register(request), HttpStatus.OK);
    }

    @PostMapping("authenticate")
    ResponseEntity<AuthenticationResponse> authenticateCustomer(@RequestBody AuthenticationRequest request) {
        return new ResponseEntity<AuthenticationResponse>(service.authenticate(request), HttpStatus.OK);
    }
}
