package aerolinea;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class Aerolinea implements IAerolinea 
{

		private String nombre;
		private String cuit;
		
		// para: 
		private HashMap<String, Vuelo> vuelos;
		
		private LinkedList<Aeropuerto> aeropuertos;
		
		private HashMap<Integer, Cliente> clientes;
		
		
		private HashMap<String, Double> FacturacionPorDestino;
		
		// para: asientosDisponibles, venderPasaje
		
		//El sistema conoce todos los asientos libres por vuelo
				
		//		       codVuelo, diccionario<codAsiento, Asiento>
		private HashMap<String, HashMap<Integer, Asiento>> asientosDisponiblesPorVuelo;
		
		// para: venderPasaje
		private Integer codigoBase;		//Los codigos numericos se obtienen en base a esta variable.
		
	
	public Aerolinea(String nombre, String cuit) //Aca no se puede usar excepciones o hay que cambiar Principal.java
	{
		
		if(!(nombre != null && nombre.length() > 0 && cuit != null && cuit.length() > 0))
			throw new RuntimeException("Valor de parametros invalido!!");
			
			this.nombre = nombre;
			this.cuit = cuit;
			this.vuelos = new HashMap<>();
			this.aeropuertos = new LinkedList<>();
			this.clientes = new HashMap<>();
			this.asientosDisponiblesPorVuelo = new HashMap<>();
			this.codigoBase = 100;
			this.FacturacionPorDestino  = new HashMap<>();
			
		
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
		
		((HashMap<Integer, Cliente>) clientes).put(cliente.getDni(), cliente);
		
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
		
		/*Tenemos que: 1) crear un Nacional, 2) agregarlo a la lista de vuelos (polimorfismo), 
		 			   3) generar codigo (el cual retornaremos) y 4) almacenar <codigo, asientosLibres>
		 			   5) Agregar en el diccionario de vuelos*/
		
		
		
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
		
		Map<Integer, String> retorno = new HashMap<>();
		
		//OJO que codigoVuelo es un String.
		//Integer codigoVuelo = Integer.parseInt(codVuelo);
		
		HashMap<Integer, Asiento> AsientosPorVuelo = asientosDisponiblesPorVuelo.get(codVuelo);
		
		Iterator<HashMap.Entry<Integer, Asiento>> iterador = AsientosPorVuelo.entrySet().iterator();
	    
	    while (iterador.hasNext()) {
			HashMap.Entry<Integer, Asiento> entrada = iterador.next();
			retorno.put(entrada.getKey(), entrada.getValue().toString());
		}
	    
	    return retorno;
	}

	
	
	@Override
	public int venderPasaje(int dni, String codVuelo, int nroAsiento, boolean aOcupar) {
		Integer Dni = dni;
		
		Cliente pasajero = clientes.get(Dni);
		
		if(pasajero == null) throw new RuntimeException("venderPasaje: El DNI provisto no pertenece a un cliente registrado.");
		
		Vuelo vuelo = vuelos.get(codVuelo);
		
		if(vuelo == null) throw new RuntimeException("venderPasaje: El codigo de vuelo provisto no pertenece a un vuelo registrado.");
		
		Asiento asiento = asientosDisponiblesPorVuelo.get(codVuelo).get(nroAsiento);
		
		if(asiento == null) throw new RuntimeException("venderPasaje: El asiento solicitado no esta disponible.");
		
		asientosDisponiblesPorVuelo.get(codVuelo).remove(nroAsiento); //Si encontre el asiento como libre, lo saco del listado
		
		asiento.setOcupado(aOcupar); //Pongo el asiento si esta ocupado o descoupado.
		
		//Dentro de VUELO hay que hacer el metodo de registrar asiento. 
/*Mi idea es, yo le paso al vuelo el cliente y el asiento. Con eso, vuelo tiene todo lo que necesita. 
 * Vuelo adentro tiene un dicionario HashMap<Dni, Pasajero> pasajeros . Ahora, el encargado de construir sus pasajeros conforme se van sumado es el vuelo mismo, entonces vuelo tiene que hacer los sigueintes chequeos:
 * Como un mismo clinete puede tener varios asientos, pero siempre es el mismo pasajero, con estos datos que le paso debe fijarse si el Cliente ya existe adentro del diccionario de pasajeros
 * Si el cliente que le estoy pasando no figura como pasajero, great, genera un pasajero (recordemos que pasajero adentro tiene una propiedad que es el objeto Cliente que yo le paso aca) y un diccionario de asientos, que para empezar solo tiene ste asiento que le paso.
 * Si el pasajero ya estaba en el vuelo, y solo esta sumando un asiento (cosa que nos damos cuenta buscando al pasajero por su dni, y viendo si ya existia), sencillamente nos metemos al pasajero y añadimos otro asiento.*/

		//vuelo.registrarAsiento(asiento, cliente);
		
		return obtenerCodigo();
	}

	
	
	@Override
	public List<String> consultarVuelosSimilares(String origen, String destino, String Fecha) {
		//IREP: Recibe Fechas con el formato "dd/mm/aaaa".
		
		List <String> codVuelosSimilares = new ArrayList<String>();
		return null;
	}

	
	
	@Override
	public void cancelarPasaje(int dni, String codVuelo, int nroAsiento) {
		
		//Version O(1) cuidado
		
		/* 1) Conseguir el vuelo con el codigo desde la variable local "vuelos"
		 * 2) Acceder al pasajero con el dni en el diccionario pasajeros
		 * 3) Guardar 3 valores, datos del asiento en cuestion, en Pasajero
		 * 						 cantidad de consumo del cliente (double consumo) en Pasajero
		 * 						 destino del vuelo (String) en Vuelo
		 * 
		 * 4) Borrar el asiento en el Pasajero
		 * 5) Crear un nuevo asiento y agregarlo en el diccionario "AsientosDisponiblesPorVuelo" con el codigo de vuelo
		 * 6) Restar el costo al total por destino en el diccionario "FacturacionPorDestino", utilizando el destino guardado
		 * */
		
		//1)
		Vuelo vuelo = vuelos.get(codVuelo); //O(1)
		
		//2)
		Pasajero pasajero = vuelo.getPasajeros().get(dni);
		
		//3)
		Aeropuerto aeropuertoDestino = vuelo.getDestino();
		String destino = aeropuertoDestino.getLocacion();
		
		double consumo = pasajero.getCosto();
		
		int codigoAsiento = pasajero.getAsiento(nroAsiento).getCodigo();//ACA VER SI MEJOR SE GENERA NUEVO CODIGO DE ASIENTO
		int seccion = pasajero.getAsiento(nroAsiento).getSeccion();
		String clase = pasajero.getAsiento(nroAsiento).getClase();
		boolean ocupado = false;
		
		//4)
		pasajero.eliminarASiento(nroAsiento);
		
		//5)
		Asiento redisponible = new Asiento(codigoAsiento, seccion, clase, ocupado); 
		
		asientosDisponiblesPorVuelo.get(codVuelo).remove(codigoAsiento);
		asientosDisponiblesPorVuelo.get(codVuelo).put(codigoAsiento, redisponible);
		
		//6)
		double facturado = FacturacionPorDestino.get(destino);
		FacturacionPorDestino.put(destino, facturado - consumo);
		
		
		int cantAsientos = pasajero.getCantAsientos(); //O(1)
		if(cantAsientos == 0) vuelo.eliminarPasajero(dni);
		
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
