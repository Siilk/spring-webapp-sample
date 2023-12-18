package ru.alishev.springcourse.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.alishev.springcourse.dao.PersonDAO;

@Controller
@RequestMapping("/people")
public class PeopleController
{
    private final PersonDAO personDAO;

    public PeopleController(PersonDAO personDAO)
    {
        this.personDAO = personDAO;
    }

    @GetMapping
    public String index(Model model)
    {
        model.addAttribute("people", personDAO.getPeople());
        return "people/index";
    }

    @GetMapping("/{id}")
    public String showPerson(@PathVariable("id") long id, Model model)
    {
        model.addAttribute("person", personDAO.getById(id));
        return "people/person";
    }
}
