package com.ylq.demo.web;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Class description goes here
 * Created by ylq on 15/12/17 上午11:34.
 */
@RestController
@RequestMapping(value = "/user")
public class MyRestController {
    @RequestMapping(value = "/{user}",method= RequestMethod.GET)
    public String getUser(@PathVariable Long user) {
        return "hello" + user;
    }
}
