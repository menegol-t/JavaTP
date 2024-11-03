package aerolinea;

import java.util.HashMap;
import java.util.LinkedList;

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
	
	public void eliminarASiento(Integer id)
	{
		asientos.remove(id);
	}
	
	//Auxiliar
	
	public Cliente consultarCliente()
	{
		return cliente;
	}
	
	
}
