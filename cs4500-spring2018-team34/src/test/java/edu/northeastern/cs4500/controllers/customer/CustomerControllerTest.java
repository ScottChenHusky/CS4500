package edu.northeastern.cs4500.controllers.customer;

import edu.northeastern.cs4500.controllers.customer.CustomerController;
import edu.northeastern.cs4500.controllers.movie.Movie;
import edu.northeastern.cs4500.repositories.Customer;
import edu.northeastern.cs4500.repositories.CustomerPlaylist;
import edu.northeastern.cs4500.repositories.CustomerRecommend;
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

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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
    
    @Before
    public void setup() throws Exception {
    	//mockMvc = MockMvcBuilders.standaloneSetup(new CustomerController()).build();
    }

    @Test
    public void testKeepAlive() throws Exception {
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
    public void testLogin() throws Exception {
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
        
        Mockito.when(customerService.login("2", "2"))
	    	.thenThrow(new IllegalArgumentException("username"));
        request.put("username", "2");
        request.put("password", "2");
	    mockMvc.perform(post("/api/login")
	            .contentType(MediaType.APPLICATION_JSON_UTF8)
	            .content(request.toJSONString()))
	    .andExpect(status().isBadRequest());
	    
	    Mockito.when(customerService.login("3", "3"))
	    	.thenThrow(new IllegalArgumentException("password"));
	    request.put("username", "3");
        request.put("password", "3");
	    mockMvc.perform(post("/api/login")
	            .contentType(MediaType.APPLICATION_JSON_UTF8)
	            .content(request.toJSONString()))
	    .andExpect(status().isBadRequest());
	    
	    Mockito.when(customerService.login("4", "4"))
	    	.thenThrow(new IllegalArgumentException("idk"));
	    request.put("username", "4");
        request.put("password", "4");
	    mockMvc.perform(post("/api/login")
	            .contentType(MediaType.APPLICATION_JSON_UTF8)
	            .content(request.toJSONString()))
	    .andExpect(status().isBadRequest());
    }

    @Test
    public void testLogout() throws Exception {
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
    public void testRegister() throws Exception {
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
        
        Mockito.when(customerService.register("1", password, email, phone, code))
	    	.thenThrow(new IllegalArgumentException("username"));
        request.put("username", "1");
	    mockMvc.perform(post("/api/register")
	            .contentType(MediaType.APPLICATION_JSON_UTF8)
	            .content(request.toJSONString()))
	    .andExpect(status().isBadRequest());
    
	    Mockito.when(customerService.register("2", password, email, phone, code))
			.thenThrow(new IllegalArgumentException("code"));
	    request.put("username", "2");
		mockMvc.perform(post("/api/register")
		        .contentType(MediaType.APPLICATION_JSON_UTF8)
		        .content(request.toJSONString()))
		.andExpect(status().isBadRequest());

		Mockito.when(customerService.register("3", password, email, phone, code))
			.thenThrow(new IllegalArgumentException("c"));
		request.put("username", "3");
		mockMvc.perform(post("/api/register")
		    .contentType(MediaType.APPLICATION_JSON_UTF8)
		    .content(request.toJSONString()))
		.andExpect(status().isBadRequest());
    }

    @Test
    public void testApplyAdminCode() throws Exception {
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
        
        Mockito.doThrow(new IllegalArgumentException("username")).when(customerService).applyAdminCode("1", email, phone);
	    request.put("username", "1");
	    mockMvc.perform(post("/api/applyAdminCode")
	            .contentType(MediaType.APPLICATION_JSON_UTF8)
	            .content(request.toJSONString()))
	    .andExpect(status().isBadRequest());
	
	    Mockito.doThrow(new IllegalArgumentException("code")).when(customerService).applyAdminCode("2", email, phone);
	    request.put("username", "2");
		mockMvc.perform(post("/api/applyAdminCode")
		        .contentType(MediaType.APPLICATION_JSON_UTF8)
		        .content(request.toJSONString()))
		.andExpect(status().isBadRequest());
	
		Mockito.doThrow(new IllegalArgumentException("c")).when(customerService).applyAdminCode("3", email, phone);
		request.put("username", "3");
		mockMvc.perform(post("/api/applyAdminCode")
		    .contentType(MediaType.APPLICATION_JSON_UTF8)
		    .content(request.toJSONString()))
		.andExpect(status().isBadRequest());
    }

    @Test
    public void testUpdateUserPassword() throws Exception {
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
        
        Mockito.doThrow(new IllegalArgumentException("oldPassword")).when(customerService)
	    	.changePassword(loggedInUserId, userId, oldPassword, newPassword);
	    
	    mockMvc.perform(post("/api/updateUserPassword")
	            .contentType(MediaType.APPLICATION_JSON_UTF8)
	            .content(request.toJSONString()))
	    .andExpect(status().isBadRequest());
	    
	    Mockito.doThrow(new IllegalArgumentException("idk")).when(customerService)
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
    	mockMvc.perform(get("/api/user").param("id", "")).andExpect(status().isBadRequest());
    }
    
    @Test
    public void testGetAllUsers() throws Exception {
    	Customer c = new Customer();
    	c.setCreateDate(new Date());
    	c.setLastLogin(new Date());
    	c.setPrivacyLevel(2);
    	c.setScore(2);
    	
    	Object[] o = new Object[] {c, true};
    	List<Object[]> l = new ArrayList<Object[]>();
    	l.add(o);
    	Mockito.when(customerService.getAllCustomers(1)).thenReturn(l);
    	Mockito.doThrow(Exception.class).when(customerService).getAllCustomers(2);
    	mockMvc.perform(get("/api/getAllUsers/{adminId}", 1)).andExpect(status().isOk());
    	mockMvc.perform(get("/api/getAllUsers/{adminId}", "h*ck")).andExpect(status().isBadRequest());
    	mockMvc.perform(get("/api/getAllUsers/{adminId}", 2)).andExpect(status().isBadRequest());
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
    	
    	Mockito.doThrow(Exception.class).when(customerService).updateCustomer(1, 1, new JSONObject());
    	
    	mockMvc.perform(post("/api/updateUser")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(request.toJSONString()))
        .andExpect(status().isBadRequest());
    	
    	Mockito.doThrow(new IllegalArgumentException("key")).when(customerService).updateCustomer(1, 1, new JSONObject());
    	
    	mockMvc.perform(post("/api/updateUser")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(request.toJSONString()))
        .andExpect(status().isBadRequest());
    	
    	Mockito.doThrow(new IllegalArgumentException("value")).when(customerService).updateCustomer(1, 1, new JSONObject());
    	
    	mockMvc.perform(post("/api/updateUser")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(request.toJSONString()))
        .andExpect(status().isBadRequest());
    	
    	Mockito.doThrow(new IllegalArgumentException("sumbs")).when(customerService).updateCustomer(1, 1, new JSONObject());
    	
    	mockMvc.perform(post("/api/updateUser")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(request.toJSONString()))
        .andExpect(status().isBadRequest());
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
    	
    	Mockito.doThrow(new IllegalArgumentException("executor")).when(customerService).deleteCustomer(1, 1);
    	
    	mockMvc.perform(post("/api/deleteUser")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(request.toJSONString()))
        .andExpect(status().isBadRequest());
    	
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
    	mockMvc.perform(get("/api/user/following/{id}", "$$")).andExpect(status().isBadRequest());
    }
    
    @Test
    public void testGetFollowers() throws Exception {
    	List l1 = new ArrayList();
    	l1.add(new Customer());
    	Mockito.when(customerService.getFollowers(1)).thenReturn(l1);
    	mockMvc.perform(get("/api/user/followers/{id}", 1)).andExpect(status().isOk());
    	mockMvc.perform(get("/api/user/followers/{id}", "$$")).andExpect(status().isBadRequest());
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
    	
    	Mockito.doThrow(new IllegalArgumentException("target")).when(customerService).follow(1, 1, 2);
    	
    	mockMvc.perform(post("/api/user/follow")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(request.toJSONString()))
        .andExpect(status().isBadRequest());
    	
    	request.put("loggedInUserId", "");
    	request.put("from", "");
    	request.put("to", "");
    	
    	mockMvc.perform(post("/api/user/follow")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(request.toJSONString()))
        .andExpect(status().isBadRequest());
    	
    	request.put("loggedInUserId", "$$");
    	request.put("from", "$$");
    	request.put("to", "$$");
    	
    	mockMvc.perform(post("/api/user/follow")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(request.toJSONString()))
        .andExpect(status().isBadRequest());
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
    	
    	Mockito.doThrow(new IllegalArgumentException("id")).when(customerService).unFollow(1, 1, 2);
    	
    	mockMvc.perform(post("/api/user/un-follow")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(request.toJSONString()))
        .andExpect(status().isBadRequest());
    }
    
    @Test
    public void getPlaylists() throws Exception {
    	List<Movie> l1 = new ArrayList<Movie>();
    	l1.add(new Movie());
    	CustomerPlaylist c = new CustomerPlaylist().withId(2);
    	Map<CustomerPlaylist, List<Movie>> m = new HashMap<CustomerPlaylist, List<Movie>>();
    	m.put(c, l1);
    	Mockito.when(customerService.getPlaylists(1)).thenReturn(m);
    	mockMvc.perform(get("/api/getPlaylists/{userId}", 1)).andExpect(status().isOk());
    	mockMvc.perform(get("/api/getPlaylists/{userId}", "$$")).andExpect(status().isBadRequest());
    }
    
    @Test
    public void createPlaylist() throws Exception {
    	JSONObject request = new JSONObject();
    	
    	mockMvc.perform(post("/api/createPlaylist")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(request.toJSONString()))
        .andExpect(status().isBadRequest());
    	
    	request.put("loggedInUserId", "1");
    	request.put("userId", "1");
    	
    	mockMvc.perform(post("/api/createPlaylist")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(request.toJSONString()))
        .andExpect(status().isBadRequest());
    	
    	request.put("name", "test");
    	request.put("description", "test");
    	
    	mockMvc.perform(post("/api/createPlaylist")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(request.toJSONString()))
        .andExpect(status().isOk());
    	
    	request.put("name", "ntest");
    	Mockito.when(customerService.createPlaylist(1, 1, "ntest", "test")).thenThrow(new IllegalArgumentException());
    	mockMvc.perform(post("/api/createPlaylist")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(request.toJSONString()))
        .andExpect(status().isBadRequest());
    	
    	request.put("name", "mtest");
    	Mockito.when(customerService.createPlaylist(1, 1, "mtest", "test"))
    		.thenThrow(new IllegalArgumentException("existed"));
    	mockMvc.perform(post("/api/createPlaylist")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(request.toJSONString()))
        .andExpect(status().isBadRequest());
    	
    	request.put("name", "ltest");
    	Mockito.when(customerService.createPlaylist(1, 1, "ltest", "test"))
    		.thenThrow(new IllegalArgumentException("h*ck"));
    	mockMvc.perform(post("/api/createPlaylist")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(request.toJSONString()))
        .andExpect(status().isBadRequest());
    }
    
    @Test
    public void testAddMovieToPlaylist() throws Exception{
    	JSONObject request = new JSONObject();
    	
    	mockMvc.perform(post("/api/addMovieToPlaylist")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(request.toJSONString()))
        .andExpect(status().isBadRequest());
    	
    	request.put("loggedInUserId", "1");
    	request.put("userId", "1");
    	
    	mockMvc.perform(post("/api/addMovieToPlaylist")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(request.toJSONString()))
        .andExpect(status().isBadRequest());
    	
    	request.put("playlistId", "1");
    	request.put("movieId", "1");
    	
    	mockMvc.perform(post("/api/addMovieToPlaylist")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(request.toJSONString()))
        .andExpect(status().isOk());
    	
    	Mockito.doThrow(new IllegalArgumentException()).when(customerService).addMovieToPlaylist(1, 1, 1, 1);
    	mockMvc.perform(post("/api/addMovieToPlaylist")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(request.toJSONString()))
        .andExpect(status().isBadRequest());
    	
    	Mockito.doThrow(new IllegalArgumentException("none")).when(customerService).addMovieToPlaylist(1, 1, 1, 1);
    	mockMvc.perform(post("/api/addMovieToPlaylist")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(request.toJSONString()))
        .andExpect(status().isBadRequest());
    	
    	Mockito.doThrow(new IllegalArgumentException("duplicated")).when(customerService).addMovieToPlaylist(1, 1, 1, 1);
    	mockMvc.perform(post("/api/addMovieToPlaylist")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(request.toJSONString()))
        .andExpect(status().isBadRequest());
    	
    	Mockito.doThrow(new IllegalArgumentException("idk")).when(customerService).addMovieToPlaylist(1, 1, 1, 1);
    	mockMvc.perform(post("/api/addMovieToPlaylist")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(request.toJSONString()))
        .andExpect(status().isBadRequest());
    }
    
    @Test
    public void testRemoveFromPlaylist() throws Exception{
    	JSONObject request = new JSONObject();
    	
    	mockMvc.perform(post("/api/removeMovieFromPlaylist")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(request.toJSONString()))
        .andExpect(status().isBadRequest());
    	
    	request.put("loggedInUserId", "1");
    	request.put("userId", "1");
    	
    	mockMvc.perform(post("/api/removeMovieFromPlaylist")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(request.toJSONString()))
        .andExpect(status().isBadRequest());
    	
    	request.put("playlistId", "1");
    	request.put("movieId", "1");
    	
    	mockMvc.perform(post("/api/removeMovieFromPlaylist")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(request.toJSONString()))
        .andExpect(status().isOk());
    	
    	Mockito.doThrow(new IllegalArgumentException()).when(customerService).removeMovieFromPlaylist(1, 1, 1, 1);
    	mockMvc.perform(post("/api/removeMovieFromPlaylist")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(request.toJSONString()))
        .andExpect(status().isBadRequest());
    	
    	Mockito.doThrow(new IllegalArgumentException("none")).when(customerService).removeMovieFromPlaylist(1, 1, 1, 1);
    	mockMvc.perform(post("/api/removeMovieFromPlaylist")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(request.toJSONString()))
        .andExpect(status().isBadRequest());
    	
    	Mockito.doThrow(new IllegalArgumentException("duplicated")).when(customerService).removeMovieFromPlaylist(1, 1, 1, 1);
    	mockMvc.perform(post("/api/removeMovieFromPlaylist")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(request.toJSONString()))
        .andExpect(status().isBadRequest());
    	
    	Mockito.doThrow(new IllegalArgumentException("idk")).when(customerService).removeMovieFromPlaylist(1, 1, 1, 1);
    	mockMvc.perform(post("/api/removeMovieFromPlaylist")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(request.toJSONString()))
        .andExpect(status().isBadRequest());
    }
    
    @Test
    public void testDeletePlaylist() throws Exception {
    	JSONObject request = new JSONObject();
    	
    	mockMvc.perform(post("/api/deletePlaylist")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(request.toJSONString()))
        .andExpect(status().isBadRequest());
    	
    	request.put("loggedInUserId", "1");
    	request.put("userId", "1");
    	
    	mockMvc.perform(post("/api/deletePlaylist")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(request.toJSONString()))
        .andExpect(status().isBadRequest());
    	
    	request.put("playlistId", "1");
    	request.put("movieId", "1");
    	
    	mockMvc.perform(post("/api/deletePlaylist")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(request.toJSONString()))
        .andExpect(status().isOk());
    	
    	Mockito.doThrow(new IllegalArgumentException("admin")).when(customerService).deletePlaylist(1, 1, 1);
    	mockMvc.perform(post("/api/deletePlaylist")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(request.toJSONString()))
        .andExpect(status().isBadRequest());
    }
    
    @Test
    public void testGetUserRec() throws Exception {
    	Map<CustomerRecommend, Movie> m = new HashMap<CustomerRecommend, Movie>();
    	m.put(new CustomerRecommend().withId(2).withCreateDate(new Date()), new Movie());
    	Mockito.when(customerService.getCustomerRecommendationOfMovies(1)).thenReturn(m);
    	mockMvc.perform(get("/api/getUserRecommendationOfMovies/{userId}", 1)).andExpect(status().isOk());
    	mockMvc.perform(get("/api/getUserRecommendationOfMovies/{userId}", "$$")).andExpect(status().isBadRequest());
    }
    
    @Test
    public void testRecommendMovieToUser() throws Exception {
    	JSONObject request = new JSONObject();
    	
    	mockMvc.perform(post("/api/recommendMovieToUser")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(request.toJSONString()))
        .andExpect(status().isBadRequest());
    	
    	request.put("loggedInUserId", "1");
    	request.put("from", "1");
    	request.put("to", "2");
    	
    	mockMvc.perform(post("/api/recommendMovieToUser")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(request.toJSONString()))
        .andExpect(status().isBadRequest());
    	
    	request.put("movieId", "$$");
    	
    	mockMvc.perform(post("/api/recommendMovieToUser")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(request.toJSONString()))
        .andExpect(status().isBadRequest());
    	
    	request.put("movieId", "1");
    	
    	mockMvc.perform(post("/api/recommendMovieToUser")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(request.toJSONString()))
        .andExpect(status().isOk());
    	
    	Mockito.doThrow(new IllegalArgumentException("admin")).when(customerService).recommendMovieToCustomer(1, 1, 2, 1);
    	
    	mockMvc.perform(post("/api/recommendMovieToUser")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(request.toJSONString()))
        .andExpect(status().isBadRequest());
    }
}
