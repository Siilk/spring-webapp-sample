package ru.alishev.springcourse.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.alishev.springcourse.models.Person;

import java.sql.*;
import java.util.*;

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
            Math.abs((int)(System.currentTimeMillis() / 1000)), person.getName(), person.getAge(), person.getEmail());
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
}
