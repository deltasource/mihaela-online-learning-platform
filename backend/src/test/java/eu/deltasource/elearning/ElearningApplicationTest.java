package eu.deltasource.elearning;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

/**
 * Test class for ElearningApplication.
 * This ensures that the Spring Boot application context loads successfully.
 */
@SpringBootTest
@ContextConfiguration(classes = TestContainerConfig.class)
class ElearningApplicationTest {

    @Test
    void contextLoads() {
    }
}
