package aerolinea;

import java.util.HashMap;

public class Internacional extends Vuelo{

	private int refrigeriosPorPasajero;
	private double precioPorRefrigerio;
	private int pasajerosEconomica;
	private int pasajerosTurista;
	private int pasajerosEjecutivo;
	private int limitePasajerosEconomica;
	private int limitePasajerosTurista;
	private int limitePasajerosEjecutivo;
	private boolean vueloDirecto;
	private HashMap<String, Aeropuerto> escalas;
	
	
	public Internacional(String codigo, Aeropuerto origen, Aeropuerto destino, int totalAsientos, int totalTripulantes, String fechaSalida, int porcentajeImpuesto, int cantidadRefrigerios, double precioRefrigerio, HashMap <String, Aeropuerto> escalas)
	{
		//Generamos el vuelo
		super(codigo, origen, destino, totalAsientos, totalTripulantes, fechaSalida, porcentajeImpuesto);
		
		validarParametros(cantidadRefrigerios, precioRefrigerio);
		
		this.refrigeriosPorPasajero = cantidadRefrigerios;
		this.precioPorRefrigerio = precioRefrigerio;
		
		//El numero de pasajeros empieza vacio, se van sumando conforme se suman pasajes.
		this.pasajerosEconomica = 0;
		this.pasajerosTurista = 0;
		this.pasajerosEjecutivo = 0;
		
		//El limite de pasajeros aparentemente no me lo pasan, solo lo sacamos nosotros??????
		this.limitePasajerosEconomica = 0;
		this.limitePasajerosTurista = 0;
		this.limitePasajerosEjecutivo = 0;
		
		this.escalas = escalas;
		
		//Si el diccionario de escalas mide "0", es un vuelo directo, porque no tiene escalas
		this.vueloDirecto = escalas.size() == 0;
		
	}
	
	private void validarParametros(int cantidadRefrigerios, double precioRefrigerio) 
	{
		if(cantidadRefrigerios < 0) throw new RuntimeException("VueloPublico: La cantidad de refrigerios no puede ser negativa.");
		if(precioRefrigerio < 0) throw new RuntimeException("VueloPublico: El precio de los refrigerios no puede ser negativo.");
	}
	
	@Override
	public void registrarAsientosDisponibles(int[]cantAsientos, double[]precios)
	{	
		//Al registrar los asientos, registramos cual es el limite de asientos por clase, algo que solicitaba la etapa de diseño pero en la segunda etapa no se especifica. 
		limitePasajerosEconomica = cantAsientos[0];
		
		limitePasajerosTurista = cantAsientos[1];
		
		limitePasajerosEjecutivo = cantAsientos[2];
		
		//El contador sirve para numerar los asientos y darles un codigo unico en el vuelo
		int contador = 0;
		
		//Se recorre el array de la cantidad de secciones que habra en el vuelo: cantiAsientos: [Economica, Turista, Pimera]  
		for(int i = 0; i < cantAsientos.length; i++)
		{
			
			//En cada posicion del array de secciones, hay un array con los asientos de esa seccion. Aca se recorre ese sub array: cantiAsientos: [ [asiento0, asiento1, asiento2], [asiento3, asiento4, asiento6], [asiento7, asiento8] ]
			for(int j = 0; j<cantAsientos[i]; j++)
			{
				//incrementamos el numero de asiento
				contador ++;

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


	public HashMap<String, Aeropuerto> getEscalas() {
		return escalas;
	}

	public boolean isVueloDirecto() {
		return vueloDirecto;
	}
	
	@Override
	public String toString() {
		return super.getCodigo()+ " - " + super.getOrigen().getNombre() + " - " + super.getDestino().getNombre() + " - " + super.getFechaSalida()+ " - " + "INTERNACIONAL";
	}

	@Override
	public double getPrecio() {
		
		return 0;
	}
}
