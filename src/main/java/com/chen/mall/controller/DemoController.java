package com.chen.mall.controller;

import com.chen.mall.rabbitmq.MQSender;
import com.chen.mall.result.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class DemoController {

    @Autowired
    private MQSender mqSender;

    @RequestMapping("/index")
    @ResponseBody
    public Result<String> index(Model model){
        model.addAttribute("name","chenyaowu");
//        mqSender.send("hello rabbitMq");
//        mqSender.sendTopic("topic rabbitMq");
        return Result.success("Hello SpringBoot");
    }
}
