package com.edts.concerts.repository;

import com.edts.concerts.entity.TicketEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import javax.persistence.LockModeType;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface TicketRepository extends JpaRepository<TicketEntity, UUID> {

    @Query("SELECT t FROM TicketEntity t WHERE t.concert_id = :concertId")
    List<TicketEntity> findByConcertId(String concertId);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT t FROM TicketEntity t " +
            "WHERE t.concert_id = :concertId " +
            "AND t.type = :type " +
            "AND t.available_qty > 0")
    Optional<TicketEntity> findTicketByConcertIdAndType(String concertId, String type);
}
