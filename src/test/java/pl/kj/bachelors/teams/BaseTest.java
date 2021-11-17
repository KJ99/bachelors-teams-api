package pl.kj.bachelors.teams;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import pl.kj.bachelors.teams.application.Application;
import pl.kj.bachelors.teams.application.config.*;

@SpringBootTest
@ContextConfiguration(classes = {
        Application.class,
        DbConfig.class,
        MapperConfig.class,
        ModelConfig.class,
        ServerConfig.class,
        StorageConfig.class
})
@Sql(value = "/db.test/seed.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(value = "/db.test/clear.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class BaseTest {
}
