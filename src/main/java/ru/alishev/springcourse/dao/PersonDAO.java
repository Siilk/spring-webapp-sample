package ru.alishev.springcourse.dao;

import org.springframework.beans.factory.annotation.Autowired;
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
        return jdbcTemplate.query("select * from person", new PersonMapper());
    }

    public Person show(int id)
    {
        List<Person> query = jdbcTemplate.query("select * from person where id = ?", new Object[]{id}, new PersonMapper());
        return (query == null || query.isEmpty()) ? null : query.get(0);
    }

    public void save(Person person)
    {
        person.setId(Math.abs((int)(System.currentTimeMillis() / 1000)));

        try
        {
            PreparedStatement statement = connection.prepareStatement("insert into person values(?, ?, ?, ?)");
            statement.setInt(1, person.getId());
            statement.setString(2, person.getName());
            statement.setInt(3, person.getAge());
            statement.setString(4, person.getEmail());

            int result = statement.executeUpdate();

            System.out.println("save executeUpdate result: " + result);
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
    }

    public void update(int id, Person updatedPerson)
    {
        try
        {
            PreparedStatement statement = connection.prepareStatement("update person set name = ?, age = ?, email = ? where id = ?");
            statement.setString(1, updatedPerson.getName());
            statement.setInt(2, updatedPerson.getAge());
            statement.setString(3, updatedPerson.getEmail());
            statement.setInt(4, updatedPerson.getId());

            int result = statement.executeUpdate();

            System.out.println("update executeUpdate result: " + result);
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
    }

    public void delete(int id)
    {
        try
        {
            PreparedStatement statement = connection.prepareStatement("delete from person where id = ?");
            statement.setInt(1, id);

            int result = statement.executeUpdate();

            System.out.println("delete executeUpdate result: " + result);
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
    }
}
