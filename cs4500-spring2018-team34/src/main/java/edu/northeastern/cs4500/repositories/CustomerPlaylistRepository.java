package edu.northeastern.cs4500.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import javax.transaction.Transactional;

public interface CustomerPlaylistRepository extends JpaRepository<CustomerPlaylist, Integer> {
    boolean existsByNameAndCustomerId(String name, Integer customerId);
    boolean existsByNameAndCustomerIdAndMovieId(String name, Integer customerId, Integer movieId);
    @Transactional
    void deleteAllByNameAndCustomerIdAndMovieId(String name, Integer customerId, Integer movieId);
    @Transactional
    void deleteAllByNameAndCustomerId(String name, Integer customerId);
}
