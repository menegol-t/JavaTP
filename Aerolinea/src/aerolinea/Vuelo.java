package aerolinea;

import java.util.Date;
import java.util.LinkedList;

public class Vuelo {

	int codigo;
	Aeropuerto destino;
	Aeropuerto origen;
	int totalAsientos;
	LinkedList <Asiento> asientosDisponibles;
	int totalTripulantes;
	LinkedList <Pasajero> pasajeros;
	Date fechaSalida;
	Date fechaLlegada;
	int cantidadSecciones;
	int porcentajeImpuesto;

	public Vuelo(int codigo, Aeropuerto destino, Aeropuerto origen, int totalAsientos, LinkedList <Asiento> asientosDisponibles, 
				 int totalTripulantes, LinkedList <Pasajero> pasajeros, Date fechaSalida, Date fechaLlegada, int cantidadSecciones, int porcentajeImpuesto) throws Exception
	{
		boolean valido = false;
		
		//for (Pasajero elem : pasajeros)
			//valido |= (!aerolinea.consultarClientes().contains(elem))
		
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
	
}
