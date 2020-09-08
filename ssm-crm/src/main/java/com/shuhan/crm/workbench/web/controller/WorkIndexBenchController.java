package com.shuhan.crm.workbench.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class WorkIndexBenchController {
    @RequestMapping("/workbench/index.do")
public String index(){
return "workbench/index";
}
}
