package com.deadpineapple.front.tools;

import com.paypal.api.payments.*;
import com.paypal.base.rest.APIContext;
import com.paypal.base.rest.OAuthTokenCredential;
import com.paypal.base.rest.PayPalRESTException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by mikael on 19/05/16.
 */
public class PayPalService {

    private APIContext apiContext;
    private double price;

    public PayPalService(double price){
        this.price = price;
    }

    public String startCheckOut(String serverUrl){
        try {
            Map<String, String> sdkConfig = new HashMap<String, String>();
            sdkConfig.put("mode", "sandbox");

            //clientID, client secret, map
            String accessToken = new OAuthTokenCredential(
                    "AUiWqyPU7-S9Lb7B40caix-WdmLbdeuyz-gFXJxm7R1PWa8cVxkz5kvkxpYzlBqUpajd6El2ybpD0w_D",
                    "EO5ZSJ7XAKLMKax7cAuS9E6h2J7KV1pK4_Lf-OozscG5M0VYBB_85Mqa1QGObHgHiwJXVyVU5IB4exEj",
                    sdkConfig).getAccessToken();

            apiContext = new APIContext(accessToken);
            apiContext.setConfigurationMap(sdkConfig);

            Amount amount = new Amount();
            amount.setCurrency("EUR");
            String p = String.format("%1$,.2f", price).replace(',','.');
            amount.setTotal(p);

            Transaction transaction = new Transaction();
            transaction.setDescription("DeadPineapple converter.");
            transaction.setAmount(amount);

            List<Transaction> transactions = new ArrayList<Transaction>();
            transactions.add(transaction);

            Payer payer = new Payer();
            payer.setPaymentMethod("paypal");

            Payment payment = new Payment();
            payment.setIntent("sale");
            payment.setPayer(payer);
            payment.setTransactions(transactions);
            RedirectUrls redirectUrls = new RedirectUrls();
            redirectUrls.setCancelUrl(serverUrl + "/upload/payment");
            redirectUrls.setReturnUrl(serverUrl + "/upload/payment");
            payment.setRedirectUrls(redirectUrls);

            Payment createdPayment = payment.create(apiContext);
            List<Links> list = createdPayment.getLinks();

            for (Links l:list) {
                if (l.getRel().equals("approval_url"))
                {
                    // get the url of paypal platform
                    return l.getHref();
                }
            }

        }
        catch (Exception e)
        {

        }
        return null;
    }

    public boolean finishCheckOut(String paymentID, String payerID, String token)
    {
        try{
            // retrieve payment Object
            Payment payment = Payment.get(apiContext,paymentID);

            // execute the payment
            PaymentExecution paymentExecute = new PaymentExecution();
            paymentExecute.setPayerId(payerID);
            payment.execute(apiContext, paymentExecute);
            return true;

        } catch (PayPalRESTException e) {
            // payment failed
            return false;
        }
    }

}
