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
	public void registrarAsientosDeVuelos(int[]cantAsientos, double[]precios, Vuelo internacional)
	{
		//Accedemos al diccionario de asientos del vuelo
		HashMap<Integer, Asiento> asientosDisponibles = internacional.getAsientosDisponibles();
		
		//Se recorre la cantidad de elementos en el array cantAsientos, que evidencia la cantidad de secciones
		for(int i = 0; i < cantAsientos.length; i++)
		{
		
			//El contador sirve para numerar los asientos y darles un codigo unico en el vuelo
			int contador = 0;
			for(int j = 0; j<cantAsientos[i]; j++)
			{
				//incrementamos el numero de asiento
				contador += 1;
				
				Asiento asientoNuevo;
				
				if(i == 0) //Si estamos en la primera seccion
				{
					
					//Se crea un asiento de clase economica
					asientoNuevo = new Asiento(contador, 1, precios[i], "Economica", false);

				}
				
				if(i == 1) //Si estamos en la segunda seccion
				{
					
					//Se crea un asiento de clase turista
				    asientoNuevo = new Asiento(contador, 2, precios[i], "Turista", false);

				}
				
				
				else //Si estamos en la tercera seccion
				{
					
					//Se crea un asiento de primera clase 
					asientoNuevo = new Asiento(contador, 3, precios[i], "Primera Clase", false);
					
				}
				
				//agregamos al retorno
				asientosDisponibles.put(asientoNuevo.getCodigo(), asientoNuevo);
			}

		}

	}
	
}
