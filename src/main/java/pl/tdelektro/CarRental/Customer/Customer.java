package pl.tdelektro.CarRental.Customer;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import org.hibernate.validator.constraints.UniqueElements;

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

    @Column(name = "name", unique = true)
    @NotBlank
    String name;
    String password;
    String emailAddress;
    String phoneNumber;
    Float funds;

    Customer(String emailAddress, String password) {
        this.name = emailAddress;
        this.emailAddress = emailAddress;
        this.password = password;
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

    String getPhoneNumber() {
        return phoneNumber;
    }

    Customer setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
        return this;
    }
}


