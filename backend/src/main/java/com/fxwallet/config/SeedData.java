package com.fxwallet.config;

import com.fxwallet.auth.AuthService;
import com.fxwallet.auth.AuthModels.RegisterRequest;
import com.fxwallet.user.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SeedData {
  @Bean
  CommandLineRunner seed(UserRepository users, AuthService authService, @Value("${app.seed-demo-users:true}") boolean seedDemoUsers) {
    return args -> {
      if (seedDemoUsers && users.count() == 0) {
        authService.register(new RegisterRequest("demo@fxwallet.local", "Password123!", "Demo User"));
        authService.register(new RegisterRequest("admin@fxwallet.local", "Password123!", "Admin User"));
      }
    };
  }
}
