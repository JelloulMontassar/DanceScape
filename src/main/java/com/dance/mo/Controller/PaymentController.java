package com.dance.mo.Controller;

import com.dance.mo.auth.DTO.PaymentRequest;
import com.dance.mo.auth.DTO.PaymentResponse;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.stripe.param.PaymentIntentCreateParams;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {
    @PostMapping("/create-payment-intent")
    public PaymentResponse createPaymentIntent(@RequestBody PaymentRequest request) throws StripeException {
        PaymentIntentCreateParams createParams = PaymentIntentCreateParams.builder()
                .setAmount(request.getAmount()*100)
                .setCurrency(request.getCurrency())

                .setReceiptEmail(request.getEmail())
                .setAutomaticPaymentMethods(
                        PaymentIntentCreateParams
                                .AutomaticPaymentMethods
                                .builder()
                                .setEnabled(true)
                                .build()
                )
                .build();


        PaymentIntent payment =  PaymentIntent.create(createParams);
        return new PaymentResponse(payment.getId(), payment.getClientSecret());
    }
}

