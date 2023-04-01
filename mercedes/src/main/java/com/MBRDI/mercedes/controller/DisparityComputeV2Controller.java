package com.MBRDI.mercedes.controller;

import com.MBRDI.mercedes.DisparityCalculator;
import org.json.JSONObject;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;



@RestController
@RequestMapping("/api/v2/mbrdi")
public class DisparityComputeV2Controller {

    @PostMapping("/computeDisparity/runTestcase")
    private Object computeDisparity() {
//      Download and run test cases
        try
        {
            return DisparityCalculator.runTestCasesAndPostResultToMBRDI();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }
}
