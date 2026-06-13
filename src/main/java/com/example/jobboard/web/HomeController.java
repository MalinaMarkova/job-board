package com.example.jobboard.web;
import com.example.jobboard.service.JobPostService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {
    private final JobPostService jobs;
    public HomeController(JobPostService jobs) { this.jobs = jobs; }

    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("latest", jobs.listAll().stream().limit(5).toList());
        return "index";
    }
    @GetMapping("/about")
    public String about() { return "about"; }
}
