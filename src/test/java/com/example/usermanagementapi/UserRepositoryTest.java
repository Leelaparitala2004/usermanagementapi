package com.example.usermanagementapi;

import com.example.usermanagementapi.model.User;
import com.example.usermanagementapi.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class  UserRepositoryTest {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TestEntityManager entityManager;
    private User user1;
    private User user2;
    private User user3;
    @BeforeEach
    void setUp(){
        userRepository.deleteAllInBatch();
        user1=new User("Alice Wonderland","alice@example.com");
        user2=new User("Bob The Builder","bob@example.com");
        user3=new User("SVL","svl@example.com");
        entityManager.persist(user1);
        entityManager.persist(user2);
        entityManager.flush();
        entityManager.clear();
    }
    @Test
    void testFindByEmailFound(){
        Optional<User> foundUser=userRepository.findByEmail(user1.getEmail());
        userRepository.findByEmail(user1.getEmail());

        assertThat(foundUser).isPresent();
        assertThat(foundUser.get().getName()).isEqualTo(user1.getName());
        assertThat(foundUser.get().getEmail()).isEqualTo(user1.getEmail());
        assertThat(foundUser.get().getId()).isEqualTo(user1.getId());
    }
    @Test
    public void testSaveUser(){
        User newUser=new User("Charlie Chaplin","charlie@example.com");
        userRepository.save(newUser);
        User savedUser=userRepository.save(newUser);
        assertThat(savedUser).isNotNull();
        assertThat(savedUser.getId()).isNotNull();
        assertThat(savedUser.getName()).isEqualTo("Charlie Chaplin");
        assertThat(savedUser.getEmail()).isEqualTo("charlie@example.com");
    }

    @Test
    public void testUpdateUser(){
        String newName="manoj reddy";
        String newEmail="manojreddy@gmail.com";

        user3.setName(newName);
        user3.setEmail(newEmail);

        User updatedUser=userRepository.save(user3);
        assertThat(updatedUser.getName()).isEqualTo("manoj reddy");
        assertThat(updatedUser.getEmail()).isEqualTo("manojreddy@gmail.com");
    }

}