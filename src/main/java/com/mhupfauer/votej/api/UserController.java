package com.mhupfauer.votej.api;

import com.mhupfauer.votej.persistence.Entity.UserEnt;
import com.mhupfauer.votej.persistence.Repo.UserEntRepository;
import com.mhupfauer.votej.security.AuthenticatedUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("api/user")
public class UserController {
    @Autowired
    private UserEntRepository userEntRepository;

    @Autowired
    private Helper helper;

    @Secured("ROLE_ADMIN")
    @GetMapping("/")
    public List<UserEnt> getAllUser() {
        return userEntRepository.findAll();
    }

    @Secured("ROLE_ADMIN")
    @GetMapping("/{search}")
    public Object searchUser(@PathVariable(name = "search") String searchStr) {
        if (searchStr.contains("@")) {
            return userEntRepository.findByEmail(searchStr);
        }
        if(userEntRepository.findById(Long.parseLong(searchStr)).isEmpty())
            return new ResponseEntity<HttpStatus>(HttpStatus.NOT_FOUND);
        return userEntRepository.findById(Long.parseLong(searchStr)).get();
    }

    @Secured({"ROLE_USER", "ROLE_ADMIN"})
    @GetMapping("/me")
    public Map<String, Object> getMe() {
        UserEnt userEnt = ((AuthenticatedUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUser();
        return helper.getMapWithoutFields(userEnt, "pass", "role");
    }

    @PostMapping("/")
    public ResponseEntity<HttpStatus> registerUser(@RequestBody Map<String, Object> fields) {
        if(userEntRepository.findByEmail(fields.get("email").toString()) != null)
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);

        if(!fields.containsKey("pass") || fields.containsKey("id"))
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        String password = new BCryptPasswordEncoder(BCryptPasswordEncoder.BCryptVersion.$2B, 10)
                                            .encode(fields.get("pass").toString());

        helper.changeField(fields, "email", fields.get("email").toString().toLowerCase());
        helper.changeField(fields, "pass", password);
        helper.changeField(fields, "role", "USER");

        UserEnt userEnt = new UserEnt();
        helper.setFields(fields, userEnt);
        userEntRepository.save(userEnt);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Secured("ROLE_ADMIN")
    @PatchMapping("/admin/{user_id}")
    public ResponseEntity<HttpStatus> patchUserAdmin(@PathVariable(name = "user_id") Long uid, @RequestBody Map<String, Object> fields)
    {
        if(userEntRepository.findById(uid).isEmpty())
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        if(fields.containsKey("id"))
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        if(fields.containsKey("email") && userEntRepository.findByEmail(fields.get("email").toString()) != null)
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);

        if(fields.containsKey("pass"))
            helper.changeField(fields, "pass", new BCryptPasswordEncoder(BCryptPasswordEncoder.BCryptVersion.$2B, 10).encode(fields.get("pass").toString()));

        UserEnt userEnt = userEntRepository.findById(uid).get();
        helper.setFields(fields, userEnt);
        userEntRepository.save(userEnt);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PatchMapping("/{user_id}")
    public ResponseEntity<HttpStatus> patchUser(@PathVariable(name = "user_id") Long uid, @RequestBody Map<String, Object> fields)
    {
        UserEnt currentUser = ((AuthenticatedUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal())
                                .getUser();

        if (currentUser.getId().equals(uid))
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);

        if(userEntRepository.findById(uid).isEmpty())
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        if(fields.containsKey("role") || fields.containsKey("id"))
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        if(fields.containsKey("email") && userEntRepository.findByEmail(fields.get("email").toString()) != null)
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);

        if(fields.containsKey("pass")) {
            String pass = new BCryptPasswordEncoder(BCryptPasswordEncoder.BCryptVersion.$2B, 10)
                                .encode(fields.get("pass").toString());
            helper.changeField(fields, "pass", pass);
        }

        UserEnt userEnt = userEntRepository.findById(uid).get();

        helper.setFields(fields, userEnt);
        userEntRepository.save(userEnt);

        return new ResponseEntity<>(HttpStatus.OK);
    }
}
