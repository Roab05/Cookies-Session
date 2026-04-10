package group3.project.cookies_and_session.controller;

import group3.project.cookies_and_session.dto.LoginRequest;
import group3.project.cookies_and_session.dto.LoginResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

@RestController
public class AuthController {

    private static final DateTimeFormatter FORMATTER =
            DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");

    /**
     * POST /login
     * - Nhận username từ request body
     * - Lưu username và loginTime vào session
     */
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest,
                                   HttpSession session) {

        String username = loginRequest.getUsername();

        if (username == null || username.isBlank()) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", "Username không được để trống."));
        }

        String loginTime = LocalDateTime.now().format(FORMATTER);

        session.setAttribute("username", username);
        session.setAttribute("loginTime", loginTime);
        // Reset bộ đếm profile khi đăng nhập mới
        session.setAttribute("profileVisitCount", 0);

        LoginResponse response = LoginResponse.builder()
                .message("Đăng nhập thành công!")
                .username(username)
                .loginTime(loginTime)
                .build();

        return ResponseEntity.ok(response);
    }

    /**
     * POST /logout
     * - Xóa session
     * - Sau khi logout, truy cập /profile bị chặn
     */
    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpSession session) {
        session.invalidate();
        return ResponseEntity.ok(
                Map.of("message", "Đăng xuất thành công! Vui lòng đăng nhập lại."));
    }
}

