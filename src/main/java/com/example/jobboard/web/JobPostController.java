package com.example.jobboard.web;
import com.example.jobboard.dto.JobPostDto;
import com.example.jobboard.exception.ForbiddenException;
import com.example.jobboard.model.Role;
import com.example.jobboard.service.*;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import java.util.UUID;

@Controller
@RequestMapping("/jobs")
public class JobPostController {
    private final JobPostService jobs;
    private final CompanyService companies;
    private final UserService userService;
    public JobPostController(JobPostService j, CompanyService c, UserService u) { this.jobs = j; this.companies = c; this.userService = u; }

    private SessionUser current(HttpSession s) {
        return (SessionUser) s.getAttribute(SessionUser.KEY);
    }
    private SessionUser requireEmployer(HttpSession s) {
        SessionUser u = current(s);
        if (u == null || u.getRole() != Role.EMPLOYER) throw new ForbiddenException("Employers only");
        return u;
    }

    @GetMapping
    public String list(Model model) {
        model.addAttribute("jobs", jobs.listAll());
        return "jobs/list";
    }
    @GetMapping("/view/{id}")
    public String detail(@PathVariable UUID id, Model model, HttpSession session) {
        model.addAttribute("job", jobs.get(id));
        model.addAttribute("user", current(session));
        return "jobs/detail";
    }
    @GetMapping("/new")
    public String createForm(HttpSession session, Model model) {
        SessionUser u = requireEmployer(session);
        model.addAttribute("companies", companies.listMine(userService.getById(u.getUserId())));
        if (!model.containsAttribute("jobPostDto")) model.addAttribute("jobPostDto", new JobPostDto());
        return "jobs/form";
    }
    @PostMapping
    public String create(@Valid @ModelAttribute("jobPostDto") JobPostDto dto, BindingResult br, HttpSession session, Model model) {
        SessionUser u = requireEmployer(session);
        if (br.hasErrors()) { model.addAttribute("companies", companies.listMine(userService.getById(u.getUserId()))); return "jobs/form"; }
        jobs.create(dto, u.getUserId());
        return "redirect:/jobs/manage";
    }
    @GetMapping("/manage")
    public String manage(HttpSession session, Model model) {
        SessionUser u = requireEmployer(session);
        model.addAttribute("jobs", jobs.listByOwner(u.getUserId()));
        return "jobs/manage";
    }
    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable UUID id, HttpSession session, Model model) {
        SessionUser u = requireEmployer(session);
        var j = jobs.get(id);
        if (!j.getCompany().getOwner().getId().equals(u.getUserId())) throw new ForbiddenException("Not your post");
        JobPostDto dto = new JobPostDto();
        dto.setTitle(j.getTitle()); dto.setDescription(j.getDescription());
        dto.setLocation(j.getLocation()); dto.setSalary(j.getSalary()); dto.setCompanyId(j.getCompany().getId());
        model.addAttribute("jobPostDto", dto); model.addAttribute("editId", id);
        model.addAttribute("companies", companies.listMine(userService.getById(u.getUserId())));
        return "jobs/form";
    }
    @PostMapping("/{id}")
    public String update(@PathVariable UUID id, @Valid @ModelAttribute("jobPostDto") JobPostDto dto, BindingResult br, HttpSession session, Model model) {
        SessionUser u = requireEmployer(session);
        if (br.hasErrors()) { model.addAttribute("editId", id); model.addAttribute("companies", companies.listMine(userService.getById(u.getUserId()))); return "jobs/form"; }
        jobs.update(id, dto, u.getUserId());
        return "redirect:/jobs/manage";
    }
    @PostMapping("/{id}/delete")
    public String delete(@PathVariable UUID id, HttpSession session) {
        SessionUser u = requireEmployer(session);
        jobs.delete(id, u.getUserId());
        return "redirect:/jobs/manage";
    }
}
