package ru.alishev.springcourse.dao;

import com.github.javafaker.Faker;
import org.springframework.stereotype.Component;
import ru.alishev.springcourse.models.Person;

import java.util.*;

@Component
public class PersonDAO
{
    private static int PEOPLE_COUNT;

    private final List<Person> people;

    {
        people = new ArrayList<>();

        Random random = new Random(42);
        for (int i = 0; i < 15; i++)
        {
            Faker faker = Faker.instance(new Random(random.nextInt()));
            String name = faker.name().fullName();

            people.add(new Person(++PEOPLE_COUNT, name, Math.abs(random.nextInt()) + 1, String.format("%s@%s.com", name.replaceAll(" ", "_"), faker.cat())));
        }
    }

    public List<Person> index()
    {
        return people;
    }

    public Person show(int id)
    {
        return people.stream().filter(person -> person.getId() == id).findAny().orElse(null);
    }

    public void save(Person person)
    {
        person.setId(++PEOPLE_COUNT);
        people.add(person);
    }

    public void update(int id, Person updatedPerson)
    {
        Person personToBeUpdated = show(id);

        personToBeUpdated.setName(updatedPerson.getName());
        personToBeUpdated.setAge(updatedPerson.getAge());
        personToBeUpdated.setEmail(updatedPerson.getEmail());
    }

    public void delete(int id)
    {
        people.removeIf(p -> p.getId() == id);
    }
}
