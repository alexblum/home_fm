package de.home_fm.entity;

import java.nio.file.Path;
import java.time.Instant;
import java.util.Objects;

public class AudioFile {

  private long id;
  private Path filename;
  private String artist;
  private String title;
  private String album;
  private long durationInSeconds;
  private Instant lastChange;

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  public Path getFilename() {
    return filename;
  }

  public void setFilename(Path filename) {
    this.filename = filename;
  }

  public String getArtist() {
    return artist;
  }

  public void setArtist(String artist) {
    this.artist = artist;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getAlbum() {
    return album;
  }

  public void setAlbum(String album) {
    this.album = album;
  }

  public long getDurationInSeconds() {
    return durationInSeconds;
  }

  public void setDurationInSeconds(long durationInSeconds) {
    this.durationInSeconds = durationInSeconds;
  }

  public Instant getLastChange() {
    return lastChange;
  }

  public void setLastChange(Instant lastChange) {
    this.lastChange = lastChange;
  }

  @Override
  public int hashCode() {
    int hash = 7;
    hash = 71 * hash + (int) (this.id ^ (this.id >>> 32));
    hash = 71 * hash + Objects.hashCode(this.filename);
    return hash;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    final AudioFile other = (AudioFile) obj;
    if (this.id != other.id) {
      return false;
    }
    if (!Objects.equals(this.filename, other.filename)) {
      return false;
    }
    return true;
  }

}
