package ru.alishev.springcourse.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.alishev.springcourse.dao.PersonDAO;
import ru.alishev.springcourse.models.Person;

@Controller
@RequestMapping("/admin")
public class AdminController
{
    private final PersonDAO personDAO;

    @Autowired
    public AdminController(PersonDAO personDAO)
    {
        this.personDAO = personDAO;
    }

    @GetMapping
    public String adminPage(Model model, @ModelAttribute("person") Person person)
    {
        model.addAttribute("people", personDAO.getAll());

        return "adminPage";
    }

    @PostMapping("/add")
    public String addAdmin(@ModelAttribute("person") Person person)
    {
        System.out.println(person.toString());
        long personId = person.getId();
        return String.format("redirect:/people/%d", personId);
    }
}
