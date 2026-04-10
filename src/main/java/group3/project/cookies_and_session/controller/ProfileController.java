package group3.project.cookies_and_session.controller;

import group3.project.cookies_and_session.dto.ProfileResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
public class ProfileController {

    /**
     * GET /profile
     * - Chỉ cho truy cập khi đã đăng nhập (session có username)
     * - Hiển thị: username, thời điểm đăng nhập, số lần truy cập /profile
     * - Mỗi lần gọi (F5), bộ đếm profileVisitCount tăng 1
     */
    @GetMapping("/profile")
    public ResponseEntity<?> profile(HttpSession session) {

        String username = (String) session.getAttribute("username");

        // Chưa đăng nhập → trả về 401 kèm thông báo chuyển về /login
        if (username == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of(
                            "error", "Bạn chưa đăng nhập. Vui lòng đăng nhập.",
                            "redirect", "/login"));
        }

        String loginTime = (String) session.getAttribute("loginTime");

        // Tăng bộ đếm
        Integer visitCount = (Integer) session.getAttribute("profileVisitCount");
        if (visitCount == null) {
            visitCount = 0;
        }
        visitCount++;
        session.setAttribute("profileVisitCount", visitCount);

        ProfileResponse response = ProfileResponse.builder()
                .username(username)
                .loginTime(loginTime)
                .profileVisitCount(visitCount)
                .build();

        return ResponseEntity.ok(response);
    }
}

