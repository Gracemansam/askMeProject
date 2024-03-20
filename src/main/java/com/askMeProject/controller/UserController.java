package com.askMeProject.controller;

import com.askMeProject.service.UserService;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RestController
//@CrossOrigin(origins = "*")
@RequestMapping("/user/")
public class UserController {

   @Autowired
   private UserService userService;

}
