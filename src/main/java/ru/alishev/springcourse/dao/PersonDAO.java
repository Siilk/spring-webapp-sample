package ru.alishev.springcourse.dao;

import com.github.javafaker.Faker;
import org.springframework.stereotype.Component;
import ru.alishev.springcourse.models.Person;

import java.sql.*;
import java.util.*;

@Component
public class PersonDAO
{
    //todo move to props
    public static final String DB_URL = "jdbc:postgresql://localhost:5432/springcourse_1_db";
    public static final String DB_USERNAME = "postgres";
    public static final String DB_PASS = "fuantadzi";

    private static int PEOPLE_COUNT;

    public static Connection connection;

    static
    {
        try
        {
            Class.forName("org.postgresql.Driver");
        }
        catch (ClassNotFoundException e)
        {
            e.printStackTrace();
        }

        try
        {
            connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASS);
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
    }

    public List<Person> index()
    {
        List<Person> res = new ArrayList<>();

        try
        {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("select * from person");

            while (resultSet.next())
            {
                Person person = new Person
                (
                    resultSet.getInt("id"),
                    resultSet.getString("name"),
                    resultSet.getInt("age"),
                    resultSet.getString("email")
                );
                res.add(person);
            }
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
        return res;
    }

    public Person show(int id)
    {
        return null;
//        return people.stream().filter(person -> person.getId() == id).findAny().orElse(null);
    }

    public void save(Person person)
    {
        person.setId(++PEOPLE_COUNT);

        try
        {
            Statement statement = connection.createStatement();
            int i = statement.executeUpdate(String.format("insert into person values(%d, '%s', %d, '%s')", person.getId(), person.getName(), person.getAge(), person.getEmail()));
            System.out.println("save executeUpdate result: " + i);
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
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
//        people.removeIf(p -> p.getId() == id);
    }
}
