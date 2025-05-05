package com.payment.paypal.paypal;


import com.paypal.api.payments.*;
import com.paypal.base.rest.APIContext;
import com.paypal.base.rest.PayPalRESTException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Service
@RequiredArgsConstructor
public class PaypalService {

    private final APIContext apiContext;


    // Create payment
    public Payment createPayment( Double total, String currency, String method,
            String intent, String description, String cancelUrl, String successUrl) throws PayPalRESTException {

        // Set payment details

        // Set the amount(currency and total)
        Amount amount = new Amount();
        amount.setCurrency(currency);
        amount.setTotal(String.format(Locale.forLanguageTag(currency),"%.2f",total)); // 9.99$

        // Set the transaction details
        Transaction transaction = new Transaction();
        transaction.setDescription(description);
        transaction.setAmount(amount);
        List<Transaction> transactions= new ArrayList<>();
        transactions.add(transaction);

        // Set the payer(payment method)
        Payer payer = new Payer();
        payer.setPaymentMethod(method);

        // Set the intent(sale or authorize)
        Payment payment = new Payment();
        payment.setIntent(intent);
        payment.setPayer(payer);
        payment.setTransactions(transactions);

        // Set the redirect URLs(cancel and success)
        RedirectUrls redirectUrls = new RedirectUrls();
        redirectUrls.setCancelUrl(cancelUrl);
        redirectUrls.setReturnUrl(successUrl);

        // Set the redirect URLs to the payment
        payment.setRedirectUrls(redirectUrls);

        // Create the payment
        return payment.create(apiContext);
    }

    // Execute payment
    public Payment executePayment(String paymentId, String payerId) throws PayPalRESTException {


        Payment payment = new Payment();
        payment.setId(paymentId);

        // Set the payer ID
        PaymentExecution paymentExecution = new PaymentExecution();
        paymentExecution.setPayerId(payerId);

        return payment.execute(apiContext, paymentExecution);
    }
}
