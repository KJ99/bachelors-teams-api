package pl.kj.bachelors.teams.unit.infrastructure.service.user;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import pl.kj.bachelors.teams.infrastructure.service.user.UserProviderImpl;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

public class UserProviderImplTests {
    private UserProviderImpl service;
    private final String uid = "some-uid";

    @BeforeEach
    public void setUp() {
        HttpServletRequest request = new MockHttpServletRequest();
        request.setAttribute("uid", this.uid);

        this.service = new UserProviderImpl(request);
    }

    @Test
    public void testGetCurrentUserId() {
        Optional<String> uid = this.service.getCurrentUserId();

        assertThat(uid).isPresent();

        String value = uid.orElseThrow();

        assertThat(value).isEqualTo(this.uid);
    }
}
