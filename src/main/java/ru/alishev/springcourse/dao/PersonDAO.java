package ru.alishev.springcourse.dao;

import com.github.javafaker.Faker;
import org.springframework.stereotype.Component;
import ru.alishev.springcourse.models.Person;

import java.util.*;

@Component
public class PersonDAO
{
    private final List<Person> people;

    {
        people = new ArrayList<>();

        Random random = new Random(42);
        for (int i = 0; i < 15; i++)
        {
            Faker faker = Faker.instance(new Random(random.nextInt()));
            people.add(new Person(i, faker.name().fullName()));
        }
    }

    public List<Person> getPeople()
    {
        return new ArrayList<>(people);
    }

    public Person getById(long id)
    {
        return (id < 0 || people.size() < id) ? null : people.get((int)id);
    }
}
