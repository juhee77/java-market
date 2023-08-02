package com.lahee.market.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class WebController {

    @GetMapping(value =  {"/", "/home","","/item-view/**","/item-add-view"})
    public String forward() {
        return "forward:/static/index.html";
    }
}