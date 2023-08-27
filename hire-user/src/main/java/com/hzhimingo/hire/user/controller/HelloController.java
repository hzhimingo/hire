package com.hzhimingo.hire.user.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("u")
public class HelloController {

    @GetMapping
    public Object hello() {
        return "Hello UserService~";
    }
}
