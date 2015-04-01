package com.elfec.cobranza.model.printer;

import java.util.ArrayList;
import java.util.List;

/**
 * Clase utilizada para los reportes de resumen diario de caja de la impresora
 * @author drodriguez
 *
 */
public class CashDeskDailyResume{
	private List<CashDeskResume> cashDeskResumes;
	private int internalControlCodeStart;
	private int internalControlCodeEnd;
	
	public CashDeskDailyResume() {
		this.cashDeskResumes = new ArrayList<CashDeskResume>();
	}

	public CashDeskDailyResume(int internalControlCodeStart, int internalControlCodeEnd) {
		this();
		this.internalControlCodeStart = internalControlCodeStart;
		this.internalControlCodeEnd = internalControlCodeEnd;
	}

	public int getInternalControlCodeStart() {
		return internalControlCodeStart;
	}

	public void setInternalControlCodeStart(int internalControlCodeStart) {
		this.internalControlCodeStart = internalControlCodeStart;
	}

	public int getInternalControlCodeEnd() {
		return internalControlCodeEnd;
	}

	public void setInternalControlCodeEnd(int internalControlCodeEnd) {
		this.internalControlCodeEnd = internalControlCodeEnd;
	}

	public List<CashDeskResume> getCashDeskResumes() {
		return cashDeskResumes;
	}	
	
	public void addCashDeskResume(CashDeskResume resume)
	{
		cashDeskResumes.add(resume);
	}
	
	public void addMultipleCashDeskResumes( List<CashDeskResume> resumes)
	{
		cashDeskResumes.addAll(resumes);
	}
	
}
