package com.elfec.cobranza.business_logic;

import java.util.Collections;
import java.util.List;

import org.joda.time.DateTime;

import com.elfec.cobranza.model.SupplyStatus;

/**
 * Maneja las operaciones de multiples SUMIN_ESTADOS de un suministro
 * @author drodriguez
 *
 */
public class SupplyStatusSet {
	private List<SupplyStatus> supplyStatuses;
	
	public SupplyStatusSet(List<SupplyStatus> supplyStatuses)
	{
		if(supplyStatuses==null)
			throw new IllegalArgumentException("La lista de sumin estados no puede ser NULL!");
		this.supplyStatuses = Collections.unmodifiableList(supplyStatuses);
	}
	
	/**
	 * Obtiene la sumatoria de consumo facturado de los SUMIN_ESTADOS que conforman el set
	 * @return Consumo facturado
	 */
	public int getBilledConsume()
	{
		int billedConsume = 0;
		for(SupplyStatus supplyStatus : supplyStatuses)
		{
			billedConsume+=supplyStatus.getBilledConsume();
		}
		return billedConsume;
	}
	
	/**
	 * Obtiene la fecha de la última lectura
	 * @return the lastReadingDate
	 */
	public DateTime getLastReadingDate()
	{
		if(supplyStatuses.size()>0)
			return supplyStatuses.get(0).getLastReadingDate();
		return new DateTime(0);
	}
	
	/**
	 * Obtiene la fecha de la lectura más reciente
	 * @return the currentReadingDate
	 */
	public DateTime getDate()
	{
		int size = supplyStatuses.size();
		if(size>0)
			return supplyStatuses.get(size-1).getDate();
		return new DateTime(Long.MAX_VALUE);
	}
	
	/**
	 * Obtiene la lista de SUMIN_ESTADOS de este set. La lista es inmodificable, si se realiza algún cambio se lanza una excepción
	 * @return lista de SUMIN_ESTADOS
	 */
	public List<SupplyStatus> getSupplyStatusList()
	{
		return supplyStatuses;
	}
}
