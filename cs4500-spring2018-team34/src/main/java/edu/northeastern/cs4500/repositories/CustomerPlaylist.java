package edu.northeastern.cs4500.repositories;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "customer_playlist")
public class CustomerPlaylist {
    @Id
    @GeneratedValue
    private Integer id;
    private String name;
    private Integer customerId;
    private Integer movieId;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public CustomerPlaylist withId(Integer id) {
        this.setId(id);
        return this;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public CustomerPlaylist withName(String name) {
        this.setName(name);
        return this;
    }

    public Integer getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Integer customerId) {
        this.customerId = customerId;
    }

    public CustomerPlaylist withCustomerId(Integer customerId) {
        this.setCustomerId(customerId);
        return this;
    }

    public Integer getMovieId() {
        return movieId;
    }

    public void setMovieId(Integer movieId) {
        this.movieId = movieId;
    }

    public CustomerPlaylist withMovieId(Integer movieId) {
        this.setMovieId(movieId);
        return this;
    }
}
