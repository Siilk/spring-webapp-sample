package ru.alishev.springcourse.dao;

import com.github.javafaker.Faker;
import com.github.javafaker.Name;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
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
    //todo move to props
    public static final String DB_DRIVER_CLASS = "org.postgresql.Driver";
    public static final String DB_URL = "jdbc:postgresql://localhost:5432/springcourse_1_db";
    public static final String DB_USERNAME = "postgres";
    public static final String DB_PASS = "fuantadzi";

    public static Connection connection;

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public PersonDAO(JdbcTemplate jdbcTemplate)
    {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Person> index()
    {
        return jdbcTemplate.query("select * from person", new BeanPropertyRowMapper<>(Person.class));
    }

    public Person show(int id)
    {
        List<Person> query = jdbcTemplate.query("select * from person where id = ?", new Object[]{id}, new BeanPropertyRowMapper<>(Person.class));
        return (query == null || query.isEmpty()) ? null : query.get(0);
    }

    public void save(Person person)
    {
        jdbcTemplate.update("insert into person values(?, ?, ?, ?)",
            Math.abs(System.currentTimeMillis()), person.getName(), person.getAge(), person.getEmail());
    }

    public void update(int id, Person updatedPerson)
    {
        jdbcTemplate.update("update person set name = ?, age = ?, email = ? where id = ?",
            updatedPerson.getName(), updatedPerson.getAge(), updatedPerson.getEmail(), updatedPerson.getId());
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
            person -> jdbcTemplate.update("insert into person values(?, ?, ?, ?)", person.getId(), person.getName(), person.getAge(), person.getEmail())
        );

        System.out.println("No batch duration: " + (System.currentTimeMillis() - start));
    }

    public void testBatchUpdate()
    {
        long start = System.currentTimeMillis();
        final List<Person> people = generatePeople(1000);

        jdbcTemplate.batchUpdate("insert into person values(?, ?, ?, ?)", new BatchPreparedStatementSetter()
        {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException
            {
                Person person = people.get(i);
                ps.setLong(1, person.getId());
                ps.setString(2, person.getName());
                ps.setInt(3, person.getAge());
                ps.setString(4, person.getEmail());
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
        Faker faker = Faker.instance(random);
        List<Person> people = Collections.nCopies(number, null).stream().map
        (
            empty -> {
                String  name = faker.name().fullName();
                return new Person
                (
                    System.currentTimeMillis(),
                    name,
                    (int)Math.abs(Math.round(Math.random() * 100) + 1),
                    String.format("%s@%s", name, faker.cat().name()).replaceAll(" ", "_").replaceAll("\\.", "") + ".com"
                );
            }
        ).collect(Collectors.toList());

        return people;
    }

    public static void main(String[] args)
    {
        PersonDAO personDAO = new PersonDAO(null);

        personDAO.generatePeople(10).forEach(System.out::println);
    }
}
