package edu.northeastern.cs4500.repositories;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "customer_playlist_detail")
public class CustomerPlaylistDetail {
    @Id
    @GeneratedValue
    private Integer id;
    private Integer playlistId;
    private Integer movieId;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public CustomerPlaylistDetail withId(Integer id) {
        this.setId(id);
        return this;
    }

    public Integer getPlaylistId() {
        return playlistId;
    }

    public void setPlaylistId(Integer playlistId) {
        this.playlistId = playlistId;
    }

    public CustomerPlaylistDetail withPlaylistId(Integer playlistId) {
        this.setPlaylistId(playlistId);
        return this;
    }

    public Integer getMovieId() {
        return movieId;
    }

    public void setMovieId(Integer movieId) {
        this.movieId = movieId;
    }

    public CustomerPlaylistDetail withMovieId(Integer movieId) {
        this.setMovieId(movieId);
        return this;
    }
}
