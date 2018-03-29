package edu.northeastern.cs4500.controllers.customer;

import edu.northeastern.cs4500.repositories.Customer;
import edu.northeastern.cs4500.services.CustomerService;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.logging.Logger;

@RestController
public class CustomerController{

    private static final Logger log = Logger.getLogger("CustomerController");

    @Autowired
    private CustomerService customerService;

    private Integer myParseInt(String s) {
        try {
            return Integer.parseInt(s);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private void putMessage(JSONObject logInfo, JSONObject response, String message) {
        logInfo.put("message", message);
        response.put("message", message);
    }

    private Integer[] extractExecutorIdAndTargetId(JSONObject logInfo, JSONObject response, JSONObject request) {
        Object executor = request.get("loggedInUserId");
        Object target = request.get("userId");
        if (executor == null || target == null) {
            putMessage(logInfo, response, "insufficient or undefined input");
            return null;
        }
        String executorStr = executor.toString();
        String targetStr = target.toString();
        if (executorStr.equals("") || targetStr.equals("")) {
            putMessage(logInfo, response, "request not complete");
            return null;
        }
        Integer executorId = myParseInt(executorStr);
        Integer targetId = myParseInt(targetStr);
        if (executorId == null || targetId == null) {
            putMessage(logInfo, response, "incorrect id format");
            return null;
        }
        return new Integer[]{executorId, targetId};
    }

    private void processAccessException(JSONObject logInfo, JSONObject response, String message) {
        switch (message) {
            case "executor":
                putMessage(logInfo, response, "loggedInUserId not logged in");
                break;
            case "target":
                putMessage(logInfo, response, "userId access denied");
                break;
            case "id":
                putMessage(logInfo, response, "userId not found");
                break;
            case "admin":
                putMessage(logInfo, response, "admin access not granted");
                break;
        }
    }

    @RequestMapping(path = "/api/keepAlive", method = RequestMethod.POST)
    public ResponseEntity<JSONObject> keepAlive(@RequestBody JSONObject request) {
        JSONObject logInfo = new JSONObject();
        logInfo.put("Task", "keepAlive");
        logInfo.put("request", request.toString());
        JSONObject response = new JSONObject();
        Integer[] ids = extractExecutorIdAndTargetId(logInfo, response, request);
        if (ids == null) {
            CustomerController.log.warning(logInfo.toString());
            return ResponseEntity.badRequest().body(response);
        }
        try {
            customerService.ensureAccess(ids[0], ids[1]);
            putMessage(logInfo, response, "keep alive succeeded");
            CustomerController.log.finest(logInfo.toString());
            return ResponseEntity.ok().body(response);
        } catch (Exception e) {
            String message = e.getMessage();
            processAccessException(logInfo, response, message);
            CustomerController.log.warning(logInfo.toString());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @RequestMapping(path = "/api/login", method = RequestMethod.POST)
    public ResponseEntity<JSONObject> login(@RequestBody JSONObject request) {
        JSONObject logInfo = new JSONObject();
        logInfo.put("Task", "login");
        logInfo.put("request", request.toString());
        JSONObject response = new JSONObject();
        Object username = request.get("username");
        Object password = request.get("password");
        if (username == null || password == null) {
            putMessage(logInfo, response, "insufficient or undefined input");
            CustomerController.log.warning(logInfo.toString());
            return ResponseEntity.badRequest().body(response);
        }
        String usernameStr = username.toString();
        String passwordStr = password.toString();
        if (usernameStr.equals("") || passwordStr.equals("")) {
            putMessage(logInfo, response, "login request not complete");
            CustomerController.log.warning(logInfo.toString());
            return ResponseEntity.badRequest().body(response);
        }
        try {
            Integer[] result = customerService.login(usernameStr, passwordStr);
            response.put("id", result[0]);
            response.put("level", result[1]);
            putMessage(logInfo, response, "login succeeded");
            CustomerController.log.finest(logInfo.toString());
            return ResponseEntity.ok().body(response);
        } catch (IllegalArgumentException e) {
            String message = e.getMessage();
            switch (message) {
                case "username":
                    putMessage(logInfo, response, "username not found");
                    break;
                case "password":
                    putMessage(logInfo, response, "password not matched");
                    break;
                default:
                    putMessage(logInfo, response, "unknown error");
                    break;
            }
            CustomerController.log.warning(logInfo.toString());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @RequestMapping(path = "/api/logout", method = RequestMethod.POST)
    public ResponseEntity<JSONObject> logout(@RequestBody JSONObject request) {
        JSONObject logInfo = new JSONObject();
        logInfo.put("Task", "logout");
        logInfo.put("request", request.toString());
        JSONObject response = new JSONObject();
        Integer[] ids = extractExecutorIdAndTargetId(logInfo, response, request);
        if (ids == null) {
            CustomerController.log.warning(logInfo.toString());
            return ResponseEntity.badRequest().body(response);
        }
        try {
            customerService.logout(ids[0], ids[1]);
            putMessage(logInfo, response, "logout succeeded");
            CustomerController.log.finest(logInfo.toString());
            return ResponseEntity.ok().body(response);
        } catch (Exception e) {
            String message = e.getMessage();
            processAccessException(logInfo, response, message);
            CustomerController.log.warning(logInfo.toString());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @RequestMapping(path = "/api/register", method = RequestMethod.POST)
    public ResponseEntity<JSONObject> register(@RequestBody JSONObject request) {
        JSONObject logInfo = new JSONObject();
        logInfo.put("Task", "register");
        logInfo.put("request", request.toString());
        JSONObject response = new JSONObject();
        Object username = request.get("username");
        Object password = request.get("password");
        Object email = request.get("email");
        Object phone = request.getOrDefault("phone", ""); // optional
        Object code = request.getOrDefault("adminCode", ""); // optional
        if (username == null || password == null || email == null || phone == null || code == null) {
            putMessage(logInfo, response, "insufficient or undefined input");
            CustomerController.log.warning(logInfo.toString());
            return ResponseEntity.badRequest().body(response);
        }
        String usernameStr = username.toString();
        String passwordStr = password.toString();
        String emailStr = email.toString();
        String phoneStr = phone.toString();
        String codeStr = code.toString();
        if (usernameStr.equals("") || passwordStr.equals("") || emailStr.equals("")) {
            putMessage(logInfo, response, "registration request not complete");
            CustomerController.log.warning(logInfo.toString());
            return ResponseEntity.badRequest().body(response);
        }
        try {
            Integer[] result = customerService.register(usernameStr, passwordStr, emailStr, phoneStr, codeStr);
            response.put("id", result[0]);
            response.put("level", result[1]);
            putMessage(logInfo, response, "registration succeeded");
            CustomerController.log.finest(logInfo.toString());
            return ResponseEntity.ok().body(response);
        } catch (Exception e) {
            String message = e.getMessage();
            switch (message) {
                case "username":
                    putMessage(logInfo, response, "username already exists");
                    break;
                case "code":
                    putMessage(logInfo, response, "invalid admin code");
                    break;
                default:
                    putMessage(logInfo, response, "unknown error");
                    break;
            }
            CustomerController.log.warning(logInfo.toString());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @RequestMapping(path = "/api/applyAdminCode", method = RequestMethod.POST)
    public ResponseEntity<JSONObject> applyAdminCode(@RequestBody JSONObject request) {
        JSONObject logInfo = new JSONObject();
        logInfo.put("Task", "applyAdminCode");
        logInfo.put("request", request.toString());
        JSONObject response = new JSONObject();
        Object username = request.get("username");
        Object email = request.get("email");
        Object phone = request.getOrDefault("phone", ""); // optional
        if (username == null || email == null || phone == null) {
            putMessage(logInfo, response, "insufficient or undefined input");
            CustomerController.log.warning(logInfo.toString());
            return ResponseEntity.badRequest().body(response);
        }
        String usernameStr = username.toString();
        String emailStr = email.toString();
        String phoneStr = phone.toString();
        if (usernameStr.equals("") || emailStr.equals("")) {
            putMessage(logInfo, response, "application request not complete");
            CustomerController.log.warning(logInfo.toString());
            return ResponseEntity.badRequest().body(response);
        }
        try {
            customerService.applyAdminCode(usernameStr, emailStr, phoneStr);
            putMessage(logInfo, response, "application succeeded");
            CustomerController.log.finest(logInfo.toString());
            return ResponseEntity.ok().body(response);
        } catch (Exception e) {
            String message = e.getMessage();
            switch (message) {
                case "username":
                    putMessage(logInfo, response, "username already exists");
                    break;
                case "code":
                    putMessage(logInfo, response, "admin code temporary unavailable");
                    break;
                default:
                    putMessage(logInfo, response, "unknown error");
                    break;
            }
            CustomerController.log.warning(logInfo.toString());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @RequestMapping(path = "/api/updateUserPassword", method = RequestMethod.POST)
    public ResponseEntity<JSONObject> changePassword(@RequestBody JSONObject request) {
        JSONObject logInfo = new JSONObject();
        logInfo.put("Task", "changePassword");
        logInfo.put("request", request.toString());
        JSONObject response = new JSONObject();
        Integer[] ids = extractExecutorIdAndTargetId(logInfo, response, request);
        if (ids == null) {
            CustomerController.log.warning(logInfo.toString());
            return ResponseEntity.badRequest().body(response);
        }
        Object oldPassword = request.get("oldPassword");
        Object newPassword = request.get("newPassword");
        if (oldPassword == null || newPassword == null) {
            putMessage(logInfo, response, "insufficient or undefined input");
            CustomerController.log.warning(logInfo.toString());
            return ResponseEntity.badRequest().body(response);
        }
        String oldPasswordStr = oldPassword.toString();
        String newPasswordStr = newPassword.toString();
        if (oldPasswordStr.equals("") || newPasswordStr.equals("")) {
            putMessage(logInfo, response, "password change request not complete");
            CustomerController.log.warning(logInfo.toString());
            return ResponseEntity.badRequest().body(response);
        }
        try {
            customerService.changePassword(ids[0], ids[1], oldPasswordStr, newPasswordStr);
            putMessage(logInfo, response, "password changed");
            CustomerController.log.finest(logInfo.toString());
            return ResponseEntity.ok().body(response);
        } catch (Exception e) {
            String message = e.getMessage();
            processAccessException(logInfo, response, message);
            switch (message) {
                case "oldPassword":
                    putMessage(logInfo, response, "old password not matched");
                    break;
                default:
                    putMessage(logInfo, response, "unknown error");
                    break;
            }
            CustomerController.log.warning(logInfo.toString());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @RequestMapping(path = "/api/user", method = RequestMethod.GET)
    public ResponseEntity<JSONObject> getCustomers(
            @RequestParam(name = "id", required = false) String id,
            @RequestParam(name = "username", required = false) String username) {
        JSONObject logInfo = new JSONObject();
        logInfo.put("Task", "getCustomers");
        logInfo.put("id", id);
        logInfo.put("username", username);
        JSONObject json = new JSONObject();
        Integer idAsInt = id != null ? myParseInt(id) : null;
        if (id != null && idAsInt == null) {
            putMessage(logInfo, json, "incorrect id format");
            CustomerController.log.warning(logInfo.toString());
            return ResponseEntity.badRequest().body(json);
        }
        List<Object[]> customers = customerService.getCustomers(idAsInt, username);
        JSONArray result = processCustomers(customers);
        json.put("result", result);
        putMessage(logInfo, json, "results fetched");
        CustomerController.log.finest(logInfo.toString());
        return ResponseEntity.ok().body(json);
    }

    @RequestMapping(path = "/api/getAllUsers/{adminId}", method = RequestMethod.GET)
    public ResponseEntity<JSONObject> getAllCustomers(@PathVariable(name = "adminId") String adminId) {
        JSONObject logInfo = new JSONObject();
        logInfo.put("Task", "getAllCustomers");
        logInfo.put("adminId", adminId);
        JSONObject json = new JSONObject();
        Integer adminIdAsInt = myParseInt(adminId);
        if (adminIdAsInt == null) {
            putMessage(logInfo, json, "incorrect id format");
            CustomerController.log.warning(logInfo.toString());
            return ResponseEntity.badRequest().body(json);
        }
        try {
            List<Object[]> customers = customerService.getAllCustomers(adminIdAsInt);
            JSONArray result = processCustomers(customers);
            json.put("result", result);
            putMessage(logInfo, json, "results fetched");
            CustomerController.log.finest(logInfo.toString());
            return ResponseEntity.ok().body(json);
        } catch (Exception e) {
            String message = e.getMessage();
            processAccessException(logInfo, json, message);
            CustomerController.log.warning(logInfo.toString());
            return ResponseEntity.badRequest().body(json);
        }
    }

    private JSONArray processCustomers(List<Object[]> customers) {
        JSONArray array = new JSONArray();
        for (Object[] each : customers) {
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
        return array;
    }

    @RequestMapping(path = "/api/updateUser", method = RequestMethod.POST)
    public ResponseEntity<JSONObject> updateCustomer(@RequestBody JSONObject request) {
        JSONObject logInfo = new JSONObject();
        logInfo.put("Task", "updateCustomer");
        logInfo.put("request", request.toString());
        JSONObject response = new JSONObject();
        Integer[] ids = extractExecutorIdAndTargetId(logInfo, response, request);
        if (ids == null) {
            CustomerController.log.warning(logInfo.toString());
            return ResponseEntity.badRequest().body(response);
        }
        request.remove("loggedInUserId");
        request.remove("userId");
        try {
            customerService.updateCustomer(ids[0], ids[1], request);
            putMessage(logInfo, response, "update user succeeded");
            CustomerController.log.finest(logInfo.toString());
            return ResponseEntity.ok().body(response);
        } catch (Exception e) {
            String message = e.getMessage();
            processAccessException(logInfo, response, message);
            switch (message) {
                case "key":
                    putMessage(logInfo, response, "invalid key-value update");
                    break;
                case "value":
                    putMessage(logInfo, response, "value of invalid type exists");
                    break;
                default:
                    putMessage(logInfo, response, "unknown error");
                    break;
            }
            CustomerController.log.warning(logInfo.toString());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @RequestMapping(path = "/api/deleteUser", method = RequestMethod.POST)
    public ResponseEntity<JSONObject> deleteCustomer(@RequestBody JSONObject request) {
        JSONObject logInfo = new JSONObject();
        logInfo.put("Task", "deleteCustomer");
        logInfo.put("request", request.toString());
        JSONObject response = new JSONObject();
        Integer[] ids = extractExecutorIdAndTargetId(logInfo, response, request);
        if (ids == null) {
            CustomerController.log.warning(logInfo.toString());
            return ResponseEntity.badRequest().body(response);
        }
        try {
            customerService.deleteCustomer(ids[0], ids[1]);
            putMessage(logInfo, response, "delete user succeeded");
            CustomerController.log.finest(logInfo.toString());
            return ResponseEntity.ok().body(response);
        } catch (Exception e) {
            String message = e.getMessage();
            processAccessException(logInfo, response, message);
            CustomerController.log.warning(logInfo.toString());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @RequestMapping(path = "/api/user/following/{id}", method = RequestMethod.GET)
    public ResponseEntity<JSONObject> getFollowing(@PathVariable(name = "id") String id) {
        JSONObject logInfo = new JSONObject();
        logInfo.put("Task", "getFollowing");
        logInfo.put("id", id);
        JSONObject json = new JSONObject();
        JSONArray array = new JSONArray();
        json.put("result", array);
        Integer idAsInt = myParseInt(id);
        if (idAsInt == null) {
            putMessage(logInfo, json, "incorrect id format");
            CustomerController.log.warning(logInfo.toString());
            return ResponseEntity.badRequest().body(json);
        }
        List<Customer> result = customerService.getFollowing(idAsInt);
        for (Customer customer : result) {
            JSONObject following = new JSONObject();
            following.put("user_name", customer.getUsername());
            following.put("following", customer.getId());
            array.add(following);
        }
        putMessage(logInfo, json, "results fetched");
        CustomerController.log.finest(logInfo.toString());
        return ResponseEntity.ok().body(json);
    }

    @RequestMapping(path = "/api/user/followers/{id}", method = RequestMethod.GET)
    public ResponseEntity<JSONObject> getFollowers(@PathVariable(name = "id") String id) {
        JSONObject logInfo = new JSONObject();
        logInfo.put("Task", "getFollowers");
        logInfo.put("id", id);
        JSONObject json = new JSONObject();
        JSONArray array = new JSONArray();
        json.put("result", array);
        Integer idAsInt = myParseInt(id);
        if (idAsInt == null) {
            putMessage(logInfo, json, "incorrect id format");
            CustomerController.log.warning(logInfo.toString());
            return ResponseEntity.badRequest().body(json);
        }
        List<Customer> result = customerService.getFollowers(idAsInt);
        for (Customer customer : result) {
            JSONObject follower = new JSONObject();
            follower.put("user_name", customer.getUsername());
            follower.put("follower", customer.getId());
            array.add(follower);
        }
        putMessage(logInfo, json, "results fetched");
        CustomerController.log.finest(logInfo.toString());
        return ResponseEntity.ok().body(json);
    }

    @RequestMapping(path = "/api/user/follow", method = RequestMethod.POST)
    public ResponseEntity<JSONObject> follow(@RequestBody JSONObject request) {
        JSONObject logInfo = new JSONObject();
        logInfo.put("Task", "follow");
        logInfo.put("request", request.toString());
        JSONObject response = new JSONObject();
        Integer[] ids = extractExecutorIdAndFromIdAndToId(logInfo, response, request);
        if (ids == null) {
            CustomerController.log.warning(logInfo.toString());
            return ResponseEntity.badRequest().body(response);
        }
        try {
            customerService.follow(ids[0], ids[1], ids[2]);
            putMessage(logInfo, response, "follow succeeded");
            CustomerController.log.finest(logInfo.toString());
            return ResponseEntity.ok().body(response);
        } catch (Exception e) {
            String message = e.getMessage();
            processAccessException(logInfo, response, message);
            CustomerController.log.warning(logInfo.toString());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @RequestMapping(path = "/api/user/un-follow", method = RequestMethod.POST)
    public ResponseEntity<JSONObject> unFollow(@RequestBody JSONObject request) {
        JSONObject logInfo = new JSONObject();
        logInfo.put("Task", "unFollow");
        logInfo.put("request", request.toString());
        JSONObject response = new JSONObject();
        Integer[] ids = extractExecutorIdAndFromIdAndToId(logInfo, response, request);
        if (ids == null) {
            CustomerController.log.warning(logInfo.toString());
            return ResponseEntity.badRequest().body(response);
        }
        try {
            customerService.unFollow(ids[0], ids[1], ids[2]);
            putMessage(logInfo, response, "un-follow succeeded");
            CustomerController.log.finest(logInfo.toString());
            return ResponseEntity.ok().body(response);
        } catch (Exception e) {
            String message = e.getMessage();
            processAccessException(logInfo, response, message);
            CustomerController.log.warning(logInfo.toString());
            return ResponseEntity.badRequest().body(response);
        }
    }

    private Integer[] extractExecutorIdAndFromIdAndToId(JSONObject logInfo, JSONObject response, JSONObject request) {
        Object executor = request.get("loggedInUserId");
        Object from = request.get("from");
        Object to = request.get("to");
        if (executor == null || from == null || to == null) {
            putMessage(logInfo, response, "insufficient or undefined input");
            return null;
        }
        String executorStr = executor.toString();
        String fromStr = from.toString();
        String toStr = to.toString();
        if (executorStr.equals("") || fromStr.equals("") || toStr.equals("")) {
            putMessage(logInfo, response, "request not complete");
            return null;
        }
        Integer executorId = myParseInt(executorStr);
        Integer fromId = myParseInt(fromStr);
        Integer toId = myParseInt(toStr);
        if (executorId == null || fromId == null || toId == null) {
            putMessage(logInfo, response, "incorrect id format");
            return null;
        }
        return new Integer[]{executorId, fromId, toId};
    }

}
