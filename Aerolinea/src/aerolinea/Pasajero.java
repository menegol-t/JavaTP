package aerolinea;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;

public class Pasajero 
{
	private Cliente cliente;
	private HashMap<Integer,Asiento> asientos;
	
	public Pasajero(Cliente cliente) 
	{
		verificarParametros(cliente);
		
		this.cliente = cliente;
		this.asientos = new HashMap<Integer, Asiento>();
	}
	
	private void verificarParametros(Cliente cliente) 
	{
		if(cliente == null) throw new RuntimeException("Pasajeros: Se debe pasar un cliente para generar el pasajero");
	}
	
	public Asiento getAsiento(Integer codigoAsiento)
	{
		Asiento asiento = asientos.get(codigoAsiento);
		
		if(asiento == null) throw new RuntimeException("El numero de asiento provisto no corresponde a este pasajero :" + cliente.getDni());
		
		return asiento;
	}
	
	public ArrayList<Asiento> getAsientos()
	{
		ArrayList<Asiento> asiento = new ArrayList<>(asientos.values());
		
		return asiento;
	}
	
	/*
	 * Para obtener el costo de cada pasajero, sumo el costo de todos los asientos que tiene. 
	 * */
	public double getCosto() 
	{
		double costo = 0;
		
		for(Asiento asientoActual: asientos.values() ) 
		{
			costo =+ asientoActual.getPrecio();
		}
		
		return costo;
	} 
	
	/*
	 * Esto suma un asiento a los asientos que compro el pasajero
	 * */
	public int asignarAsiento(Asiento asiento) 
	{	
		//Guardo el asiento en el diccionario de asientos del pasajero.
		asientos.put(asiento.getCodigo(), asiento);
		
		return asiento.getCodPasaje();
	}

	
	/*
	 * Para cancelar un pasaje O(1) y redisponibilizar el asiento, elimino el asiento del pasajero
	 * Lo "limpio" (quito su numero de pasaje y lo pongo desocupado)
	 * Y finalmente lo retorno.
	 * */
	public Asiento removerAsiento(Integer nroAsiento)
	{
		//Busco el asiento.
		Asiento asiento = getAsiento(nroAsiento);
		
		//Lo "limpio" (pongo en desocupado y reseteo su codigo de pasaje)
		asiento.setOcupado(false);
		
		asiento.setCodPasaje(0);
		
		//Remuevo el asiento del diccionario de asientos 
		asientos.remove(asiento.getCodigo());
		
		//Retorno el asiento ya liberado
		return asiento;
	}
	
	/*
	 * Para cancelarPasje busco entre todos los asientos del cliente cual es el que me pidieron eliminar por su numero de pasaje
	 * Si lo encontre, lo elimino y retorno su precio. 
	 * Si no lo encontre, es que ya estaba eliminado y retorno el precio 0.
	 * */
	public double eliminarPasaje(int codPasaje) 
	{
		double precio = 0.0;
		
		for(Asiento asientoActual: asientos.values()) {
			 
			/* Cuando encuentro el asiento cuyo codigo coincide con el dado, lo elimino de memoria y que el garbage colector se encargue. 
			 * Si nunca lo encuentro, entonces ya estaba eliminado.
			 * */
			if(asientoActual.getCodPasaje() == codPasaje) {
				
				precio = asientoActual.getPrecio();
				
				asientoActual = null;
			}
		}
		
		//Si nunca encontre el asiento, great ya estaba eliminado, retorno que el precio de ese asiento a cancelar es 0.
		return precio;
	}
	
	public Cliente getCliente()
	{
		return cliente;
	}
	
	public int getDniCliente() 
	{
		return cliente.getDni();
	}
	
	
}