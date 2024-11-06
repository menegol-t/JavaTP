package aerolinea;

import java.util.HashMap;
import java.util.Iterator;


public abstract class Vuelo {

	private String codigo;
	private Aeropuerto destino;
	private Aeropuerto origen;
	private int totalAsientos; //Remover?
	private int totalTripulantes;
	
	private HashMap <Integer, Pasajero> pasajeros;
	private String fechaSalida;
	private int porcentajeImpuesto;
	
	
	
	public Vuelo(String codigo, Aeropuerto destino, Aeropuerto origen, int totalAsientos, int totalTripulantes, HashMap <Integer, Pasajero> pasajeros, 
				 String fechaSalida, int porcentajeImpuesto, HashMap<Integer, Cliente> ClientesRegistrados)
	
	{
		
		if(ClientesRegistrados != null) {
			
			boolean valido = true;
			
			
			Iterator<HashMap.Entry<Integer, Pasajero>> iterador = pasajeros.entrySet().iterator();
		    
		    while (iterador.hasNext()) {
				HashMap.Entry<Integer, Pasajero> entrada = iterador.next();
				valido &= (ClientesRegistrados.containsValue(entrada.getValue().consultarCliente()));
			}
			
			
			if (!(valido && porcentajeImpuesto > 0 && totalAsientos > 0 && totalTripulantes > 0 && 
				origen != null && destino != null &&  fechaSalida != null))
				
				throw new RuntimeException("Valor de parametros invalido!!");
			
			this.codigo = codigo;
			this.destino = destino;
			this.origen = origen;
			this.totalAsientos =  totalAsientos;
			this.totalTripulantes= totalTripulantes;
			this.pasajeros= pasajeros;
			this.fechaSalida= fechaSalida;
			this.porcentajeImpuesto = porcentajeImpuesto;
		}
		
		else throw new RuntimeException("Valor de parametros invalido!!");
		
	}
	
	
	public String getCodigo() 
	{
		return codigo;
	}
	
	public int getCantidadPasajeros()
	{
		return pasajeros.size(); 
	}
	
	public Aeropuerto getOrigen()
	{
		return origen;
	}
	
	public Aeropuerto getDestino()
	{
		return destino;
	}
	
	public String getFechaSalida() {
		
		return fechaSalida;
	}
	
	
	//Aux
	
	public HashMap<Integer, Pasajero> getPasajeros()
	{
		return pasajeros;
	}
	
	
	public void eliminarPasajero(int id)
	{
		pasajeros.remove(id);
	}

		public Pasajero getPasajero(int dni) 
	{
		return pasajeros.get(dni);
	}
	
	public void eliminarAsiento(int dni, int numAsiento)
	{
		Pasajero pasajero = pasajeros.get(dni);
		pasajero.eliminarAsiento(numAsiento);
	}

	
	public void eliminarPasaje(int dni, int codPasaje) 
	{
		Pasajero pasajero= getPasajero(dni);
		pasajero.eliminarPasaje(codPasaje);

	}
	
	public void  registrarAsientosDeVuelos(int[]cantAsientos, double[]precios, Nacional nacional)
	{
		
	}


	public HashMap<String, HashMap<Integer, Asiento>>  registrarAsientosDeVuelos(int[] cantAsientos, double[] precios, Nacional nacional,
			HashMap<String, HashMap<Integer, Asiento>> asientosDisponiblesPorVuelo) {
				
		return asientosDisponiblesPorVuelo;

		
	}
	
}
