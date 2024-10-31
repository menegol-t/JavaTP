package aerolinea;

import java.util.Date;
import java.util.LinkedList;

public class Vuelo {

	private int codigo;
	private Aeropuerto destino;
	private Aeropuerto origen;
	private int totalAsientos;
	private LinkedList <Asiento> asientosDisponibles;
	private int totalTripulantes;
	private LinkedList <Pasajero> pasajeros;
	private Date fechaSalida;
	private Date fechaLlegada;
	private int cantidadSecciones;
	private int porcentajeImpuesto;
	
	private Aerolinea aerolinea;

	public Vuelo(int codigo, Aeropuerto destino, Aeropuerto origen, int totalAsientos, LinkedList <Asiento> asientosDisponibles, int totalTripulantes, 
			LinkedList <Pasajero> pasajeros, Date fechaSalida, Date fechaLlegada, int cantidadSecciones, int porcentajeImpuesto, Aerolinea aerolinea) throws Exception
	
	{
		
		if(aerolinea != null) {
			
			boolean valido = true;
			
			for (Pasajero pasajero : pasajeros)
				valido &= aerolinea.consultarClientes().containsValue(pasajero.consultarCliente());
			
			if (!(valido && cantidadSecciones > 0 && porcentajeImpuesto > 0 && totalAsientos > 0 && totalTripulantes > 0 && 
				origen != null && destino != null &&  fechaSalida != null &&  fechaLlegada != null))
				
				throw new Exception("Valor de parametros invalido!!");
			
			this.codigo = codigo;
			this.destino = destino;
			this.origen = origen;
			this.totalAsientos =  totalAsientos;
			this.asientosDisponibles = asientosDisponibles;
			this.totalTripulantes= totalTripulantes;
			this.pasajeros= pasajeros;
			this.fechaSalida= fechaSalida;
			this.fechaLlegada = fechaLlegada;
			this.cantidadSecciones = cantidadSecciones;
			this.porcentajeImpuesto = porcentajeImpuesto;
		}
		
		else throw new Exception("Valor de parametros invalido!!");
		
	}
	
}
