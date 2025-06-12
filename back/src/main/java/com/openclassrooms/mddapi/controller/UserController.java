package com.openclassrooms.mddapi.controller;

import com.openclassrooms.mddapi.model.User;
import com.openclassrooms.mddapi.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/utilisateurs")
public class UserController {

    private final UserService utilisateurService;

    @Autowired
    public UserController(UserService utilisateurService) {
        this.utilisateurService = utilisateurService;
    }


}