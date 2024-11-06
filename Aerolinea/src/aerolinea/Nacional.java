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
	public HashMap<String, HashMap<Integer, Asiento>> registrarAsientosDeVuelos(int[]cantAsientos, double[]precios, Nacional nacional, HashMap<String, HashMap<Integer, Asiento>> asientosDisponiblesPorVuelo)
	{
		for(int i = 0; i < cantAsientos.length; i++)
		{
			HashMap<Integer, Asiento> nuevosAsientos = new HashMap<>();
			int contador = 0;
			for(int j = 0; j<cantAsientos[i]; j++)
			{
				if(i == 0)
				{
					contador += 1;
					Asiento asiento = new Asiento(contador, 1, precios[i], "Economica", false);
					nuevosAsientos.put(asiento.getCodigo(), asiento);
				}
				
				else
				{
					contador += 1;
					Asiento asiento = new Asiento(contador, 2, precios[i], "Primera Clase", false);
					nuevosAsientos.put(asiento.getCodigo(), asiento);
				}
			}
			
			asientosDisponiblesPorVuelo.put(nacional.getCodigo(), nuevosAsientos);

		}
		return asientosDisponiblesPorVuelo;
	}

	
}
