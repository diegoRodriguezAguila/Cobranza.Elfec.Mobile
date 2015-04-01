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
	private long longernalControlCodeStart;
	private long longernalControlCodeEnd;
	
	public CashDeskDailyResume() {
		this.cashDeskResumes = new ArrayList<CashDeskResume>();
	}

	public CashDeskDailyResume(long longernalControlCodeStart, long longernalControlCodeEnd) {
		this();
		this.longernalControlCodeStart = longernalControlCodeStart;
		this.longernalControlCodeEnd = longernalControlCodeEnd;
	}

	public long getInternalControlCodeStart() {
		return longernalControlCodeStart;
	}

	public void setInternalControlCodeStart(long longernalControlCodeStart) {
		this.longernalControlCodeStart = longernalControlCodeStart;
	}

	public long getInternalControlCodeEnd() {
		return longernalControlCodeEnd;
	}

	public void setInternalControlCodeEnd(long longernalControlCodeEnd) {
		this.longernalControlCodeEnd = longernalControlCodeEnd;
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
