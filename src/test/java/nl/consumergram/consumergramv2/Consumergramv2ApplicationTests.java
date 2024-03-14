package nl.consumergram.consumergramv2;

import nl.consumergram.consumergramv2.services.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
class Consumergramv2ApplicationTests {

    @Autowired
    private ApplicationContext applicationContext;

    @Test
    void contextLoads() {
        UserService userService = applicationContext.getBean(UserService.class);
        assertNotNull(userService, "UserService bean should be loaded");
    }



}
