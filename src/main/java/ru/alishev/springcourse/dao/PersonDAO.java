package ru.alishev.springcourse.dao;

import org.springframework.stereotype.Component;
import ru.alishev.springcourse.models.Person;

import java.sql.*;
import java.util.*;
import java.util.Date;

@Component
public class PersonDAO
{
    //todo move to props
    public static final String DB_URL = "jdbc:postgresql://localhost:5432/springcourse_1_db";
    public static final String DB_USERNAME = "postgres";
    public static final String DB_PASS = "fuantadzi";

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
                Person person = fillPersonObj(resultSet);
                res.add(person);
            }
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
        return res;
    }

    private Person fillPersonObj(ResultSet resultSet) throws SQLException
    {
        return new Person
        (
            resultSet.getInt("id"),
            resultSet.getString("name"),
            resultSet.getInt("age"),
            resultSet.getString("email")
        );
    }

    public Person show(int id)
    {
        Person person = null;
        try
        {
            PreparedStatement statement = connection.prepareStatement("select * from person where id = ?");

            statement.setInt(1, id);

            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next())
            {
                person = fillPersonObj(resultSet);
            }
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
        return person;
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
