package de.home_fm.boundary;

import de.home_fm.control.App;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
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
  @Path("get_playlists")
  @Produces(MediaType.APPLICATION_JSON)
  public String getPlaylists() {
    return "";
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
