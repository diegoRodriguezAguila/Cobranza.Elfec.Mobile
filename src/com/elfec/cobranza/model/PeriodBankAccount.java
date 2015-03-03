package com.elfec.cobranza.model;

import org.joda.time.DateTime;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

/**
 * Almacena los BAN_CTAS_PER
 * @author drodriguez
 *
 */
@Table(name = "PeriodBankAccounts")
public class PeriodBankAccount extends Model {
	/**
	 * IDBANCO en Oracle
	 */
	@Column(name = "BankId", notNull=true)
	private int bankId;	
	/**
	 * IDBAN_CTA en Oracle
	 */
	@Column(name = "BankAccountId", notNull=true)
	private int bankAccountId;	
	/**
	 * NROPERIODO en Oracle
	 */
	@Column(name = "PeriodNumber", notNull=true)
	private int periodNumber;	
	/**
	 * NROPERIODO en Oracle
	 */
	@Column(name = "CashierId")
	private int cashierId;	
	/**
	 * NROPERIODO y HORA_APERTURA en Oracle
	 */
	@Column(name = "OpeningDateTime")
	private DateTime openingDateTime;
	/**
	 * FECHA_CIERRE y HORA_CIERRE en Oracle
	 */
	@Column(name = "ClosingDateTime")
	private DateTime closingDateTime;
	/**
	 * IDSTATUS en Oracle
	 */
	@Column(name = "StatusId")
	private short statusId;	
	/**
	 * IDZONA en Oracle
	 */
	@Column(name = "ZoneId")
	private int zoneId;
	/**
	 * IDEMPRESA en Oracle
	 */
	@Column(name = "EnterpriseId")
	private int enterpriseId;
	
	public PeriodBankAccount() {
		super();
	}
	
	public PeriodBankAccount(int bankId, int bankAccountId, int periodNumber,
			int cashierId, DateTime openingDateTime, DateTime closingDateTime,
			short statusId, int zoneId, int enterpriseId) {
		super();
		this.bankId = bankId;
		this.bankAccountId = bankAccountId;
		this.periodNumber = periodNumber;
		this.cashierId = cashierId;
		this.openingDateTime = openingDateTime;
		this.closingDateTime = closingDateTime;
		this.statusId = statusId;
		this.zoneId = zoneId;
		this.enterpriseId = enterpriseId;
	}

	//#region Getters y Setters
	public int getBankId() {
		return bankId;
	}
	public void setBankId(int bankId) {
		this.bankId = bankId;
	}
	public int getBankAccountId() {
		return bankAccountId;
	}
	public void setBankAccountId(int bankAccountId) {
		this.bankAccountId = bankAccountId;
	}
	public int getPeriodNumber() {
		return periodNumber;
	}
	public void setPeriodNumber(int periodNumber) {
		this.periodNumber = periodNumber;
	}
	public int getCashierId() {
		return cashierId;
	}
	public void setCashierId(int cashierId) {
		this.cashierId = cashierId;
	}
	public DateTime getOpeningDateTime() {
		return openingDateTime;
	}
	public void setOpeningDateTime(DateTime openingDateTime) {
		this.openingDateTime = openingDateTime;
	}
	public DateTime getClosingDateTime() {
		return closingDateTime;
	}
	public void setClosingDateTime(DateTime closingDateTime) {
		this.closingDateTime = closingDateTime;
	}
	public short getStatusId() {
		return statusId;
	}
	public void setStatusId(short statusId) {
		this.statusId = statusId;
	}
	public int getZoneId() {
		return zoneId;
	}
	public void setZoneId(int zoneId) {
		this.zoneId = zoneId;
	}
	public int getEnterpriseId() {
		return enterpriseId;
	}
	public void setEnterpriseId(int enterpriseId) {
		this.enterpriseId = enterpriseId;
	}
	
	//#endregion
}
