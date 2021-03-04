package ru.job4j.dream.store;

import ru.job4j.dream.model.Post;

public class PsqlMain {
    public static void main(String[] args) {
        Store store = PsqlStore.instOf();
        store.save(new Post(0, "Java Job"));
        store.save(new Post(0, "Java Job"));
        store.save(new Post(1, "Java Dev"));
        store.save(new Post(2, "Java QA"));
        for (Post post : store.findAllPosts()) {
            System.out.println(post.getId() + " " + post.getName());
        }
        Post foundPost = store.findById(1);
        System.out.printf("Found vacancy: %s, date of create - %s", foundPost.getName(), foundPost.getCreated());
    }
}
