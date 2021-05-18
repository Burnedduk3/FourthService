package com.fourthservice.controller;

import com.fourthservice.services.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/report")
public class ReportController {

    @Autowired
    ReportService reportService;

    @GetMapping("/top-joiners")
    public String createTopJoinersReport (@RequestParam String numberOfJoiners, @RequestParam String stack) {
        return reportService.createTopJoinersReportService(numberOfJoiners, stack);
    }

    @GetMapping("task-full-report")
    public String createFullTaskReport (){
        return reportService.createFullTaskReportService();
    }

    @GetMapping("task-full-report/{joinerId}")
    public String createFullTaskReport (@PathVariable String joinerId){
        return reportService.createJoinerTaskReportService(joinerId);
    }

    @GetMapping("time-report")
    public String timeReport (){
        return reportService.timeReportGenerator();
    }
}
