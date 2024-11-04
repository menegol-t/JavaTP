package aerolinea;

import java.util.HashMap;

public class Nacional extends Vuelo {
	
	int refrigeriosPorPasajero;
	int limitePasajerosPrimera;
	int pasajerosPrimera;
	int limitePasajerosSegunda;
	int pasajerosSegunda;
	
	//Dado a que nos lo pasan por parametro, se almacena para facturacion
	int precioPorRefrigerio;
	double precioAsientosPrimera; 
	double precioAsientosSegunda;

	public Nacional(String codigo, Aeropuerto destino, Aeropuerto origen, int totalAsientos, int totalTripulantes, HashMap <Integer, Pasajero> pasajeros,
					String fechaSalida, String fechaLlegada, int cantidadSecciones, int porcentajeImpuesto, HashMap<Integer, Cliente> ClientesRegistrados, 
					int limitePasPrimera, int limitePasaSegunda, int precioRefrigerio) 
	{	
		//Se crea la clase padre vuelo, por lo que su irep se mantiene
		super(codigo, destino, origen, totalAsientos, totalTripulantes, pasajeros, fechaSalida, fechaLlegada,
				cantidadSecciones, porcentajeImpuesto, ClientesRegistrados);
		
		if(!(limitePasPrimera > 0 && limitePasaSegunda > 0 && limitePasaSegunda > 0 && pasajeros.size() <= totalAsientos &&
				limitePasPrimera + limitePasaSegunda == totalAsientos)) throw new RuntimeException("Valor de parametros invalido!!");
		
		refrigeriosPorPasajero = 1;
		limitePasajerosPrimera = limitePasPrimera;
		limitePasajerosSegunda = pasajerosSegunda;
		precioPorRefrigerio = precioRefrigerio;
		
		//De momento no tiene pasajeros, la logica de aumentar este numero es de venderPasaje, al igual que incrementar agregar el pasajero a la lista
		pasajerosPrimera = 0;
		pasajerosSegunda = 0;
		
		
		
	}
	
	
	

	
}
