package aerolinea;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;

public class Pasajero 
{
	private Cliente cliente;
	private HashMap<Integer,Asiento> asientos;
	private int refrigeriosConsumidos;
	private double costo;
	
	public Pasajero(Cliente cliente) 
	{
		this.cliente = cliente;
		this.asientos = new HashMap<Integer, Asiento>();
		this.refrigeriosConsumidos = 0;
		this.costo = 0.0;
	}
	
	public Asiento getAsiento(Integer codigoAsiento)
	{
		return asientos.get(codigoAsiento);
	}
	
	public int getCantAsientos()
	{
		return asientos.size();
	}
	
	public void consumirRefrigerio()
	{
		refrigeriosConsumidos += 1;
	}
	
	public int getRefrigeriosConsumidos()
	{
		return refrigeriosConsumidos;
	}
	
	public double getCosto()
	{
		return costo;
	}
	
	public void setCosto(double costo)
	{
		this.costo = costo;
	}
	
	public int asignarAsiento(Asiento asiento, int codPasaje) 
	{	
		//Le asigno al asiento su codigo de pasaje
		asiento.setCodPasaje(codPasaje);
		
		//Pongo el asiento en el diccionario de asientos del pasajero.
		asientos.put(asiento.getCodigo(), asiento);
		
		/*
		 * Esto debe retornar el codigo de pasaje para asegurarse que todo salio bien. 
		 * Para asegurarnos que todo salio bien, el codigo de pasaje lo vamos a buscar desde el asiento que acabmos de crear.
		 * Entonces, busco el asiento que acabo de añadir al diccionario, busco su codigo de pasaje, y retorno eso.
		 */ 
		return asientos.get(asiento.getCodigo()).getCodPasaje();
	}
	
	public void eliminarAsiento(Integer id)
	{
		asientos.remove(id);
	}
	
	public void eliminarPasaje(int codPasaje) 
	{
		Iterator<Map.Entry<Integer, Asiento>> it = asientos.entrySet().iterator();
		
		//Recorro todos los asientos del pasajero
		while (it.hasNext()) {
			
			Asiento asientoActual = (Asiento) it.next();
			 
			//Cuando encuentro el asiento cuyo codigo coincide con el dado, lo elimino de memoria y que el garbage colector se encargue.
			//Si nunca lo encuentro, entonces ya estaba eliminado.
			if(asientoActual.getCodPasaje() == codPasaje) asientoActual = null;
		}
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