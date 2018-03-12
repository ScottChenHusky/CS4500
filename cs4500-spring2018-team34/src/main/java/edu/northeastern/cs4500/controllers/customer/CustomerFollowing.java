package edu.northeastern.cs4500.controllers.customer;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.Date;

@Entity(name = "customer-followlist")
public class CustomerFollow {
    @Id
    @GeneratedValue
    private Integer id;
    private Date date;
    private Integer customerFromId;
    private Integer customerToId;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public CustomerFollow withId(Integer id) {
        this.setId(id);
        return this;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Integer getCustomerFromId() {
        return customerFromId;
    }

    public void setCustomerFromId(Integer customerFromId) {
        this.customerFromId = customerFromId;
    }

    public CustomerFollow withCustomerFromId(Integer customerFromId) {
        this.setCustomerFromId(customerFromId);
        return this;
    }

    public Integer getCustomerToId() {
        return customerToId;
    }

    public void setCustomerToId(Integer customerToId) {
        this.customerToId = customerToId;
    }

    public CustomerFollow withCustomerToId(Integer customerToId) {
        this.setCustomerToId(customerToId);
        return this;
    }
}
