package com.lahee.market.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class WebController {

    @GetMapping(value = {"/home", "/", "","/login/**", "/signup","/index.html", "/profile",
            "/item-add-view", "/item-view/**", "/chatroomlist-view", "/chatroom-view/**"})
    public String forward() {
        return "forward:/static/index.html";
    }
}