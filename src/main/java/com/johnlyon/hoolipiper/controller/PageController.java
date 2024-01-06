package com.johnlyon.hoolipiper.controller;

import com.johnlyon.hoolipiper.mapper.ManagerMapper;
import com.johnlyon.hoolipiper.pojo.Manager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
public class PageController {
    @Autowired
    private ManagerMapper managerMapper;
    @RequestMapping("/")
    public String index() {
        return "/index";
    }
    @RequestMapping("/{page}")
    public String indexPage(@PathVariable String page) {
        return "/" + page;
    }

    @RequestMapping("/api/manager")
    @ResponseBody
    public List<Manager> manger() {
        return managerMapper.selectList(null);
    }

}
