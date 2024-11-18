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
	
	public Privado(String codigo, Aeropuerto origen, Aeropuerto destino, int totalAsientos, int totalTripulantes,  String fechaSalida, double precioPorJet, int porcentajeImpuesto, Cliente comprador)
	{
		super(codigo, origen, destino, totalAsientos, totalTripulantes, fechaSalida, 30, "PRIVADO" + " ( )");
		
		this.comprador = comprador;
		this.asientosPorJet = 15;
		this.precioPorJet = precioPorJet;
//		this.cantidadDeJets = cantidadDeJets; Hay que calcular
		
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
		
		//Itero sobre todos los asientos disponibles
		for(Asiento asientoActual: asientosDisponibles) {
			
			//Tengo que asignarle asientos tanto al comprador como a los acompañantes.
			//Si ya registre todos los acompañantes, registro el asiento del pasajeroComprador
			if(contador > acompaniantes.size()) 
			{
				super.registrarAsiento(pasajeroComprador, asientoActual);
				
				contador ++;
				
			//Si no, es porque todavia tengo que registrar a los acompañantes
			}else {
				super.registrarAsiento(acompaniantes.get(contador), asientoActual);
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
