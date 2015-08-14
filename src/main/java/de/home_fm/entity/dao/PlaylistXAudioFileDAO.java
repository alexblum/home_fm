package de.home_fm.entity.dao;

import de.home_fm.entity.PlaylistXAudioFile;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PlaylistXAudioFileDAO extends AbstractFacade {

  public PlaylistXAudioFileDAO() throws IOException, SQLException {
    super();
  }

  public void create(PlaylistXAudioFile x) throws SQLException {
    try (PreparedStatement preparedStatement = mapIntoPreparedStatement("INSERT INTO playlist_x_audio_file VALUES (?,?)", x)) {
      int updatedRows = preparedStatement.executeUpdate();
      if (updatedRows != 1) {
        Logger.getLogger(this.getClass().getName()).log(Level.INFO, "x not properly created. updatedRows={1}", new Object[]{updatedRows});
      }
    }
  }

  public void delete(PlaylistXAudioFile x) throws SQLException {
    String sql = "DELETE FROM audio_file WHERE playlist_id=? AND audio_file_id=?";
    try (PreparedStatement preparedStatement = mapIntoPreparedStatement(sql, x)) {

      int updatedRows = preparedStatement.executeUpdate();
      if (updatedRows != 1) {
        Logger.getLogger(this.getClass().getName()).log(Level.INFO, "x ''{0}'' / ''{1}'' not properly deleted. updatedRows={2}", new Object[]{x.getPlaylistId(), x.getAudioFileId(), updatedRows});
      }
    }
  }

  private PreparedStatement mapIntoPreparedStatement(String sql, PlaylistXAudioFile x) throws SQLException {
    PreparedStatement preparedStatement = getConnection().prepareStatement(sql);
    preparedStatement.setLong(1, x.getPlaylistId());
    preparedStatement.setLong(2, x.getAudioFileId());
    return preparedStatement;
  }

  public List<PlaylistXAudioFile> findByPlaylistId(long playlistId) throws SQLException {
    List<PlaylistXAudioFile> result = new LinkedList<>();
    try (PreparedStatement preparedStatement = getConnection().prepareStatement("SELECT * FROM playlist_x_audio_file WHERE playlist_id = ?")) {
      preparedStatement.setLong(1, playlistId);
      try (ResultSet resultSet = preparedStatement.executeQuery()) {
        while (resultSet.next()) {
          result.add(map(resultSet));
        }
      }
    }
    return result;
  }

  private PlaylistXAudioFile map(ResultSet resultSet) throws SQLException {
    PlaylistXAudioFile playlist = new PlaylistXAudioFile();
    playlist.setPlaylistId(resultSet.getLong("playlist_id"));
    playlist.setAudioFileId(resultSet.getLong("audio_file_id"));
    return playlist;
  }
}
