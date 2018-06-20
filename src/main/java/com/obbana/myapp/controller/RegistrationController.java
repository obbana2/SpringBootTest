package com.obbana.myapp.controller;

import com.obbana.myapp.domain.Role;
import com.obbana.myapp.domain.User;
import com.obbana.myapp.repos.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Map;

@Controller
public class RegistrationController {
    @Autowired
    private UserRepo userRepo;

    @GetMapping("/registration")
    public String registration() {
        return "registration";
    }

    @PostMapping("/registration")
    public String addUser(User user, Map<String, Object> model) {
        User userFromDb = userRepo.findByUsername(user.getUsername());

        if (userFromDb != null) {
            model.put("message", "Пользователь уже зарегистрирован!");
            return "registration";
        }

        user.setActive(true);
        if(userRepo.count() > 0)
            user.setRoles(Role.USER.name());
        else
            user.setRoles(Role.ADMIN.name());
        userRepo.save(user);

        return "redirect:/login";
    }
}