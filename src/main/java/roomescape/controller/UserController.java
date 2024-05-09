package roomescape.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class UserController {

    @GetMapping
    public String showPopularTheme() {
        return "/index";
    }

    @GetMapping("/reservation")
    public String showUserPage() {
        return "/reservation";
    }

    @GetMapping("/login") //TODO redirect를 쿠키로만 확인하면 안될듯
    public String showLoginPage() {
        return "/login";
    }

    //TODO 여기에 있는게 맞을 지 고민해보자.
    @PostMapping("/logout")
    public String logout(final HttpServletResponse response) {
        // todo 세션도 만료시키기.
        Cookie cookie = new Cookie("token", null);
        cookie.setMaxAge(0);
        response.addCookie(cookie);
        return "/login";
    }
}
