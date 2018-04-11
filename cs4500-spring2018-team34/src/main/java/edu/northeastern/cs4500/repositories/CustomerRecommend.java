package edu.northeastern.cs4500.repositories;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Entity
@Table(name = "customer_recommend")
public class CustomerRecommend {
    @Id
    @GeneratedValue
    private Integer id;
    private Integer customerFrom;
    private Integer customerTo;
    private Integer movieId;
    private Date createDate;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public CustomerRecommend withId(Integer id) {
        this.setId(id);
        return this;
    }

    public Integer getCustomerFrom() {
        return customerFrom;
    }

    public void setCustomerFrom(Integer customerFrom) {
        this.customerFrom = customerFrom;
    }

    public CustomerRecommend withCustomerFrom(Integer customerFrom) {
        this.setCustomerFrom(customerFrom);
        return this;
    }

    public Integer getCustomerTo() {
        return customerTo;
    }

    public void setCustomerTo(Integer customerTo) {
        this.customerTo = customerTo;
    }

    public CustomerRecommend withCustomerTo(Integer customerTo) {
        this.setCustomerTo(customerTo);
        return this;
    }

    public Integer getMovieId() {
        return movieId;
    }

    public void setMovieId(Integer movieId) {
        this.movieId = movieId;
    }

    public CustomerRecommend withMovieId(Integer movieId) {
        this.setMovieId(movieId);
        return this;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public CustomerRecommend withCreateDate(Date createDate) {
        this.setCreateDate(createDate);
        return this;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (! (obj instanceof CustomerRecommend)) {
            return false;
        }
        CustomerRecommend that = (CustomerRecommend) obj;
        return this.id.equals(that.id)
                && this.customerFrom.equals(that.customerFrom)
                && this.customerTo.equals(that.customerTo)
                && this.movieId.equals(that.movieId)
                && this.createDate.equals(that.createDate);
    }

    @Override
    public int hashCode() {
        return this.id;
    }
}
