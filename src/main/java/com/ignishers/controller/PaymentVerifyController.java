package com.ignishers.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

import com.razorpay.Utils;
import org.json.JSONObject;

@Controller
@RequestMapping("/payment")
public class PaymentVerifyController {

    private static final String RAZORPAY_SECRET = "9R9oXHMGHGCLyKfWHh3fJ403"; // same as order creation

    @PostMapping("/verify")
    @ResponseBody
    public ResponseEntity<String> verifyPayment(
            @RequestParam("razorpay_payment_id") String paymentId,
            @RequestParam("razorpay_order_id") String orderId,
            @RequestParam("razorpay_signature") String signature) {

        try {
            // Create JSON object for verification
            JSONObject options = new JSONObject();
            options.put("razorpay_order_id", orderId);
            options.put("razorpay_payment_id", paymentId);
            options.put("razorpay_signature", signature);

            boolean isValidSignature = Utils.verifyPaymentSignature(options, RAZORPAY_SECRET);

            if (isValidSignature) {
                return ResponseEntity.ok("✅ Payment verified successfully!");
                // TODO: Save to database
            } else {
                return ResponseEntity.ok("❌ Payment verification failed!");
            }

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body("Error: " + e.getMessage());
        }
    }
}
