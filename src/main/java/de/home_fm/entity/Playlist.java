package de.home_fm.entity;

import java.time.Instant;

public class Playlist {

  private long id;
  private String name;
  private Instant lastChange;

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Instant getLastChange() {
    return lastChange;
  }

  public void setLastChange(Instant lastChange) {
    this.lastChange = lastChange;
  }
}
