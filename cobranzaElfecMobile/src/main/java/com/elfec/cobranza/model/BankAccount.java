package com.elfec.cobranza.model;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

import org.joda.time.DateTime;

import java.math.BigDecimal;
/**
 * Almacena los BAN_CTAS
 * @author drodriguez
 *
 */
@Table(name = "BankAccounts")
public class BankAccount extends Model{
	/**
	 * IDBANCO en Oracle
	 */
	@Column(name = "BankId", notNull=true, index=true)
	private int bankId;	
	/**
	 * IDBAN_CTA en Oracle
	 */
	@Column(name = "BankAccountId", notNull=true, index=true)
	private int bankAccountId;	
	/**
	 * NROCUENTA en Oracle
	 */
	@Column(name = "AccountNumber")
	private String accountNumber;	
	/**
	 * TIPO_CTA en Oracle
	 */
	@Column(name = "AccountType")
	private String accountType;	
	/**
	 * SALDO en Oracle
	 */
	@Column(name = "Balance")
	private BigDecimal balance;	
	/**
	 * SALDO_FECHA en Oracle
	 */
	@Column(name = "BalanceDate")
	private DateTime balanceDate;	
	/**
	 * IDCENTRO_COSTO en Oracle
	 */
	@Column(name = "CostCenterId")
	private int costCenterId;	
	/**
	 * DESCRIPCION en Oracle
	 */
	@Column(name = "Description")
	private String description;
	/**
	 * DA_VUELTO en Oracle
	 */
	@Column(name = "GivesChange")
	private short givesChange;	
	/**
	 * DA_ANTICIPO en Oracle
	 */
	@Column(name = "GivesCashAdvance")
	private short givesCashAdvance;	
	/**
	 * IDEMPRESA en Oracle
	 */
	@Column(name = "EnterpriseId")
	private int enterpriseId;
	/**
	 * IDSUCURSAL en Oracle
	 */
	@Column(name = "BranchOfficeId")
	private int branchOfficeId;
	/**
	 * IDZONA en Oracle
	 */
	@Column(name = "ZoneId")
	private int zoneId;
	/**
	 * IDTIPO_CAJA en Oracle
	 */
	@Column(name = "CashDeskTypeId")
	private int cashDeskTypeId;
	/**
	 * NROCAJA_EXT en Oracle Número de caja
	 */
	@Column(name = "CashDeskNumber")
	private int cashDeskNumber;
	/**
	 * IDCENTRO_COSTO_CR en Oracle
	 */
	@Column(name = "CrCostCenterId")
	private int crCostCenterId;	
	/**
	 * IDCTA_CONTAB_CR en Oracle
	 */
	@Column(name = "CrAccountancyAccount")
	private int crAccountancyAccount;	
	/**
	 * IDCENTRO_COSTO_DB en Oracle
	 */
	@Column(name = "DbCostCenterId")
	private int dbCostCenterId;	
	/**
	 * IDCTA_CONTAB_DB en Oracle
	 */
	@Column(name = "DbAccountancyAccount")
	private int dbAccountancyAccount;	
	/**
	 * IDTARJETA en Oracle
	 */
	@Column(name = "CardId")
	private int cardId;
	
	public BankAccount() {
		super();
	}
	
	public BankAccount(int bankId, int bankAccountId, String accountNumber,
			String accountType, BigDecimal balance, DateTime balanceDate,
			int costCenterId, String description, short givesChange,
			short givesCashAdvance, int enterpriseId, int branchOfficeId,
			int zoneId, int cashDeskTypeId, int cashDeskNumber,
			int crCostCenterId, int crAccountancyAccount, int dbCostCenterId,
			int dbAccountancyAccount, int cardId) {
		super();
		this.bankId = bankId;
		this.bankAccountId = bankAccountId;
		this.accountNumber = accountNumber;
		this.accountType = accountType;
		this.balance = balance;
		this.balanceDate = balanceDate;
		this.costCenterId = costCenterId;
		this.description = description;
		this.givesChange = givesChange;
		this.givesCashAdvance = givesCashAdvance;
		this.enterpriseId = enterpriseId;
		this.branchOfficeId = branchOfficeId;
		this.zoneId = zoneId;
		this.cashDeskTypeId = cashDeskTypeId;
		this.cashDeskNumber = cashDeskNumber;
		this.crCostCenterId = crCostCenterId;
		this.crAccountancyAccount = crAccountancyAccount;
		this.dbCostCenterId = dbCostCenterId;
		this.dbAccountancyAccount = dbAccountancyAccount;
		this.cardId = cardId;
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
	public String getAccountNumber() {
		return accountNumber;
	}
	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}
	public String getAccountType() {
		return accountType;
	}
	public void setAccountType(String accountType) {
		this.accountType = accountType;
	}
	public BigDecimal getBalance() {
		return balance;
	}
	public void setBalance(BigDecimal balance) {
		this.balance = balance;
	}
	public DateTime getBalanceDate() {
		return balanceDate;
	}
	public void setBalanceDate(DateTime balanceDate) {
		this.balanceDate = balanceDate;
	}
	public int getCostCenterId() {
		return costCenterId;
	}
	public void setCostCenterId(int costCenterId) {
		this.costCenterId = costCenterId;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public short getGivesChange() {
		return givesChange;
	}
	public void setGivesChange(short givesChange) {
		this.givesChange = givesChange;
	}
	public short getGivesCashAdvance() {
		return givesCashAdvance;
	}
	public void setGivesCashAdvance(short givesCashAdvance) {
		this.givesCashAdvance = givesCashAdvance;
	}
	public int getEnterpriseId() {
		return enterpriseId;
	}
	public void setEnterpriseId(int enterpriseId) {
		this.enterpriseId = enterpriseId;
	}
	public int getBranchOfficeId() {
		return branchOfficeId;
	}
	public void setBranchOfficeId(int branchOfficeId) {
		this.branchOfficeId = branchOfficeId;
	}
	public int getZoneId() {
		return zoneId;
	}
	public void setZoneId(int zoneId) {
		this.zoneId = zoneId;
	}
	public int getCashDeskTypeId() {
		return cashDeskTypeId;
	}
	public void setCashDeskTypeId(int cashDeskTypeId) {
		this.cashDeskTypeId = cashDeskTypeId;
	}
	public int getCashDeskNumber() {
		return cashDeskNumber;
	}
	public void setCashDeskNumber(int cashDeskNumber) {
		this.cashDeskNumber = cashDeskNumber;
	}
	public int getCrCostCenterId() {
		return crCostCenterId;
	}
	public void setCrCostCenterId(int crCostCenterId) {
		this.crCostCenterId = crCostCenterId;
	}
	public int getCrAccountancyAccount() {
		return crAccountancyAccount;
	}
	public void setCrAccountancyAccount(int crAccountancyAccount) {
		this.crAccountancyAccount = crAccountancyAccount;
	}
	public int getDbCostCenterId() {
		return dbCostCenterId;
	}
	public void setDbCostCenterId(int dbCostCenterId) {
		this.dbCostCenterId = dbCostCenterId;
	}
	public int getDbAccountancyAccount() {
		return dbAccountancyAccount;
	}
	public void setDbAccountancyAccount(int dbAccountancyAccount) {
		this.dbAccountancyAccount = dbAccountancyAccount;
	}
	public int getCardId() {
		return cardId;
	}
	public void setCardId(int cardId) {
		this.cardId = cardId;
	}	
	//#endregion
}
