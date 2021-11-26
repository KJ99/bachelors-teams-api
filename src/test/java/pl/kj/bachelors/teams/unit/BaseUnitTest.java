package pl.kj.bachelors.teams.unit;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import pl.kj.bachelors.teams.BaseTest;
import pl.kj.bachelors.teams.infrastructure.user.RequestHandler;

import java.util.Optional;

import static org.mockito.Mockito.when;

public class BaseUnitTest extends BaseTest {
    protected MockedStatic<RequestHandler> requestHandlerMock;

    @BeforeEach
    public void beforeEach() {
        this.requestHandlerMock = Mockito.mockStatic(RequestHandler.class);

        when(RequestHandler.getCurrentUserId()).thenReturn(Optional.ofNullable(this.getCurrentUserId()));
    }

    @AfterEach
    public void afterEach() {
        this.requestHandlerMock.close();
    }
}
