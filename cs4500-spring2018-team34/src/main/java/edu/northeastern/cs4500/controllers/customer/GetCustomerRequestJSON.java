package edu.northeastern.cs4500.controllers.customer;

public class GetCustomerRequestJSON {
    private Integer id;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public GetCustomerRequestJSON withId(Integer id) {
        this.setId(id);
        return this;
    }
}
