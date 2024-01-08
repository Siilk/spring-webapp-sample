package ru.alishev.springcourse.dao;

import com.github.javafaker.Address;
import com.github.javafaker.Faker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.alishev.springcourse.models.Person;

import java.sql.*;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class PersonDAO
{
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public PersonDAO(JdbcTemplate jdbcTemplate)
    {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Person> getAll()
    {
        return jdbcTemplate.query("select * from person", new BeanPropertyRowMapper<>(Person.class));
    }

    public Person getById(int id)
    {
        List<Person> query = jdbcTemplate.query("select * from person where id = ?", new Object[]{id}, new BeanPropertyRowMapper<>(Person.class));
        return (query == null || query.isEmpty()) ? null : query.get(0);
    }

    public Person getByEmail(String email)
    {
        List<Person> query = jdbcTemplate.query("select * from person where email = ?", new Object[]{email}, new BeanPropertyRowMapper<>(Person.class));
        return (query == null || query.isEmpty()) ? null : query.get(0);
    }

    public void save(Person person)
    {
        jdbcTemplate.update("insert into person(name, age, email, address) values(?, ?, ?, ?)", person.getName(), person.getAge(), person.getEmail(), person.getAddress());
    }

    public void update(int id, Person updatedPerson)
    {
        jdbcTemplate.update("update person set name = ?, age = ?, email = ?, address = ? where id = ?",
            updatedPerson.getName(), updatedPerson.getAge(), updatedPerson.getEmail(), updatedPerson.getId(), updatedPerson.getAddress());
    }

    public void delete(int id)
    {
        jdbcTemplate.update("delete from person where id = ?", id);
    }

    public void testMultipleUpdate()
    {
        long start = System.currentTimeMillis();
        generatePeople(1000).forEach
        (
            person -> jdbcTemplate.update("insert into person(name, age, email, address) values(?, ?, ?, ?)", person.getName(), person.getAge(), person.getEmail(), person.getAddress())
        );

        System.out.println("No batch duration: " + (System.currentTimeMillis() - start));
    }

    public void testBatchUpdate()
    {
        long start = System.currentTimeMillis();
        final List<Person> people = generatePeople(1000);

        jdbcTemplate.batchUpdate("insert into person(name, age, email, address) values(?, ?, ?, ?)", new BatchPreparedStatementSetter()
        {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException
            {
                Person person = people.get(i);
                ps.setString(1, person.getName());
                ps.setInt(2, person.getAge());
                ps.setString(3, person.getEmail());
                ps.setString(4, person.getAddress());
            }

            @Override
            public int getBatchSize()
            {
                return people.size();
            }
        });

        System.out.println("No batch duration: " + (System.currentTimeMillis() - start));
    }

    private List<Person> generatePeople(int number)
    {
        Random random = new Random();
        Faker faker = Faker.instance(new Locale("en-AU"), random);
        return Collections.nCopies(number, null).stream().map
        (
            empty -> {
                String  name = faker.name().fullName();
                Address address = faker.address();
                return new Person
                (
                    -1,
                    name,
                    (int)Math.abs(Math.round(Math.random() * 100) + 1),
                    String.format("%s@%s", name, faker.cat().name()).replaceAll(" ", "_").replaceAll("\\.", "") + ".com",
                    address.country().replaceAll(" ", "").replaceAll("\\.", "").replaceAll(",", "").replaceAll("-", "") + ", " + address.cityName().replaceAll(" ", "") + ", " + address.zipCode()
                );
            }
        ).collect(Collectors.toList());
    }

    public static void main(String[] args)
    {
        PersonDAO personDAO = new PersonDAO(null);

        personDAO.generatePeople(10).forEach(System.out::println);
    }
}
