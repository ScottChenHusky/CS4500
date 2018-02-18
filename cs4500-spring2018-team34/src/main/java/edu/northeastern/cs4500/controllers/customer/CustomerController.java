package edu.northeastern.cs4500.controllers.customer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import edu.northeastern.cs4500.controllers.hello.HelloObject;

import java.util.List;

@RestController
public class CustomerController{

    @Autowired
    CustomerRepository customerRepository;

    @RequestMapping(path = "/api/login", method = RequestMethod.POST)
    public List<CustomerObject> login(@RequestBody LoginJSON login) {
        // This version shows how a param can go in an be returned from a stored procedure
        String inParam = login.getUsername();
        System.out.println(inParam);
        List<CustomerObject> hellos = (List<CustomerObject>)customerRepository.findAll();
        System.out.println(hellos);
        return hellos;
    }

    @RequestMapping(path = "/api/register", method = RequestMethod.POST)
    public String register(@RequestBody RegisterJSON register) {

        return "123";
    }

}
