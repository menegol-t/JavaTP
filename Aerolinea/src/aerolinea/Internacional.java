package aerolinea;

import java.util.HashMap;
import java.util.LinkedList;

public class Internacional extends Nacional{

	private int limitePasajerosTercera;
	private double precioAsientosTercera;
	private int pasajerosTercera;
	private boolean vueloDirecto;
	private LinkedList<Aeropuerto> escalas;
	
	
	public Internacional(String codigo, Aeropuerto destino, Aeropuerto origen, int totalAsientos, int totalTripulantes, String fechaSalida, int porcentajeImpuesto, 
			int limitePasPrimera, int limitePasSegunda, int limitePasTercera, double precioRefrigerio, double precioAsientosPrimera, double precioAsientosSegunda, 
			double precioAsientosTercera, LinkedList<Aeropuerto> escalas, boolean vueloDirecto)
	{
		
		super(codigo, destino, origen, totalAsientos, totalTripulantes, fechaSalida, porcentajeImpuesto, limitePasPrimera, limitePasSegunda, precioRefrigerio, precioAsientosPrimera, precioAsientosSegunda);
		
		
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
	public void registrarAsientosDisponibles(int[]cantAsientos, double[]precios)
	{	
		//Se recorre el array de la cantidad de secciones que habra en el vuelo: cantiAsientos: [Economica, Turista, Pimera]  
		for(int i = 0; i < cantAsientos.length; i++)
		{
			//El contador sirve para numerar los asientos y darles un codigo unico en el vuelo
			int contador = 0;
			
			//En cada posicion del array de secciones, hay un array con los asientos de esa seccion. Aca se recorre ese sub array: cantiAsientos: [ [asiento0, asiento1, asiento2], [asiento3, asiento4, asiento6], [asiento7, asiento8] ]
			for(int j = 0; j<cantAsientos[i]; j++)
			{
				//incrementamos el numero de asiento
				contador += 1;
				
				Asiento asientoNuevo;
				
				if(i == 0) 
				{
					//Se crea un asiento de clase economica
					asientoNuevo = new Asiento(contador, 1, precios[i], "Economica");
				}
				
				if(i == 1) //Si estamos en la segunda seccion
				{
					//Se crea un asiento de clase turista
				    asientoNuevo = new Asiento(contador, 2, precios[i], "Turista");
				}
				
				else //Si estamos en la tercera seccion
				{	
					//Se crea un asiento de primera clase 
					asientoNuevo = new Asiento(contador, 3, precios[i], "Primera Clase");	
				}
				
				//Agregamos el asiento al diccionario de asientos disponibles. 
				super.registrarAsientoDisponible(asientoNuevo);
			}

		}

	}
	
}
