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
    private String description;

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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public CustomerPlaylist withDescription(String description) {
        this.setDescription(description);
        return this;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (! (obj instanceof CustomerPlaylist)) {
            return false;
        }
        CustomerPlaylist that = (CustomerPlaylist) obj;
        return this.id.equals(that.id)
                && this.name.equals(that.name)
                && this.customerId.equals(that.customerId)
                && this.description.equals(that.description);
    }

    @Override
    public int hashCode() {
        return this.id;
    }
}
