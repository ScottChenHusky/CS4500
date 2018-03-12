package edu.northeastern.cs4500.controllers.customer;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
public class CustomerFollowingController {

    @Autowired
    private CustomerFollowingRepository repository;

    @RequestMapping(path = "/api/user/following/{id}", method = RequestMethod.GET)
    public ResponseEntity<JSONObject> getFollowing(@PathVariable(name = "id") String id) {
        List<CustomerFollowing> result = repository.findByCustomerFromId(Integer.parseInt(id));
        JSONObject json = new JSONObject();
        JSONArray array = new JSONArray();
        for (CustomerFollowing each : result) {
            JSONObject following = new JSONObject();
            following.put("following", each.getCustomerToId());
            following.put("date", each.getDate().toString());
            array.add(following);
        }
        json.put("result", array);
        return ResponseEntity.ok(json);
    }

    @RequestMapping(path = "/api/user/followers/{id}", method = RequestMethod.GET)
    public ResponseEntity<JSONObject> getFollowers(@PathVariable(name = "id") String id) {
        List<CustomerFollowing> result = repository.findByCustomerToId(Integer.parseInt(id));
        JSONObject json = new JSONObject();
        JSONArray array = new JSONArray();
        for (CustomerFollowing each : result) {
            JSONObject follower = new JSONObject();
            follower.put("follower", each.getCustomerFromId());
            follower.put("date", each.getDate().toString());
            array.add(follower);
        }
        json.put("result", array);
        return ResponseEntity.ok(json);
    }

    @RequestMapping(path = "/api/user/follow", method = RequestMethod.POST)
    public ResponseEntity<String> follow(@RequestBody JSONObject request) {
        String fromStr = request.getOrDefault("from", "").toString();
        String toStr = request.getOrDefault("to", "").toString();
        if (fromStr.isEmpty() || toStr.isEmpty()) {
            return ResponseEntity.badRequest().body("\"from\" and \"to\" must be specified");
        }
        Integer from = Integer.parseInt(fromStr);
        Integer to = Integer.parseInt(toStr);
        List<CustomerFollowing> result = repository.findByCustomerFromIdAndAndCustomerToId(from, to);
        if (result.isEmpty()) {
            CustomerFollowing following = new CustomerFollowing()
                    .withCustomerFromId(from)
                    .withCustomerToId(to)
                    .withDate(new Date());
            repository.save(following);
            return ResponseEntity.ok().body("follow successful");
        } else {
            return ResponseEntity.status(409).body("follow failed");
        }
    }

    @RequestMapping(path = "/api/user/un-follow", method = RequestMethod.POST)
    public ResponseEntity<String> unFollow(@RequestBody JSONObject request) {
        String fromStr = request.getOrDefault("from", "").toString();
        String toStr = request.getOrDefault("to", "").toString();
        if (fromStr.isEmpty() || toStr.isEmpty()) {
            return ResponseEntity.badRequest().body("\"from\" and \"to\" must be specified");
        }
        Integer from = Integer.parseInt(fromStr);
        Integer to = Integer.parseInt(toStr);
        List<CustomerFollowing> result = repository.findByCustomerFromIdAndAndCustomerToId(from, to);
        if (!result.isEmpty()) {
            repository.delete(result.get(0).getId());
            return ResponseEntity.ok().body("un-follow successful");
        } else {
            return ResponseEntity.status(409).body("un-follow failed");
        }
    }

}
