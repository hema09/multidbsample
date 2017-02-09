package com.hema.examples.multidbsample.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by hemabhatia on 2/7/17.
 */
@Controller
public class MainController {

    @RequestMapping("/")
    @ResponseBody
    public String index() {
        return "test";
    }

}
