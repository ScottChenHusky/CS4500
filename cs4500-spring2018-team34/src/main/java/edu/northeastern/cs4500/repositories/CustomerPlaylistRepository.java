package edu.northeastern.cs4500.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CustomerPlaylistRepository extends JpaRepository<CustomerPlaylist, Integer> {
    boolean existsByNameAndCustomerId(String name, Integer customerId);
    List<CustomerPlaylist> findAllByCustomerId(Integer customerId);
}
