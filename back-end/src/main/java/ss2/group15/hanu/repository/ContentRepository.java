package ss2.group15.hanu.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ss2.group15.hanu.entity.Content;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ContentRepository extends JpaRepository<Content, Long> {

    Page<Content> findAllBySub(String sub, Pageable pageable);

    @Query("SELECT c FROM content c WHERE (:sub IS NULL OR c.sub = :sub) AND (:type IS NULL OR c.type = :type) AND (:dateFrom IS NULL OR c.dateRequest >= :dateFrom) AND (:dateTo IS NULL OR c.dateRequest <= :dateTo) AND (:keyword IS NULL OR c.inputText LIKE %:keyword%) ORDER BY c.dateRequest DESC")
    Page<Content> doSearch(String keyword, String type, LocalDateTime dateFrom, LocalDateTime dateTo, String sub, Pageable pageable);
}
