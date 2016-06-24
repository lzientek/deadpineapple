package com.deadpineapple.front.controllers;

import com.deadpineapple.front.Forms.LoginForm;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Created by saziri on 10/03/2016.
 */
@Controller
@RequestMapping({"/index", "/"})
public class HomeController {
    @RequestMapping(method= RequestMethod.GET)
    public String index(Model model, LoginForm loginForm){
        model.addAttribute("loginAttribute", loginForm);
        return "index";
    }
}
