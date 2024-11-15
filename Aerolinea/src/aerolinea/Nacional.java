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

	public Nacional(String codigo, Aeropuerto destino, Aeropuerto origen, int totalAsientos, int totalTripulantes, HashMap <Integer, Pasajero> pasajeros,
					String fechaSalida, int porcentajeImpuesto, HashMap<Integer, Cliente> ClientesRegistrados, 
					int limitePasPrimera, int limitePasSegunda, double precioRefrigerio, double precioAsientosPrimera, double precioAsientosSegunda) 
	{	
		//Se crea la clase padre vuelo, por lo que su irep se mantiene
		super(codigo, destino, origen, totalAsientos, totalTripulantes, pasajeros, fechaSalida, porcentajeImpuesto, ClientesRegistrados);
		
		if(!(limitePasPrimera > 0 && limitePasSegunda > 0 && limitePasSegunda > 0 && pasajeros.size() <= totalAsientos))
			throw new RuntimeException("Valor de parametros invalido!!");
		
		refrigeriosPorPasajero = 1;
		limitePasajerosPrimera = limitePasPrimera;
		limitePasajerosSegunda = pasajerosSegunda;
		precioPorRefrigerio = precioRefrigerio;
		
		//De momento no tiene pasajeros, la logica de aumentar este numero es de venderPasaje, al igual que incrementar agregar el pasajero a la lista
		pasajerosPrimera = 0;
		pasajerosSegunda = 0;
		
	}
	
	
	@Override
	public void registrarAsientosDeVuelos(int[]cantAsientos, double[]precios, Vuelo nacional)
	{
		
		//Accedemos al diccionario de asientos del vuelo
		HashMap<Integer, Asiento> asientosDisponibles = nacional.getAsientosDisponibles();
		
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
				
				else //Si estamos en la segunda seccion
				{
					//Se crea un asiento de primera clase
					asientoNuevo = new Asiento(contador, 2, precios[i], "Primera Clase", false);
	
				}
				
				asientosDisponibles.put(asientoNuevo.getCodigo(), asientoNuevo);
			}

		}
		
	}

	
}
