package aerolinea;

import java.util.HashMap;
import java.util.Iterator;


public abstract class Vuelo {

	private String codigo;
	private Aeropuerto destino;
	private Aeropuerto origen;
	private int totalAsientos; //Remover?
	private int totalTripulantes;
	private HashMap <Integer, Asiento> asientosDisponibles;
	private HashMap <Integer, Pasajero> pasajeros;
	private String fechaSalida;
	private String fechaLlegada;
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
	
	public int getTotalAsientos() 
	{
		return totalAsientos;
	}
	
	public int getTotalTripulantes() 
	{
		return totalTripulantes;
	}
	
	public HashMap<Integer, Asiento> getAsientosDisponibles()
	{
		return asientosDisponibles;
	}
	
	public HashMap<Integer, Pasajero> getPasajeros()
	{
		return pasajeros;
	}
	
	public String getFechaSalida() 
	{
		
		return fechaSalida;
	}
	
	public String getFechaLlegada() 
	{
		return fechaLlegada;
	}
	
	public void eliminarPasajero(int id)
	{
		pasajeros.remove(id);
	}

	public Pasajero getPasajero(int dni) 
	{
		Pasajero pasajero = pasajeros.get(dni);
		
		if(pasajero == null) throw new RuntimeException("Vuelo.getPasajero: El DNI provisto no pertenece a un pasajero en el vuelo.");
		
		return pasajero;
	}
	
	public void eliminarAsiento(int dni, int numAsiento)
	{
		Pasajero pasajero = getPasajero(dni);
		pasajero.eliminarAsiento(numAsiento);
	}

	public void eliminarPasaje(int dni, int codPasaje) 
	{
		Pasajero pasajero = getPasajero(dni);
		pasajero.eliminarPasaje(codPasaje);

	}
	
	public Asiento getAsientoDisponible(Integer codAsiento) 
	{
		Asiento asiento =  asientosDisponibles.get(codAsiento);
		
		if(asiento == null) throw new RuntimeException("Vuelo.getAsientoDisponible: El asiento solicitado no esta disponible.");
		
		return asiento;
	}
	
	public int venderAsiento(Cliente cliente, Asiento asiento, boolean aOcupar, int codPasaje) 
	{
		asientosDisponibles.remove(asiento.getCodigo());
		
		/*
		 * Queda por hacer:
		 * Mi idea es, yo le paso al vuelo el cliente y el asiento. Con eso, vuelo tiene todo lo que necesita. 
		 * Vuelo adentro tiene un dicionario HashMap<Dni, Pasajero> pasajeros . Ahora, el encargado de construir sus pasajeros conforme se van sumado es el vuelo mismo, entonces vuelo tiene que hacer los sigueintes chequeos:
		 * Como un mismo clinete puede tener varios asientos, pero siempre es el mismo pasajero, con estos datos que le paso debe fijarse si el Cliente ya existe adentro del diccionario de pasajeros
		 * - Si el cliente que le estoy pasando no figura como pasajero, great, genera un pasajero (recordemos que pasajero adentro tiene una propiedad que es el objeto Cliente que yo le paso aca) y un diccionario de asientos, que para empezar solo tiene ste asiento que le paso.
		 * - Si el pasajero ya estaba en el vuelo, y solo esta sumando un asiento (cosa que nos damos cuenta buscando al pasajero por su dni, y viendo si ya existia), sencillamente nos metemos al pasajero y añadimos otro asiento.
		 */
		
		return codPasaje;
	}
	
	public void  registrarAsientosDeVuelos(int[]cantAsientos, double[]precios, Nacional nacional)
	{
		
	}

	public HashMap<String, HashMap<Integer, Asiento>>  registrarAsientosDeVuelos(int[] cantAsientos, double[] precios, Nacional nacional,
			HashMap<String, HashMap<Integer, Asiento>> asientosDisponiblesPorVuelo) 
	{
		return asientosDisponiblesPorVuelo;	
	}
	
}
