package edu.northeastern.cs4500.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface AdminCodeRepository extends JpaRepository<AdminCode, String> {
    @Query(value = "SELECT * from admin_code order by RAND() limit 1", nativeQuery = true)
    AdminCode selectRandom();
}
