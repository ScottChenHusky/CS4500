package edu.northeastern.cs4500.controllers.customer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.Date;
import java.util.List;

@RestController
public class CustomerController{

    @Autowired
    private CustomerRepository customerRepository;

    @RequestMapping(path = "/api/login", method = RequestMethod.POST)
    public ResponseEntity<LoginResponseJSON> login(@RequestBody LoginRequestJSON request) {
        List<Customer> result = customerRepository.findByUsernameAndPassword(
                request.getUsername(), request.getPassword()
        );
        if (result.isEmpty()) {
            return ResponseEntity.badRequest().body(
                    new LoginResponseJSON()
                            .withMessage("credential not found")
            );
        } else {
            return ResponseEntity.ok().body(
                   new LoginResponseJSON()
                            .withId(result.get(0).getId())
                            .withMessage("credential found")
            );
        }
    }

    @RequestMapping(path = "/api/register", method = RequestMethod.POST)
    public ResponseEntity<RegisterResponseJSON> register(@RequestBody RegisterRequestJSON request) {
        if (customerRepository.existsByUsername(request.getUsername())) {
            return ResponseEntity.badRequest().body(
                    new RegisterResponseJSON()
                            .withMessage("username already exists")
            );
        }
        Customer customer = new Customer()
                .withUsername(request.getUsername())
                .withPassword(request.getPassword())
                .withEmail(request.getEmail())
                .withPhone(request.getPhone())
                .withCreateDate(new Date());
        customerRepository.save(customer);
        return ResponseEntity.ok().body(
                new RegisterResponseJSON()
                        .withId(customer.getId())
                        .withMessage("user created")
        );
    }

    @RequestMapping(path = "/api/get-user/{id}", method = RequestMethod.GET)
    public ResponseEntity<GetCustomerResponseJSON> getCustomer(@PathVariable("id") String id) {
        Customer customer = customerRepository.findById(Integer.parseInt(id));
        if (customer == null) {
            return ResponseEntity.badRequest().body(
                    new GetCustomerResponseJSON()
                            .withMessage("user not found")
            );
        } else {
            return ResponseEntity.ok().body(
                    new GetCustomerResponseJSON()
                            .withCustomer(customer)
                            .withMessage("user found")
            );
        }
    }

}
