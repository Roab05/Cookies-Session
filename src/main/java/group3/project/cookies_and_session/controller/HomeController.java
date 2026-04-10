package group3.project.cookies_and_session.controller;

import group3.project.cookies_and_session.dto.HomeResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
public class HomeController {

    /**
     * GET /
     * - Hiển thị tiêu đề "Mini Profile App"
     * - Nếu chưa đăng nhập  → "Bạn chưa đăng nhập"
     * - Nếu đã đăng nhập    → "Xin chào, <username>"
     * - Hiển thị theme lấy từ cookie (mặc định "light")
     */
    @GetMapping("/")
    public ResponseEntity<HomeResponse> home(
            @CookieValue(value = "theme", defaultValue = "light") String theme,
            HttpSession session) {

        String username = (String) session.getAttribute("username");
        String message;

        if (username == null || username.isBlank()) {
            message = "Bạn chưa đăng nhập";
        } else {
            message = "Xin chào, " + username;
        }

        HomeResponse response = HomeResponse.builder()
                .title("Mini Profile App")
                .message(message)
                .theme(theme)
                .build();

        return ResponseEntity.ok(response);
    }

    /**
     * GET /set-theme/{theme}
     * - Chỉ chấp nhận "light" hoặc "dark"
     * - Lưu cookie "theme" sống trong 10 phút (600 giây)
     */
    @GetMapping("/set-theme/{theme}")
    public ResponseEntity<?> setTheme(@PathVariable String theme,
                                      jakarta.servlet.http.HttpServletResponse response) {

        if (!"light".equalsIgnoreCase(theme) && !"dark".equalsIgnoreCase(theme)) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", "Theme không hợp lệ. Chỉ chấp nhận 'light' hoặc 'dark'."));
        }

        String normalizedTheme = theme.toLowerCase();

        jakarta.servlet.http.Cookie cookie = new jakarta.servlet.http.Cookie("theme", normalizedTheme);
        cookie.setMaxAge(10 * 60); // 10 phút
        cookie.setPath("/");
        response.addCookie(cookie);

        return ResponseEntity.ok(
                Map.of("message", "Theme đã được đặt thành: " + normalizedTheme,
                        "theme", normalizedTheme));
    }
}

