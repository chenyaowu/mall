package com.chen.mall.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class DemoController {

    @RequestMapping("/index")
    public String index(Model model){
        model.addAttribute("name","chenyaowu");
        return "index";
    }
}
