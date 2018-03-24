package edu.northeastern.cs4500.controllers.customer;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RestController
public class CustomerController{

    @Autowired
    private CustomerRepository customerRepository;

    @RequestMapping(path = "/api/login", method = RequestMethod.POST)
    public ResponseEntity<JSONObject> login(@RequestBody JSONObject request) {
        String username = request.getOrDefault("username", "").toString();
        String password = request.getOrDefault("password", "").toString();
        List<Customer> result = customerRepository.findByUsernameAndPassword(username, password);
        JSONObject response = new JSONObject();
        if (result.isEmpty()) {
            response.put("message", "credential not found");
            return ResponseEntity.badRequest().body(response);
        } else {
            response.put("id", result.get(0).getId());
            response.put("message", "credential found");
            return ResponseEntity.ok().body(response);
        }
    }

    @RequestMapping(path = "/api/register", method = RequestMethod.POST)
    public ResponseEntity<JSONObject> register(@RequestBody JSONObject request) {
        String username = request.getOrDefault("username", "").toString();
        String password = request.getOrDefault("password", "").toString();
        String email = request.getOrDefault("email", "").toString();
        String phone = request.getOrDefault("phone", "").toString();
        JSONObject response = new JSONObject();
        if (customerRepository.existsByUsername(username)) {
            response.put("message", "username already exists");
            return ResponseEntity.badRequest().body(response);
        }
        Date now = new Date();
        Customer customer = new Customer()
                .withUsername(username)
                .withPassword(password)
                .withEmail(email)
                .withPhone(phone)
                .withCreateDate(now)
                .withLastLogin(now)
                .withPrivacyLevel(1)
                .withLevel(1)
                .withScore(0);
        customerRepository.save(customer);
        CustomerEmail.sendEmail(customer.getUsername(), customer.getEmail());
        // CustomerPhoneNumber.sendCodeToPhone(customer.getPhone());
        response.put("id", customer.getId());
        response.put("message", "user created");
        return ResponseEntity.ok().body(response);
    }

    @RequestMapping(path = "/api/user", method = RequestMethod.GET)
    public ResponseEntity<JSONObject> getCustomer(
            @RequestParam(name = "id", required = false) String id,
            @RequestParam(name = "username", required = false) String username) {
        JSONObject json = new JSONObject();
        JSONArray array = new JSONArray();
        List<Customer> result = new ArrayList<>();
        if (id != null) {
            result.add(customerRepository.findById(Integer.parseInt(id)));
        } else if (username != null) {
            result.addAll(customerRepository.findByUsernameLike("%" + username + "%"));
        }
        for (Customer each : result) {
            JSONObject object = new JSONObject();
            object.put("id", each.getId());
            object.put("username", each.getUsername());
            object.put("email", each.getEmail());
            object.put("phone", each.getPhone());
            object.put("dob", each.getDob() == null ? null : each.getDob().toString());
            object.put("level", each.getLevel());
            object.put("create_date", each.getCreateDate().toString());
            object.put("last_login", each.getLastLogin().toString());
            object.put("privacy_level", each.getPrivacyLevel());
            object.put("score", each.getScore());
            array.add(object);
        }
        json.put("result", array);
        json.put("message", "results fetched");
        return ResponseEntity.ok().body(json);
    }

}
