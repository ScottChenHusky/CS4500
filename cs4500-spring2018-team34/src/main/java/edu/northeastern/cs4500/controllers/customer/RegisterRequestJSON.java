package edu.northeastern.cs4500.controllers.customer;

public class RegisterRequestJSON {
    private String username;
    private String password;
    private String email;
    private String phone;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public RegisterRequestJSON withUsername(String username) {
        this.setUsername(username);
        return this;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public RegisterRequestJSON withPassword(String password) {
        this.setPassword(password);
        return this;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public RegisterRequestJSON withEmail(String email) {
        this.setEmail(email);
        return this;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public RegisterRequestJSON withPhone(String phone) {
        this.setPhone(phone);
        return this;
    }
}
