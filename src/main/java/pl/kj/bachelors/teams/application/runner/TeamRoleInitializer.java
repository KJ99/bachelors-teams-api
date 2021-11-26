package pl.kj.bachelors.teams.application.runner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import pl.kj.bachelors.teams.domain.model.extension.Role;
import pl.kj.bachelors.teams.domain.model.entity.TeamRole;
import pl.kj.bachelors.teams.infrastructure.repository.TeamRoleRepository;

import java.util.HashMap;
import java.util.Map;

@Component
public class TeamRoleInitializer implements ApplicationRunner {
    private final TeamRoleRepository roleRepository;

    @Autowired
    public TeamRoleInitializer(TeamRoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    @Transactional
    public void run(ApplicationArguments args) {
        Logger logger = LoggerFactory.getLogger(TeamRoleInitializer.class);
        logger.info("Role initializer was started");
        Map<Role, String> data = new HashMap<>();
        data.put(Role.OWNER, "Team Owner");
        data.put(Role.ADMIN, "Administrator");
        data.put(Role.SCRUM_MASTER, "Scrum Master");
        data.put(Role.PRODUCT_OWNER, "Product Owner");
        data.put(Role.TEAM_MEMBER, "Member");

        for(Map.Entry<Role, String> entry : data.entrySet()) {
            if(this.roleRepository.findById(entry.getKey()).isEmpty()) {
                logger.info(String.format("Creating role %s", entry.getKey()));
                TeamRole role = new TeamRole();
                role.setCode(entry.getKey());
                role.setName(entry.getValue());

                this.roleRepository.save(role);
                logger.info(String.format("Role %s created", entry.getKey()));
            }
        }
        logger.info("Role initializer has initialized member roles");
    }
}
