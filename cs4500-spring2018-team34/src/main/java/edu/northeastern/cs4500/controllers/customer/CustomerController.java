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

    private Integer[] extractExecutorIdAndTargetId(JSONObject response, JSONObject request) {
        Object executor = request.getOrDefault("loggedInUserId", "");
        Object target = request.getOrDefault("userId", "");
        if (executor == null || target == null) {
            response.put("message", "input value cannot be undefined");
            return null;
        }
        String executorStr = executor.toString();
        String targetStr = target.toString();
        if (executorStr.equals("") || targetStr.equals("")) {
            System.out.println(executorStr);
            System.out.println(targetStr);
            response.put("message", "request not complete");
            return null;
        }
        Integer executorId = myParseInt(executorStr);
        Integer targetId = myParseInt(targetStr);
        if (executorId == null || targetId == null) {
            response.put("message", "incorrect id format");
            return null;
        }
        return new Integer[]{executorId, targetId};
    }

    private void processAccessException(JSONObject response, String message) {
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
        }
    }

    @RequestMapping(path = "/api/keepAlive", method = RequestMethod.POST)
    public ResponseEntity<JSONObject> keepAlive(@RequestBody JSONObject request) {
        JSONObject response = new JSONObject();
        Integer[] ids = extractExecutorIdAndTargetId(response, request);
        if (ids == null) {
            return ResponseEntity.badRequest().body(response);
        }
        try {
            customerService.ensureAccess(ids[0], ids[1]);
            response.put("message", "keep alive succeeded");
            return ResponseEntity.ok().body(response);
        } catch (Exception e) {
            String message = e.getMessage();
            processAccessException(response, message);
            return ResponseEntity.badRequest().body(response);
        }
    }

    @RequestMapping(path = "/api/login", method = RequestMethod.POST)
    public ResponseEntity<JSONObject> login(@RequestBody JSONObject request) {
        JSONObject response = new JSONObject();
        Object username = request.getOrDefault("username", "");
        Object password = request.getOrDefault("password", "");
        if (username == null || password == null) {
            response.put("message", "input value cannot be undefined");
            return ResponseEntity.badRequest().body(response);
        }
        String usernameStr = username.toString();
        String passwordStr = password.toString();
        if (usernameStr.equals("") || passwordStr.equals("")) {
            response.put("message", "login request not complete");
            return ResponseEntity.badRequest().body(response);
        }
        try {
            Integer result = customerService.login(usernameStr, passwordStr);
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
        Integer[] ids = extractExecutorIdAndTargetId(response, request);
        if (ids == null) {
            return ResponseEntity.badRequest().body(response);
        }
        try {
            customerService.logout(ids[0], ids[1]);
            response.put("message", "logout succeeded");
            return ResponseEntity.ok().body(response);
        } catch (Exception e) {
            String message = e.getMessage();
            processAccessException(response, message);
            return ResponseEntity.badRequest().body(response);
        }
    }

    @RequestMapping(path = "/api/register", method = RequestMethod.POST)
    public ResponseEntity<JSONObject> register(@RequestBody JSONObject request) {
        JSONObject response = new JSONObject();
        Object username = request.getOrDefault("username", "");
        Object password = request.getOrDefault("password", "");
        Object email = request.getOrDefault("email", "");
        Object phone = request.getOrDefault("phone", "");
        Object code = request.getOrDefault("adminCode", "");
        if (username == null || password == null || email == null || phone == null || code == null) {
            response.put("message", "input value cannot be undefined");
            return ResponseEntity.badRequest().body(response);
        }
        String usernameStr = username.toString();
        String passwordStr = password.toString();
        String emailStr = email.toString();
        String phoneStr = phone.toString(); // optional
        String codeStr = code.toString(); // admin
        if (usernameStr.equals("") || passwordStr.equals("") || emailStr.equals("")) {
            response.put("message", "registration request not complete");
            return ResponseEntity.badRequest().body(response);
        }
        try {
            Integer result = customerService.register(usernameStr, passwordStr, emailStr, phoneStr, codeStr);
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
        Integer[] ids = extractExecutorIdAndTargetId(response, request);
        if (ids == null) {
            return ResponseEntity.badRequest().body(response);
        }
        Object oldPassword = request.getOrDefault("oldPassword", "");
        Object newPassword = request.getOrDefault("newPassword", "");
        if (oldPassword == null || newPassword == null) {
            response.put("message", "input value cannot be undefined");
            return ResponseEntity.badRequest().body(response);
        }
        String oldPasswordStr = oldPassword.toString();
        String newPasswordStr = newPassword.toString();
        if (oldPasswordStr.equals("") || newPasswordStr.equals("")) {
            response.put("message", "password change request not complete");
            return ResponseEntity.badRequest().body(response);
        }
        try {
            customerService.changePassword(ids[0], ids[1], oldPasswordStr, newPasswordStr);
        } catch (Exception e) {
            String message = e.getMessage();
            processAccessException(response, message);
            switch (message) {
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
        json.put("result", array);
        Integer idAsInt = id != null ? myParseInt(id) : null;
        if (id != null && idAsInt == null) {
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
        json.put("message", "results fetched");
        return ResponseEntity.ok().body(json);
    }

    @RequestMapping(path = "/api/updateUser", method = RequestMethod.POST)
    public ResponseEntity<JSONObject> updateCustomer(@RequestBody JSONObject request) {
        JSONObject response = new JSONObject();
        Integer[] ids = extractExecutorIdAndTargetId(response, request);
        if (ids == null) {
            return ResponseEntity.badRequest().body(response);
        }
        request.remove("loggedInUserId");
        request.remove("userId");
        try {
            customerService.updateCustomer(ids[0], ids[1], request);
            response.put("message", "update user succeeded");
            return ResponseEntity.ok().body(response);
        } catch (Exception e) {
            String message = e.getMessage();
            processAccessException(response, message);
            switch (message) {
                case "key":
                    response.put("message", "invalid key-value update");
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
        Integer[] ids = extractExecutorIdAndTargetId(response, request);
        if (ids == null) {
            return ResponseEntity.badRequest().body(response);
        }
        try {
            customerService.deleteCustomer(ids[0], ids[1]);
            response.put("message", "delete user succeeded");
            return ResponseEntity.ok().body(response);
        } catch (Exception e) {
            String message = e.getMessage();
            processAccessException(response, message);
            return ResponseEntity.badRequest().body(response);
        }
    }

    @RequestMapping(path = "/api/user/following/{id}", method = RequestMethod.GET)
    public ResponseEntity<JSONObject> getFollowing(@PathVariable(name = "id") String id) {
        JSONObject json = new JSONObject();
        JSONArray array = new JSONArray();
        json.put("result", array);
        Integer idAsInt = myParseInt(id);
        if (idAsInt == null) {
            json.put("message", "incorrect id format");
            return ResponseEntity.badRequest().body(json);
        }
        List<Customer> result = customerService.getFollowing(idAsInt);
        for (Customer customer : result) {
            JSONObject following = new JSONObject();
            following.put("user_name", customer.getUsername());
            following.put("following", customer.getId());
            array.add(following);
        }
        json.put("message", "results fetched");
        return ResponseEntity.ok().body(json);
    }

    @RequestMapping(path = "/api/user/followers/{id}", method = RequestMethod.GET)
    public ResponseEntity<JSONObject> getFollowers(@PathVariable(name = "id") String id) {
        JSONObject json = new JSONObject();
        JSONArray array = new JSONArray();
        json.put("result", array);
        Integer idAsInt = myParseInt(id);
        if (idAsInt == null) {
            json.put("message", "incorrect id format");
            return ResponseEntity.badRequest().body(json);
        }
        List<Customer> result = customerService.getFollowers(idAsInt);
        for (Customer customer : result) {
            JSONObject follower = new JSONObject();
            follower.put("user_name", customer.getUsername());
            follower.put("follower", customer.getId());
            array.add(follower);
        }
        json.put("message", "results fetched");
        return ResponseEntity.ok().body(json);
    }

    @RequestMapping(path = "/api/user/follow", method = RequestMethod.POST)
    public ResponseEntity<JSONObject> follow(@RequestBody JSONObject request) {
        JSONObject response = new JSONObject();
        Integer[] ids = extractExecutorIdAndFromIdAndToId(response, request);
        if (ids == null) {
            return ResponseEntity.badRequest().body(response);
        }
        try {
            customerService.follow(ids[0], ids[1], ids[2]);
            response.put("message", "follow succeeded");
            return ResponseEntity.ok().body(response);
        } catch (Exception e) {
            String message = e.getMessage();
            processAccessException(response, message);
            return ResponseEntity.badRequest().body(response);
        }
    }

    @RequestMapping(path = "/api/user/un-follow", method = RequestMethod.POST)
    public ResponseEntity<JSONObject> unFollow(@RequestBody JSONObject request) {
        JSONObject response = new JSONObject();
        Integer[] ids = extractExecutorIdAndFromIdAndToId(response, request);
        if (ids == null) {
            return ResponseEntity.badRequest().body(response);
        }
        try {
            customerService.unFollow(ids[0], ids[1], ids[2]);
            response.put("message", "un-follow succeeded");
            return ResponseEntity.ok().body(response);
        } catch (Exception e) {
            String message = e.getMessage();
            processAccessException(response, message);
            return ResponseEntity.badRequest().body(response);
        }
    }

    private Integer[] extractExecutorIdAndFromIdAndToId(JSONObject response, JSONObject request) {
        Object executor = request.getOrDefault("loggedInUserId", "");
        Object from = request.getOrDefault("from", "");
        Object to = request.getOrDefault("to", "");
        if (executor == null || from == null || to == null) {
            response.put("message", "input value cannot be undefined");
            return null;
        }
        String executorStr = executor.toString();
        String fromStr = from.toString();
        String toStr = to.toString();
        if (executorStr.equals("") || fromStr.equals("") || toStr.equals("")) {
            response.put("message", "request not complete");
            return null;
        }
        Integer executorId = myParseInt(executorStr);
        Integer fromId = myParseInt(fromStr);
        Integer toId = myParseInt(toStr);
        if (executorId == null || fromId == null || toId == null) {
            response.put("message", "incorrect id format");
            return null;
        }
        return new Integer[]{executorId, fromId, toId};
    }

}
