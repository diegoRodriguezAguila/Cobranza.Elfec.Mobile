package com.elfec.cobranza.model.printer;

import java.math.BigDecimal;

import org.joda.time.DateTime;

/**
 * Clase utilizada para los reportes de la impresora
 * @author drodriguez
 *
 */
public class CashDeskResume {
	private DateTime date;
	private BigDecimal amount;
	private int collectionPaymentsNum;	
	
	public CashDeskResume() {
		amount = BigDecimal.ZERO;
	}

	public CashDeskResume(DateTime date, BigDecimal amount,
			int collectionPaymentsNum) {
		super();
		this.date = date;
		this.amount = amount;
		this.collectionPaymentsNum = collectionPaymentsNum;
	}

	public DateTime getDate() {
		return date;
	}

	public void setDate(DateTime date) {
		this.date = date;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public int getCollectionPaymentsNum() {
		return collectionPaymentsNum;
	}

	public void setCollectionPaymentsNum(int collectionPaymentsNum) {
		this.collectionPaymentsNum = collectionPaymentsNum;
	}
	
}
