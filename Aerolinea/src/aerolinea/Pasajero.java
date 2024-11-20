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
	
	public double eliminarPasaje(int codPasaje) 
	{
		for(Asiento asientoActual: asientos.values()) {
			 
			/* Cuando encuentro el asiento cuyo codigo coincide con el dado, lo elimino de memoria y que el garbage colector se encargue. 
			 * Si nunca lo encuentro, entonces ya estaba eliminado.
			 * */
			if(asientoActual.getCodPasaje() == codPasaje) {
				asientoActual = null;
				return asientoActual.getPrecio();
			}
		}
		
		//Si nunca encontre el asiento, great ya estaba eliminado, retorno que el precio de ese asiento a cancelar es 0.
		return 0.0;
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