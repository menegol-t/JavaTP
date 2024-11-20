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
	//private int refrigeriosConsumidos;
	//private double costo;
	
	public Pasajero(Cliente cliente) 
	{
		verificarParametros(cliente);
		
		this.cliente = cliente;
		this.asientos = new HashMap<Integer, Asiento>();
		
		//Los podemos omitir, no se necesitan
		//this.refrigeriosConsumidos = 0;
		//this.costo = 0.0;
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
	
	//no se si la usamos, omitir?
	public int getCantAsientos() 
	{
		return asientos.size();
	}
	
	/*public void consumirRefrigerio()  --> se puede omitir
	{
		refrigeriosConsumidos += 1;
	}
	
	public int getRefrigeriosConsumidos() --> se puede omitir
	{
		return refrigeriosConsumidos;
	}
	
	
	
	//Esta no se para que estar, borrar?
	//Pq querriamos alterar el costo si solo al asignar asiento y removerlo el valor se altera
	public double getCosto()
	{
		return costo;
	}
	
	public void setCosto(double costo) --> se puede omitir
	{
		this.costo = costo;
	}
	
	*/
	
	//Funcion que calcula el costo total de todos los asientos
	public double calcularCosto()
	{
		if(asientos.size() == 0) throw new RuntimeException("Pasajero.calcularCosto: No se puede calcular el costo de un avion sin asientos vendidos. ");
		
		double total = 0;
		
		Iterator<Asiento> iterator = asientos.values().iterator();
		
		while(iterator.hasNext())
		{
			Asiento asiento = iterator.next();
			
			total = total + asiento.getPrecio();
			
		}
		
		return total;
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