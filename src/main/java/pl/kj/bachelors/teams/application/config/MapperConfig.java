package pl.kj.bachelors.teams.application.config;

import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.modelmapper.spi.MappingContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Page;
import org.springframework.web.util.UriComponentsBuilder;
import pl.kj.bachelors.teams.application.dto.response.UploadedFileResponse;
import pl.kj.bachelors.teams.application.dto.response.health.HealthCheckResponse;
import pl.kj.bachelors.teams.application.dto.response.health.SingleCheckResponse;
import pl.kj.bachelors.teams.application.dto.response.member.MemberRoleResponse;
import pl.kj.bachelors.teams.application.dto.response.member.TeamRoleResponse;
import pl.kj.bachelors.teams.application.dto.response.member.TeamMemberResponse;
import pl.kj.bachelors.teams.application.dto.response.page.PageMetadata;
import pl.kj.bachelors.teams.application.dto.response.team.TeamResponse;
import pl.kj.bachelors.teams.application.dto.response.team.TeamSimpleResponse;
import pl.kj.bachelors.teams.application.model.HealthCheckResult;
import pl.kj.bachelors.teams.application.model.SingleCheckResult;
import pl.kj.bachelors.teams.domain.config.ApiConfig;
import pl.kj.bachelors.teams.domain.model.embeddable.TeamSettings;
import pl.kj.bachelors.teams.domain.model.extension.AppTheme;
import pl.kj.bachelors.teams.domain.model.extension.Role;
import pl.kj.bachelors.teams.domain.model.create.TeamCreateModel;
import pl.kj.bachelors.teams.domain.model.create.TeamMemberCreateModel;
import pl.kj.bachelors.teams.domain.model.entity.Team;
import pl.kj.bachelors.teams.domain.model.entity.TeamMember;
import pl.kj.bachelors.teams.domain.model.entity.TeamRole;
import pl.kj.bachelors.teams.domain.model.entity.UploadedFile;
import pl.kj.bachelors.teams.domain.model.remote.UserProfile;
import pl.kj.bachelors.teams.domain.model.result.TeamMemberWithProfileResult;
import pl.kj.bachelors.teams.domain.model.result.TeamWithParticipationResult;
import pl.kj.bachelors.teams.domain.model.update.TeamMemberUpdateModel;
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
                   UploadedFile file = null;
                   if(src.getPictureId() != null) {
                       file = new UploadedFile();
                       file.setId(src.getPictureId());
                   }
                   return file;
                }).map(source, destination.getPicture());

                using(ctx -> {
                    TeamCreateModel src = (TeamCreateModel) ctx.getSource();
                    TeamSettings settings = new TeamSettings();
                    settings.setTheme(AppTheme.valueOf(src.getTheme()));

                    return settings;
                }).map(source, destination.getSettings());
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
                    UploadedFile file = null;
                    if(src.getPictureId() != null) {
                        file = new UploadedFile();
                        file.setId(src.getPictureId());
                    }
                    return file;
                }).map(source, destination.getPicture());
            }
        });

        mapper.addMappings(new PropertyMap<Team, TeamSimpleResponse>() {
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

                using(ctx -> {
                    Team src = (Team) ctx.getSource();
                    return src.getPicture() != null ? src.getPicture().getId() : null;
                }).map(source, destination.getPictureId());
            }
        });

        mapper.addConverter(new Converter<TeamWithParticipationResult, TeamResponse>() {
            @Override
            public TeamResponse convert(MappingContext<TeamWithParticipationResult, TeamResponse> ctx) {
                TeamWithParticipationResult src = ctx.getSource();
                TeamResponse dest = new TeamResponse();
                mapper.map(src.getTeam(), dest);
                dest.setRoles(src.getMember().getRoles().stream()
                        .map(TeamRole::getName)
                        .collect(Collectors.toList()));

                return dest;
            }
        });

        mapper.addConverter(new Converter<TeamMemberWithProfileResult, TeamMemberResponse>() {
            @Override
            public TeamMemberResponse convert(MappingContext<TeamMemberWithProfileResult, TeamMemberResponse> ctx) {
                TeamMemberWithProfileResult src = ctx.getSource();
                TeamMemberResponse dest = new TeamMemberResponse();
                mapper.map(src.getMember(), dest);
                mapper.map(src.getProfile(), dest);

                return dest;
            }
        });

        mapper.addMappings(new PropertyMap<UserProfile, TeamMemberResponse>() {
            @Override
            protected void configure() {
                skip(destination.getId());
                map().setUserId(source.getId());
            }
        });

        mapper.addMappings(new PropertyMap<TeamMember, TeamMemberResponse>() {
            @Override
            protected void configure() {
                map().setUserId(source.getUserId());

                using(ctx -> ((TeamMember) ctx.getSource())
                            .getRoles()
                            .stream()
                            .map(role -> mapper.map(role, TeamRoleResponse.class))
                            .collect(Collectors.toList()))
                .map(source, destination.getRoles());
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

        mapper.addMappings(new PropertyMap<TeamMember, MemberRoleResponse>() {
            @Override
            protected void configure() {
                using(ctx ->
                        ((TeamMember)ctx.getSource())
                                .getRoles()
                                .stream()
                                .map(TeamRole::getCode)
                                .collect(Collectors.toList())
                ).map(source, destination.getRoles());
            }
        });

        mapper.addMappings(new PropertyMap<TeamMember, TeamMemberUpdateModel>() {
            @Override
            protected void configure() {
                using(ctx ->
                        ((TeamMember)ctx.getSource()).getRoles()
                                .stream()
                                .map(TeamRole::getCode)
                                .collect(Collectors.toList())
                ).map(source, destination.getRoles());
            }
        });

        mapper.addMappings(new PropertyMap<TeamMemberUpdateModel, TeamMember>() {
            @Override
            protected void configure() {
                using(ctx ->
                        ((TeamMemberUpdateModel)ctx.getSource()).getRoles()
                                .stream()
                                .map(role -> {
                                    var teamRole = new TeamRole();
                                    teamRole.setCode(role);

                                    return teamRole;
                                })
                                .collect(Collectors.toSet())
                ).map(source, destination.getRoles());
            }
        });

        return mapper;
    }
}
