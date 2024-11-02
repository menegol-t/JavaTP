package aerolinea;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class Aerolinea implements IAerolinea 
{

		private String nombre;
		private String cuit;
		//private HashMap<Integer, Vuelo> Vuelos; --> De momento no se utiliza
		private LinkedList<Aeropuerto> Aeropuertos;
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
		
		/*Esta implementacion me parece mas acertada, porque cada vez que quiera acceder a un asiento necesito
		 un codigo de asiento, el cual no poseemos en la funcion, es mas sencillo una lista la cual recorrer.
		 y como NO nos solicitan complejiodad O(1) creo que es apropiado (reviar linea
		*/
		private HashMap<Integer, LinkedList<Asiento>> AsientosDisponiblesPorVuelo;
		private Integer codigoBase;		//Los codigos numericos se obtienen en base a esta variable.
		
	
	public Aerolinea(String nombre, String cuit) //Aca no se puede usar excepciones o hay que cambiar Principal.java
	{
		
		if(nombre != null && nombre.length() > 0 && cuit != null && cuit.length() > 0) {
			
			this.nombre = nombre;
			this.cuit = cuit;
			//this.Vuelos = new HashMap<>();
			this.Aeropuertos = new LinkedList<>();
			this.clientes = new HashMap<>();
			this.AsientosDisponiblesPorVuelo = new HashMap<>();
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
			Aeropuertos.add(nuevo);
		}
		else
		{
			try {
				nuevo = new Aeropuerto(nombre, provincia, direccion, esNacional);
			}
			catch(Exception Exception) {
				Exception.printStackTrace();
			}
			Aeropuertos.add(nuevo);
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
		
		LinkedList< Asiento> AsientosPorVuelo = AsientosDisponiblesPorVuelo.get(codigoVuelo);
		
		for(Asiento asiento : AsientosPorVuelo)
		{
			// Registramos: (codigoAsiento, el asiento impreso)
			retorno.put(asiento.consultarCodigo(), asiento.toString());
		}
			
		return retorno;
	}

	@Override
	public int venderPasaje(int dni, String codVuelo, int nroAsiento, boolean aOcupar) {
		Integer Dni = dni;
		
		Cliente cliente = clientes.get(Dni);
		
		if(cliente == null) return 0;
		
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