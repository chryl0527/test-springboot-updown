package com.chryl.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * Created By Chr on 2019/7/11.
 */
@RestController
@RequestMapping("/img")
public class ImgController {

    @GetMapping("/1")
    public Object show(HttpServletRequest request) {

        Map<String, Object> map = new HashMap<>();
        String[] arr = {"abc", "aqz", "架构大纲"};
        map.put("data", arr);//imgurl
        map.put("status", "suc");
        return map;
    }


}
