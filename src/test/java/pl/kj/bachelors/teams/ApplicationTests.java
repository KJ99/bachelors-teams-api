package pl.kj.bachelors.teams;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import pl.kj.bachelors.teams.application.Application;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ContextConfiguration(classes = { Application.class })
class ApplicationTests {

	@Test
	void contextLoads() {
		assertThat(true).isTrue();
	}

}
