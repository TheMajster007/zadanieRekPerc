package pl.apanowicz.backDPC.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api")
@Slf4j
public class UserAPI {

    @Autowired
    UserRepository userRepo;

    User user;

    @GetMapping("/user")
    public User open(){
        return user;
    }

    public boolean makeUser(){
        user = userRepo.generateUser();
        if(user==null) {
            log.error("failed to create user");
            return false;
        }

        //log.info("User was generated correctly");
            return true;
    }



}
