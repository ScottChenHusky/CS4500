package edu.northeastern.cs4500.controllers.customer;

import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class CustomerFollowController {
    @Autowired
    private CustomerFollowRepository customerFollowingRepository;

    @RequestMapping(path = "/api/user/following/{id}", method = RequestMethod.GET)
    public ResponseEntity<JSONObject> getFollowings(@PathVariable(name = "id") String id) {
        return ResponseEntity.ok(new JSONObject());
    }

    @RequestMapping(path = "/api/user/follower/{id}", method = RequestMethod.GET)
    public ResponseEntity<JSONObject> getFollowers(@PathVariable(name = "id") String id) {
        return ResponseEntity.ok(new JSONObject());
    }

    @RequestMapping(path = "/api/user/follow", method = RequestMethod.POST)
    public ResponseEntity<JSONObject> follow(@RequestBody JSONObject ids) {
        return ResponseEntity.ok(new JSONObject());
    }
    
    @RequestMapping(path = "/api/user/un-follow", method = RequestMethod.POST)
    public ResponseEntity<JSONObject> unFollow(@RequestBody JSONObject ids) {
        return ResponseEntity.ok(new JSONObject());
    }
}
