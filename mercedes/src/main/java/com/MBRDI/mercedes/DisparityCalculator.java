package com.MBRDI.mercedes;

import org.apache.http.StatusLine;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class DisparityCalculator {


    //  This function calculates the minimum possible disparity sum
    public static int calculateMinimumDisparity(int noOfCars, int[] speed) {
        Arrays.sort(speed);
        int minDisparity = 0;
        int maxSpeed = speed[0];
        int minSpeed = speed[0];
        for (int i = 0; i < noOfCars; i++) {
            maxSpeed = Math.max(maxSpeed, speed[i]);
            minSpeed = Math.min(minSpeed, speed[i]);
            minDisparity += (maxSpeed - minSpeed);
        }
        return minDisparity;
    }

    //  V1 API - This function handles JSON input
    public static Map<Integer, Integer> getComputeResult(JSONObject testCaseJSON) throws Exception {
        String totalTestCaseNode = "Total Test Cases";
        if (!testCaseJSON.has(totalTestCaseNode)) {
            throw new Exception("Invalid Data provided");
        }
        Map<Integer, Integer> resultMap = new HashMap<>();
        int totalTestCase = testCaseJSON.getInt(totalTestCaseNode);
        for (int i = 0; i < totalTestCase; i++) {
            JSONObject testCase = testCaseJSON.getJSONObject(String.valueOf(i + 1));
            int noOfCars = testCase.getInt("N");
            int[] speed = getIntArrayFromJSONArray(testCase.getJSONArray("S"), noOfCars);
            int result = calculateMinimumDisparity(noOfCars, speed);
            resultMap.put(i + 1, result);
        }
        return resultMap;
    }

    //    Function to convert JSONArray to Java primitive array for faster computation
    private static int[] getIntArrayFromJSONArray(JSONArray speedJsonArray, int noOfCars) {
        int[] speed = new int[noOfCars];
        for (int i = 0; i < noOfCars; i++) {
            speed[i] = speedJsonArray.getInt(i);
        }
        return speed;
    }


//  V2 API - download and run the test cases and post results to MBRDI POST link

    public static String runTestCasesAndPostResultToMBRDI() throws Exception {
        String resultStr = "";
        for (int i = 0; i < TestCaseFileLinks.TESTCASE_FILE_URLS.size(); i++) {
            JSONObject testcaseFileJson = getJSONFromURL(TestCaseFileLinks.TESTCASE_FILE_URLS.get(i));
            if (testcaseFileJson != null) {
                Map<Integer, Integer> resultMap = getComputeResult(testcaseFileJson);
                resultStr += executePost(new JSONObject(resultMap), i + 1);
            }
        }
        return "Pushed testcase results" + resultStr;
    }

    public static JSONObject getJSONFromURL(String urlString) throws IOException {
        try {
            URL url = new URL(urlString);
            BufferedInputStream bis = new BufferedInputStream(url.openStream());
            return new JSONObject(new JSONTokener(Objects.requireNonNull(bis)));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String executePost(JSONObject answerStr, int testcaseNo) throws Exception {

        String POST_URL = "https://mercedes-hiring-2023.hackerearth.com/check";

        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            final HttpPost httpPost = new HttpPost(POST_URL);
            final List<BasicNameValuePair> nameValuePairs = new ArrayList<>();
            nameValuePairs.add(new BasicNameValuePair("email", "vaivassaravanan1@gmail.com"));
            nameValuePairs.add(new BasicNameValuePair("password", "vaivas123"));
            nameValuePairs.add(new BasicNameValuePair("testCase", String.valueOf(testcaseNo)));
            nameValuePairs.add(new BasicNameValuePair("answerStr", answerStr.toString()));
            httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

            try (final CloseableHttpResponse response = httpClient.execute(httpPost)) {
                var statusLine = response.getStatusLine();
                System.out.println(((StatusLine) statusLine).getStatusCode() + " " + statusLine.getReasonPhrase());
                String responseBody = EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8);
                System.out.println("Response body: " + responseBody);
                return responseBody;
            }
        }

    }

}
