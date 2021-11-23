package pl.kj.bachelors.teams.infrastructure.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import pl.kj.bachelors.teams.domain.model.entity.CacheItem;
import pl.kj.bachelors.teams.domain.model.extension.CacheTag;

import java.util.Optional;

public interface CacheItemRepository extends JpaRepository<CacheItem, Integer> {
    @Query(
            value = "select * from cache_items where " +
                    "tag = :tag and " +
                    "item_key = :key and " +
                    "expires_at > current_timestamp() " +
                    "limit 1",
            nativeQuery = true
    )
    Optional<CacheItem> findFirstValidByTagAndKey(String tag, String key);
}
