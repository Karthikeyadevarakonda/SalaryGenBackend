package com.example.salaryAquittance.controller;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class DemoController {

    @GetMapping("/admin/hello")
    public String admin() { return "Hello ADMIN"; }

    @GetMapping("/hr/hello")
    public String hr() { return "Hello HR (or ADMIN)"; }

    @GetMapping("/staff/hello")
    public String staff() { return "Hello STAFF/HR/ADMIN"; }

    @GetMapping("/secure/hello")
    public String anyAuth() { return "Hello any authenticated user"; }
}
