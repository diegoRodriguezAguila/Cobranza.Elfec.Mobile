package com.elfec.cobranza.model.printer;

import java.math.BigDecimal;

import org.joda.time.DateTime;

/**
 * Clase utilizada para los reportes de resumen de caja de la impresora
 * @author drodriguez
 *
 */
public class CashDeskResume {
	private String concept;
	protected DateTime date;
	protected BigDecimal amount;
	protected int collectionPaymentsNum;	
	
	public CashDeskResume() {
		amount = BigDecimal.ZERO;
	}

	public CashDeskResume(DateTime date, BigDecimal amount,
			int collectionPaymentsNum) {
		this.date = date;
		this.amount = amount;
		this.collectionPaymentsNum = collectionPaymentsNum;
	}
	
	public CashDeskResume(String concept, DateTime date, BigDecimal amount,
			int collectionPaymentsNum) {
		this.concept = concept;
		this.date = date;
		this.amount = amount;
		this.collectionPaymentsNum = collectionPaymentsNum;
	}

	public String getConcept() {
		return concept;
	}

	public void setConcept(String concept) {
		this.concept = concept;
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
