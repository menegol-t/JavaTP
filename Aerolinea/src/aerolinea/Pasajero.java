package aerolinea;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;

public class Pasajero 
{
	HashMap<Integer,Asiento> asientos;
	int refrigeriosConsumidos;
	double costo;
	Cliente cliente;
	
	public Pasajero(LinkedList<Asiento> asientos, int refrigeriosConsumidos, double costo, Cliente cliente) 
	{
		if(!(costo>0 && refrigeriosConsumidos >= 0)) throw new RuntimeException("Valor de parametros invalido!!");
		
		this.asientos = new HashMap<Integer,Asiento>();
		this.refrigeriosConsumidos = refrigeriosConsumidos;
		this.costo = costo;
		this.cliente = cliente;
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
	
	public void asignarAsiento(Asiento asiento) 
	{	
		asientos.put(asiento.getCodigo(), asiento);
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
	
	public Cliente consultarCliente()
	{
		return cliente;
	}
	
	
}
