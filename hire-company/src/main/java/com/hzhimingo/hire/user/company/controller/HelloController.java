package com.hzhimingo.hire.user.company.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("c")
public class HelloController {

    @GetMapping
    public Object hello() {
        return "Hello CompanyService~";
    }
}
