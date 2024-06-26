package pl.tdelektro.CarRental.Customer;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Entity
@Table(name = "customer")
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
class Customer implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    @JdbcTypeCode(SqlTypes.INTEGER)
    private Integer id;

    @Column(name = "name", unique = true)
    @NotBlank
    private String name;
    private String password;
    @Email
    private String emailAddress;
    private String phoneNumber;
    private String role;
    private Collection<? extends GrantedAuthority> authorities;
    private Float funds;

public Customer(String emailAddress, String password, String role) {
        this.name = emailAddress;
        this.emailAddress = emailAddress;
        this.password = password;
        this.role = role;
        List<GrantedAuthority> auths = new ArrayList<>();
        auths.add(new SimpleGrantedAuthority(role));
        this.authorities = auths;
        this.funds = 0.0f;
    }

    public Customer setAuthorities(Collection<? extends GrantedAuthority> authorities) {
        this.authorities = authorities;
        return this;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role));
    }

    @Override
    public String getPassword() {
        return password;
    }


    @Override
    public String getUsername() {
        return emailAddress;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    Customer setPassword(String password) {
        this.password = password;
        return this;
    }

    String getEmailAddress() {
        return emailAddress;
    }

    Customer setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
        return this;
    }

    Float getFunds() {
        return funds;
    }

    Customer setFunds(Float funds) {
        this.funds = funds;
        return this;
    }

    String getName() {
        return name;
    }

    Customer setName(String name) {
        this.name = name;
        return this;
    }

    String getPhoneNumber() {
        return phoneNumber;
    }

    Customer setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
        return this;
    }
}


