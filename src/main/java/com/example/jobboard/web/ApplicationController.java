package com.example.jobboard.web;
import com.example.jobboard.dto.ApplicationDto;
import com.example.jobboard.exception.ForbiddenException;
import com.example.jobboard.model.Role;
import com.example.jobboard.model.ApplicationStatus;
import com.example.jobboard.service.ApplicationService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import java.util.UUID;

@Controller
@RequestMapping("/applications")
public class ApplicationController {
    private final ApplicationService applications;
    public ApplicationController(ApplicationService a) { this.applications = a; }

    private SessionUser current(HttpSession s) {
        SessionUser u = (SessionUser) s.getAttribute(SessionUser.KEY);
        if (u == null) throw new ForbiddenException("Login required");
        return u;
    }

    @GetMapping("/apply/{jobId}")
    public String applyForm(@PathVariable UUID jobId, HttpSession session, Model model) {
        SessionUser u = current(session);
        if (u.getRole() != Role.APPLICANT) throw new ForbiddenException("Only applicants can apply");
        if (!model.containsAttribute("applicationDto")) model.addAttribute("applicationDto", new ApplicationDto());
        model.addAttribute("jobId", jobId);
        return "applications/apply";
    }
    @PostMapping("/apply/{jobId}")
    public String apply(@PathVariable UUID jobId, @Valid @ModelAttribute("applicationDto") ApplicationDto dto, BindingResult br, HttpSession session, Model model) {
        SessionUser u = current(session);
        if (u.getRole() != Role.APPLICANT) throw new ForbiddenException("Only applicants can apply");
        if (br.hasErrors()) { model.addAttribute("jobId", jobId); return "applications/apply"; }
        applications.apply(jobId, dto, u.getUserId());
        return "redirect:/applications/mine";
    }
    @GetMapping("/mine")
    public String mine(HttpSession session, Model model) {
        SessionUser u = current(session);
        model.addAttribute("applications", applications.listMine(u.getUserId()));
        return "applications/mine";
    }
    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable UUID id, HttpSession session, Model model) {
        SessionUser u = current(session);
        var a = applications.get(id);
        if (!a.getApplicant().getId().equals(u.getUserId())) throw new ForbiddenException("Not your application");
        ApplicationDto dto = new ApplicationDto(); dto.setCoverLetter(a.getCoverLetter());
        model.addAttribute("applicationDto", dto); model.addAttribute("editId", id);
        return "applications/edit";
    }
    @PostMapping("/{id}")
    public String update(@PathVariable UUID id, @Valid @ModelAttribute("applicationDto") ApplicationDto dto, BindingResult br, HttpSession session, Model model) {
        SessionUser u = current(session);
        if (br.hasErrors()) { model.addAttribute("editId", id); return "applications/edit"; }
        applications.updateCoverLetter(id, dto, u.getUserId());
        return "redirect:/applications/mine";
    }
    @PostMapping("/{id}/withdraw")
    public String withdraw(@PathVariable UUID id, HttpSession session) {
        SessionUser u = current(session);
        applications.withdraw(id, u.getUserId());
        return "redirect:/applications/mine";
    }
    @GetMapping("/received")
    public String received(HttpSession session, Model model) {
        SessionUser u = current(session);
        if (u.getRole() != Role.EMPLOYER) throw new ForbiddenException("Employers only");
        model.addAttribute("applications", applications.listForOwner(u.getUserId()));
        return "applications/received";
    }
    @PostMapping("/{id}/decide")
    public String decide(@PathVariable UUID id, @RequestParam ApplicationStatus status, HttpSession session) {
        SessionUser u = current(session);
        if (u.getRole() != Role.EMPLOYER) throw new ForbiddenException("Employers only");
        applications.decide(id, status, u.getUserId());
        return "redirect:/applications/received";
    }
}
