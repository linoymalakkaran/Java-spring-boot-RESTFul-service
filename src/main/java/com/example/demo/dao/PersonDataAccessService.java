package com.example.demo.dao;

import com.example.demo.model.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository("postgres")
public class PersonDataAccessService implements PersonDao {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public PersonDataAccessService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public int insertPerson(UUID id, Person person) {
//        Map<String, Object> params = new HashMap<>();
//        params.put("id",id);
//        params.put("name",person.getName());
//        int status = jdbcTemplate.update(
//                "INSERT INTO person (id, name) VALUES (?, ?)",
//                params
//        );
        int status = jdbcTemplate.update(
                "INSERT INTO person (id, name) VALUES (?, ?)",
                id, person.getName()
        );
        return status;
    }

    @Override
    public List<Person> selectAllPeople() {
        final String sql = "SELECT id, name FROM person";
        List<Person> people = jdbcTemplate.query(sql, (resultSet, i) -> {
            UUID id = UUID.fromString(resultSet.getString("id"));
            String name = resultSet.getString("name");
            return new Person(id, name);
        });
        return people;
    }

    @Override
    public Optional<Person> getPersonById(UUID id) {
        final String sql = "SELECT id, name FROM person WHERE id = ?";
        Person person = jdbcTemplate.queryForObject(sql,
                new Object[]{id},
                (resultSet, i) -> {
                    UUID personId = UUID.fromString(resultSet.getString("id"));
                    String name = resultSet.getString("name");
                    return new Person(personId, name);
                });
        return Optional.ofNullable(person);
    }

    @Override
    public int deletePersonById(UUID id) {
        int status = jdbcTemplate.update(
                "DELETE FROM person WHERE id = ?",
                id
        );
        return status;
    }

    @Override
    public int updatePersonById(UUID id, Person person) {
//        Map<String, Object> params = new HashMap<>();
//        params.put("id",id);
//        params.put("name",person.getName());
        int status = jdbcTemplate.update(
                "UPDATE person set name = ? WHERE id = ?",
                person.getName(), id
        );
        return status;
    }
}
