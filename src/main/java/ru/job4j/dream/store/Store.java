package ru.job4j.dream.store;

import ru.job4j.dream.model.Candidate;
import ru.job4j.dream.model.City;
import ru.job4j.dream.model.Post;
import ru.job4j.dream.model.User;

import java.sql.SQLException;
import java.util.Collection;

public interface Store {
    Collection<Post> findAllPosts();
    Collection<Candidate> findAllCandidates();
    Collection<City> findAllCities();

    void save(Post post);
    void save(Candidate candidate);
    void save(User user) throws SQLException;
    void save(City city) throws SQLException;

    Post findPostById(int id);
    Candidate findCanById(int id);
    User findUserByEmail(String email);
    City findCityById(int id);


    void deleteCan(Candidate candidate);
    void deleteUser(User user);
}
