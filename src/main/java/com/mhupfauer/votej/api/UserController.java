package com.mhupfauer.votej.api;

import com.mhupfauer.votej.persistence.Entity.UserEnt;
import com.mhupfauer.votej.persistence.Repo.UserEntRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/user")
public class UserController {
    @Autowired
    private UserEntRepository userEntRepository;

    @GetMapping("/")
    public List<UserEnt> getAllUser(){
        return userEntRepository.findAll();
    }

    @GetMapping("/{search}")
    public Object searchUser(@PathVariable(name = "search")String searchStr){
        if(searchStr.contains("@")){
            return userEntRepository.findByEmail(searchStr);
        }
        return userEntRepository.findById(Long.parseLong(searchStr)).get();
    }
}
