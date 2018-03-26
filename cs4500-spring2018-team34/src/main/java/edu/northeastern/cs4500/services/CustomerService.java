package edu.northeastern.cs4500.services;

import edu.northeastern.cs4500.repositories.Customer;
import edu.northeastern.cs4500.repositories.CustomerFollowingRepository;
import edu.northeastern.cs4500.repositories.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class CustomerService {

    private static final Map<Integer, Integer[]> SESSION = new HashMap<>();
    private static final Integer AUTO_LOGOUT = 1800;

    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private CustomerFollowingRepository customerFollowingRepository;

    static {
        Thread thread = new Thread(() -> {
            while (true) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    CustomerService.SESSION.clear();
                    return;
                }
                for (Map.Entry<Integer, Integer[]> session : CustomerService.SESSION.entrySet()) {
                    session.getValue()[1] -= 1;
                    if (session.getValue()[1] <= 0) {
                        CustomerService.SESSION.remove(session.getKey());
                    }
                }
            }
        });
        thread.start();
    }

    public Integer register(String username, String password, String email, String phone, String code) {
        if (customerRepository.existsByUsername(username)) {
            throw new IllegalArgumentException("username");
        }
        //TODO: admin registration
        if (code != null && ! code.equals("")) {
            throw new IllegalArgumentException("code");
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
        Thread thread = new Thread(() -> CustomerEmail.sendEmail(customer.getUsername(), customer.getEmail()));
        thread.start();
        Integer id = customer.getId();
        Integer level = customer.getLevel();
        CustomerService.SESSION.put(id, new Integer[]{level, CustomerService.AUTO_LOGOUT});
        return id;
    }

    public Integer login(String username, String password) {
        List<Customer> result = customerRepository.findByUsername(username);
        if (result.isEmpty()) {
            throw new IllegalArgumentException("username");
        }
        Customer found = result.get(0);
        // TODO: password should be retrieved from a separate table in the future
        if (! found.getPassword().equals(password)) {
            throw new IllegalArgumentException("password");
        }
        found.setLastLogin(new Date());
        customerRepository.save(found);
        Integer id = found.getId();
        Integer level = found.getLevel();
        CustomerService.SESSION.put(id, new Integer[]{level, CustomerService.AUTO_LOGOUT});
        return id;
    }

    public void logout(Integer executor, Integer target) throws Exception {
        internalEnsureAccess(executor, target);
        CustomerService.SESSION.remove(target);
    }

    public void changePassword(Integer executor, Integer target, String oldPassword, String newPassword) throws Exception {
        internalEnsureAccess(executor, target);
        Customer customer = internalFindTheCustomer(target);
        if (! customer.getPassword().equals(oldPassword)) {
            throw new IllegalArgumentException("oldPassword");
        }
        customer.setPassword(newPassword);
        customerRepository.save(customer);
    }

    // TODO
    public void resetPassword() {

    }

    // TODO: to support searching users by email or phone
    // TODO: to support mass-searching users (by a list of id's and/or username's, etc.)
    public List<Object[]> getCustomers(Integer id, String username) {
        List<Object[]> result = new ArrayList<>();
        if (id != null) {
            Customer customer = customerRepository.findById(id);
            Boolean isOnline = CustomerService.SESSION.containsKey(id);
            result.add(new Object[]{customer, isOnline});
        } else if (username != null) {
            List<Customer> customers = customerRepository.findByUsernameLike("%" + username + "%");
            for (Customer customer : customers) {
                Boolean isOnline = CustomerService.SESSION.containsKey(customer.getId());
                result.add(new Object[]{customer, isOnline});
            }
        }
        return result;
    }

    public void updateCustomer(Integer executor, Integer target, Map<String, Object> kwargs) throws Exception {
        internalEnsureAccess(executor, target);
        Customer customer = internalFindTheCustomer(target);
        for (Map.Entry<String, Object> each : kwargs.entrySet()) {
            try {
                String keyword = each.getKey();
                switch (keyword) {
                    case "email":
                        customer.setEmail((String) each.getValue());
                        break;
                    case "phone":
                        customer.setPhone((String) each.getValue());
                        break;
                    case "dob":
                        customer.setDob(new Date((String) each.getValue()));
                        break;
                    case "privacyLevel":
                        customer.setPrivacyLevel(Integer.parseInt((String) each.getValue()));
                        break;
                }
            } catch (Exception e) {
                throw new IllegalArgumentException("value");
            }
        }
        customerRepository.save(customer);
    }

    public void deleteCustomer(Integer executor, Integer target) throws Exception {
        internalEnsureAccess(executor, target);
        customerRepository.delete(target);
        CustomerService.SESSION.remove(target);
    }

    public void internalEnsureAccess(Integer executor, Integer target) throws Exception {
        Integer[] access = CustomerService.SESSION.get(executor);
        if (access == null) {
            throw new IllegalStateException("executor");
        }
        Integer level = access[0];
        if (! executor.equals(target) && ! level.equals(0)) {
            throw new IllegalAccessException("target");
        }
        access[1] = CustomerService.AUTO_LOGOUT;
    }

    public Customer internalFindTheCustomer(Integer id) {
        Customer customer = customerRepository.findById(id);
        if (customer == null) {
            throw new IllegalStateException("id");
        }
        return customer;
    }

}
