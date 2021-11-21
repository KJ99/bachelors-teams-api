package pl.kj.bachelors.teams.infrastructure.user;

import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import java.util.Optional;

public class UserHandler {
    public static Optional<String> getCurrentUserId() {
        String uid = null;
        RequestAttributes attributes = RequestContextHolder.getRequestAttributes();
        if (attributes != null && attributes.getAttribute("uid", RequestAttributes.SCOPE_REQUEST) != null) {
            uid = (String) attributes.getAttribute("uid", RequestAttributes.SCOPE_REQUEST);
        }
        return Optional.ofNullable(uid);
    }
}
