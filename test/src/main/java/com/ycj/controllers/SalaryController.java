package com.ycj.controllers;

import com.ycj.beans.AutoWired;
import com.ycj.service.SalaryService;
import com.ycj.web.mvc.Controller;
import com.ycj.web.mvc.RequestMapping;
import com.ycj.web.mvc.RequestParam;

@Controller
public class SalaryController {

    @AutoWired
    private SalaryService salaryService ;//声明对SalaryService的依赖

    @RequestMapping("/get_salary.json")
    public Integer getSalary(@RequestParam("name") String name, @RequestParam("experience")String experience){
        // return 10000 ;
        return salaryService.calSalary(Integer.parseInt(experience)) ;
    }
}
