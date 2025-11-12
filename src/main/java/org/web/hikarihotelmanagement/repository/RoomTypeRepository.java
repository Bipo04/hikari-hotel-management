package org.web.hikarihotelmanagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.web.hikarihotelmanagement.entity.RoomType;
import java.util.Optional;


@Repository
public interface RoomTypeRepository extends JpaRepository<RoomType, Long> {
    Optional<RoomType> findByName(String name);

}
