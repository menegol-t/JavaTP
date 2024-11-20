package aerolinea;

import java.util.HashMap;
import java.util.Iterator;

public class Nacional extends Vuelo {
	
	private int refrigeriosPorPasajero;
	private double precioPorRefrigerio;
	private int pasajerosEconomica;
	private int pasajerosEjecutivo;
	private int limitePasajerosEconomica;
	private int limitePasajerosEjecutivo;
	
	
	public Nacional(String codigo, Aeropuerto origen, Aeropuerto destino, int totalAsientos, int totalTripulantes, String fechaSalida, double precioPorRefrigerio) 
	{			
		//Se crea la clase padre vuelo, seteamos impuesto como 20
		super(codigo, origen, destino, totalAsientos, totalTripulantes, fechaSalida, 20);
		
		//Establecemos un solo refrigerio por pasajero y el precio del mismo
		this.refrigeriosPorPasajero = 1;
		this.precioPorRefrigerio = precioPorRefrigerio;
		
		validarParametros(destino, precioPorRefrigerio);
		
		//El numero de pasajeros empieza vacio, se van sumando conforme se suman pasajes.
		this.pasajerosEconomica = 0;
		this.pasajerosEjecutivo = 0;
		
		//Esto se puede solucionar, give me time bitch - leo
		this.limitePasajerosEconomica = 0;
		this.limitePasajerosEjecutivo = 0;
		
		
	}
	
	//Agregue las validaciones de refrigerios
	
	private void validarParametros(Aeropuerto destino, double precioPorRefrigerio2) 
	{
		if(precioPorRefrigerio2 < 0) throw new RuntimeException("VueloPublico: El precio de los refrigerios no puede ser negativo.");
		if(!destino.esNacional()) throw new RuntimeException("VueloNacional: Los vuelos nacionales solo pueden ir a destinos nacionales.");
	}
	
	/*
	 * Registrar asientos no deberia registrar COMO MAXIMO el limitePasajerosPrimera y limitePasajerosEconomica?????
	 * */
	@Override
	public void registrarAsientosDisponibles(int[]cantAsientos, double[]precios)
	{
		
		//El contador sirve para numerar los asientos y darles un codigo unico en el vuelo
		int contador = 0;
		
		//Se recorre el array de la cantidad de secciones que habra en el vuelo: cantiAsientos: [Economica, Pimera]  
		for(int i = 0; i < cantAsientos.length; i++)
		{	
			//En cada posicion del array de secciones, hay un array con los asientos de esa seccion. Aca se recorre ese sub array: cantiAsientos: [ [asiento0, asiento1, asiento2], [asiento3, asiento4, asiento6] ]
			for(int j = 0; j<cantAsientos[i]; j++)
			{
				//incrementamos el numero de asiento
				contador++;
				
				//Calculamos el pasaje con impuestos
				double precioFinal = precios[i] + precios[i] * super.getPorcentajeImpuesto()/100;
				
				Asiento asientoNuevo;
				
				if(i == 0) //Si estamos en la primera seccion
				{
					//Se crea un asiento de clase economica
					asientoNuevo = new Asiento(contador, precioFinal, "Economica");
				}
				
				else //Si estamos en la segunda seccion
				{
					//Se crea un asiento de primera clase
					asientoNuevo = new Asiento(contador, precioFinal, "Ejecutivo");
				}
				
				super.registrarAsientoDisponible(asientoNuevo);
			}
		}
	}
	

	@Override
	public String toString() 
	{
		return super.getCodigo()+ " - " + super.getOrigen().getNombre() + " - " + super.getDestino().getNombre() + " - " + super.getFechaSalida()+ " - " + "NACIONAL";
	}

	@Override
	public double getPrecio() 

	/* Retorna la recaudacion total del vuelo
	 * 
	 * 1) recorremos pasajero a pasajero
	 * 2) sumar el precio de todos sus asientos + el precio de los refrigerios
	 * 3) sumar el procentaje de impuesto
	 * 4) retornar el total
	 * 
	 */
	
	
	{
		
		//Obtenemos los pasajeros
		HashMap<Integer, Pasajero> pasajeros = super.getPasajeros();
		
		//no hay pasajeros, no se recaudo nada
		if(pasajeros.size() == 0) return 0;
		
		double retorno = 0;
		
		//Calculamos el total por los refrigerios por pasajero
		double refrigerios = precioPorRefrigerio * refrigeriosPorPasajero;
		
		//Creamos el iterador, este es por valores, es decir por pasajeros
		Iterator<Pasajero> iterator = pasajeros.values().iterator();
		
		//Recorremos los pasajeros
		while(iterator.hasNext())
		{
			Pasajero pasajero = iterator.next();
			
			//Sumamos el costo total de los pasajes + impuestos
			retorno += pasajero.calcularCosto() /*+ refrigerios*/;
			
		}
	 
		
		//agregamos el porcentaje de impuesto
		retorno += (retorno * (super.getPorcentajeImpuesto() / 100));
		
		//retornamos
		return retorno;
		
	}
	
	
}
