package com.example.demo.repository;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.jpa.repository.JpaRepository;
import static org.assertj.core.api.Assertions.assertThat;
import com.example.demo.model.User;

@DataJpaTest
public class  UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;
    private User testUser;

    // TODO I dont like the autowired annotation that much.
    //      How can instantiate this class as I have the other classes were I avoided @Autowired?
    //      e.g. UserServiceImpl
    /*
    public UserRepositoryTest(UserRepository userRepository){
        this.userRepository = userRepository;
    }*/

    @BeforeEach
    private void setUp(){
        testUser = new User();
        testUser.setMail("test@mail.com");
        testUser.setPassword("abc");
        testUser.setUsername("bob");
        testUser = this.userRepository.save(testUser);
    }

    @AfterEach
    private void tearDown(){
        this.userRepository.deleteAll();
        testUser = null;
    }

    @Test
    public void testFindByUsername_Found(){
        User user = userRepository.findByUsername("bob");
        assertThat(user).isEqualTo(this.testUser);
    }

    @Test
    public void testFindByUsername_NotFound(){
        User user = userRepository.findByUsername("alice");
        assertThat(user).isNull();
    }

}
