package pl.tdelektro.CarRental.Customer;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Entity
@Table(name = "customer")
@Builder
@AllArgsConstructor
@NoArgsConstructor
class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    @JdbcTypeCode(SqlTypes.INTEGER)
    private int id;

    String name;
    String password;
    String emailAddress;
    Float funds;

    Customer(String name, String password, String emailAddress) {
        this.name = name;
        this.password = password;
        this.emailAddress = emailAddress;
    }

    String getPassword() {
        return password;
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
}


