package edu.northeastern.cs4500.controllers.customer;

import java.util.List;

public class GetCustomerResponseJSON {
    private List<Customer> customer;
    private String message;

    public List<Customer> getCustomer() {
        return customer;
    }

    public void setCustomer(List<Customer> customer) {
        this.customer = customer;
    }

    public GetCustomerResponseJSON withCustomer(List<Customer> customer) {
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
