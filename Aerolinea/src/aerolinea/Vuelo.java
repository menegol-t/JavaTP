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
//		
//		if(ClientesRegistrados != null) {
//			
//			boolean valido = true;
//			
//			
//			Iterator<HashMap.Entry<Integer, Pasajero>> iterador = pasajeros.entrySet().iterator();
//		    
//		    while (iterador.hasNext()) {
//				HashMap.Entry<Integer, Pasajero> entrada = iterador.next();
//				valido &= (ClientesRegistrados.containsValue(entrada.getValue().consultarCliente()));
//			}
//			
//			
//			if (!(valido && porcentajeImpuesto > 0 && totalAsientos > 0 && totalTripulantes > 0 && 
//				origen != null && destino != null &&  fechaSalida != null))
//				
//				throw new RuntimeException("Valor de parametros invalido!!");
			
			this.codigo = codigo;
			this.destino = destino;
			this.origen = origen;
			this.totalAsientos =  totalAsientos;
			this.totalTripulantes= totalTripulantes;
			this.pasajeros= pasajeros;
			this.fechaSalida= fechaSalida;
			this.porcentajeImpuesto = porcentajeImpuesto;
//		}
//		
//		else throw new RuntimeException("Valor de parametros invalido!!");
		
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

	public Pasajero getPasajero(int dni) 
	{
		Integer Dni = dni;
		
		Pasajero pasajero = pasajeros.get(Dni);
		
		if(pasajero == null) throw new RuntimeException("Vuelo.getPasajero: El DNI provisto no pertenece a un pasajero en el vuelo.");
		
		return pasajero;
	}
	
	public void eliminarPasajero(Integer dni)
	{
		pasajeros.remove(dni);
	}
	
	public void eliminarAsiento(int dni, int numAsiento)
	{
		//Si el dni que me pasaron es invalido, getPasajero() se encarga de tirar una runtimeexception
		Pasajero pasajero = getPasajero(dni);
		
		pasajero.eliminarAsiento(numAsiento);
	}

	public void eliminarPasaje(int dni, int codPasaje) 
	{
		//Si el dni que me pasaron es invalido, getPasajero() se encarga de tirar una runtimeexception
		Pasajero pasajero = getPasajero(dni);
		
		pasajero.eliminarPasaje(codPasaje);

	}
	
	public Asiento getAsientoDisponible(Integer codAsiento) 
	{
		//Busco el asiento disponible dentro del vuelo
		Asiento asiento =  asientosDisponibles.get(codAsiento);
		
		//Si este no existe, tiro una excepcion
		if(asiento == null) throw new RuntimeException("Vuelo.getAsientoDisponible: El asiento solicitado no esta disponible.");
		
		//Si lo encontre, lo devuelvo.
		return asiento;
	}
	
	/*
	 * La idea es, yo le paso al vuelo el cliente y el asiento. Con eso, vuelo tiene todo lo que necesita. 
	 * Vuelo adentro tiene un dicionario HashMap<Dni, Pasajero> pasajeros. Ahora, el encargado de construir sus pasajeros conforme se van sumado clientes al vuelo es el mismo Vuelo.
	 * Entonces, vuelo tiene las siguientes responsabilidades:
	 * El Vuelo convierte a los clientes en pasajeros. Adentro suyo, puede tener un pasajero con varios asientos. Con estos datos que le paso el vuelo debe fijarse si el Cliente ya existe como pasajero:
	 * - Si el cliente que le estoy pasando ya existia como pasajero dentro del vuelo, significa que este solo esta comprando un asiento mas. Great, solo me meto al pasajero y le añado una asiento.
	 * - Si el cliente que le estoy pasando no figura como pasajero, eso signfica que es un pasajero nuevo, lo debo generar y le asigno su primer asiento.
	 *  
	 * Si todo sale bien en cualquier caso, retorno el codigo de pasaje.
	 */
	public int registrarAsiento(Cliente cliente, Asiento asiento, int codPasaje) 
	{	
		//Removemos el asiento de asientosDisponibles
		asientosDisponibles.remove(asiento.getCodigo());
		
		Pasajero pasajero = pasajeros.get(cliente.getDni());

		//Si el pasajero no estaba previamente registrado en el vuelo, lo registro y asigno su asiento.
		if(pasajero == null) return registrarPasajero(cliente, asiento, codPasaje);
		
		//Si el pasajero ya estaba registrado en el vuelo, solo asigno su asiento.
		return pasajero.asignarAsiento(asiento, codPasaje);
	}
	
	private int registrarPasajero(Cliente cliente, Asiento asiento, int codPasaje) 
	{
		//Creo un nuevo pasajero en el diccionario de pasajeros.
		pasajeros.put(cliente.getDni(), new Pasajero(cliente));
		
		//Busco al pasajero nuevo por su DNI, le asigno su asiento
		return pasajeros.get(cliente.getDni()).asignarAsiento(asiento, codPasaje);
		
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
