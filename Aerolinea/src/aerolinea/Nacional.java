package aerolinea;

import java.util.HashMap;

public class Nacional extends Vuelo {
	
	int refrigeriosPorPasajero;
	int limitePasajerosPrimera;
	int pasajerosPrimera;
	int limitePasajerosSegunda;
	int pasajerosSegunda;
	
	//Dado a que nos lo pasan por parametro, se almacena para facturacion
	double precioPorRefrigerio;
	double precioAsientosPrimera; 
	double precioAsientosSegunda;

	public Nacional(String codigo, Aeropuerto origen, Aeropuerto destino, int totalAsientos, int totalTripulantes, String fechaSalida, int limitePasPrimera, int limitePasSegunda, double precioRefrigerio, double precioAsientosPrimera, double precioAsientosSegunda) 
	{	
		//Se crea la clase padre vuelo, por lo que su irep se mantiene. Se le pasa "porcentajeImpuesto" como 20
		super(codigo, destino, origen, totalAsientos, totalTripulantes, fechaSalida, 20);
		
		if(!(limitePasPrimera > 0 && limitePasSegunda > 0 && limitePasSegunda > 0))
			throw new RuntimeException("Valor de parametros invalido!!");
		
		refrigeriosPorPasajero = 1;
		limitePasajerosPrimera = limitePasPrimera;
		limitePasajerosSegunda = pasajerosSegunda;
		precioPorRefrigerio = precioRefrigerio;
		
		//De momento no tiene pasajeros, la logica de aumentar este numero es de venderPasaje, al igual que incrementar agregar el pasajero a la lista
		pasajerosPrimera = 0;
		pasajerosSegunda = 0;
		
	}
	
	/*
	 * Registrar asientos no deberia registrar COMO MAXIMO el limitePasajerosPrimera y limitePasajerosEconomica?????
	 * */
	@Override
	public void registrarAsientosDisponibles(int[]cantAsientos, double[]precios)
	{
		//Se recorre el array de la cantidad de secciones que habra en el vuelo: cantiAsientos: [Economica, Pimera]  
		for(int i = 0; i < cantAsientos.length; i++)
		{
			//El contador sirve para numerar los asientos y darles un codigo unico en el vuelo
			int contador = 0;
			
			//En cada posicion del array de secciones, hay un array con los asientos de esa seccion. Aca se recorre ese sub array: cantiAsientos: [ [asiento0, asiento1, asiento2], [asiento3, asiento4, asiento6] ]
			for(int j = 0; j<cantAsientos[i]; j++)
			{
				//incrementamos el numero de asiento
				contador += 1;
				
				Asiento asientoNuevo;
				
				if(i == 0) //Si estamos en la primera seccion
				{
					//Se crea un asiento de clase economica
					asientoNuevo = new Asiento(contador, 1, precios[i], "Economica");
				}
				
				else //Si estamos en la segunda seccion
				{
					//Se crea un asiento de primera clase
					asientoNuevo = new Asiento(contador, 2, precios[i], "Primera Clase");
				}
				
				super.registrarAsientoDisponible(asientoNuevo);
			}

		}
		
	}

	
}
