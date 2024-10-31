package aerolinea;

import java.util.LinkedList;

public class Pasajero 
{
	LinkedList<Asiento> asientos;
	int refrigeriosConsumidos;
	double costo;
	Cliente cliente;
	
	public Pasajero(LinkedList<Asiento> asientos, int refrigeriosConsumidos, double costo, Cliente cliente) throws Exception
	{
		if(!(costo>0 && refrigeriosConsumidos >= 0 && asientos.size() > 0 )) throw new Exception("Valor de parametros invalido!!");
		
		this.asientos =asientos;
		this.refrigeriosConsumidos = refrigeriosConsumidos;
		this.costo = costo;
		this.cliente = cliente;
	}
	
	private LinkedList<Asiento> consultarAsientos()
	{
		return asientos;
	}
	
	private void consumirRefrigerio()
	{
		refrigeriosConsumidos += 1;
	}
	
	private int consultarRefrigeriosConsumidos()
	{
		return refrigeriosConsumidos;
	}
	
	private void registrarCosto(double costo)
	{
		this.costo = costo;
	}
	
	private double consultarCosto()
	{
		return costo;
	}
	
	private void asignarAsiento(Asiento asiento) 
	{	
		asientos.add(asiento);
	}
	
	//Auxiliar
	
	public Cliente consultarCliente()
	{
		return cliente;
	}
}
