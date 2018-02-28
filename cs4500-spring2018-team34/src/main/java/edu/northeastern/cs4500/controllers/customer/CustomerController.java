package java.edu.northeastern.cs4500.controllers.customer;

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
        Date now = new Date();
        Customer customer = new Customer()
                .withUsername(request.getUsername())
                .withPassword(request.getPassword())
                .withEmail(request.getEmail())
                .withPhone(request.getPhone())
                .withCreateDate(now)
                .withLastLogin(now)
                .withPrivacyLevel(1)
                .withLevel(1)
                .withScore(0);
        customerRepository.save(customer);
        return ResponseEntity.ok().body(
                new RegisterResponseJSON()
                        .withId(customer.getId())
                        .withMessage("user created")
        );
    }

    @RequestMapping(path = "/api/user", method = RequestMethod.GET)
    public ResponseEntity<GetCustomerResponseJSON> getCustomer(
            @RequestParam(name = "id", required = false) String id,
            @RequestParam(name = "username", required = false) String username) {
        List<Customer> result = new ArrayList<>();
        if (id != null) {
            result.add(customerRepository.findById(Integer.parseInt(id)));
        } else if (username != null) {
            result.addAll(customerRepository.findByUsernameLike("%" + username + "%"));
        }
        return ResponseEntity.ok().body(
                new GetCustomerResponseJSON()
                        .withResult(result)
                        .withMessage("results fetched")
        );
    }

}
