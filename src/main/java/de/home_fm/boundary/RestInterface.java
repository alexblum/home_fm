package de.home_fm.boundary;

import de.home_fm.control.App;
import de.home_fm.control.AudioFileBO;
import de.home_fm.control.FileWalker;
import de.home_fm.entity.AudioFile;
import de.home_fm.entity.Playlist;
import de.home_fm.entity.dao.PlaylistDAO;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

@Path("/")
public class RestInterface {

  @GET
  @Path("hello")
  @Produces(MediaType.APPLICATION_JSON)
  public String hello() {
    return "hi";
  }

  @GET
  @Path("stop")
  @Produces(MediaType.APPLICATION_JSON)
  public String stop() {
    App.getSingleton().interruptMainLoop();
    return "stopping now";
  }

  @GET
  @Path("scan_files")
  @Produces(MediaType.APPLICATION_JSON)
  public String scanFiles() throws IOException {
    FileWalker fileWalker = new FileWalker(Paths.get("/data/"));
    fileWalker.walk();
    fileWalker.persist();
    return "done";
  }

  @GET
  @Path("get_playlists")
  @Produces(MediaType.APPLICATION_JSON)
  public List<Playlist> getPlaylists() throws Exception {
    return new PlaylistDAO().findAll();
  }

  @GET
  @Path("get_songs_for_playlist/{playlist_id}")
  @Produces(MediaType.APPLICATION_JSON)
  public List<AudioFile> getPlaylists(@PathParam("playlist_id") long playlistId) throws Exception {
    return new AudioFileBO().findByPlaylistId(playlistId);
  }

  @GET
  @Path("song/{song_id}")
  @Produces(MediaType.APPLICATION_JSON)
  public byte[] getSong(@PathParam("song_id") long songId) throws Exception {
    AudioFile audioFile = new AudioFileBO().findById(songId);
    if (audioFile == null) {
      return null;
    }
    System.out.println("loading " + audioFile.getFilename().toString());
    return Files.readAllBytes(audioFile.getFilename());
  }

  @POST
  @Path("interface")
  @Produces(MediaType.APPLICATION_JSON)
  @Consumes(MediaType.APPLICATION_JSON)
  public String entry(@Context final HttpServletRequest httpServletRequest) {
    System.out.println("received post!");
    return "hi " + httpServletRequest.getRemoteAddr() + " :)";
  }
}
