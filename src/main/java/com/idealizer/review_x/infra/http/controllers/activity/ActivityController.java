package com.idealizer.review_x.infra.http.controllers.activity;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/activities")
public class ActivityController {

    @GetMapping
    public String test() {
        return "Feed Activity works!";
    }
}
