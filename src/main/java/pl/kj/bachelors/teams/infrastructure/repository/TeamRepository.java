package pl.kj.bachelors.teams.infrastructure.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pl.kj.bachelors.teams.domain.model.entity.Team;

import java.util.List;
import java.util.Optional;

@Repository
public interface TeamRepository extends JpaRepository<Team, Integer> {
    @Query("select t from Team t inner join TeamMember tm on tm.team = t and tm.userId = :uid")
    Page<Team> findByUserId(@Param("uid") String uid, Pageable paging);

    @Query("select t from Team t inner join TeamMember tm on tm.team = t and tm.userId = :uid where t.id = :id")
    Optional<Team> findByIdWithParticipation(@Param("id") int id, @Param("uid") String uid);
}
