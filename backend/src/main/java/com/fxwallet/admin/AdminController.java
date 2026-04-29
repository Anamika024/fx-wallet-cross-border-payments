package com.fxwallet.admin;

import static com.fxwallet.admin.AdminModels.*;

import com.fxwallet.auth.AuthService;
import java.security.Principal;
import java.util.List;
import java.util.UUID;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin")
public class AdminController {
  private final AdminService adminService;
  private final AuthService authService;

  public AdminController(AdminService adminService, AuthService authService) {
    this.adminService = adminService;
    this.authService = authService;
  }

  @GetMapping("/fraud-alerts")
  List<FraudAlertView> fraudAlerts() {
    return adminService.pending();
  }

  @PostMapping("/fraud-alerts/{id}/approve")
  ResponseEntity<Void> approve(Principal principal, @PathVariable UUID id) {
    adminService.approve(id, authService.currentUser(principal.getName()).getId());
    return ResponseEntity.noContent().build();
  }

  @PostMapping("/fraud-alerts/{id}/reject")
  ResponseEntity<Void> reject(Principal principal, @PathVariable UUID id) {
    adminService.reject(id, authService.currentUser(principal.getName()).getId());
    return ResponseEntity.noContent().build();
  }

  @GetMapping("/users")
  List<UserAdminView> users() {
    return adminService.users();
  }

  @PostMapping("/users/{id}/suspend")
  ResponseEntity<Void> suspend(@PathVariable UUID id) {
    adminService.suspend(id);
    return ResponseEntity.noContent().build();
  }
}
