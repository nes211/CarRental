package pl.tdelektro.CarRental.Auth;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
class RegisterRequest {

    private String name;
    private String emailAddress;
    private String password;
}
