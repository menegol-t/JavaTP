package aerolinea;

import java.util.HashMap;
import java.util.LinkedList;

public class Internacional extends Nacional{

	private int limitePasajerosTercera;
	private double precioAsientosTercera;
	private int pasajerosTercera;
	private boolean vueloDirecto;
	private LinkedList<Aeropuerto> escalas;
	
	
	public Internacional(String codigo, Aeropuerto destino, Aeropuerto origen, int totalAsientos, int totalTripulantes, 
			HashMap <Integer, Pasajero> pasajeros,String fechaSalida, int porcentajeImpuesto, HashMap<Integer, Cliente> ClientesRegistrados, 
			int limitePasPrimera, int limitePasSegunda, int limitePasTercera, double precioRefrigerio, double precioAsientosPrimera, 
			double precioAsientosSegunda, double precioAsientosTercera, LinkedList<Aeropuerto> escalas, boolean vueloDirecto)
	{
		
		super(codigo, destino, origen, totalAsientos, totalTripulantes, pasajeros, fechaSalida, porcentajeImpuesto, ClientesRegistrados, 
			  limitePasPrimera, limitePasSegunda, precioRefrigerio, precioAsientosPrimera, precioAsientosSegunda);
		
		
		if(!(limitePasTercera > 0 && limitePasTercera < totalAsientos && precioAsientosTercera > 0)
		  && limitePasTercera + limitePasPrimera + limitePasSegunda == totalAsientos ) throw new RuntimeException("Valor de parametros invalido!!");
		
		
		this.limitePasajerosTercera = limitePasTercera;
		this.precioAsientosTercera = precioAsientosTercera;
		this.escalas = escalas;
		this.vueloDirecto = vueloDirecto;
		
		//Misma logica que en Nacional
		this.pasajerosTercera = 0;
		
	}
	
	@Override
	public HashMap<String, HashMap<Integer, Asiento>> registrarAsientosDeVuelos(int[]cantAsientos, double[]precios, Nacional nacional, HashMap<String, HashMap<Integer, Asiento>> asientosDisponiblesPorVuelo)
	{
		//Se recorre la cantidad de elementos en el array cantAsientos, que evidencia la cantidad de secciones
		for(int i = 0; i < cantAsientos.length; i++)
		{
			HashMap<Integer, Asiento> nuevosAsientos = new HashMap<>();
			
			//El contador sirve para numerar los asientos y darles un codigo unico en el vuelo
			int contador = 0;
			for(int j = 0; j<cantAsientos[i]; j++)
			{
				if(i == 0) //Si estamos en la primera seccion
				{
					contador += 1;
					//Se crea un asiento de clase economica
					Asiento asiento = new Asiento(contador, 1, precios[i], "Economica", false);
					//agregamos al retorno
					nuevosAsientos.put(asiento.getCodigo(), asiento);
				}
				
				if(i == 1) //Si estamos en la segunda seccion
				{
					contador += 1;
					//Se crea un asiento de clase turista
					Asiento asiento = new Asiento(contador, 2, precios[i], "Turista", false);
					//agregamos al retorno
					nuevosAsientos.put(asiento.getCodigo(), asiento);
				}
				
				
				if(i == 2) //Si estamos en la tercera seccion
				{
					contador += 1;
					//Se crea un asiento de primera clase 
					Asiento asiento = new Asiento(contador, 3, precios[i], "Primera Clase", false);
					//agregamos al retorno
					nuevosAsientos.put(asiento.getCodigo(), asiento);
				}
			}
			
			//Agregamos los asientos al diccionario de asientos disponibles
			asientosDisponiblesPorVuelo.put(nacional.getCodigo(), nuevosAsientos);
		}
		
		//Retornamos
		return asientosDisponiblesPorVuelo;
	}
	
}
