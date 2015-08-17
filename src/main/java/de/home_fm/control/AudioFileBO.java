package de.home_fm.control;

import de.home_fm.entity.AudioFile;
import de.home_fm.entity.PlaylistXAudioFile;
import de.home_fm.entity.dao.AudioFileDAO;
import de.home_fm.entity.dao.PlaylistXAudioFileDAO;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.SQLException;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class AudioFileBO {

  private final AudioFileDAO audioFileDAO;

  public AudioFileBO() {
    try {
      this.audioFileDAO = new AudioFileDAO();
    } catch (IOException | SQLException ex) {
      Logger.getLogger(AudioFileBO.class.getName()).log(Level.SEVERE, null, ex);
      throw new IllegalStateException(ex);
    }
  }

  public List<AudioFile> findByPlaylistId(long playlistId) {
    List<AudioFile> result = new LinkedList<>();
    try {
      List<PlaylistXAudioFile> xs = new PlaylistXAudioFileDAO().findByPlaylistId(playlistId);
      result.addAll(findByIds(xs.stream().map(PlaylistXAudioFile::getAudioFileId).collect(Collectors.toList())));
    } catch (IOException | SQLException ex) {
      Logger.getLogger(AudioFileBO.class.getName()).log(Level.SEVERE, null, ex);
    }
    return result;
  }

  public AudioFile findById(long songId) {
    List<AudioFile> audioFiles = findByIds(Collections.singletonList(songId));
    if (audioFiles.isEmpty()) {
      return null;
    }
    return audioFiles.get(0);
  }

  private List<AudioFile> findByIds(List<Long> ids) {
    try {
      if (ids.size() > 0) {
        return audioFileDAO.findByIds(ids);
      }
    } catch (SQLException ex) {
      Logger.getLogger(AudioFileBO.class.getName()).log(Level.SEVERE, null, ex);
    }
    return new LinkedList<>();
  }

  public void createOrUpdate(AudioFile audioFile) {
    AudioFile fromDB = findAudioFile(audioFile);
    if (fromDB == null) {
      create(audioFile);
      return;
    }
    audioFile.setId(fromDB.getId());
    if (alreadyPresent(fromDB, audioFile)) {
      //do nothing
      return;
    }
    update(audioFile);
  }

  private AudioFile findAudioFile(AudioFile audioFile) {
    try {
      return audioFileDAO.findByFilename(audioFile.getFilename().toString());
    } catch (SQLException ex) {
      Logger.getLogger(AudioFileBO.class.getName()).log(Level.SEVERE, null, ex);
    }
    return null;
  }

  private void update(AudioFile audioFile) {
    try {
      audioFileDAO.update(audioFile);
    } catch (SQLException ex) {
      Logger.getLogger(AudioFileBO.class.getName()).log(Level.SEVERE, null, ex);
    }
  }

  private void create(AudioFile audioFile) {
    try {
      audioFileDAO.create(audioFile);
    } catch (SQLException ex) {
      Logger.getLogger(AudioFileBO.class.getName()).log(Level.SEVERE, "audioFile could not be created", ex);
    }
  }

  private boolean alreadyPresent(AudioFile fromDB, AudioFile audioFile) {
    for (Field field : AudioFile.class.getDeclaredFields()) {
      try {
        Method getter = AudioFile.class.getDeclaredMethod("get" + capitalise(field.getName()));
        if (!Objects.equals(getter.invoke(fromDB), getter.invoke(audioFile))) {
          return false;
        }
      } catch (IllegalArgumentException | IllegalAccessException | NoSuchMethodException | SecurityException | InvocationTargetException ex) {
        Logger.getLogger(AudioFileBO.class.getName()).log(Level.SEVERE, null, ex);
      }
    }
    return true;
  }

  private String capitalise(String name) {
    return name.substring(0, 1).toUpperCase() + name.substring(1);
  }
}
