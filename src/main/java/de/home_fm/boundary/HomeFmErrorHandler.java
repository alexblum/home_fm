package de.home_fm.boundary;

import java.io.IOException;
import java.io.Writer;
import javax.servlet.http.HttpServletRequest;
import org.eclipse.jetty.servlet.ErrorPageErrorHandler;

public class HomeFmErrorHandler extends ErrorPageErrorHandler {

  @Override
  protected void writeErrorPage(HttpServletRequest request, Writer writer, int code, String message, boolean showStacks) throws IOException {
    writer.write("<!DOCTYPE html><html><head>");
    writer.write("<title>" + code + "</title>");
    writer.write("</head><body>");
    writer.write(code + " / " + message);
    writer.write("</body></html>");
  }

  @Override
  protected void handleErrorPage(HttpServletRequest request, Writer writer, int code, String message) throws IOException {
    writeErrorPage(request, writer, code, message, false);
  }

}
