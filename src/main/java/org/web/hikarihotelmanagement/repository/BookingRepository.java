package org.web.hikarihotelmanagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.web.hikarihotelmanagement.entity.Booking;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {
}
