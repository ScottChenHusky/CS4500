package edu.northeastern.cs4500.controllers.customer;

import edu.northeastern.cs4500.repositories.Customer;
import edu.northeastern.cs4500.services.CustomerService;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class CustomerController{

    @Autowired
    private CustomerService customerService;

    private Integer myParseInt(String s) {
        try {
            return Integer.parseInt(s);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    @RequestMapping(path = "/api/keepAlive", method = RequestMethod.POST)
    public ResponseEntity<JSONObject> keepAlive(@RequestBody JSONObject request) {
        JSONObject response = new JSONObject();
        String executor = request.getOrDefault("loggedInUserId", "").toString();
        String target = request.getOrDefault("userId", "").toString();
        if (executor.equals("") || target.equals("")) {
            response.put("message", "logout request not complete");
            return ResponseEntity.badRequest().body(response);
        }
        Integer executorId = myParseInt(executor);
        Integer targetId = myParseInt(target);
        if (executorId == null || targetId == null) {
            response.put("message", "incorrect id format");
            return ResponseEntity.badRequest().body(response);
        }
        try {
            customerService.internalEnsureAccess(executorId, targetId);
            response.put("message", "keep alive succeeded");
            return ResponseEntity.ok().body(response);
        } catch (Exception e) {
            String message = e.getMessage();
            switch (message) {
                case "executor":
                    response.put("message", "loggedInUserId not logged in");
                    break;
                case "target":
                    response.put("message", "userId access denied");
                    break;
                default:
                    response.put("message", "unknown error");
                    break;
            }
            return ResponseEntity.badRequest().body(response);
        }
    }

    @RequestMapping(path = "/api/login", method = RequestMethod.POST)
    public ResponseEntity<JSONObject> login(@RequestBody JSONObject request) {
        JSONObject response = new JSONObject();
        String username = request.getOrDefault("username", "").toString();
        String password = request.getOrDefault("password", "").toString();
        if (username.equals("") || password.equals("")) {
            response.put("message", "login request not complete");
            return ResponseEntity.badRequest().body(response);
        }
        try {
            Integer result = customerService.login(username, password);
            response.put("id", result);
            response.put("message", "login succeeded");
            return ResponseEntity.ok().body(response);
        } catch (IllegalArgumentException e) {
            String message = e.getMessage();
            switch (message) {
                case "username":
                    response.put("message", "username not found");
                    break;
                case "password":
                    response.put("message", "password not matched");
                    break;
                default:
                    response.put("message", "unknown error");
                    break;
            }
            return ResponseEntity.badRequest().body(response);
        }
    }

    @RequestMapping(path = "/api/logout", method = RequestMethod.POST)
    public ResponseEntity<JSONObject> logout(@RequestBody JSONObject request) {
        JSONObject response = new JSONObject();
        String executor = request.getOrDefault("loggedInUserId", "").toString();
        String target = request.getOrDefault("userId", "").toString();
        if (executor.equals("") || target.equals("")) {
            response.put("message", "logout request not complete");
            return ResponseEntity.badRequest().body(response);
        }
        Integer executorId = myParseInt(executor);
        Integer targetId = myParseInt(target);
        if (executorId == null || targetId == null) {
            response.put("message", "incorrect id format");
            return ResponseEntity.badRequest().body(response);
        }
        try {
            customerService.logout(executorId, targetId);
            response.put("message", "logout succeeded");
            return ResponseEntity.ok().body(response);
        } catch (Exception e) {
            String message = e.getMessage();
            switch (message) {
                case "executor":
                    response.put("message", "loggedInUserId not logged in");
                    break;
                case "target":
                    response.put("message", "userId access denied");
                    break;
                default:
                    response.put("message", "unknown error");
                    break;
            }
            return ResponseEntity.badRequest().body(response);
        }
    }

    @RequestMapping(path = "/api/register", method = RequestMethod.POST)
    public ResponseEntity<JSONObject> register(@RequestBody JSONObject request) {
        JSONObject response = new JSONObject();
        String username = request.getOrDefault("username", "").toString();
        String password = request.getOrDefault("password", "").toString();
        String email = request.getOrDefault("email", "").toString();
        String phone = request.getOrDefault("phone", "").toString(); // optional
        String code = request.getOrDefault("adminCode", "").toString(); // admin
        if (username.equals("") || password.equals("") || email.equals("")) {
            response.put("message", "registration request not complete");
            return ResponseEntity.badRequest().body(response);
        }
        try {
            Integer result = customerService.register(username, password, email, phone, code);
            response.put("id", result);
            response.put("message", "registration succeeded");
            return ResponseEntity.ok().body(response);
        } catch (Exception e) {
            String message = e.getMessage();
            switch (message) {
                case "username":
                    response.put("message", "username already exists");
                    break;
                case "code":
                    //TODO: admin registration
                    response.put("message", "admin registration currently not supported");
                    break;
                default:
                    response.put("message", "unknown error");
                    break;
            }
            return ResponseEntity.badRequest().body(response);
        }
    }

    @RequestMapping(path = "/api/updateUserPassword", method = RequestMethod.POST)
    public ResponseEntity<JSONObject> changePassword(@RequestBody JSONObject request) {
        JSONObject response = new JSONObject();
        String executor = request.getOrDefault("loggedInUserId", "").toString();
        String target = request.getOrDefault("userId", "").toString();
        String oldPassword = request.getOrDefault("oldPassword", "").toString();
        String newPassword = request.getOrDefault("newPassword", "").toString();
        if (executor.equals("") || target.equals("") || oldPassword.equals("") || newPassword.equals("")) {
            response.put("message", "password change request not complete");
            return ResponseEntity.badRequest().body(response);
        }
        Integer executorId = myParseInt(executor);
        Integer targetId = myParseInt(target);
        if (executorId == null || targetId == null) {
            response.put("message", "incorrect id format");
            return ResponseEntity.badRequest().body(response);
        }
        try {
            customerService.changePassword(executorId, targetId, oldPassword, newPassword);
        } catch (Exception e) {
            String message = e.getMessage();
            switch (message) {
                case "executor":
                    response.put("message", "loggedInUserId not logged in");
                    break;
                case "target":
                    response.put("message", "userId access denied");
                    break;
                case "id":
                    response.put("message", "userId not found");
                    break;
                case "oldPassword":
                    response.put("message", "old password not matched");
                    break;
                default:
                    response.put("message", "unknown error");
                    break;
            }
            return ResponseEntity.badRequest().body(response);
        }
        response.put("message", "password changed");
        return ResponseEntity.ok().body(response);
    }

    @RequestMapping(path = "/api/user", method = RequestMethod.GET)
    public ResponseEntity<JSONObject> getCustomers(
            @RequestParam(name = "id", required = false) String id,
            @RequestParam(name = "username", required = false) String username) {
        JSONObject json = new JSONObject();
        JSONArray array = new JSONArray();
        Integer idAsInt = id != null ? myParseInt(id) : null;
        if (id != null && idAsInt == null) {
            json.put("result", array);
            json.put("message", "incorrect id format");
            return ResponseEntity.badRequest().body(json);
        }
        List<Object[]> result = customerService.getCustomers(idAsInt, username);
        for (Object[] each : result) {
            Customer customer = (Customer) each[0];
            Boolean isOnline = (Boolean) each[1];
            JSONObject object = new JSONObject();
            object.put("id", customer.getId());
            object.put("username", customer.getUsername());
            object.put("email", customer.getEmail());
            object.put("phone", customer.getPhone());
            object.put("dob", customer.getDob() == null ? null : customer.getDob().toString());
            object.put("level", customer.getLevel());
            object.put("createDate", customer.getCreateDate().toString());
            object.put("lastLogin", customer.getLastLogin().toString());
            object.put("privacyLevel", customer.getPrivacyLevel());
            object.put("score", customer.getScore());
            object.put("isOnline", isOnline);
            array.add(object);
        }
        json.put("result", array);
        json.put("message", "results fetched");
        return ResponseEntity.ok().body(json);
    }

    @RequestMapping(path = "/api/updateUser", method = RequestMethod.POST)
    public ResponseEntity<JSONObject> updateCustomer(@RequestBody JSONObject request) {
        JSONObject response = new JSONObject();
        String executor = request.getOrDefault("loggedInUserId", "").toString();
        String target = request.getOrDefault("userId", "").toString();
        if (executor.equals("") || target.equals("")) {
            response.put("message", "delete user request not complete");
            return ResponseEntity.badRequest().body(response);
        }
        Integer executorId = myParseInt(executor);
        Integer targetId = myParseInt(target);
        if (executorId == null || targetId == null) {
            response.put("message", "incorrect id format");
            return ResponseEntity.badRequest().body(response);
        }
        request.remove("loggedInUserId");
        request.remove("userId");
        try {
            customerService.updateCustomer(executorId, targetId, request);
            response.put("message", "update user succeeded");
            return ResponseEntity.ok().body(response);
        } catch (Exception e) {
            String message = e.getMessage();
            switch (message) {
                case "executor":
                    response.put("message", "loggedInUserId not logged in");
                    break;
                case "target":
                    response.put("message", "userId access denied");
                    break;
                case "id":
                    response.put("message", "userId not found");
                    break;
                case "value":
                    response.put("message", "value of invalid type exists");
                    break;
                default:
                    response.put("message", "unknown error");
                    break;
            }
            return ResponseEntity.badRequest().body(response);
        }
    }

    @RequestMapping(path = "/api/deleteUser", method = RequestMethod.POST)
    public ResponseEntity<JSONObject> deleteCustomer(@RequestBody JSONObject request) {
        JSONObject response = new JSONObject();
        String executor = request.getOrDefault("loggedInUserId", "").toString();
        String target = request.getOrDefault("userId", "").toString();
        if (executor.equals("") || target.equals("")) {
            response.put("message", "delete user request not complete");
            return ResponseEntity.badRequest().body(response);
        }
        Integer executorId = myParseInt(executor);
        Integer targetId = myParseInt(target);
        if (executorId == null || targetId == null) {
            response.put("message", "incorrect id format");
            return ResponseEntity.badRequest().body(response);
        }
        try {
            customerService.deleteCustomer(executorId, targetId);
            response.put("message", "delete user succeeded");
            return ResponseEntity.ok().body(response);
        } catch (Exception e) {
            String message = e.getMessage();
            switch (message) {
                case "executor":
                    response.put("message", "loggedInUserId not logged in");
                    break;
                case "target":
                    response.put("message", "userId access denied");
                    break;
                default:
                    response.put("message", "unknown error");
                    break;
            }
            return ResponseEntity.badRequest().body(response);
        }
    }

}
