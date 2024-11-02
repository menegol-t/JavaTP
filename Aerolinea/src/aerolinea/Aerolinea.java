package aerolinea;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class Aerolinea implements IAerolinea 
{

		private String nombre;
		private String cuit;
		private HashMap<String, Vuelo> vuelos;
		private LinkedList<Aeropuerto> aeropuertos;
		private HashMap<Integer, Cliente> clientes;
		
		/*
		diccionario<codVuelo, AsientosLibres> AsientosDisponibles

		diccionario<codAsiento, asiento> AsientosLibres

		asiento
		this.seccion
		this.precio 
		*/
		
		//El sistema conoce todos los asientos libres por vuelo
		
		//				codVuelo, diccionario<codAsiento, Asiento>
		//private HashMap<Integer, HashMap<Integer, Asiento>> AsientosDisponiblesPorVuelo; 
						
		
		private HashMap<String, HashMap<Integer, Asiento>> asientosDisponiblesPorVuelo;
		private Integer codigoBase;		//Los codigos numericos se obtienen en base a esta variable.
		
	
	public Aerolinea(String nombre, String cuit) //Aca no se puede usar excepciones o hay que cambiar Principal.java
	{
		
		if(nombre != null && nombre.length() > 0 && cuit != null && cuit.length() > 0) {
			
			this.nombre = nombre;
			this.cuit = cuit;
			//this.Vuelos = new HashMap<>();
			this.aeropuertos = new LinkedList<>();
			this.clientes = new HashMap<>();
			this.asientosDisponiblesPorVuelo = new HashMap<>();
			this.codigoBase = 100;
			
		}
		
		else System.out.println("Valor de parametros invalido!!");
	}
	
	
	private Integer obtenerCodigo()		//En base a la variable global, se generan los codigos para cada objeto.
	{
		codigoBase = codigoBase + 1;
		
		return codigoBase;
	}

	
	
	@Override
	public void registrarCliente(int dni, String nombre, String telefono) 
	{	
		Integer Dni = dni;
		
		Cliente cliente = new Cliente(Dni, nombre, telefono);
		
		((HashMap<Integer, Cliente>) clientes).put(cliente.consultarDni(), cliente);
		
	}

	
	
	@Override
	public void registrarAeropuerto(String nombre, String pais, String provincia, String direccion) 
	{	
		boolean esNacional = false;
		
		Aeropuerto nuevo = null;
		
		if(pais.equals("Argentina") || pais.equals("argentina"))	
		{	
			esNacional = true;
			
			try {
				nuevo = new Aeropuerto(nombre, provincia, direccion, esNacional);	
			}
			catch(Exception Exception) {
				 Exception.printStackTrace();
			}
			aeropuertos.add(nuevo);
		}
		else
		{
			try {
				nuevo = new Aeropuerto(nombre, provincia, direccion, esNacional);
			}
			catch(Exception Exception) {
				Exception.printStackTrace();
			}
			aeropuertos.add(nuevo);
		}
	}
	

	
	@Override
	public String registrarVueloPublicoNacional(String origen, String destino, String fecha, int tripulantes,
			double valorRefrigerio, double[] precios, int[] cantAsientos) 
	{
		// TODO Auto-generated method stub
		return null;
	}

	
	
	@Override
	public String registrarVueloPublicoInternacional(String origen, String destino, String fecha, int tripulantes,
			double valorRefrigerio, int cantRefrigerios, double[] precios, int[] cantAsientos, String[] escalas) 
	{
		// TODO Auto-generated method stub
		return null;
	}

	
	
	@Override
	public String VenderVueloPrivado(String origen, String destino, String fecha, int tripulantes, double precio,
			int dniComprador, int[] acompaniantes) 
	{
		// TODO Auto-generated method stub
		return null;
	}

	
	
	@Override
	public Map<Integer, String> asientosDisponibles(String codVuelo) {
		
		//codAsiento, clase y precio
		Map<Integer, String> retorno = new HashMap();
		
		Integer codigoVuelo = Integer.parseInt(codVuelo);
		
		LinkedList< Asiento> AsientosPorVuelo = asientosDisponiblesPorVuelo.get(codigoVuelo);
		
		for(Asiento asiento : AsientosPorVuelo)
		{
			// Registramos: (codigoAsiento, el asiento impreso)
			retorno.put(asiento.consultarCodigo(), asiento.toString());
		}
			
		return retorno;
	}

	
	//aOcupar indica si el asiento que será ocupado por el cliente, o si solo lo compro para viajar más cómodo.
	// Devuelve un código de pasaje único que se genera incrementalmente sin distinguir entre tipos de vuelos.
	@Override
	public int venderPasaje(int dni, String codVuelo, int nroAsiento, boolean aOcupar) {
		Integer Dni = dni;
		
		Cliente pasajero = clientes.get(Dni);
		
		if(pasajero == null) throw new RuntimeException("venderPasaje: El DNI provisto no pertenece a un cliente registrado.");
		
		Vuelo vuelo = vuelos.get(codVuelo);
		
		if(vuelo == null) throw new RuntimeException("venderPasaje: El codigo de vuelo provisto no pertenece a un vuelo registrado.");
		
		//ahora neceisto buscar en todos los asientos disponibles del vuelo especifco, un asiento en particular por su codigo
		
		//Asiento asiento = asientosDisponibles(codVuelo).get(vuelo); 
		
		return 0;
	}

	
	
	@Override
	public List<String> consultarVuelosSimilares(String origen, String destino, String Fecha) {
		// TODO Auto-generated method stub
		return null;
	}

	
	
	@Override
	public void cancelarPasaje(int dni, String codVuelo, int nroAsiento) {
		// TODO Auto-generated method stub
		
	}

	
	
	@Override
	public void cancelarPasaje(int dni, int codPasaje) {
		// TODO Auto-generated method stub
		
	}

	
	
	@Override
	public List<String> cancelarVuelo(String codVuelo) {
		// TODO Auto-generated method stub
		return null;
	}

	
	
	@Override
	public double totalRecaudado(String destino) {
		// TODO Auto-generated method stub
		return 0;
	}

	
	
	@Override
	public String detalleDeVuelo(String codVuelo) {
		// TODO Auto-generated method stub
		return null;
	}
	
	//Auxiliar
	
	public HashMap<Integer, Cliente> consultarClientes()
	{
		return clientes;
	}
	 
}