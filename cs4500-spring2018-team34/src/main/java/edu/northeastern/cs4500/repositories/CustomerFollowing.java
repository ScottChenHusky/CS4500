package edu.northeastern.cs4500.repositories;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Entity
@Table(name = "customer_followlist")
public class CustomerFollowing {
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

    public CustomerFollowing withId(Integer id) {
        this.setId(id);
        return this;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public CustomerFollowing withDate(Date date) {
        this.setDate(date);
        return this;
    }

    public Integer getCustomerFromId() {
        return customerFromId;
    }

    public void setCustomerFromId(Integer customerFromId) {
        this.customerFromId = customerFromId;
    }

    public CustomerFollowing withCustomerFromId(Integer customerFromId) {
        this.setCustomerFromId(customerFromId);
        return this;
    }

    public Integer getCustomerToId() {
        return customerToId;
    }

    public void setCustomerToId(Integer customerToId) {
        this.customerToId = customerToId;
    }

    public CustomerFollowing withCustomerToId(Integer customerToId) {
        this.setCustomerToId(customerToId);
        return this;
    }
}
