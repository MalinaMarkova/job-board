package com.example.jobboard.web;
import com.example.jobboard.dto.CompanyDto;
import com.example.jobboard.exception.ForbiddenException;
import com.example.jobboard.model.Role;
import com.example.jobboard.service.CompanyService;
import com.example.jobboard.service.UserService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import java.util.UUID;

@Controller
@RequestMapping("/companies")
public class CompanyController {
    private final CompanyService companies;
    private final UserService userService;
    public CompanyController(CompanyService c, UserService u) { this.companies = c; this.userService = u; }

    private SessionUser current(HttpSession s) {
        SessionUser u = (SessionUser) s.getAttribute(SessionUser.KEY);
        if (u == null) throw new ForbiddenException("Login required");
        return u;
    }
    private void requireEmployer(SessionUser u) {
        if (u.getRole() != Role.EMPLOYER) throw new ForbiddenException("Only employers can manage companies");
    }

    @GetMapping
    public String list(HttpSession session, Model model) {
        SessionUser u = current(session);
        requireEmployer(u);
        model.addAttribute("companies", companies.listMine(userService.getById(u.getUserId())));
        return "companies/list";
    }
    @GetMapping("/new")
    public String createForm(HttpSession session, Model model) {
        requireEmployer(current(session));
        if (!model.containsAttribute("companyDto")) model.addAttribute("companyDto", new CompanyDto());
        return "companies/form";
    }
    @PostMapping
    public String create(@Valid @ModelAttribute("companyDto") CompanyDto dto, BindingResult br, HttpSession session) {
        SessionUser u = current(session); requireEmployer(u);
        if (br.hasErrors()) return "companies/form";
        companies.create(dto, userService.getById(u.getUserId()));
        return "redirect:/companies";
    }
    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable UUID id, HttpSession session, Model model) {
        SessionUser u = current(session); requireEmployer(u);
        var c = companies.get(id);
        if (!c.getOwner().getId().equals(u.getUserId())) throw new ForbiddenException("Not your company");
        CompanyDto dto = new CompanyDto();
        dto.setName(c.getName()); dto.setDescription(c.getDescription()); dto.setWebsite(c.getWebsite());
        model.addAttribute("companyDto", dto); model.addAttribute("editId", id);
        return "companies/form";
    }
    @PostMapping("/{id}")
    public String update(@PathVariable UUID id, @Valid @ModelAttribute("companyDto") CompanyDto dto, BindingResult br, HttpSession session, Model model) {
        SessionUser u = current(session); requireEmployer(u);
        if (br.hasErrors()) { model.addAttribute("editId", id); return "companies/form"; }
        companies.update(id, dto, u.getUserId());
        return "redirect:/companies";
    }
    @PostMapping("/{id}/delete")
    public String delete(@PathVariable UUID id, HttpSession session) {
        SessionUser u = current(session); requireEmployer(u);
        companies.delete(id, u.getUserId());
        return "redirect:/companies";
    }
}
