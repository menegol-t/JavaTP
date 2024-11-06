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
				
				if(i == 1)
				{
					contador += 1;
					Asiento asiento = new Asiento(contador, 2, precios[i], "Turista", false);
					nuevosAsientos.put(asiento.getCodigo(), asiento);
				}
				
				
				if(i == 2)
				{
					contador += 1;
					Asiento asiento = new Asiento(contador, 3, precios[i], "Primera Clase", false);
					nuevosAsientos.put(asiento.getCodigo(), asiento);
				}
			}
			
			asientosDisponiblesPorVuelo.put(nacional.getCodigo(), nuevosAsientos);
		}
		
		return asientosDisponiblesPorVuelo;
	}
	
}
