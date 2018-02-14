package edu.northeastern.cs4500.controllers.customer;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "customer")
@NamedStoredProcedureQueries({
        @NamedStoredProcedureQuery(name = "customer_info",
                procedureName = "customer_info",
                parameters = {
                        @StoredProcedureParameter(mode = ParameterMode.IN, name = "username", type = String.class)
                })
})
public class CustomerObject {
    @Id
    private Integer id;
    private String username;
    private String password;
    private String email;
    private String phone;
    private Integer score;
    private Date dob;
    private Date createDate;
    private Date lastLogin;
    private Integer level;
    private Integer privacyLevel;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    public Date getDob() {
        return dob;
    }

    public void setDob(Date dob) {
        this.dob = dob;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Date getLastLogin() {
        return lastLogin;
    }

    public void setLastLogin(Date lastLogin) {
        this.lastLogin = lastLogin;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public Integer getPrivacyLevel() {
        return privacyLevel;
    }

    public void setPrivacyLevel(Integer privacyLevel) {
        this.privacyLevel = privacyLevel;
    }
}
