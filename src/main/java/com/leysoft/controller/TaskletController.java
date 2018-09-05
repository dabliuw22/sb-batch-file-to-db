
package com.leysoft.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.leysoft.service.inter.BatchService;

@RestController
@RequestMapping(
        value = "/tasklet")
public class TaskletController {

    @Autowired
    @Qualifier(
            value = "taskletServiceImp")
    private BatchService taskletService;

    @GetMapping(
            value = {
                "/run"
            })
    public ResponseEntity<String> run() {
        taskletService.run();
        return ResponseEntity.ok("Ok");
    }
}
