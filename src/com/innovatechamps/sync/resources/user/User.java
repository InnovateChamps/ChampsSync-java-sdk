package com.innovatechamps.sync.resources.user;

import java.math.BigDecimal;
import java.util.List;

import com.innovatechamps.sync.network.DCResource;
import com.innovatechamps.sync.resources.payment.PaymentItem;

import lombok.Getter;

/** User information.. */
@Getter
public class User extends DCResource {

	/** Id of the user. */
	private Long id;

	/** Nickname of user. */
	private String nickname;

	/** First name of user. */
	private String firstName;

	/** Last name of user. */
	private String lastName;

	/** Email of the user. */
	private String email;

	/** Id of the user's site. */
	private String siteId;

	/** Id of the user's country. */
	private String countryId;

	/** Current balance from account */
	private BigDecimal accountBalance;

	/** Transactions made by the account */
	private List<PaymentItem> transactions;

}
