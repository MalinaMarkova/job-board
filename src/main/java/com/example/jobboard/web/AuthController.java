package com.example.jobboard.web;
import com.example.jobboard.dto.*;
import com.example.jobboard.model.User;
import com.example.jobboard.service.UserService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
public class AuthController {
    private final UserService userService;
    public AuthController(UserService userService) { this.userService = userService; }

    @GetMapping("/register")
    public String registerForm(Model model) {
        if (!model.containsAttribute("registerDto")) model.addAttribute("registerDto", new RegisterDto());
        return "register";
    }
    @PostMapping("/register")
    public String register(@Valid @ModelAttribute("registerDto") RegisterDto dto, BindingResult br, Model model) {
        if (br.hasErrors()) return "register";
        try { userService.register(dto); } catch (RuntimeException ex) {
            model.addAttribute("error", ex.getMessage()); return "register";
        }
        return "redirect:/login";
    }
    @GetMapping("/login")
    public String loginForm(Model model) {
        if (!model.containsAttribute("loginDto")) model.addAttribute("loginDto", new LoginDto());
        return "login";
    }
    @PostMapping("/login")
    public String login(@Valid @ModelAttribute("loginDto") LoginDto dto, BindingResult br, HttpSession session, Model model) {
        if (br.hasErrors()) return "login";
        try {
            User u = userService.login(dto);
            session.setAttribute(SessionUser.KEY, new SessionUser(u.getId(), u.getUsername(), u.getRole()));
        } catch (RuntimeException ex) { model.addAttribute("error", ex.getMessage()); return "login"; }
        return "redirect:/jobs";
    }
    @GetMapping("/logout")
    public String logout(HttpSession session) { session.invalidate(); return "redirect:/"; }
}
