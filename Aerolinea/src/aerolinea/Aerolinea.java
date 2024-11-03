package aerolinea;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
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
		
		private HashMap<String, Double> facturacionPorDestino;
		
		private HashMap<String, HashMap<Integer, Asiento>> asientosDisponiblesPorVuelo;
		
		private Integer codigoBase;		//Los codigos numericos se obtienen en base a esta variable.
		
	
	public Aerolinea(String nombre, String cuit)
	{
		//Che hay que convertir todas estas validaciones en una funcion...
		if(!(nombre != null && nombre.length() > 0 && cuit != null && cuit.length() > 0))
			throw new RuntimeException("Valor de parametros invalido!!");
			
			this.nombre = nombre;
			this.cuit = cuit;
			this.vuelos = new HashMap<>();
			this.aeropuertos = new LinkedList<>();
			this.clientes = new HashMap<>();
			this.asientosDisponiblesPorVuelo = new HashMap<>();
			this.codigoBase = 100;
			this.facturacionPorDestino  = new HashMap<>();
			
		
	}
	

	
	private Integer obtenerCodigo()
	/*
	 * Con esta funcion genero IDs unicos en todo el codigo, solo voy sumando un int.
	 * */
	{
		codigoBase = codigoBase + 1;
		
		return codigoBase;
	}


	
	private LocalDate obtenerFecha(String fecha) 
	/*
	 * Con esta funcion, convierto un String del formato dd/mm/aaaa en un objeto Fecha el cual puedo manipular, por ejemplo,
	 * para saber cuanto es la fecha dada + 1 semana. Esto me permite lidiar con casos donde por ejemplo, si mi fecha 
	 * dada es 29/12/2024, el resultado + 1 semana sera 5/1/2025, sin tener que meterme a contar cambios de mes o año un string. 
	 * */
	{	
		DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd/MM/yyyy");
		
		try {
			
            LocalDate objetoFecha= LocalDate.parse(fecha, formato);
            return objetoFecha;
            
		}catch(Exception e){
			throw new RuntimeException("La fecha es invalida, favor de proveer una fecha en formato 'dd/mm/aaaa'.");
		}
		
	}

	
	
	@Override
	public void registrarCliente(int dni, String nombre, String telefono) 
	/*
	 * Hay que hacer validaciones de nombre string telefono etc etc...
	 * */
	{	
		Integer Dni = dni;
		
		Cliente cliente = new Cliente(Dni, nombre, telefono);
		
		((HashMap<Integer, Cliente>) clientes).put(cliente.getDni(), cliente);
		
	}

	
	
	@Override
	public void registrarAeropuerto(String nombre, String pais, String provincia, String direccion) 
	/*
	 * Hay que hacer validaciones de nombre != null, pais !=null, etc etc...
	 * */
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
	public int venderPasaje(int dni, String codVuelo, int nroAsiento, boolean aOcupar) 
	/**
	* 8 y 9 devuelve el codigo del pasaje comprado.
	* Los pasajeros que compran pasajes deben estar registrados como clientes, con todos sus datos, antes de realizar la compra. Devuelve el codigo del pasaje y lanza una excepción si no puede venderlo.
	* aOcupar indica si el asiento que será ocupado por el cliente, o si solo lo compro para viajar más cómodo.
	* Devuelve un código de pasaje único que se genera incrementalmente sin distinguir entre tipos de vuelos.
	* 
	* Obtiene el cliente, el vuelo y asiento de asientosDisponiblesPorVuelo. Si alguno de estos no se consigue, excepcion.
	* Una vez conseguidos todos los datos, se borra el asiento de asientosDisponiblesPorVuelo
	* Al asiento se le establece si esta ocupado o no
	* Se registra el asiento vendido adentro del vuelo
	* Por ultimo se genera un codigo de pasaje y se retorna.
	*  
	*/
	{
		Integer Dni = dni;
		
		Cliente pasajero = clientes.get(Dni);
		
		if(pasajero == null) throw new RuntimeException("venderPasaje: El DNI provisto no pertenece a un cliente registrado.");
		
		Vuelo vuelo = vuelos.get(codVuelo);
		
		if(vuelo == null) throw new RuntimeException("venderPasaje: El codigo de vuelo provisto no pertenece a un vuelo registrado.");
		
		Asiento asiento = asientosDisponiblesPorVuelo.get(codVuelo).get(nroAsiento);
		
		if(asiento == null) throw new RuntimeException("venderPasaje: El asiento solicitado no esta disponible.");
		
		//Si encontre el asiento en el listado de asientos libres, perfecto, lo remuevo. Si no lo encontre ya estaba vendido y tiro excepcion. 
		asientosDisponiblesPorVuelo.get(codVuelo).remove(nroAsiento); 
		
		//Pongo el asiento en el estado que me solicitaron, ocupado o desocupado.
		asiento.setOcupado(aOcupar); 
		
		//Dentro de VUELO hay que hacer el metodo de registrar asiento. 
/*Mi idea es, yo le paso al vuelo el cliente y el asiento. Con eso, vuelo tiene todo lo que necesita. 
 * Vuelo adentro tiene un dicionario HashMap<Dni, Pasajero> pasajeros . Ahora, el encargado de construir sus pasajeros conforme se van sumado es el vuelo mismo, entonces vuelo tiene que hacer los sigueintes chequeos:
 * Como un mismo clinete puede tener varios asientos, pero siempre es el mismo pasajero, con estos datos que le paso debe fijarse si el Cliente ya existe adentro del diccionario de pasajeros
 * Si el cliente que le estoy pasando no figura como pasajero, great, genera un pasajero (recordemos que pasajero adentro tiene una propiedad que es el objeto Cliente que yo le paso aca) y un diccionario de asientos, que para empezar solo tiene ste asiento que le paso.
 * Si el pasajero ya estaba en el vuelo, y solo esta sumando un asiento (cosa que nos damos cuenta buscando al pasajero por su dni, y viendo si ya existia), sencillamente nos metemos al pasajero y añadimos otro asiento.*/

		//Registro el asiento vendido.
		//vuelo.registrarAsiento(asiento, cliente);
		
		//Retorno el codigo de pasaje. 
		return obtenerCodigo();
	}

	
	
	@Override
	public List<String> consultarVuelosSimilares(String origen, String destino, String Fecha) 
	
	/* IREP: Recibe Fechas con el formato "dd/mm/aaaa".
	 * - 11. 
	 * devuelve una lista de códigos de vuelos. que estén entre fecha dada y hasta una semana despues. La lista estará vacía si no se encuentran vuelos similares. La Fecha es la fecha de salida.
	 * 
	 * Genero un nuevo array para devolver
	 * Convierto la fecha en un objeto para despues calcular la diferencia entre aca y una semana
	 * En este array meto todos los vuelos cuyo origen y destino matcheen con los parametros 
	*/
	
	{	
		//Genero la lista de vuelos vacia. 
		List <String> codVuelosSimilares = new ArrayList<String>();
		
		//Convierto el String a un objeto fecha para manipularlo
		LocalDate fecha = obtenerFecha(Fecha);
		
		return verificarVuelosSimilares(codVuelosSimilares, origen, destino, fecha);
	}

	public List<String> verificarVuelosSimilares(List <String> codVuelosSimilares, String origen, String destino, LocalDate fecha)
	/*
	 * Añade a la lista los vuelos que cumplan con mismo destino, origen y estar a una semana de la fecha.
	 * */
	{
		//Itero sobre todos los vuelos
		Iterator<Map.Entry<String, Vuelo>> it = vuelos.entrySet().iterator();
		
		while (it.hasNext()) {
			
			Vuelo vueloActual = (Vuelo) it.next();
			
			//Si el vuelo cumple con los parametros, sumo su codigo al array de vuelos similares. 
			if(vueloEsSimilar(vueloActual, origen, destino, fecha)) codVuelosSimilares.add(vueloActual.getCodigo());
		}
		
		return codVuelosSimilares;
	}
	
	private boolean vueloEsSimilar(Vuelo vuelo, String origen, String destino, LocalDate fecha) 
	/*
	 * Verifica 1 por 1 los vuelos que se le dan para ver si cumple con las condiciones. Si esto se da, retorna true. 
	 * */
	{
		//Si el destino del vuelo es el mismo que el dado
		if(vuelo.getDestino().getDireccion().equals(destino) &&
			
				//Y el origen del vuelo es el mismo que el dado
				vuelo.getOrigen().getDireccion().equals(origen) &&
					
					//Y el vuelo esta a una semana (o menos) de partir
					estaAUnaSemana(fecha, obtenerFecha(vuelo.getFechaSalida())))
		{
			return true;
		}
		
		return false;
	}
	
	private boolean estaAUnaSemana(LocalDate fecha, LocalDate fechaSalida) 
	/*
	 * Verifica si 2 fechas dadas se encuentran a una semana, en cuyo caso retorna true. Si no, false.
	 * Importante, solo se retorna true si fechaSalida es posterior a fecha. 
	 * Es decir, si fechaSalida es el 1/1/2000 y fecha es 2/1/2000, retorna false, 
	 * porque lo que nos interese es que fechaSalida sea hasta una semana despues de fecha.  
	 * */
	{
		long diasEntreFechas = ChronoUnit.DAYS.between(fecha, fechaSalida);

		//Retorna true si la diferencia entre fecha y fechaSalida es menor a una semana CUANDO fechaSalida es posterior a fecha. 
        return Math.abs(diasEntreFechas) < 7 && diasEntreFechas > 0;
	}
	
	@Override
	public void cancelarPasaje(int dni, String codVuelo, int nroAsiento) 
	/*
	 * Se borra el pasaje y se libera el lugar para que pueda comprarlo otro cliente.
	 * IMPORTANTE: Se debe resolver en O(1).
	 * 
	 * COMENTARIOS TOMAS ------
	 * Se busca el pasajero en el vuelo, se obtiene su asiento buscandolo por el nro de asiento en el diccionario de asientos del pasajero. Aca pueden pasar 2 cosas:
	 *  - Si el pasajero tiene multiples asientos, genial, se devuelve una copia del asiento en cuestion, y se elimina del diccionario de asientos del pasajero.
	 *  - Si el pasajero solo tenia ESTE asiento, se devuelve una copia del asiento en cuestion pero se borra el pasajero del array de pasajeros. Porque si un pasajero no tiene asiento, ya no es pasajero.
	 * 
	 * Una vez hecho esto, el asiento que habiamos retornado se vuelve a guardar en asientosDisponiblesPorVuelo, 
	 * HAY QUE ACORDARNOS antes de guardarlo en setear asiento.ocupado en false
	 * 
	 * 
	 * Respecto a los comentarios de abajo, en el punto 3, para que necesitamos guardar todos esos valores?
	 * Creo que solo guardando el asiento estamos, total es solo para guardarlo en asientosDisponiblesPorVuelo de vuelta.
	 * No necesitamos guardar lo que consume el cliente, solo restarle el valor del asiento a su costo. 
	 * Este valor es el mismo que vamos a restar en facturacionPorDestino
	 * 
	 * Si guardamos el asiento, el punto 5 de crear un nuevo asiento nos lo salteamos y solo lo guardamos en asientosDisponiblesPorVuelo
	 * 
	 * ------------------------
	 *  
	 * 1) Conseguir el vuelo con el codigo desde la variable local "vuelos"
	 * 2) Acceder al pasajero con el dni en el diccionario pasajeros
	 * 
	 * 
	 * 3) Guardar 3 valores, datos del asiento en cuestion, en Pasajero
	 * 						 cantidad de consumo del cliente (double consumo) en Pasajero
	 * 						 destino del vuelo (String) en Vuelo
	 * 
	 * 4) Borrar el asiento en el Pasajero
	 * 5) Crear un nuevo asiento y agregarlo en el diccionario "AsientosDisponiblesPorVuelo" con el codigo de vuelo
	 * 6) Restar el costo al total por destino en el diccionario "facturacionPorDestino", utilizando el destino guardado
	 * */
	
	{
		
		//1)
		Vuelo vuelo = vuelos.get(codVuelo); //O(1)
		
		//2)
		Pasajero pasajero = vuelo.getPasajeros().get(dni);
		
		//3)
		Aeropuerto aeropuertoDestino = vuelo.getDestino();
		/* Tom: Acordate que el "codigo" unico de cada aeropuerto es su nombre.
		 * Podrias hacer solo un:
		 * String destino = vuelo.getDestino().getNombre(); 
		 */
		String destino = aeropuertoDestino.getLocacion();
		
		/*
		 * En la parte que sigue yo guardaria el asiento entero:
		 * Asiento asiento = pasajero.getAsiento; 
		 * 
		 * Despues hay que sacarle el asiento:
		 * pasajero.eliminarAsiento(nroAsiento);
		 * 
		 * Resetear el asiento (para que no este ocupado):
		 * asiento.liberar(); Este metodo habria que crearlo
		 * 
		 * Asi ya tenemos el asiento guardado y directamente enganchamos ese en asientosDisponiblesPorVuelo:
		 * asientosDisponiblesPorVuelo.add(asiento);
		 * 
		 * La parte del costo ya si se complica, capaz cada pasajero deberia tener un pasajero.calcularCosto()
		 * que le calcula el costo en el momento, con su cantidad de asientos y demas. Entonces nosotros nos guardamos
		 * la diferencia entre el costo antes de sacarle el asiento, y despues. Esta es la diferencia que restamos 
		 * en facturacionPorDestino():
		 * 
		 * ANTES de sacar asiento:
		 * 
		 * double costoPrevio = pasajero.getCosto();
		 * 
		 *  //Le quitamos el asiento y demas
		 *
		 * pasajero.guardarCosto()
		 * 
		 * double costoActual = pasajero.getCosto()
		 * 
		 * double diferencia = costoPrevio - costoActual
		 * 
		 * Y esa diferencia la restamos a fecturacionPorDestino...
		 * */
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
		double facturado = facturacionPorDestino.get(destino);
		
		/*
		 * TOM: Aca le estas restando a facturacionPorDestino el precio del pasaje ANTES de sacarle el asiento 
		 * */
		facturacionPorDestino.put(destino, facturado - consumo);
		
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
