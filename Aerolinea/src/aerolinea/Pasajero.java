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
		Asiento asiento = asientos.get(codigoAsiento);
		
		if(asiento == null) throw new RuntimeException("El numero de asiento provisto no corresponde a este pasajero :" + cliente.getDni());
		
		return asiento;
	}
	
	public ArrayList<Asiento> getAsientos()
	{
		ArrayList<Asiento> asiento = new ArrayList<>(asientos.values());
		
		return asiento;
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
	
	public int asignarAsiento(Asiento asiento) 
	{	
		//Guardo el asiento en el diccionario de asientos del pasajero.
		asientos.put(asiento.getCodigo(), asiento);
		
		/*
		 * Esto debe retornar el codigo de pasaje para asegurarse que todo salio bien. 
		 * Para asegurarnos que todo salio bien, el codigo de pasaje lo vamos a buscar desde el asiento que acabmos de crear.
		 * Entonces, busco el asiento que acabo de añadir al diccionario, busco su codigo de pasaje, y retorno eso.
		 */ 
		
		Integer codigoPasaje = asientos.get(asiento.getCodigo()).getCodPasaje();

		if(codigoPasaje == null) throw new RuntimeException("Pasajero.asignarAsiento: Hello darkness my old friend...");
		
		return codigoPasaje;
		
//		return asiento.getCodigo();
	}
	
	public Asiento removerAsiento(Integer nroAsiento)
	{
		Asiento asiento = getAsiento(nroAsiento);
		
		asiento.setOcupado(false);
		
		asiento.setCodPasaje(0);
		
		asientos.remove(asiento.getCodigo());
		
		return asiento;
	}
	
	public void eliminarPasaje(int codPasaje) 
	{
		//Voy a recorrer todos los asientos del pasajero hasta obtener el que tiene el numero de psasje correcto.
		Iterator<Map.Entry<Integer, Asiento>> it = asientos.entrySet().iterator();
		
		//Recorro todos los asientos del pasajero
		while (it.hasNext()) {
			
			Asiento asientoActual = (Asiento) it.next();
			 
			/* Cuando encuentro el asiento cuyo codigo coincide con el dado, lo elimino de memoria y que el garbage colector se encargue. 
			 * Si nunca lo encuentro, entonces ya estaba eliminado.
			 * */
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