package aerolinea;

import java.util.HashMap;

public abstract class Vuelo {

	private String codigo;
	private Aeropuerto destino;
	private Aeropuerto origen;
	private int totalAsientos; //Remover?
	private int totalTripulantes;
	private HashMap <Integer, Asiento> asientosDisponibles;
	private HashMap <Integer, Pasajero> pasajeros;
	private String fechaSalida;
	private int porcentajeImpuesto;
	
	public Vuelo(String codigo, Aeropuerto origen, Aeropuerto destino, int totalAsientos, int totalTripulantes, String fechaSalida, int porcentajeImpuesto)
	{
			this.codigo = codigo;
			this.destino = destino;
			this.origen = origen;
			this.totalAsientos =  totalAsientos;
			this.totalTripulantes= totalTripulantes;
			this.asientosDisponibles = new HashMap<Integer, Asiento>();
			this.pasajeros = new HashMap<Integer, Pasajero>();
			this.fechaSalida= fechaSalida;
			this.porcentajeImpuesto = porcentajeImpuesto;
	}
		
	public String getCodigo() 
	{
		return codigo;
	}
	
	public Aeropuerto getOrigen()
	{
		return origen;
	}
	
	public Aeropuerto getDestino()
	{
		return destino;
	}
	
	public int getCantidadPasajeros()
	{
		return pasajeros.size(); 
	}
	
	public int getTotalAsientos() 
	{
		return totalAsientos;
	}
	
	public int getTotalTripulantes() 
	{
		return totalTripulantes;
	}

	public int getPorcentajeImpuesto()
	{
		return porcentajeImpuesto;
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
	
	public Pasajero getPasajero(int dni) 
	{
		Integer Dni = dni;
		
		Pasajero pasajero = pasajeros.get(Dni);
		
		if(pasajero == null) throw new RuntimeException("Vuelo.getPasajero: El DNI provisto no pertenece a un pasajero en el vuelo.");
		
		return pasajero;
	}
	
	/*
	 * Determina si este vuelo en particular contiene al pasajero. Para aerolinea.cancelarPasajer no en O(1)
	 * */
	public boolean contienePasajero(int dni) 
	{
		Integer Dni = dni;
		
		Pasajero pasajero = pasajeros.get(Dni);
		
		if(pasajero == null) return false;
		
		return true;
	}
	
	public void eliminarPasajero(Integer dni)
	{
		pasajeros.remove(dni);
	}

	public void cancelarPasaje(int dni, int nroAsiento)//Esta es en O(1) 
	{
		//Busco al pasajero dentro del vuelo por su DNI. Si no existe, el vuelo tira una excepcion. 
		Pasajero pasajero = getPasajero(dni);
				
		//Le digo al pasajero que libere su asiento, esto me lo pasa con ocupado(false) y codPasje(0) 
		Asiento asiento = pasajero.removerAsiento(nroAsiento);
		
		//Disponibilizo nuevamente el asiento en el listado de asientos disponibles del vuelo
		registrarAsientoDisponible(asiento);
	}
	
	public void eliminarPasaje(int dni, int codPasaje) //Lo mismo que la de arriba pero no en O(1)
	{
		//Si el dni que me pasaron es invalido, getPasajero() se encarga de tirar una runtimeexception
		Pasajero pasajero = getPasajero(dni);
		
		pasajero.eliminarPasaje(codPasaje);

	}
	
	/*
	 * Busca un asiento disponible por su codigo. Si no lo encuentra, dinamita todo.
	 * */
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
	 * El vuelo busca el asiento disponible por su numero. Cuando lo obtiene, le asigna sus caracteristicas (aOcupar, codPasaje) y llama a registrar asiento
	 * */
	public int venderPasaje(Cliente cliente, int nroAsiento, boolean aOcupar, int codPasaje) 
	{
		Integer codAsiento = nroAsiento;
		
		Asiento asiento = getAsientoDisponible(codAsiento);
		
		asiento.setOcupado(aOcupar);
		
		asiento.setCodPasaje(codPasaje);
		
		return registrarAsiento(cliente, asiento);
	}
	
	/*
	 * La idea es, yo le paso el cliente y el asiento. Con eso, vuelo tiene todo lo que necesita. 
	 * Vuelo adentro tiene un dicionario HashMap<Dni, Pasajero> pasajeros. Ahora, el encargado de construir sus pasajeros conforme se van sumado clientes al vuelo es el mismo Vuelo.
	 * Entonces, vuelo tiene las siguientes responsabilidades:
	 * El Vuelo convierte a los clientes en pasajeros. Adentro suyo, puede tener un pasajero con varios asientos. Con estos datos que le paso el vuelo debe fijarse si el Cliente ya existe como pasajero:
	 * - Si el cliente que le estoy pasando ya existia como pasajero dentro del vuelo, significa que este solo esta comprando un asiento mas. Great, solo me meto al pasajero y le añado una asiento.
	 * - Si el cliente que le estoy pasando no figura como pasajero, eso signfica que es un pasajero nuevo, lo debo generar y le asigno su primer asiento.
	 *  
	 * Si todo sale bien en cualquier caso, retorno el codigo de pasaje.
	 */
	public int registrarAsiento(Cliente cliente, Asiento asiento) 
	{	
		//Removemos el asiento de asientosDisponibles
		asientosDisponibles.remove(asiento.getCodigo());
		
		//Busco al pasajero por su DNI
		Pasajero pasajero = getPasajero(cliente.getDni());

		//Si el pasajero no estaba previamente registrado en el vuelo, lo registro y asigno su asiento.
		if(pasajero == null) return registrarPasajero(cliente, asiento);
		
		//Si el pasajero ya estaba registrado en el vuelo, solo asigno su asiento.
		return pasajero.asignarAsiento(asiento);
	}
	
	/*
	 * Dado un cliente y un asiento, registra un NUEVO pasajero vacio, y despues le asigna su asiento. Retorna el codigo de pasaje. 
	 * */
	private int registrarPasajero(Cliente cliente, Asiento asiento) 
	{
		//Creo un nuevo pasajero en el diccionario de pasajeros.
		pasajeros.put(cliente.getDni(), new Pasajero(cliente));
		
		//Busco al pasajero que acabo de registrar por su DNI para asegurarme que exista, le asigno su asiento
		return pasajeros.get( cliente.getDni() ).asignarAsiento(asiento);
	}
	
	/*
	 * Registra asientos, uno por uno. Se utiliza dentro de registrarAsientoSdisponibles aca abajo.
	 * */
	public void registrarAsientoDisponible(Asiento asiento) 
	{
		asientosDisponibles.put(asiento.getCodigo(), asiento);
	}
	
	public void  registrarAsientosDisponibles(int[]cantAsientos, double[]precios)
	{
		//Cada vuelo modificara los asientos antes de registrarlos. Cada vuelo debe implementar su propia manera de registrar los asientos.
	}

	public void registrarAsientosDisponibles(int[] acompaniantes)
	{
		//version para vuelos privados
	}
}
