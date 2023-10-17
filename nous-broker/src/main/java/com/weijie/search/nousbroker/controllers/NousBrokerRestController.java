package com.weijie.search.nousbroker.controllers;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class NousBrokerRestController {
    @GetMapping("/search")
    public String search() {
        return "Test!!";
    }
}
