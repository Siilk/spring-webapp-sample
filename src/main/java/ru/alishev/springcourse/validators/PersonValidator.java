package ru.alishev.springcourse.validators;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import ru.alishev.springcourse.dao.PersonDAO;
import ru.alishev.springcourse.models.Person;

@Component
public class PersonValidator implements Validator
{
    private final PersonDAO personDAO;

    @Autowired
    public PersonValidator(PersonDAO personDAO)
    {
        this.personDAO = personDAO;
    }

    @Override
    public boolean supports(Class<?> clazz)
    {
        return Person.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors)
    {
        Person person = (Person)target;
        if (personDAO.getByEmail(person.getEmail()) != null)
        {
            errors.rejectValue("email", "", "Email already exists");
        }
    }
}
