package com.edts.concerts.repository;

import com.edts.concerts.entity.ConcertEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.UUID;

@Repository
public interface ConcertRepository extends JpaRepository<ConcertEntity, UUID> {

    @Query("SELECT c FROM ConcertEntity c " +
            "WHERE (:name IS NULL OR LOWER(c.name) LIKE LOWER(CONCAT('%', :name, '%'))) " +
            "AND (:venue IS NULL OR LOWER(c.venue) LIKE LOWER(CONCAT('%', :venue, '%'))) ")
    List<ConcertEntity> findByFields(String name, String venue);

    @Query("SELECT c FROM ConcertEntity c " +
            "WHERE (:name IS NULL OR LOWER(c.name) LIKE LOWER(CONCAT('%', :name, '%'))) " +
            "AND (:venue IS NULL OR LOWER(c.venue) LIKE LOWER(CONCAT('%', :venue, '%'))) ")
    Page<ConcertEntity> findByFieldsWithPagination(@Param("name") String name, @Param("venue") String venue, Pageable pageable);
}
