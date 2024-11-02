package aerolinea;

import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;

public class Vuelo {

	private int codigo;
	private Aeropuerto destino;
	private Aeropuerto origen;
	private int totalAsientos;
	private LinkedList <Asiento> asientosVendidos;
	private int totalTripulantes;
	private LinkedList <Pasajero> pasajeros;
	private Date fechaSalida;
	private Date fechaLlegada;
	private int cantidadSecciones;
	private int porcentajeImpuesto;
	//Acordate que el vuelo se genera SIN asientos vendidos, ess se van sumando despues. 
	//No se porque tenemos un "pasajeros" y un "clientesRegistrados".
	//Lo de cantidad de secciones deberiamos revisarlo.
	//asientosVendidos deberia ser un hashMap
	public Vuelo(int codigo, Aeropuerto destino, Aeropuerto origen, int totalAsientos, LinkedList <Asiento> asientosVendidos, int totalTripulantes, 
			LinkedList <Pasajero> pasajeros, Date fechaSalida, Date fechaLlegada, int cantidadSecciones, int porcentajeImpuesto, HashMap<Integer, Cliente> ClientesRegistrados) throws Exception
	
	{
		
		if(ClientesRegistrados != null) {
			
			boolean valido = true;
			
			for (Pasajero pasajero : pasajeros)
				valido &= ClientesRegistrados.containsValue(pasajero.consultarCliente());
			
			if (!(valido && cantidadSecciones > 0 && porcentajeImpuesto > 0 && totalAsientos > 0 && totalTripulantes > 0 && 
				origen != null && destino != null &&  fechaSalida != null &&  fechaLlegada != null))
				
				throw new Exception("Valor de parametros invalido!!");
			
			this.codigo = codigo;
			this.destino = destino;
			this.origen = origen;
			this.totalAsientos =  totalAsientos;
			this.asientosVendidos = asientosVendidos;
			this.totalTripulantes= totalTripulantes;
			this.pasajeros= pasajeros;
			this.fechaSalida= fechaSalida;
			this.fechaLlegada = fechaLlegada;
			this.cantidadSecciones = cantidadSecciones;
			this.porcentajeImpuesto = porcentajeImpuesto;
		}
		
		else throw new Exception("Valor de parametros invalido!!");
		
	}
	
	
	public int consultarCantidadPasajeros()
	{
		return pasajeros.size(); 
	}
	
	public LinkedList<Asiento> consultarAsientosVendidos() 
	{
		return asientosVendidos;
	}
	
	public Aeropuerto consultarOrigen()
	{
		return origen;
	}
	
	public Aeropuerto consultarDestino()
	{
		return destino;
	}
	
	
}
