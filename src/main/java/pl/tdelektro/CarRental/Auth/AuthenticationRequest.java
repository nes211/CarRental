package pl.tdelektro.CarRental.Auth;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
class AuthenticationRequest {
    private String emailAddress;
    private String password;
}
