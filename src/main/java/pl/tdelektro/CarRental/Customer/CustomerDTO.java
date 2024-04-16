package pl.tdelektro.CarRental.Customer;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class CustomerDTO {

    @JsonIgnore
    private int id;
    @JsonIgnore
    String name;
    @JsonIgnore
    String password;
    String emailAddress;
    @JsonIgnore
    Float funds;
    @JsonIgnore
    String token;

    CustomerDTO(Customer customer) {
        this.name = customer.getName();
        this.emailAddress = customer.getEmailAddress();
        this.funds = customer.getFunds();
    }

    //Constructor for test class, because Customer class is private
    public CustomerDTO(String name, String emailAddress, Float funds) {
        this.name = name;
        this.emailAddress = emailAddress;
        this.funds = funds;
    }

    String getToken() {
        return token;
    }

    CustomerDTO setToken(String token) {
        this.token = token;
        return this;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public CustomerDTO setName(String name) {
        this.name = name;
        return this;
    }

    public String getPassword() {
        return password;
    }

    public CustomerDTO setPassword(String password) {
        this.password = password;
        return this;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public CustomerDTO setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
        return this;
    }

    public Float getFunds() {
        return funds;
    }

    public CustomerDTO setFunds(Float funds) {
        this.funds = funds;
        return this;
    }
}
