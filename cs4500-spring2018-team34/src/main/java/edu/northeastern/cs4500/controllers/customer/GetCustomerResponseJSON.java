package edu.northeastern.cs4500.controllers.customer;

public class GetCustomerResponseJSON {
    private Customer customer;
    private String message;

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public GetCustomerResponseJSON withCustomer(Customer customer) {
        this.setCustomer(customer);
        return this;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public GetCustomerResponseJSON withMessage(String message) {
        this.setMessage(message);
        return this;
    }
}
