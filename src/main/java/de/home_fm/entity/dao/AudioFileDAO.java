package de.home_fm.entity.dao;

import de.home_fm.entity.AudioFile;
import java.io.IOException;
import java.nio.file.Paths;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Instant;
import java.util.LinkedList;
import java.util.List;
import java.util.StringJoiner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AudioFileDAO extends AbstractFacade {

  public AudioFileDAO() throws IOException, SQLException {
    super();
  }

  public void create(AudioFile audioFile) throws SQLException {
    try (PreparedStatement preparedStatement = mapIntoPreparedStatement("INSERT INTO audio_file VALUES (0,?,?,?,?,?,?)", audioFile)) {
      int updatedRows = preparedStatement.executeUpdate();
      if (updatedRows != 1) {
        Logger.getLogger(this.getClass().getName()).log(Level.INFO, "file ''{0}'' not properly created. updatedRows={1}", new Object[]{audioFile.getFilename().toString(), updatedRows});
      }
    }
  }

  public void update(AudioFile audioFile) throws SQLException {
    String sql = "UPDATE audio_file SET filename=?,artist=?,title=?,album=?,duration_in_seconds=?,last_change=? WHERE id=?";
    audioFile.setLastChange(Instant.now());
    try (PreparedStatement preparedStatement = mapIntoPreparedStatement(sql, audioFile)) {
      preparedStatement.setLong(7, audioFile.getId());

      int updatedRows = preparedStatement.executeUpdate();
      if (updatedRows != 1) {
        Logger.getLogger(this.getClass().getName()).log(Level.INFO, "file ''{0}'' not properly created. updatedRows={1}", new Object[]{audioFile.getFilename().toString(), updatedRows});
      }
    }
  }

  private PreparedStatement mapIntoPreparedStatement(String sql, AudioFile audioFile) throws SQLException {
    PreparedStatement preparedStatement = getConnection().prepareStatement(sql);
    preparedStatement.setString(1, audioFile.getFilename().toString());
    preparedStatement.setString(2, audioFile.getArtist());
    preparedStatement.setString(3, audioFile.getTitle());
    preparedStatement.setString(4, audioFile.getAlbum());
    preparedStatement.setLong(5, audioFile.getDurationInSeconds());
    preparedStatement.setLong(6, audioFile.getLastChange().toEpochMilli());
    return preparedStatement;
  }

  public AudioFile findByFilename(String filename) throws SQLException {
    AudioFile audioFile = null;
    try (PreparedStatement preparedStatement = getConnection().prepareStatement("SELECT * FROM audio_file WHERE filename = ?")) {
      preparedStatement.setString(1, filename);
      try (ResultSet resultSet = preparedStatement.executeQuery()) {
        if (resultSet.next()) {
          audioFile = map(resultSet);
        }
      }
    }
    return audioFile;
  }

  public List<AudioFile> findByIds(List<Long> ids) throws SQLException {
    List<AudioFile> result = new LinkedList<>();
    StringJoiner j = new StringJoiner(",");
    ids.stream().forEach(id -> j.add(Long.toString(id)));
    try (PreparedStatement preparedStatement = getConnection().prepareStatement("SELECT * FROM audio_file WHERE id IN (" + j.toString() + ")")) {
      try (ResultSet resultSet = preparedStatement.executeQuery()) {
        while (resultSet.next()) {
          result.add(map(resultSet));
        }
      }
    }
    return result;
  }

  private AudioFile map(ResultSet resultSet) throws SQLException {
    AudioFile audioFile = new AudioFile();
    audioFile.setId(resultSet.getLong("id"));
    audioFile.setFilename(Paths.get(resultSet.getString("filename")));
    audioFile.setArtist(resultSet.getString("artist"));
    audioFile.setTitle(resultSet.getString("title"));
    audioFile.setAlbum(resultSet.getString("album"));
    audioFile.setDurationInSeconds(resultSet.getLong("duration_in_seconds"));
    audioFile.setLastChange(Instant.ofEpochMilli(resultSet.getLong("last_change")));
    return audioFile;
  }
}
