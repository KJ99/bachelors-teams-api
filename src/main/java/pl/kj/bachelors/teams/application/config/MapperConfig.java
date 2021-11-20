package pl.kj.bachelors.teams.application.config;

import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Page;
import org.springframework.web.util.UriComponentsBuilder;
import pl.kj.bachelors.teams.application.dto.response.UploadedFileResponse;
import pl.kj.bachelors.teams.application.dto.response.health.HealthCheckResponse;
import pl.kj.bachelors.teams.application.dto.response.health.SingleCheckResponse;
import pl.kj.bachelors.teams.application.dto.response.page.PageMetadata;
import pl.kj.bachelors.teams.application.dto.response.page.PageResponse;
import pl.kj.bachelors.teams.application.dto.response.team.TeamResponse;
import pl.kj.bachelors.teams.application.model.HealthCheckResult;
import pl.kj.bachelors.teams.application.model.SingleCheckResult;
import pl.kj.bachelors.teams.domain.config.ApiConfig;
import pl.kj.bachelors.teams.domain.model.Role;
import pl.kj.bachelors.teams.domain.model.create.TeamCreateModel;
import pl.kj.bachelors.teams.domain.model.create.TeamMemberCreateModel;
import pl.kj.bachelors.teams.domain.model.entity.Team;
import pl.kj.bachelors.teams.domain.model.entity.TeamMember;
import pl.kj.bachelors.teams.domain.model.entity.TeamRole;
import pl.kj.bachelors.teams.domain.model.entity.UploadedFile;
import pl.kj.bachelors.teams.domain.model.update.TeamUpdateModel;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Configuration
public class MapperConfig {
    private final ApiConfig config;

    @Autowired
    public MapperConfig(ApiConfig config) {
        this.config = config;
    }

    @Bean
    public ModelMapper mapper() {
        ModelMapper mapper = new ModelMapper();

        mapper.addMappings(new PropertyMap<SingleCheckResult, SingleCheckResponse>() {
            @Override
            protected void configure() {
                using(ctx -> ((SingleCheckResult) ctx.getSource()).isActive() ? "On" : "Off")
                        .map(source, destination.getStatus());
            }
        });

        mapper.addMappings(new PropertyMap<HealthCheckResult, HealthCheckResponse>() {
            @Override
            protected void configure() {
                using(ctx -> ((HealthCheckResult) ctx.getSource())
                        .getResults()
                        .stream()
                        .map(item -> mapper().map(item, SingleCheckResponse.class))
                        .collect(Collectors.toList())
                ).map(source, destination.getResults());
            }
        });

        mapper.addMappings(new PropertyMap<UploadedFile, UploadedFileResponse>() {
            @Override
            protected void configure() {
                map().setFileName(source.getOriginalFileName());
            }
        });

        mapper.addMappings(new PropertyMap<TeamCreateModel, Team>() {
            @Override
            protected void configure() {
                using(ctx -> {
                   TeamCreateModel src = (TeamCreateModel) ctx.getSource();
                    UploadedFile file = new UploadedFile();
                    file.setId(src.getPictureId());
                    return src.getPictureId() != null ? file : null;
                });
            }
        });

        mapper.addMappings(new PropertyMap<Team, TeamUpdateModel>() {
            @Override
            protected void configure() {
                using(ctx -> {
                    Team src = (Team) ctx.getSource();
                    return src.getPicture() != null ? src.getPicture().getId() : null;
                }).map(source, destination.getPictureId());
            }
        });

        mapper.addMappings(new PropertyMap<TeamUpdateModel, Team>() {
            @Override
            protected void configure() {
                skip(destination.getId());
                using(ctx -> {
                    TeamUpdateModel src = (TeamUpdateModel) ctx.getSource();
                    UploadedFile file = new UploadedFile();
                    file.setId(src.getPictureId());
                    return src.getPictureId() != null ? file : null;
                }).map(source, destination.getPicture());
            }
        });

        mapper.addMappings(new PropertyMap<Team, TeamResponse>() {
            @Override
            protected void configure() {
                using(ctx -> {
                    Team src = (Team) ctx.getSource();
                    UriComponentsBuilder uriBuilder = UriComponentsBuilder
                            .newInstance()
                            .host(config.getHost())
                            .scheme("https");

                    return src.getPicture() != null
                            ? uriBuilder
                            .path("/v1/resources/{id}/download")
                            .buildAndExpand(src.getPicture().getId())
                            .toUriString()
                            : null;
                }).map(source, destination.getPictureUrl());
            }
        });

        mapper.addMappings(new PropertyMap<Page<?>, PageMetadata>() {
            @Override
            protected void configure() {
                map().setPage(source.getNumber());
                map().setPageSize((int) source.getTotalElements());
                map().setTotalPages(source.getTotalPages());
            }
        });

        mapper.addMappings(new PropertyMap<Page<Team>, PageResponse<TeamResponse>>() {
            @Override
            protected void configure() {
                using(ctx -> mapper.map(ctx.getSource(), PageMetadata.class)).map(source, destination.getMetadata());
                using(ctx -> {
                    @SuppressWarnings("unchecked")
                    Page<Team> src = (Page<Team>) ctx.getSource();
                    Page<TeamResponse> mappedPage = src.map(item -> mapper.map(item, TeamResponse.class));

                    return mappedPage.getContent();
                }).map(source, destination.getData());
            }
        });

        mapper.addMappings(new PropertyMap<TeamMemberCreateModel, TeamMember>() {
            @Override
            protected void configure() {
                skip(destination.getId());
                using(ctx -> {
                    TeamMemberCreateModel src = (TeamMemberCreateModel) ctx.getSource();
                    Set<TeamRole> result = new HashSet<>();
                    for(Role code : src.getRoles()) {
                        TeamRole role = new TeamRole();
                        role.setCode(code);
                        result.add(role);
                    }

                    return result;
                }).map(source, destination.getRoles());
            }
        });

        return mapper;
    }
}
