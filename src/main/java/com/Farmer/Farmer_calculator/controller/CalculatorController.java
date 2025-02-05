package com.Farmer.Farmer_calculator.controller;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = " http://localhost:5173/") // Allow frontend access
public class CalculatorController {

    @PostMapping("/calculate")
    public ResponseEntity<Map<String, String>> calculate(
            @RequestBody Map<String, String> request,
            @RequestHeader(value = "Accept-Language", defaultValue = "en") String language) {

        Locale locale = Locale.forLanguageTag(language);
        ResourceBundle bundle = ResourceBundle.getBundle("messages", locale);

        String calculationType = request.get("calculationType"); // Define the type of calculation
        double result = 0.0;
        String responseMessage = "";

        // Declare variables that are shared between cases
        double landSize = 0.0;
        double quantity = 0.0;
        double price = 0.0;
        double seedCost = 0.0;
        double fertilizerCost = 0.0;
        double laborCost = 0.0;
        double waterCost = 0.0;
        double loanAmount = 0.0;
        double interestRate = 0.0;
        int durationMonths = 0;

        try {
            // Read common parameters that are required in many calculations
            if (request.containsKey("landSize")) {
                landSize = Double.parseDouble(request.get("landSize"));
            }
            if (request.containsKey("quantity")) {
                quantity = Double.parseDouble(request.get("quantity"));
            }
            if (request.containsKey("price")) {
                price = Double.parseDouble(request.get("price"));
            }
            if (request.containsKey("seedCost")) {
                seedCost = Double.parseDouble(request.get("seedCost"));
            }
            if (request.containsKey("fertilizerCost")) {
                fertilizerCost = Double.parseDouble(request.get("fertilizerCost"));
            }
            if (request.containsKey("laborCost")) {
                laborCost = Double.parseDouble(request.get("laborCost"));
            }
            if (request.containsKey("waterCost")) {
                waterCost = Double.parseDouble(request.get("waterCost"));
            }
            if (request.containsKey("loanAmount")) {
                loanAmount = Double.parseDouble(request.get("loanAmount"));
            }
            if (request.containsKey("interestRate")) {
                interestRate = Double.parseDouble(request.get("interestRate"));
            }
            if (request.containsKey("durationMonths")) {
                durationMonths = Integer.parseInt(request.get("durationMonths"));
            }

            switch (calculationType) {
                case "profit":
                    result = quantity * price;
                    responseMessage = bundle.getString("totalEarnings") + " ₹" + result;
                    break;

                case "cropCost":
                    result = (seedCost + fertilizerCost + laborCost + waterCost) / landSize;
                    responseMessage = bundle.getString("cropCostPerAcre") + " ₹" + result;
                    break;

                case "loan":
                    double monthlyRate = (interestRate / 100) / 12;
                    result = (loanAmount * monthlyRate) / (1 - Math.pow(1 + monthlyRate, -durationMonths));
                    responseMessage = bundle.getString("monthlyEMI") + " ₹" + result;
                    break;

                case "yield":
                    double expectedYieldPerAcre = Double.parseDouble(request.get("expectedYieldPerAcre"));
                    double totalYield = expectedYieldPerAcre * landSize;
                    responseMessage = bundle.getString("estimatedYield") + " " + totalYield + " kg";
                    break;

                case "marketPrice":
                    String commodity = request.get("commodity");
                    responseMessage = bundle.getString("marketPriceFor") + " " + commodity + ":     Loading......";
                    break;

                case "subsidy":
                    responseMessage = bundle.getString("subsidyInfo") + "     Loading.....";
                    break;

                case "irrigationCost":
                    double waterUsagePerAcre = Double.parseDouble(request.get("waterUsagePerAcre"));
                    double costPerLiter = Double.parseDouble(request.get("costPerLiter"));
                    result = waterUsagePerAcre * costPerLiter * landSize;
                    responseMessage = bundle.getString("irrigationCost") + " ₹" + result;
                    break;

                case "fertilizerCost":
                    double fertilizerQuantityPerAcre = Double.parseDouble(request.get("fertilizerQuantityPerAcre"));
                    double fertilizerCostPerUnit = Double.parseDouble(request.get("fertilizerCostPerUnit"));
                    result = fertilizerQuantityPerAcre * fertilizerCostPerUnit * landSize;
                    responseMessage = bundle.getString("fertilizerCost") + " ₹" + result;
                    break;

                case "pesticideCost":
                    double pesticideCostPerLiter = Double.parseDouble(request.get("pesticideCostPerLiter"));
                    double pesticideAmountPerAcre = Double.parseDouble(request.get("pesticideAmountPerAcre"));
                    result = pesticideCostPerLiter * pesticideAmountPerAcre * landSize;
                    responseMessage = bundle.getString("pesticideCost") + " ₹" + result;
                    break;

                case "totalInvestment":
                    double landCost = Double.parseDouble(request.get("landCost"));
                    double equipmentCost = Double.parseDouble(request.get("equipmentCost"));
                    result = landCost + equipmentCost + (seedCost + fertilizerCost + laborCost + waterCost) * landSize;
                    responseMessage = bundle.getString("totalInvestment") + " ₹" + result;
                    break;

                case "transportCost":
                    double fuelCostPerKm = Double.parseDouble(request.get("fuelCostPerKm"));
                    double distance = Double.parseDouble(request.get("distance"));
                    result = fuelCostPerKm * distance;
                    responseMessage = bundle.getString("transportCost") + " ₹" + result;
                    break;

                default:
                    responseMessage = bundle.getString("invalidCalculation");
            }

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE + "; charset=UTF-8")
                    .body(Map.of("message", responseMessage));

        } catch (NumberFormatException e) {
            return ResponseEntity.badRequest()
                    .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE + "; charset=UTF-8")
                    .body(Map.of("error", bundle.getString("invalidInput")));
        }
    }
}
