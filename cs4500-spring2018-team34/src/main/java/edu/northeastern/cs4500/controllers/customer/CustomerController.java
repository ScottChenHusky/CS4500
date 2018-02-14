package edu.northeastern.cs4500.controllers.customer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CustomerController{

    @Autowired
    CustomerRepository customerRepository;

    @RequestMapping(path = "/api/login", method = RequestMethod.POST)
    public String login(@RequestBody LoginJSON login) {


        // This version shows how a param can go in an be returned from a stored procedure
        String inParam = null;
        System.out.println(inParam);
        String result = customerRepository.customer_info(inParam);

        System.out.println(result);
        return null;
    }

    @RequestMapping(path = "/api/register", method = RequestMethod.POST)
    public String register(@RequestBody RegisterJSON register) {

        return null;
    }

}
