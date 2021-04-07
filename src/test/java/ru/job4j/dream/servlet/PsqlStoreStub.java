package ru.job4j.dream.servlet;

import ru.job4j.dream.model.Candidate;
import ru.job4j.dream.model.City;
import ru.job4j.dream.model.Post;
import ru.job4j.dream.model.User;
import ru.job4j.dream.store.Store;

import java.sql.SQLException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class PsqlStoreStub implements Store {
    private final Map<Integer, Post> store = new HashMap<>();

    @Override
    public Collection<Post> findAllPosts() {
        return store.values();
    }

    @Override
    public Collection<Candidate> findAllCandidates() {
        return null;
    }

    @Override
    public Collection<City> findAllCities() {
        return null;
    }

    @Override
    public void save(Post post) {
        this.store.put(post.getId(), post);
    }

    @Override
    public void save(Candidate candidate) {

    }

    @Override
    public void save(User user) {

    }

    @Override
    public void save(City city) {

    }

    @Override
    public Post findPostById(int id) {
        return store.get(id);
    }

    @Override
    public Candidate findCanById(int id) {
        return null;
    }

    @Override
    public User findUserByEmail(String email) {
        return null;
    }

    @Override
    public City findCityById(int id) {
        return null;
    }

    @Override
    public void deleteCan(Candidate candidate) {

    }

    @Override
    public void deleteUser(User user) {

    }
}
