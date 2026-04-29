package com.fxwallet.config;

import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {
  private final String frontendUrl;

  public HomeController(@Value("${app.frontend-url:http://127.0.0.1:5173}") String frontendUrl) {
    this.frontendUrl = frontendUrl;
  }

  @GetMapping("/")
  public void home(HttpServletResponse response) throws IOException {
    response.sendRedirect(frontendUrl);
  }
}
