package edu.northeastern.cs4500.controllers.customer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


import java.util.Date;
import java.util.Optional;

@RestController
public class CustomerController{

    @Autowired
    private CustomerRepository customerRepository;

    @RequestMapping(path = "/api/login", method = RequestMethod.POST)
    public @ResponseBody LoginResponseJSON login(@RequestBody LoginRequestJSON login) {
        Optional<Customer> result = customerRepository.findByUsernameAndPassword(
                login.getUsername(), login.getPassword()
        );
        if (result.isPresent()) {
            return new LoginResponseJSON()
                    .withId(result.get().getId())
                    .withMessage("user successfully retrieved");
        } else {
            return new LoginResponseJSON()
                    .withId(-1)
                    .withMessage("username password pair not found");
        }
    }

    @RequestMapping(path = "/api/register", method = RequestMethod.POST)
    public @ResponseBody RegisterResponseJSON register(@RequestBody RegisterRequestJSON register) {
        if (customerRepository.existsByUsername(register.getUsername())) {
            return new RegisterResponseJSON()
                    .withId(-1)
                    .withMessage("username already exists");
        }
        Customer customer = new Customer()
                .withUsername(register.getUsername())
                .withPassword(register.getPassword())
                .withEmail(register.getEmail())
                .withPhone(register.getPhone())
                .withCreateDate(new Date());
        customerRepository.save(customer);
        return new RegisterResponseJSON()
                .withId(customer.getId())
                .withMessage("user successfully created");
    }

}
