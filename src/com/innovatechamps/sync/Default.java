package com.innovatechamps.sync;

import java.math.BigDecimal;

import com.innovatechamps.sync.client.payment.PaymentClient;
import com.innovatechamps.sync.client.payment.PaymentCreateRequest;
import com.innovatechamps.sync.client.payment.PaymentPayerRequest;
import com.innovatechamps.sync.execptions.DCApiException;
import com.innovatechamps.sync.execptions.DCException;
import com.innovatechamps.sync.resources.payment.Payment;

public class Default {
	public static void main(String[] args) {
		DevChampsConfig.setAccessToken("YOUR_ACCESS_TOKEN");

	    PaymentClient client = new PaymentClient();

	    PaymentCreateRequest createRequest =
	        PaymentCreateRequest.builder()
	            .transactionAmount(new BigDecimal(1000))
	            .token("your_cardtoken")
	            .description("description")
	            .installments(1)
	            .paymentMethodId("visa")
	            .payer(PaymentPayerRequest.builder().email("dummy_email").build())
	            .build();

	    try {
	      Payment payment = client.create(createRequest);
	      System.out.println(payment);
	    } catch (DCApiException ex) {
	      System.out.printf(
	          "API Error. Status: %s, Content: %s%n",
	          ex.getApiResponse().getStatusCode(), ex.getApiResponse().getContent());
	    } catch (DCException ex) {
	      ex.printStackTrace();
	    }
	}
}
