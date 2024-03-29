package com.example.demo.repository;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.jpa.repository.JpaRepository;
import static org.assertj.core.api.Assertions.assertThat;
import com.example.demo.model.CustomUser;

@DataJpaTest
@Disabled
public class  UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;
    private CustomUser testUser;

    // TODO I dont like the autowired annotation that much.
    //      How can instantiate this class as I have the other classes were I avoided @Autowired?
    //      e.g. UserServiceImpl
    /*
    public UserRepositoryTest(UserRepository userRepository){
        this.userRepository = userRepository;
    }*/

    @BeforeEach
    private void setUp(){
        testUser = new CustomUser();
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
        CustomUser user = userRepository.findByUsername("bob").get();
        assertThat(user).isEqualTo(this.testUser);
    }

    @Test
    public void testFindByUsername_NotFound(){
        CustomUser user = userRepository.findByUsername("alice").get();
        assertThat(user).isNull();
    }

}
