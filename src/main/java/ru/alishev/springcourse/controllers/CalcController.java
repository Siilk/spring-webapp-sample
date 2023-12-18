package ru.alishev.springcourse.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.thymeleaf.util.StringUtils;

@Controller
@RequestMapping("/calc")
public class CalcController
{
    @GetMapping("/do")
    public String doCalc(@RequestParam(name = "a", required = false)String a,
                         @RequestParam(name = "b", required = false)String b,
                         @RequestParam(name = "op", required = false)String op,
                         Model model)
    {

        String res = "";
        boolean isOk = true;
        if (StringUtils.isEmpty(a))
        {
            res += "a is required\n";
            isOk = false;
        }
        if (StringUtils.isEmpty(b))
        {
            res += "b is required\n";
            isOk = false;
        }
        if (StringUtils.isEmpty(op))
        {
            res += "op is required\n";
            isOk = false;
        }
        if (isOk)
        {
            System.out.println("a = " + a);
            System.out.println("b = " + b);
            System.out.println("op = " + op);
            try
            {
                switch (op)
                {
                    case "a":
                    case "add":
                    case "+":
                        res += Float.parseFloat(a) + Float.parseFloat(b);
                        break;
                    case "s":
                    case "sub":
                    case "-":
                        res += Float.parseFloat(a) - Float.parseFloat(b);
                        break;
                    case "d":
                    case "div":
                    case "/":
                        res += Float.parseFloat(a) / Float.parseFloat(b);
                        break;
                    case "m":
                    case "mul":
                    case "*":
                        res += Float.parseFloat(a) * Float.parseFloat(b);
                        break;
                    default:
                        res += "Unknown operation";

                }
            }
            catch (NumberFormatException nfe)
            {
                res += "Invalid number format";
            }
            catch (Throwable t)
            {
                res += "Error occurred: " + t.getMessage();
            }

        }
        model.addAttribute("res", res);
        System.out.println("res = " + res);
        return "calc/basic";
    }
}
