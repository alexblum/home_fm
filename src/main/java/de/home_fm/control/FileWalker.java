package de.home_fm.control;

import com.mpatric.mp3agic.ID3v1;
import com.mpatric.mp3agic.InvalidDataException;
import com.mpatric.mp3agic.Mp3File;
import com.mpatric.mp3agic.UnsupportedTagException;
import de.home_fm.entity.AudioFile;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Instant;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class FileWalker {

  private final Path baseDirectory;
  private final List<AudioFile> audioFiles;

  public FileWalker(Path baseDirectory) {
    this.baseDirectory = baseDirectory;
    this.audioFiles = new LinkedList<>();
  }

  public void walk() throws IOException {
    walkDirectory(baseDirectory);
  }

  public void persist() {
    AudioFileBO audioFileBO = new AudioFileBO();
    for (AudioFile audioFile : audioFiles) {
      audioFileBO.createOrUpdate(audioFile);
    }
  }

  private void walkDirectory(Path baseDir) throws IOException {
    try (DirectoryStream<Path> newDirectoryStream
            = Files.newDirectoryStream(baseDir)) {
      for (Path path : newDirectoryStream) {
        if (Files.isDirectory(path)) {
          walkDirectory(path);
          continue;
        }
        if (path.getFileName().toString().toLowerCase().endsWith(".mp3")) {
          processFile(path);
        } else {
          System.out.println("ignoring file: " + path.toString());
        }
      }
    }
  }

  private void processFile(Path path) {
    try {
      Mp3File file = new Mp3File(path.toString(), false); //TODO: this is for fast scanning. length etc. are not scanned with this

      AudioFile audioFile = new AudioFile();

      if (!file.hasId3v1Tag() && !file.hasId3v2Tag()) {
        System.out.println("no metadata: " + path.toString());
        return;
      }

      ID3v1 tag = file.hasId3v1Tag() ? file.getId3v1Tag() : file.getId3v2Tag();
      audioFile.setArtist(tag.getArtist());
      audioFile.setTitle(tag.getTitle());
      audioFile.setAlbum(tag.getAlbum());
      audioFile.setFilename(path);
      audioFile.setDurationInSeconds(file.getLengthInSeconds());
      audioFile.setLastChange(Instant.ofEpochMilli(file.getLastModified()));

      audioFiles.add(audioFile);
    } catch (IOException | UnsupportedTagException | InvalidDataException ex) {
      Logger.getLogger(FileWalker.class.getName()).log(Level.SEVERE, "error while processing '" + path.toString() + "'", ex);
    }
  }
}
