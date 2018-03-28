package edu.northeastern.cs4500.services;

import edu.northeastern.cs4500.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.*;

@Service
public class CustomerService {

    private static final Map<Integer, Integer[]> SESSION = new HashMap<>();
    private static final Integer AUTO_LOGOUT_TIME = 1800;

    private static final Map<String, Object[]> ADMIN_REGISTRATION = new HashMap<>();
    private static final Integer CODE_EXPIRE_TIME = 600;

    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private CustomerFollowingRepository customerFollowingRepository;
    @Autowired
    private AdminCodeRepository adminCodeRepository;

    @PostConstruct
    private void background() {
        new Thread(() -> {
            while (true) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    CustomerService.SESSION.clear();
                    List<AdminCode> codes = new ArrayList<>();
                    for (Object[] registration : CustomerService.ADMIN_REGISTRATION.values()) {
                        codes.add((AdminCode) registration[0]);
                    }
                    CustomerService.ADMIN_REGISTRATION.clear();
                    if (! codes.isEmpty()) {
                        adminCodeRepository.save(codes);
                    }
                    Thread.currentThread().interrupt();
                }
                for (Map.Entry<Integer, Integer[]> session : CustomerService.SESSION.entrySet()) {
                    session.getValue()[1] -= 1;
                    if (session.getValue()[1] <= 0) {
                        CustomerService.SESSION.remove(session.getKey());
                    }
                }
                List<AdminCode> codes = new ArrayList<>();
                for (Map.Entry<String, Object[]> adminRegistration : CustomerService.ADMIN_REGISTRATION.entrySet()) {
                    Integer remaining = (Integer) adminRegistration.getValue()[1] - 1;
                    adminRegistration.getValue()[1] = remaining;
                    if (remaining <= 0) {
                        codes.add((AdminCode) adminRegistration.getValue()[0]);
                        CustomerService.ADMIN_REGISTRATION.remove(adminRegistration.getKey());
                    }
                }
                if (! codes.isEmpty()) {
                    adminCodeRepository.save(codes);
                }
            }
        }).start();
    }

    public void applyAdminCode(String username, String email, String phone) {
        if (customerRepository.existsByUsername(username)) {
            throw new IllegalArgumentException("username");
        }
        AdminCode adminCode = adminCodeRepository.selectRandom();
        if (adminCode == null) {
            throw new IllegalStateException("code");
        }
        Object[] current = CustomerService.ADMIN_REGISTRATION.get(username);
        CustomerService.ADMIN_REGISTRATION.put(username, new Object[]{adminCode, CustomerService.CODE_EXPIRE_TIME});
        adminCodeRepository.delete(adminCode);
        CustomerEmail.sendAdminCodeEmail(email, username, adminCode.getCode());
        if (current != null) {
            adminCodeRepository.save((AdminCode) current[0]);
        }
    }

    public Integer register(String username, String password, String email, String phone, String code) {
        if (customerRepository.existsByUsername(username)) {
            throw new IllegalArgumentException("username");
        }
        Integer level = 1;
        if (code != null && ! code.equals("")) {
            Object[] applied = CustomerService.ADMIN_REGISTRATION.get(username);
            AdminCode adminCode = applied == null ? null : (AdminCode) applied[0];
            if (adminCode == null || ! adminCode.getCode().equals(code)) {
                throw new IllegalArgumentException("code");
            }
            CustomerService.ADMIN_REGISTRATION.remove(username);
            adminCodeRepository.save(adminCode); // can we re-use the code in the future?
            level = 0;
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
                .withLevel(level)
                .withScore(0);
        customerRepository.save(customer);
        CustomerEmail.sendRegistrationEmail(customer.getEmail(), customer.getUsername());
        Integer id = customer.getId();
        CustomerService.SESSION.put(id, new Integer[]{level, CustomerService.AUTO_LOGOUT_TIME});
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
        CustomerService.SESSION.put(id, new Integer[]{level, CustomerService.AUTO_LOGOUT_TIME});
        return id;
    }

    public void logout(Integer executor, Integer target) throws Exception {
        ensureAccess(executor, target);
        CustomerService.SESSION.remove(target);
    }

    public void changePassword(Integer executor, Integer target, String oldPassword, String newPassword) throws Exception {
        ensureAccess(executor, target);
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
            if (customer != null) {
                Boolean isOnline = CustomerService.SESSION.containsKey(id);
                result.add(new Object[]{customer, isOnline});
            }
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
        ensureAccess(executor, target);
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
                    default:
                        throw new IllegalArgumentException("key");
                }
            } catch (Exception e) {
                if (e.getMessage().equals("key")) {
                    throw e;
                } else {
                    throw new IllegalArgumentException("value");
                }
            }
        }
        customerRepository.save(customer);
    }

    public void deleteCustomer(Integer executor, Integer target) throws Exception {
        ensureAccess(executor, target);
        customerRepository.delete(target);
        CustomerService.SESSION.remove(target);
    }

    public void ensureAccess(Integer executor, Integer target) throws Exception {
        Integer[] access = CustomerService.SESSION.get(executor);
        if (access == null) {
            throw new IllegalStateException("executor");
        }
        Integer level = access[0];
        if (! executor.equals(target) && ! level.equals(0)) {
            throw new IllegalAccessException("target");
        }
        access[1] = CustomerService.AUTO_LOGOUT_TIME;
    }

    private Customer internalFindTheCustomer(Integer id) {
        Customer customer = customerRepository.findById(id);
        if (customer == null) {
            throw new IllegalStateException("id");
        }
        return customer;
    }

    public List<Customer> getFollowing(Integer id) {
        List<CustomerFollowing> temp = customerFollowingRepository.findByCustomerFromId(id);
        List<Integer> ids = new ArrayList<>();
        for (CustomerFollowing following : temp) {
            ids.add(following.getCustomerToId());
        }
        return customerRepository.findByIdIn(ids);
    }

    public List<Customer> getFollowers(Integer id) {
        List<CustomerFollowing> temp = customerFollowingRepository.findByCustomerToId(id);
        List<Integer> ids = new ArrayList<>();
        for (CustomerFollowing following : temp) {
            ids.add(following.getCustomerFromId());
        }
        return customerRepository.findByIdIn(ids);
    }

    public void follow(Integer executor, Integer from, Integer to) throws Exception {
        ensureAccess(executor, from);
        List<CustomerFollowing> result = customerFollowingRepository.findByCustomerFromIdAndCustomerToId(from, to);
        if (result.isEmpty()) {
            CustomerFollowing following = new CustomerFollowing()
                    .withCustomerFromId(from)
                    .withCustomerToId(to)
                    .withDate(new Date());
            customerFollowingRepository.save(following);
        }
    }

    public void unFollow(Integer executor, Integer from, Integer to) throws Exception {
        ensureAccess(executor, from);
        List<CustomerFollowing> result = customerFollowingRepository.findByCustomerFromIdAndCustomerToId(from, to);
        if (! result.isEmpty()) {
            customerFollowingRepository.delete(result.get(0).getId());
        }
    }

}
