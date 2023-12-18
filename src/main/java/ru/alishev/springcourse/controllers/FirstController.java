package ru.alishev.springcourse.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/first")
public class FirstController
{
    @GetMapping("/hello")
    public String helloPage(HttpServletRequest request)
    {
        String name = request.getParameter("name");
        System.out.println("helloPage:" + (name == null ? "<null>" : name));
        return "first/hello";
    }

    @GetMapping("/goodbye")
    public String goodbyePage(@RequestParam(value = "name", required = false)String name, Model model)
    {
        System.out.println("goodbyePage: " + (name == null ? "<null>" : name));
        model.addAttribute("staticMessage", "message test");
        model.addAttribute("message", "Name: " + (name == null ? "<null>" : name));
        return "first/goodbye";
    }

    @GetMapping("/any")
    public String anyPage(@RequestParam(value = "page", defaultValue = "first/hello")String page)
    {
        System.out.println("anyPage: " + (page == null ? "<null>" : page));
        return page;
    }

}
