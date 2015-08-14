package de.home_fm.entity.dao;

import de.home_fm.entity.Playlist;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Instant;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PlaylistDAO extends AbstractFacade {

  public PlaylistDAO() throws IOException, SQLException {
    super();
  }

  public void create(Playlist audioFile) throws SQLException {
    try (PreparedStatement preparedStatement = mapIntoPreparedStatement("INSERT INTO audio_file VALUES (0,?,?)", audioFile)) {
      int updatedRows = preparedStatement.executeUpdate();
      if (updatedRows != 1) {
        Logger.getLogger(this.getClass().getName()).log(Level.INFO, "playlist ''{0}'' not properly created. updatedRows={1}", new Object[]{audioFile.getName(), updatedRows});
      }
    }
  }

  public void update(Playlist playlist) throws SQLException {
    String sql = "UPDATE audio_file SET name=?,last_change=? WHERE id=?";
    playlist.setLastChange(Instant.now());
    try (PreparedStatement preparedStatement = mapIntoPreparedStatement(sql, playlist)) {
      preparedStatement.setLong(3, playlist.getId());

      int updatedRows = preparedStatement.executeUpdate();
      if (updatedRows != 1) {
        Logger.getLogger(this.getClass().getName()).log(Level.INFO, "playlist ''{0}'' not properly created. updatedRows={1}", new Object[]{playlist.getName(), updatedRows});
      }
    }
  }

  private PreparedStatement mapIntoPreparedStatement(String sql, Playlist playlist) throws SQLException {
    PreparedStatement preparedStatement = getConnection().prepareStatement(sql);
    preparedStatement.setString(1, playlist.getName());
    preparedStatement.setLong(2, playlist.getLastChange().toEpochMilli());
    return preparedStatement;
  }

  public List<Playlist> findAll() throws SQLException {
    List<Playlist> result = new LinkedList<>();
    try (PreparedStatement preparedStatement = getConnection().prepareStatement("SELECT * FROM playlist")) {
      try (ResultSet resultSet = preparedStatement.executeQuery()) {
        while (resultSet.next()) {
          result.add(map(resultSet));
        }
      }
    }
    return result;
  }

  public Playlist findByName(String name) throws SQLException {
    Playlist playlist = null;
    try (PreparedStatement preparedStatement = getConnection().prepareStatement("SELECT * FROM playlist WHERE name = ?")) {
      preparedStatement.setString(1, name);
      try (ResultSet resultSet = preparedStatement.executeQuery()) {
        if (resultSet.next()) {
          playlist = map(resultSet);
        }
      }
    }
    return playlist;
  }

  private Playlist map(ResultSet resultSet) throws SQLException {
    Playlist playlist = new Playlist();
    playlist.setId(resultSet.getLong("id"));
    playlist.setName(resultSet.getString("name"));
    playlist.setLastChange(Instant.ofEpochMilli(resultSet.getLong("last_change")));
    return playlist;
  }
}
