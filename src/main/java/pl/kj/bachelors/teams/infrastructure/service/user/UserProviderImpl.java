package pl.kj.bachelors.teams.infrastructure.service.user;

import org.apache.commons.lang3.NotImplementedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.kj.bachelors.teams.domain.service.user.UserProvider;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

@Service
public class UserProviderImpl implements UserProvider {
    private final HttpServletRequest request;

    @Autowired
    public UserProviderImpl(HttpServletRequest request) {
        this.request = request;
    }

    @Override
    public Optional<String> getCurrentUserId() {
        String uid = null;
        if(this.request.getAttribute("uid") != null) {
            uid = (String) request.getAttribute("uid");
        }

        return Optional.ofNullable(uid);
    }
}
