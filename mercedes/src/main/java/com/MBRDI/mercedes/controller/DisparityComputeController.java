package com.MBRDI.mercedes.controller;

import com.MBRDI.mercedes.DisparityCalculator;
import org.json.JSONObject;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/mbrdi")
public class DisparityComputeController {

    @PostMapping("/computeDisparity")
    private Object computeDisparity(@RequestBody String testCaseJsonString) {
        JSONObject testCase = new JSONObject(testCaseJsonString);
        try {
            return DisparityCalculator.getComputeResult(testCase);
        }
        catch (Exception e)
        {
            return e.getMessage();
        }
    }
}
