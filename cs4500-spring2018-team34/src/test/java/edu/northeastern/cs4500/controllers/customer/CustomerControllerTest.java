package edu.northeastern.cs4500.controllers.customer;

import edu.northeastern.cs4500.repositories.Customer;
import edu.northeastern.cs4500.services.CustomerService;
import org.json.simple.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;

@RunWith(SpringRunner.class)
@WebMvcTest(CustomerController.class)
public class CustomerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CustomerService customerService;
    
    @Before
    public void setup() throws Exception {
    	//mockMvc = MockMvcBuilders.standaloneSetup(new CustomerController()).build();
    }

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
        
        request.put("loggedInUserId", "");
        request.put("userId", "");
        
        mockMvc.perform(post("/api/keepAlive")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(request.toJSONString()))
                .andExpect(status().isBadRequest());
        
        Exception e = new RuntimeException();
        
        Mockito.doThrow(e).when(customerService).ensureAccess(1, 1);
        
        request.put("loggedInUserId", "1");
        request.put("userId", "1");
        
        mockMvc.perform(post("/api/keepAlive")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(request.toJSONString()))
                .andExpect(status().isBadRequest());
        
        
    }

    @Test
    public void testLoginShouldSucceed() throws Exception {
        String username = "user1";
        String password = "pass1";

        Integer id = 27;
        Integer level = 1;

        Mockito.when(customerService.login(username, password))
                .thenReturn(new Integer[]{id, level});

        JSONObject request = new JSONObject();
        request.put("username", username);
        request.put("password", password);

        JSONObject response = new JSONObject();
        response.put("id", id);
        response.put("level", level);
        response.put("message", "login succeeded");

        mockMvc.perform(post("/api/login")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(request.toJSONString()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(content().json(response.toJSONString()));
        
        request.put("username", "");
        request.put("password", "");
        mockMvc.perform(post("/api/login")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(request.toJSONString()))
        .andExpect(status().isBadRequest());
        
        request.put("username", null);
        request.put("password", null);
        mockMvc.perform(post("/api/login")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(request.toJSONString()))
        .andExpect(status().isBadRequest());
        
        Mockito.when(customerService.login("1", "1"))
        	.thenThrow(new IllegalArgumentException());
        request.put("username", "1");
        request.put("password", "1");
        mockMvc.perform(post("/api/login")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(request.toJSONString()))
        .andExpect(status().isBadRequest());
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
        
        request.put("loggedInUserId", "");
        request.put("userId", "");
        
        mockMvc.perform(post("/api/logout")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(request.toJSONString()))
        .andExpect(status().isBadRequest());
        
        Mockito.doThrow(new Exception()).when(customerService).logout(1, 1);
        
        request.put("loggedInUserId", "1");
        request.put("userId", "1");
        
        mockMvc.perform(post("/api/logout")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(request.toJSONString()))
        .andExpect(status().isBadRequest());
    }

    @Test
    public void testRegisterShouldSucceed() throws Exception {
        String username = "user2";
        String password = "pass2";
        String email = "user2@neu.edu";
        String phone = "6177165124";
        String code = "";

        Integer id = 26;
        Integer level = 1;

        Mockito.when(customerService.register(username, password, email, phone, code))
                .thenReturn(new Integer[]{id, level});

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
        
        Mockito.when(customerService.register(username, password, email, phone, code))
        	.thenThrow(Exception.class);
        mockMvc.perform(post("/api/register")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(request.toJSONString()))
        .andExpect(status().isBadRequest());
        
        request.put("username", null);
        mockMvc.perform(post("/api/register")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(request.toJSONString()))
        .andExpect(status().isBadRequest());
        
        request.put("username", "");
        mockMvc.perform(post("/api/register")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(request.toJSONString()))
        .andExpect(status().isBadRequest());
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
        
        Mockito.doThrow(Exception.class).when(customerService).applyAdminCode(username, email, phone);
        mockMvc.perform(post("/api/applyAdminCode")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(request.toJSONString()))
        .andExpect(status().isBadRequest());
        
        request.put("username", null);
        mockMvc.perform(post("/api/applyAdminCode")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(request.toJSONString()))
        .andExpect(status().isBadRequest());
        
        request.put("username", "");
        mockMvc.perform(post("/api/applyAdminCode")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(request.toJSONString()))
        .andExpect(status().isBadRequest());
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
        
        Mockito.doThrow(Exception.class).when(customerService)
        	.changePassword(loggedInUserId, userId, oldPassword, newPassword);
        
        mockMvc.perform(post("/api/updateUserPassword")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(request.toJSONString()))
        .andExpect(status().isBadRequest());
        
        request.put("oldPassword", null);
        request.put("newPassword", null);
        mockMvc.perform(post("/api/updateUserPassword")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(request.toJSONString()))
        .andExpect(status().isBadRequest());
        
        request.put("oldPassword", "");
        request.put("newPassword", "");
        mockMvc.perform(post("/api/updateUserPassword")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(request.toJSONString()))
        .andExpect(status().isBadRequest());
        
        request.put("loggedInUserId", "");
        request.put("userId", "");
        mockMvc.perform(post("/api/updateUserPassword")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(request.toJSONString()))
        .andExpect(status().isBadRequest());
    }
    
    @Test
    public void testGetCustomers() throws Exception {
    	//Mockito.doNothing().when(customerService).getCustomers(1, "");
    	mockMvc.perform(get("/api/user").param("id", "1")).andExpect(status().isOk());
    }
    
    @Test
    public void testGetAllUsers() throws Exception {
    	//Mockito.doNothing().when(customerService).getCustomers(1, "");
    	mockMvc.perform(get("/api/getAllUsers/{adminId}", 1)).andExpect(status().isOk());
    }
    
    @Test
    public void testUpdateUser() throws Exception {
    	JSONObject request = new JSONObject();
    	
    	mockMvc.perform(post("/api/updateUser")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(request.toJSONString()))
        .andExpect(status().isBadRequest());
    	
    	request.put("loggedInUserId", "1");
    	request.put("userId", "1");
    	
    	mockMvc.perform(post("/api/updateUser")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(request.toJSONString()))
        .andExpect(status().isOk());
    }

    @Test
    public void testDeleteUser() throws Exception {
    	JSONObject request = new JSONObject();
    	
    	mockMvc.perform(post("/api/deleteUser")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(request.toJSONString()))
        .andExpect(status().isBadRequest());
    	
    	request.put("loggedInUserId", "1");
    	request.put("userId", "1");
    	
    	mockMvc.perform(post("/api/deleteUser")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(request.toJSONString()))
        .andExpect(status().isOk());
    	
    	request.put("loggedInUserId", "");
    	request.put("userId", "");
    	
    	mockMvc.perform(post("/api/deleteUser")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(request.toJSONString()))
        .andExpect(status().isBadRequest());
    	
    	request.put("loggedInUserId", "$");
    	request.put("userId", "$");
    	
    	mockMvc.perform(post("/api/deleteUser")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(request.toJSONString()))
        .andExpect(status().isBadRequest());
    }
    
    @Test
    public void testGetFollowing() throws Exception {
    	List l1 = new ArrayList();
    	l1.add(new Customer());
    	Mockito.when(customerService.getFollowing(1)).thenReturn(l1);
    	mockMvc.perform(get("/api/user/following/{id}", 1)).andExpect(status().isOk());
    }
    
    @Test
    public void testGetFollowers() throws Exception {
    	List l1 = new ArrayList();
    	l1.add(new Customer());
    	Mockito.when(customerService.getFollowers(1)).thenReturn(l1);
    	mockMvc.perform(get("/api/user/followers/{id}", 1)).andExpect(status().isOk());
    }
    
    @Test
    public void testFollow() throws Exception {
    	JSONObject request = new JSONObject();
    	
    	mockMvc.perform(post("/api/user/follow")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(request.toJSONString()))
        .andExpect(status().isBadRequest());
    	
    	request.put("loggedInUserId", "1");
    	request.put("from", "1");
    	request.put("to", "2");
    	
    	mockMvc.perform(post("/api/user/follow")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(request.toJSONString()))
        .andExpect(status().isOk());
    }
    
    @Test
    public void testUnFollow() throws Exception {
    	JSONObject request = new JSONObject();
    	
    	mockMvc.perform(post("/api/user/un-follow")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(request.toJSONString()))
        .andExpect(status().isBadRequest());
    	
    	request.put("loggedInUserId", "1");
    	request.put("from", "1");
    	request.put("to", "2");
    	
    	mockMvc.perform(post("/api/user/un-follow")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(request.toJSONString()))
        .andExpect(status().isOk());
    }
}
