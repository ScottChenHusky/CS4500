package edu.northeastern.cs4500.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import javax.transaction.Transactional;
import java.util.List;

public interface CustomerPlaylistDetailRepository extends JpaRepository<CustomerPlaylistDetail, Integer> {
    boolean existsByPlaylistIdAndMovieId(Integer playlistId, Integer movieId);
    List<CustomerPlaylistDetail> findAllByPlaylistId(Integer playlistId);
    @Transactional
    void deleteByPlaylistIdAndMovieId(Integer playlistId, Integer movieId);
    @Transactional
    void deleteAllByPlaylistId(Integer playlistId);
}
