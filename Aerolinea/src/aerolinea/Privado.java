package aerolinea;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class Privado extends Vuelo{
	
	private Cliente comprador;
	private int asientosPorJet;
	private double precioPorJet;
	private int cantidadDeJets;
	
	public Privado(String codigo, Aeropuerto origen, Aeropuerto destino, int totalAsientos, int totalTripulantes,  String fechaSalida, int porcentajeImpuesto, double precioPorJet, Cliente comprador)
	{
		super(codigo, origen, destino, totalAsientos, totalTripulantes, fechaSalida, 30, "PRIVADO" + " ( )");
		
		validarParametros(precioPorJet, comprador);
		
		this.comprador = comprador;
		this.asientosPorJet = 15;
		this.precioPorJet = precioPorJet;
		this.cantidadDeJets = calcularCantJets(totalAsientos);
	}
	
	private void validarParametros(double precioPorJet, Cliente comprador) 
	{
		if(precioPorJet < 0) throw new RuntimeException("Privado: El precio por jet no puede ser negativo");
		if(comprador == null) throw new RuntimeException("Privado: Se debe bridnar un comprador");
	}
	
	/*
	 * Dado un numero de asientos, sabiendo que cada jet lleva maximo 15, devuelve un int como
	 * la cantidad de jets necesarios. 
	 * */
	private int calcularCantJets(int totalAsientos) 
	{
		//Divido los asientos necesarios por los que lleva un jet, y redondeo el resultado al int mayor mas cercano
		return Math.round(totalAsientos / 15);
	}
	
	public void registrarAsientosDisponibles(int[] acompaniantes)
	{
		int nroAsiento = 0;
		
		for(int i = 0; i<acompaniantes.length + 1; i++)
		{
			nroAsiento += 1;
			
			//Generamos un asiento privado por acompaniante + comprador. Estos asientos no tiene precio, porque este es a nivel vuelo.
			Asiento nuevoAsiento = new Asiento(nroAsiento, 0, "Privado");
			
			//Va a haber 1 asiento por ocupante
			nuevoAsiento.setOcupado(true);
			
			//Añadimos los asientos a los disponibles
			super.registrarAsientoDisponible(nuevoAsiento);
		}
	}

	
	public void registrarPasajeros(ArrayList<Cliente> acompaniantes, Cliente pasajeroComprador)
	{
		ArrayList<Asiento> asientosDisponibles = super.getAsientosDisponibles();
		
		int contador = 0;
		
		//Primero registro al comprador como pasajero. Esto tambien va a remover el asiento disponible.
		super.registrarAsiento(pasajeroComprador, asientosDisponibles.get(contador));
		
		asientosDisponibles = super.getAsientosDisponibles();
		
		//Itero sobre todos los asientos disponibles
		for(Asiento asientoActual: asientosDisponibles) {
			
			super.registrarAsiento(acompaniantes.get(contador), asientoActual);
				
			contador ++;
		}
	}
	
	
	public double conseguiPresioFinal()
	{
		//Calculamos el precio final + impuestos
		double precioFinal = precioPorJet * cantidadDeJets;
		
		return precioFinal += (precioFinal * (super.getPorcentajeImpuesto() / 100));
		
	}
	
}
