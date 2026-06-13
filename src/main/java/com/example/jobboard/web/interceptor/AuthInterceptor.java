package com.example.jobboard.web.interceptor;
import com.example.jobboard.web.SessionUser;
import jakarta.servlet.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import java.util.Set;

@Component
public class AuthInterceptor implements HandlerInterceptor {
    private static final Set<String> PUBLIC = Set.of("/", "/about", "/login", "/register", "/logout");
    private static final Set<String> PUBLIC_PREFIX = Set.of("/css/", "/js/", "/images/", "/error");

    @Override
    public boolean preHandle(HttpServletRequest req, HttpServletResponse res, Object handler) throws Exception {
        String path = req.getRequestURI();
        if (PUBLIC.contains(path)) return true;
        for (String p : PUBLIC_PREFIX) if (path.startsWith(p)) return true;
        if (path.equals("/jobs") || path.startsWith("/jobs/view/")) return true;
        Object user = req.getSession().getAttribute(SessionUser.KEY);
        if (user == null) { res.sendRedirect("/login"); return false; }
        return true;
    }
}
