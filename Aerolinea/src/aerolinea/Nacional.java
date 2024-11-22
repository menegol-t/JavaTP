package aerolinea;

import java.util.HashMap;
import java.util.Iterator;

public class Nacional extends Vuelo {
	
	private int refrigeriosPorPasajero;
	private double precioPorRefrigerio;
	
	public Nacional(String codigo, Aeropuerto origen, Aeropuerto destino, int totalAsientos, int totalTripulantes, String fechaSalida, double precioPorRefrigerio) 
	{			
		//Se crea la clase padre vuelo, seteamos impuesto como 20
		super(codigo, origen, destino, totalAsientos, totalTripulantes, fechaSalida, 20);
		
		validarParametros(destino, precioPorRefrigerio);
		
		//Establecemos un solo refrigerio por pasajero y el precio del mismo
		this.refrigeriosPorPasajero = 1;
		this.precioPorRefrigerio = precioPorRefrigerio;
	}
	
	
	
	private void validarParametros(Aeropuerto destino, double precioPorRefrigerio) 
	{
		if(precioPorRefrigerio < 0) throw new RuntimeException("VueloPublico: El precio de los refrigerios no puede ser negativo.");
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
				
				double precio = precios[i];
				
				Asiento asientoNuevo;
				
				if(i == 0) //Si estamos en la primera seccion
				{
					//Se crea un asiento de clase economica
					asientoNuevo = new Asiento(contador, precio, "Economica");
				}
				
				else //Si estamos en la segunda seccion
				{
					//Se crea un asiento de primera clase
					asientoNuevo = new Asiento(contador, precio, "Ejecutivo");
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

	
	/*
	 * El precio se obtiene de la cantidad de refrigerios que consumieron los pasajeros + la sumatoria de todos los 
	 * precios de sus asientos. A eso se le suma el 20%
	 * */
	@Override
	public double getPrecio() 
	{
		//El precio base es cantidadDePasajeros * refrigeriosPorPasajero * precioPorRefrigerio
		double precio =  (super.getAllPasajeros().size() * refrigeriosPorPasajero) * precioPorRefrigerio;
		
		//Recorro todos los pasajeros y voy sumando el precio de su costo total (la sumatoria de todos los asientos que tienen)
		for(Pasajero pasajeroaActual: super.getAllPasajeros()) 
		{
			precio = precio + pasajeroaActual.getCosto();
		}
		
		//Al precio final le sumo el 20%. O sea "precio * 1.20"
		return precio * (1 + (super.getPorcentajeImpuesto()/100));
	}
}
