package com.elfec.cobranza.model;

import org.joda.time.DateTime;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;
import com.elfec.cobranza.model.serializers.JodaDateTimeSerializer;

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
	@Column(name = "BankAccountId", notNull=true, index=true)
	private int bankAccountId;	
	/**
	 * NROPERIODO en Oracle
	 */
	@Column(name = "PeriodNumber", notNull=true, index=true)
	private int periodNumber;	
	/**
	 * IDCAJERO en Oracle
	 */
	@Column(name = "CashierId")
	private int cashierId;	
	/**
	 * FECHA en Oracle
	 */
	@Column(name = "PeriodDate")
	private DateTime periodDate;
	/**
	 * HORA_APERTURA en Oracle
	 */
	@Column(name = "OpeningTime")
	private DateTime openingTime;
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
			int cashierId, DateTime periodDate, DateTime openingTime,
			DateTime closingDateTime, short statusId, int zoneId,
			int enterpriseId) {
		super();
		this.bankId = bankId;
		this.bankAccountId = bankAccountId;
		this.periodNumber = periodNumber;
		this.cashierId = cashierId;
		this.periodDate = periodDate;
		this.openingTime = openingTime;
		this.closingDateTime = closingDateTime;
		this.statusId = statusId;
		this.zoneId = zoneId;
		this.enterpriseId = enterpriseId;
	}



	/**
	 * Obtiene el periodo de la cuenta
	 * @param cashdeskNumber
	 * @return
	 */
	public static PeriodBankAccount findByCashdeskNumberAndDate(int cashdeskNumber)
	{
		return new Select().from(PeriodBankAccount.class)
				.where("BankAccountId = ? AND PeriodDate = ?", cashdeskNumber, 
				(new JodaDateTimeSerializer()).serialize(DateTime.now().withTimeAtStartOfDay())).executeSingle();
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
	
	public DateTime getPeriodDate() {
		return periodDate;
	}

	public void setPeriodDate(DateTime periodDate) {
		this.periodDate = periodDate;
	}

	public DateTime getOpeningTime() {
		return openingTime;
	}

	public void setOpeningTime(DateTime openingTime) {
		this.openingTime = openingTime;
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
