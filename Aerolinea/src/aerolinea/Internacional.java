package aerolinea;

import java.util.HashMap;

public class Internacional extends Nacional{

	private int limitePasajerosTurista;
	private int pasajerosTurista;
	private boolean vueloDirecto;
	private HashMap<String, Aeropuerto> escalas;
	
	
	public Internacional(String codigo, Aeropuerto origen, Aeropuerto destino, int totalAsientos, int totalTripulantes, String fechaSalida, int porcentajeImpuesto, int cantidadRefrigerios, double precioRefrigerio, HashMap <String, Aeropuerto> escalas)
	{
		super(codigo, origen, destino, totalAsientos, totalTripulantes, fechaSalida, porcentajeImpuesto, cantidadRefrigerios, precioRefrigerio, "INTERNACIONAL");
		
		//El numero de pasajeros empieza vacio, se van sumando conforme se suman pasajes.
		this.pasajerosTurista = 0;
		
		//El limite de pasajeros aparentemente no me lo pasan, solo lo sacamos nosotros??????
		this.limitePasajerosTurista = 0;
		
		this.vueloDirecto = escalas.size() == 0;
		this.escalas = new HashMap<String, Aeropuerto>();
	}
	
	@Override
	public void registrarAsientosDisponibles(int[]cantAsientos, double[]precios)
	{	
		//Al registrar los asientos, registramos cual es el limite de asientos por clase, algo que solicitaba la etapa de diseño pero en la segunda etapa no se especifica. 
		super.setLimitePasajerosEconomica(cantAsientos[0]);
		
		limitePasajerosTurista = cantAsientos[1];
		
		super.setLimitePasajerosEjecutivo(cantAsientos[2]);
		
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

				//Calculamos el pasaje con impuestos
				double precioFinal = precios[i] + precios[i] * super.getPorcentajeImpuesto()/100;
				
				Asiento asientoNuevo;
				
				if(i == 0) 
				{
					//Se crea un asiento de clase economica
					asientoNuevo = new Asiento(contador, precioFinal, "Economica");
				}
				
				if(i == 1) //Si estamos en la segunda seccion
				{
					//Se crea un asiento de clase turista
				    asientoNuevo = new Asiento(contador, precioFinal, "Turista");
				}
				
				else //Si estamos en la tercera seccion
				{	
					//Se crea un asiento de primera clase 
					asientoNuevo = new Asiento(contador, precioFinal, "Ejecutivo");	
				}
				
				//Agregamos el asiento al diccionario de asientos disponibles. 
				super.registrarAsientoDisponible(asientoNuevo);
			}
		}
	}

	public int getLimitePasajerosTurista() {
		return limitePasajerosTurista;
	}

	public HashMap<String, Aeropuerto> getEscalas() {
		return escalas;
	}

	public boolean isVueloDirecto() {
		return vueloDirecto;
	}
	
	
}
