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
		super(codigo, origen, destino, totalAsientos, totalTripulantes, fechaSalida, 30);
		
		validarParametros(destino, precioPorJet, comprador);
		
		this.comprador = comprador;
		this.asientosPorJet = 15;
		this.precioPorJet = precioPorJet;
		this.cantidadDeJets = calcularCantJets(totalAsientos);
		
	}
	
	private void validarParametros(Aeropuerto destino, double precioPorJet, Cliente comprador) 
	{
		if(!destino.esNacional()) throw new RuntimeException("Privado: El vuelo solo puede llegar a destinos nacionales");
		if(precioPorJet < 0) throw new RuntimeException("Privado: El precio por jet no puede ser negativo");
		if(comprador == null) throw new RuntimeException("Privado: Se debe bridnar un comprador");
	}
	
	/*
	 * Dado un numero de asientos, sabiendo que cada jet lleva maximo 15, devuelve un int como
	 * la cantidad de jets necesarios. 
	 * */
	private int calcularCantJets(int totalAsientos) 
	{
		/*
		 * Aca tengo un problema: Ponele que divido 2 ints, total asientos = 1 y hago totalAsientos /15, eso me da 0,0067. Yo quiero redondear ese numero con
		 * coma a 1 en int. Como totalAsientos es un int, y necesito que me de el numero con coma para redondearlo, lo casteo como double.
		 * Entonces, con math.ceil, el double 0,0067 lo puedo redondear a 1.0, genial. Problema: Tengo que retornar un int no un 1,0 entonces 
		 * casteo tambien el resultado de la operacion como un int.
		 * */
		
		double asientosRequeridos = totalAsientos;
		
		int jetsRequeridos = (int) Math.ceil(asientosRequeridos / 15);
		
		return jetsRequeridos;
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
	

	@Override
	public String toString() {
		return super.getCodigo()+ " - " + super.getOrigen().getNombre() + " - " + super.getDestino().getNombre() + " - " + super.getFechaSalida()+ " - " + "PRIVADO (" + this.cantidadDeJets + ")";
	}


	@Override
	public double getPrecio() {
		//Calculamos el precio final + impuestos
		double precioFinal = precioPorJet * cantidadDeJets;
				
		return precioFinal += (precioFinal * (super.getPorcentajeImpuesto() / 100));
	}

	@Override
	public void registrarAsientosDisponibles(int[] cantAsientos, double[] precios) {
		
		int nroAsiento = 0;
		
		for(int i = 0; i<cantAsientos.length + 1; i++)
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
	
	@Override
	public ArrayList<Asiento> getAsientosDisponibles()
	{
		throw new RuntimeException("VueloPrivado: No se pueden consultar los asientos disponibles de un vuelo privado");
	}
}
