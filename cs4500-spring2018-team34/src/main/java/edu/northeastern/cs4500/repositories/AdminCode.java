package edu.northeastern.cs4500.repositories;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "admin_code")
public class AdminCode {

    @Id
    private String code;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public AdminCode withCode(String code) {
        this.setCode(code);
        return this;
    }
}
