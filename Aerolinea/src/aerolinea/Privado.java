package aerolinea;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class Privado extends Vuelo{

	
	private Cliente comprador;
	private int asientosPorJet;
	private double precioPorJet;
	private int cantidadDeJets;

	//String VenderVueloPrivado(String origen, String destino, String fecha, int tripulantes, double precio,  int dniComprador, int[] acompaniantes)
	
	public Privado(String codigo, Aeropuerto destino, Aeropuerto origen, int totalAsientos, int totalTripulantes, String fechaSalida, int porcentajeImpuesto,
				   Cliente comprador, double precioPorJet, int cantidadDeJets)
	{
		
		super(codigo, destino, origen, totalAsientos, totalTripulantes, fechaSalida, 30);
		
		this.comprador = comprador;
		this.asientosPorJet = 15;
		this.precioPorJet = precioPorJet;
		
		this.cantidadDeJets = cantidadDeJets;
		
	}
	
	public void registrarAsientosDisponibles(int[] acompaniantes)
	{
		int contador =0;
		
		for(int i = 0; i<acompaniantes.length + 1; i++)
		{
			contador += 1;
			
			Asiento nuevoAsiento = new Asiento(contador, 0, "Privado");
			
			super.registrarAsientoDisponible(nuevoAsiento);
		}
	}
	
	
	public void registrarPasajeros(Cliente[] acompaniantes, Cliente pasajeroComprador, HashMap<Integer, Asiento>asientosDisponibles)
	{
		int contador = 0;
		
		//Generamos un iterador sobre todos los asientosDisponibles
		Iterator<Map.Entry<Integer, Asiento>> iterador = asientosDisponibles.entrySet().iterator();
				
		while (iterador.hasNext()) {
		Asiento asientoActual = (Asiento) iterador.next();
		
		//Si el contador es mayor al largo del array de acompañantes, entonces agregamos al comprador
		if(contador > acompaniantes.length) 
		{
			super.registrarAsiento(pasajeroComprador, asientoActual);
			contador ++;
		}
		
		else {
			super.registrarAsiento(acompaniantes[contador], asientoActual);
			contador ++;
		}
		
		
		}
		
	}
	
	
	public double conseguiPresioFinal()
	{
		
		//Calculamos el precio final + impuestos
		
		double precioFinal = precioPorJet * cantidadDeJets;
		
		return precioFinal += precioFinal * super.getPorcentajeImpuesto() / 100;
		
	}
	
}
