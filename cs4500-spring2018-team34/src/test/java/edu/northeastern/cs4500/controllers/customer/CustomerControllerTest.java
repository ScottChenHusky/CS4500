package edu.northeastern.cs4500.controllers.customer;

import edu.northeastern.cs4500.services.CustomerService;
import org.json.simple.JSONObject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(CustomerController.class)
public class CustomerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CustomerService customerService;

    @Test
    public void testKeepAliveShouldSucceed() throws Exception {
        Integer loggedInUserId = 25;
        Integer userId = 25;

        Mockito.doNothing().when(customerService).ensureAccess(loggedInUserId, userId);

        JSONObject request = new JSONObject();
        request.put("loggedInUserId", loggedInUserId);
        request.put("userId", userId);

        JSONObject response = new JSONObject();
        response.put("message", "keep alive succeeded");

        mockMvc.perform(post("/api/keepAlive")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(request.toJSONString()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(content().json(response.toJSONString()));
    }

    @Test
    public void testLoginShouldSucceed() throws Exception {
        String username = "user1";
        String password = "pass1";

        Integer id = 27;

        Mockito.when(customerService.login(username, password)).thenReturn(id);

        JSONObject request = new JSONObject();
        request.put("username", username);
        request.put("password", password);

        JSONObject response = new JSONObject();
        response.put("id", id);
        response.put("message", "login succeeded");

        mockMvc.perform(post("/api/login")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(request.toJSONString()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(content().json(response.toJSONString()));
    }

    @Test
    public void testLogoutShouldSucceed() throws Exception {
        Integer loggedInUserId = 27;
        Integer userId = 27;

        Mockito.doNothing().when(customerService).logout(loggedInUserId, userId);

        JSONObject request = new JSONObject();
        request.put("loggedInUserId", loggedInUserId);
        request.put("userId", userId);

        JSONObject response = new JSONObject();
        response.put("message", "logout succeeded");

        mockMvc.perform(post("/api/logout")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(request.toJSONString()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(content().json(response.toJSONString()));
    }

    @Test
    public void testRegisterShouldSucceed() throws Exception {
        String username = "user2";
        String password = "pass2";
        String email = "user2@neu.edu";
        String phone = "6177165124";
        String code = "";

        Integer id = 26;

        Mockito.when(customerService.register(username, password, email, phone, code)).thenReturn(id);

        JSONObject request = new JSONObject();
        request.put("username", username);
        request.put("password", password);
        request.put("email", email);
        request.put("phone", phone);
        request.put("code", code);

        JSONObject response = new JSONObject();
        request.put("id", id);
        request.put("message", "registration succeeded");

        mockMvc.perform(post("/api/register")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(request.toJSONString()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(content().json(response.toJSONString()));
    }

    @Test
    public void testApplyAdminCodeShouldSucceed() throws Exception {
        String username = "user3";
        String email = "user3@neu.edu";
        String phone = "6177634215";

        Mockito.doNothing().when(customerService).applyAdminCode(username, email, phone);

        JSONObject request = new JSONObject();
        request.put("username", username);
        request.put("email", email);
        request.put("phone", phone);

        JSONObject response = new JSONObject();
        request.put("message", "application succeeded");

        mockMvc.perform(post("/api/applyAdminCode")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(request.toJSONString()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(content().json(response.toJSONString()));
    }

    @Test
    public void testUpdateUserPasswordShouldSucceed() throws Exception {
        Integer loggedInUserId = 25;
        Integer userId = 25;
        String oldPassword = "oldP4ss";
        String newPassword = "newP4ss";

        Mockito.doNothing().when(customerService).changePassword(loggedInUserId, userId, oldPassword, newPassword);

        JSONObject request = new JSONObject();
        request.put("loggedInUserId", loggedInUserId);
        request.put("userId", userId);
        request.put("oldPassword", oldPassword);
        request.put("newPassword", newPassword);

        JSONObject response = new JSONObject();
        response.put("message", "password changed");

        mockMvc.perform(post("/api/updateUserPassword")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(request.toJSONString()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(content().json(response.toJSONString()));
    }

    

}
