package ru.alishev.springcourse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class GoodbyeController
{
    @RequestMapping(method = RequestMethod.GET, value = "/goodbye-world")
    public String sayGoodbye()
    {
        return "goodbye_world";
    }
}
