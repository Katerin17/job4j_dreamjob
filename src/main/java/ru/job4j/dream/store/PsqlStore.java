package ru.job4j.dream.store;

import org.apache.commons.dbcp2.BasicDataSource;
import ru.job4j.dream.model.Candidate;
import ru.job4j.dream.model.City;
import ru.job4j.dream.model.Post;
import ru.job4j.dream.model.User;

import java.io.BufferedReader;
import java.io.FileReader;
import java.sql.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Properties;

public class PsqlStore implements Store {
    private final BasicDataSource pool = new BasicDataSource();

    private PsqlStore() {
        Properties cfg = new Properties();
        try (BufferedReader io = new BufferedReader(
                new FileReader("db.properties")
        )) {
            cfg.load(io);
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
        try {
            Class.forName(cfg.getProperty("jdbc.driver"));
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
        pool.setDriverClassName(cfg.getProperty("jdbc.driver"));
        pool.setUrl(cfg.getProperty("jdbc.url"));
        pool.setUsername(cfg.getProperty("jdbc.username"));
        pool.setPassword(cfg.getProperty("jdbc.password"));
        pool.setMinIdle(5);
        pool.setMaxIdle(10);
        pool.setMaxOpenPreparedStatements(100);
    }

    private static final class Lazy {
        private static final Store INST = new PsqlStore();
    }

    public static Store instOf() {
        return Lazy.INST;
    }

    @Override
    public Collection<Post> findAllPosts() {
        List<Post> posts = new ArrayList<>();
        try (Connection cn = pool.getConnection();
             PreparedStatement ps =  cn.prepareStatement("SELECT * FROM posts")
        ) {
            try (ResultSet allPosts = ps.executeQuery()) {
                while (allPosts.next()) {
                    posts.add(new Post(allPosts.getInt("id"), allPosts.getString("name")));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return posts;
    }

    @Override
    public Collection<Candidate> findAllCandidates() {
        List<Candidate> candidates = new ArrayList<>();
        try (Connection cn = pool.getConnection();
             PreparedStatement ps = cn.prepareStatement("SELECT * FROM candidates")
        ) {
            try (ResultSet allCandidates = ps.executeQuery()) {
                while (allCandidates.next()) {
                    candidates.add(new Candidate(allCandidates.getInt("id"), allCandidates.getString("name")));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return candidates;
    }

    @Override
    public Collection<City> findAllCities() {
        List<City> cities = new ArrayList<>();
        try (Connection cn = pool.getConnection();
             PreparedStatement ps = cn.prepareStatement("SELECT * FROM cities")
        ) {
            try (ResultSet allCities = ps.executeQuery()) {
                while (allCities.next()) {
                    cities.add(new City(allCities.getInt("id"), allCities.getString("name")));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return cities;
    }

    @Override
    public void save(Post post) {
        if (post.getId() == 0) {
            create(post);
        } else {
            update(post);
        }
    }

    @Override
    public void save(Candidate candidate) {
        if (candidate.getId() == 0) {
            create(candidate);
        } else {
            update(candidate);
        }
    }

    @Override
    public void save(User user) throws SQLException {
        try (Connection cn = pool.getConnection();
             PreparedStatement ps = cn.prepareStatement("INSERT INTO users(name, email, password) VALUES (?, ?, ?)", PreparedStatement.RETURN_GENERATED_KEYS)
        ) {
            ps.setString(1, user.getName());
            ps.setString(2, user.getEmail());
            ps.setString(3, user.getPassword());
            ps.execute();
            try (ResultSet id = ps.getGeneratedKeys()) {
                if(id.next()) {
                    user.setId(id.getInt(1));
                }
            }
        }
    }

    @Override
    public void save(City city) throws SQLException {
        try (Connection cn = pool.getConnection();
             PreparedStatement ps = cn.prepareStatement("INSERT INTO cities(name) VALUES (?)", PreparedStatement.RETURN_GENERATED_KEYS)
        ) {
            ps.setString(1, city.getName());
            ps.execute();
            try (ResultSet id = ps.getGeneratedKeys()) {
                if(id.next()) {
                    city.setId(id.getInt(1));
                }
            }
        }
    }

    private void create(Post post) {
        try (Connection cn = pool.getConnection();
             PreparedStatement ps =  cn.prepareStatement("INSERT INTO posts(name,description,created) VALUES (?,?,?)", PreparedStatement.RETURN_GENERATED_KEYS)
        ) {
            ps.setString(1, post.getName());
            ps.setString(2, post.getDescription());
            ps.setDate(3, new Date(post.getCreated().getTime()));
            ps.execute();
            try (ResultSet id = ps.getGeneratedKeys()) {
                if (id.next()) {
                    post.setId(id.getInt(1));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void create(Candidate candidate) {
        try (Connection cn = pool.getConnection();
             PreparedStatement ps =  cn.prepareStatement("INSERT INTO candidates(name, city_id) VALUES (?,?)", PreparedStatement.RETURN_GENERATED_KEYS)
        ) {
            ps.setString(1, candidate.getName());
            ps.setInt(2, candidate.getCity_id());
            ps.execute();
            try (ResultSet id = ps.getGeneratedKeys()) {
                if (id.next()) {
                    candidate.setId(id.getInt(1));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void update(Post post) {
        try (Connection cn = pool.getConnection();
             PreparedStatement ps = cn.prepareStatement("UPDATE posts SET name = ?, description = ?, created = CURRENT_DATE WHERE id = ?")
        ) {
            ps.setString(1, post.getName());
            ps.setString(2, post.getDescription());
            ps.setInt(3, post.getId());
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void update(Candidate candidate) {
        try (Connection cn = pool.getConnection();
             PreparedStatement ps = cn.prepareStatement("UPDATE candidates SET name = ?, city_id = ? WHERE id = ?")
        ) {
            ps.setString(1, candidate.getName());
            ps.setInt(2, candidate.getCity_id());
            ps.setInt(3, candidate.getId());
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public Post findPostById(int id) {
        Post foundPost = null;
        try (Connection cn = pool.getConnection();
            PreparedStatement ps = cn.prepareStatement("SELECT * FROM posts WHERE id = ?")
        ) {
            ps.setInt(1, id);
            try (ResultSet foundDbEntry = ps.executeQuery()) {
                if (foundDbEntry.next()) {
                    foundPost = new Post(id, foundDbEntry.getString("name"));
                    foundPost.setDescription(foundDbEntry.getString("description"));
                    }
                }
            } catch (Exception e) {
            e.printStackTrace();
        }
        return foundPost;
    }

    @Override
    public Candidate findCanById(int id) {
        Candidate foundCandidate = null;
        try (Connection cn = pool.getConnection();
             PreparedStatement ps = cn.prepareStatement("SELECT * FROM candidates WHERE id = ?")
        ) {
            ps.setInt(1, id);
            try (ResultSet foundDbEntry = ps.executeQuery()) {
                if (foundDbEntry.next()) {
                    foundCandidate = new Candidate(id, foundDbEntry.getString("name"));
                    foundCandidate.setCity_id(foundDbEntry.getInt("city_id"));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return foundCandidate;
    }

    @Override
    public User findUserByEmail(String email) {
        User foundUser = null;
        try (Connection cn = pool.getConnection();
             PreparedStatement ps = cn.prepareStatement("SELECT * FROM users WHERE email = ?")
        ) {
            ps.setString(1, email);
            try (ResultSet foundDbEntry = ps.executeQuery()) {
                if (foundDbEntry.next()) {
                    int id = foundDbEntry.getInt("id");
                    String name = foundDbEntry.getString("name");
                    String password = foundDbEntry.getString("password");
                    foundUser = new User(id, name, email, password);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return foundUser;
    }

    @Override
    public City findCityById(int id) {
        City foundCity = null;
        try (Connection cn = pool.getConnection();
             PreparedStatement ps = cn.prepareStatement("SELECT * FROM cities WHERE id = ?")
        ) {
            ps.setInt(1, id);
            try (ResultSet foundDbEntry = ps.executeQuery()) {
                if (foundDbEntry.next()) {
                    foundCity = new City(id, foundDbEntry.getString("name"));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return foundCity;
    }

    @Override
    public void deleteCan(Candidate candidate) {
        try (Connection cn = pool.getConnection();
        PreparedStatement ps = cn.prepareStatement("DELETE FROM candidates WHERE id = ?")
        ) {
            ps.setInt(1, candidate.getId());
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deleteUser(User user) {
        try (Connection cn = pool.getConnection();
             PreparedStatement ps = cn.prepareStatement("DELETE FROM users WHERE id = ?")
        ) {
            ps.setInt(1, user.getId());
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
