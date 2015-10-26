package com.elfec.cobranza.model.printer;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Concepto para impresión
 * @author drodriguez
 *
 */
public class PrintConcept {
	private String description;
	private BigDecimal amount;
	
	public PrintConcept() {
	}
	public PrintConcept(String description, BigDecimal amount) {
		this.description = description;
		this.amount = amount.setScale(2, RoundingMode.FLOOR);
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}
	
	@Override
	public String toString()
	{
		return description+": "+amount.toString();
	}
	
}
