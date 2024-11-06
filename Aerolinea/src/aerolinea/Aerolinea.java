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
		
		private HashMap<String, Aeropuerto> aeropuertos;
		
		private HashMap<Integer, Cliente> clientes;
		
		private HashMap<String, Double> facturacionPorDestino;
		
		private HashMap<String, HashMap<Integer, Asiento>> asientosDisponiblesPorVuelo;
		
		private Integer codigoBase;		//Los codigos numericos se obtienen en base a esta variable.
		
	
	public Aerolinea(String nombre, String cuit)
	{
		stringInvalido(nombre, "Nombre aerolonia"); stringInvalido(cuit, "CUIT"); 

		this.nombre = nombre;
		this.cuit = cuit;
		this.vuelos = new HashMap<>();
		this.aeropuertos = new HashMap<>();
		this.clientes = new HashMap<>();
		this.asientosDisponiblesPorVuelo = new HashMap<>();
		this.codigoBase = 1;
		this.facturacionPorDestino  = new HashMap<>();	
	}
	

	/*
	 * Con esta funcion genero IDs unicos en todo el codigo, solo voy sumando un int.
	 * */
	private Integer obtenerCodigo()
	{
		codigoBase = codigoBase + 1;
		
		return codigoBase;
	}


	/*
	 * Con esta funcion, convierto un String del formato dd/mm/aaaa en un objeto Fecha el cual puedo manipular, por ejemplo,
	 * para saber cuanto es la fecha dada + 1 semana. Esto me permite lidiar con casos donde por ejemplo, si mi fecha 
	 * dada es 29/12/2024, el resultado + 1 semana sera 5/1/2025, sin tener que meterme a contar cambios de mes o año un string. 
	 * */
	private LocalDate obtenerFecha(String fecha) 
	{	
		DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd/MM/yyyy");
		
		try {
			
            LocalDate objetoFecha= LocalDate.parse(fecha, formato);
            return objetoFecha;
            
		}catch(Exception e){
			throw new RuntimeException("obtenerFecha: La fecha es invalida, favor de proveer una fecha en formato 'dd/mm/aaaa'.");
		}
		
	}

	
	
	/*
	 * Con esta funcion detectamos si un numero int es invalido, es decir si vale 0 o vale negativo.
	 * */
	private void intInvalidoCero(int i, String valor) 
	{
		if(i <= 0){
			StringBuilder st = new StringBuilder("Error, ingresaste un valor invalido: ");
		
			st.append(valor);
			st.append("\n");
		
			throw new RuntimeException(st.toString());
		}
	}
	
	
	
	/*
	 * Con esta funcion valido si me pasaron un String vacio
	 * */
	private void stringInvalido(String s, String valor) 
	{
		if(s == null || s.length() == 0) {
			StringBuilder st = new StringBuilder("Error, ingresate un campo vacio: ");
			
			st.append(valor);
			st.append("\n");
			
			throw new RuntimeException(st.toString());
		}
	}
	
	
	
	/*
	 * Con esta funcion detectamos si un numero Double es invalido, es decir si vale 0 o vale negativo.
	 * */
	private void doubleInvalidoCero(double d, String valor) {
	    
		if (d <= 0) {
	        StringBuilder st = new StringBuilder("Error, ingresaste un valor inválido: ");
	        
	        st.append(valor);
	        st.append("\n");
	        
	        throw new RuntimeException(st.toString());
	    }
	}
	
	
	
	/*
	 * Con esta funcion valido si me pasaron un Array Int vacio
	 * */
	private void arrayIntInvalido(int[] array, String valor) {
	    if (array == null || array.length == 0) {
	        StringBuilder st = new StringBuilder("Error, ingresaste un array de enteros inválido o vacío: ");
	        st.append(valor);
	        st.append("\n");

	        throw new RuntimeException(st.toString());
	    }
	}
	
	
	
	/*
	 * Con esta funcion valido si me pasaron un Array Double vacio
	 * */
	private void arrayDoubleInvalido(double[] array, String valor) {
	    if (array == null || array.length == 0) {
	        StringBuilder st = new StringBuilder("Error, ingresaste un array de dobles inválido o vacío: ");
	        st.append(valor);
	        st.append("\n");

	        throw new RuntimeException(st.toString());
	    }
	}
	
	
	
	/*
	 * Con esta funcion valido si me pasaron un Array de cualquier objeto vacio 
	 * */
	private <T> void arrayInvalido(T[] array, String valor) {
	    
		if (array == null || array.length == 0) {
	        StringBuilder st = new StringBuilder("Error, ingresaste un array inválido o vacío: ");
	        
	        st.append(valor);
	        st.append("\n");

	        throw new RuntimeException(st.toString());
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
	{	
		
		stringInvalido(nombre, "Nombre"); stringInvalido(pais, "Pais"); stringInvalido(provincia, "Provincia");
		stringInvalido(direccion, "Direccion");
		
		boolean esNacional = false;
		
		Aeropuerto nuevo = null;
		
		if(pais.equals("Argentina") || pais.equals("argentina"))	
		{	
			esNacional = true;
			
			try {
				nuevo = new Aeropuerto(nombre, provincia, direccion, pais, esNacional);	
			}
			catch(Exception Exception) {

				 throw new RuntimeException("Error al crear el aeropuerto");
			}
			aeropuertos.put(direccion,nuevo);
		}
		else
		{
			try {
				nuevo = new Aeropuerto(nombre, provincia, direccion, pais, esNacional);
			}
			catch(Exception Exception) {
				throw new RuntimeException("Error al crear el aeropuerto");
			}
			aeropuertos.put(direccion,nuevo);
		}
	}
	
	

	/*El origen y destino deben ser aeropuertos con país=”Argentina” y ya registrados en la aerolinea. 
	 * La fecha es la fecha de salida del vuelo.
	* Los asientos se considerarán numerados correlativamente empezando con clase Turista y terminando con la clase Ejecutivo.
	* Se cumple que precios.length == cantAsientos.length == 2
	* -  cantAsientos[0] = cantidad total de asientos en clase Turista.
	* -  cantAsientos[1] = cantidad total de asientos en clase Ejecutivo
	*   idem precios.
	* Tripulantes es la cantidad de tripulantes del vuelo.
	* valorRefrigerio es el valor del unico refrigerio que se sirve en el vuelo.
	* 
	* Devuelve el código del Vuelo con el formato: {Nro_vuelo_publico}-PUB. Por ejemplo--> 103-PUB
	* Si al validar los datos no se puede registrar, se deberá lanzar una excepción.
	 * 
	 * 
	 * 
	 * 
	 1) crear un codigo Nacional y un Nacional
	 2) agregarlo a la lista de vuelos (polimorfismo),
	 3) Crear los asientos y asignarles un precio
	 4) almacenar <codigo, asientosLibres>
	 5) retornar el codigo
	*/
	@Override
	public String registrarVueloPublicoNacional(String origen, String destino, String fecha, int tripulantes,
			double valorRefrigerio, double[] precios, int[] cantAsientos) 
	{
		
		stringInvalido(origen, "Origen"); stringInvalido(destino, "Destino"); stringInvalido(origen, "Fecha");
		intInvalidoCero(tripulantes, "Tripulantes"); doubleInvalidoCero(valorRefrigerio, "Valor refrigerio");  
		arrayDoubleInvalido(precios, "Precios asientos"); arrayIntInvalido(cantAsientos, "Cantidad Asientos"); 
		
		//1)
		
		//Creacion del codigo
		Integer parteNumerica = obtenerCodigo();
		StringBuilder cod = new StringBuilder();
		cod.append(parteNumerica);
		cod.append("-PUB");
		String codigo = cod.toString();
		
		//Obtencion del origen y destino
		Aeropuerto Origen = aeropuertos.get(origen);
		Aeropuerto Destino = aeropuertos.get(destino);
		
		//Calculo del total de asientos
		int totalAsientos = cantAsientos[0] + cantAsientos[1]; 
		
		//Se genera un diccionario vacio de pasajeros, despues se iran agregando cuando se venda el pasaje
		HashMap<Integer, Pasajero> pasajerosVuelo = new HashMap<>();
		
		//Se crea el vuelo nacional
		Nacional nuevoNacional = new Nacional(codigo,Origen,Destino,totalAsientos,tripulantes,pasajerosVuelo, fecha, 
											  20, clientes,cantAsientos[0],cantAsientos[1], valorRefrigerio, precios[0],precios[1]);
		
		//2)
		vuelos.put(codigo, nuevoNacional);
		
		//3) y 4)
		
		asientosDisponiblesPorVuelo = nuevoNacional.registrarAsientosDeVuelos(cantAsientos, precios, nuevoNacional, asientosDisponiblesPorVuelo);
		
		//5)
		return codigo;
	}
	
	

	/*  Pueden ser vuelos con escalas o sin escalas. 
	 * La fecha es la de salida y debe ser posterior a la actual.  
	 * Los asientos se considerarán numerados correlativamente empezando con clase Turista, siguiendo por Ejecutiva 
	 * y terminando con Primera clase.
	 * 
	 * precios.length == cantAsientos.llength == 3
	 *    - cantAsientos[0]  = cantidad total de asientos en clase Turista.
	 *    - cantAsientos[1]  = cantidad total de asientos en clase Ejecutiva.
	 *    - cantAsientos[2]  = cantidad total de asientos en Primera clase.
	 *	 idem precios.
	 *	 - escalas = nombre del aeropuerto donde hace escala. Si no tiene escalas, esto es un arreglo vacío.
	 * Tripulantes es la cantidad de tripulantes del vuelo. 
	 * valorRefrigerio es el valor del refrigerio que se sirve en el vuelo.
	 * cantRefrigerios es la cantidad de refrigerio que se sirven en el vuelo.
	 *
	 * Devuelve el código del vuelo.  Con el formato: {Nro_vuelo_publico}-PUB, por ejemplo--> 103-PUB
	 * Si al validar los datos no se puede registrar, se deberá lanzar una excepción.

	 *  1) crear un codigo Internacional y un Internacional 
	 * 	2) agregarlo a la lista de vuelos (polimorfismo),
	  	3) Crear los asientos y asignarles un precio
	 	4) almacenar <codigo, asientosLibres>
	 	5) retornar el codigo
	*/
	@Override
	public String registrarVueloPublicoInternacional(String origen, String destino, String fecha, int tripulantes,
			double valorRefrigerio, int cantRefrigerios, double[] precios, int[] cantAsientos, String[] escalas) 
	{		
		//Validaciones / Excepciones
		stringInvalido(origen, "Origen"); stringInvalido(destino, "Destino"); stringInvalido(origen, "Fecha");
		intInvalidoCero(tripulantes, "Tripulantes"); doubleInvalidoCero(valorRefrigerio, "Valor refrigerio");  
		arrayDoubleInvalido(precios, "Precios asientos"); arrayIntInvalido(cantAsientos, "Cantidad Asientos"); 
		arrayInvalido(escalas, "Escalas"); 
		
		
		//1)
		
		//Creacion del codigo
		Integer parteNumerica = obtenerCodigo();
		StringBuilder cod = new StringBuilder();
		cod.append(parteNumerica);
		cod.append("-PUB");
		String codigo = cod.toString();
		
		//Obtencion de origen y destino
		Aeropuerto Origen = aeropuertos.get(origen);
		Aeropuerto Destino = aeropuertos.get(destino);
		
		//Calculo total de asientos
		int totalAsientos = cantAsientos[0] + cantAsientos[1] + cantAsientos[2]; 
		
		//Creamos la lista de las escalas
		LinkedList<Aeropuerto> listaEscalas = new LinkedList<>();
		
		//Buscamos y agregamos todas las escalas
		for(int i = 0; i<escalas.length; i++)
		{
			Aeropuerto escala = aeropuertos.get(escalas[i]);
			listaEscalas.add(escala);
		}
		
		//Ternario, si el largo del array de escalas es mayor que 0, entonces se valida
		boolean hayEscalas = escalas.length > 0 ? true : false;
		
		//Se genera un diccionario vacio de pasajeros, despues se iran agregando cuando se venda el pasaje
		HashMap<Integer, Pasajero> pasajerosVuelo = new HashMap<>();
		
		//Se genera nuevo internacional
		Nacional nuevoInternacional = new Internacional(codigo,Origen,Destino,totalAsientos,tripulantes,pasajerosVuelo, fecha, 
											  20, clientes,cantAsientos[0],cantAsientos[1],cantAsientos[2],valorRefrigerio, precios[0],precios[1],
											  precios[2], listaEscalas, hayEscalas);
		
		//2)
		vuelos.put(codigo, nuevoInternacional);
		
		//3) y 4)
		asientosDisponiblesPorVuelo = nuevoInternacional.registrarAsientosDeVuelos(cantAsientos, precios, nuevoInternacional, asientosDisponiblesPorVuelo);
		
		//5)
		return codigo;
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
		
		//Creamos el diccionario de retorno
		Map<Integer, String> retorno = new HashMap<>();
		
		//Obetenemos el diccionario de asientos disponibles para el vuelo en cuestion
		HashMap<Integer, Asiento> AsientosPorVuelo = asientosDisponiblesPorVuelo.get(codVuelo);
		
		//Generamos el iterador en el diccionario de asientos disponibles
		Iterator<HashMap.Entry<Integer, Asiento>> iterador = AsientosPorVuelo.entrySet().iterator();
	    
		//Recorremos
	    while (iterador.hasNext()) {
			HashMap.Entry<Integer, Asiento> entrada = iterador.next();
			
			//Agregamos al retorno el codigo de asiento y el asiento impreso
			retorno.put(entrada.getKey(), entrada.getValue().toString());
		}
	    
	    return retorno;
	}

	
	
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
	@Override
	public int venderPasaje(int dni, String codVuelo, int nroAsiento, boolean aOcupar)
	{
		//Realizamos validaciones, si no pasa un check tiramos runtime exception. 
		intInvalidoCero(dni, "DNI"); intInvalidoCero(nroAsiento, "Numero de asiento"); stringInvalido(codVuelo, "Codigo de vuelo");
		
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
		
		int codigo = obtenerCodigo();
		
		asiento.setCodPasaje(codigo);
		
		//Dentro de VUELO hay que hacer el metodo de registrar asiento. 
/*Mi idea es, yo le paso al vuelo el cliente y el asiento. Con eso, vuelo tiene todo lo que necesita. 
 * Vuelo adentro tiene un dicionario HashMap<Dni, Pasajero> pasajeros . Ahora, el encargado de construir sus pasajeros conforme se van sumado es el vuelo mismo, entonces vuelo tiene que hacer los sigueintes chequeos:
 * Como un mismo clinete puede tener varios asientos, pero siempre es el mismo pasajero, con estos datos que le paso debe fijarse si el Cliente ya existe adentro del diccionario de pasajeros
 * Si el cliente que le estoy pasando no figura como pasajero, great, genera un pasajero (recordemos que pasajero adentro tiene una propiedad que es el objeto Cliente que yo le paso aca) y un diccionario de asientos, que para empezar solo tiene ste asiento que le paso.
 * Si el pasajero ya estaba en el vuelo, y solo esta sumando un asiento (cosa que nos damos cuenta buscando al pasajero por su dni, y viendo si ya existia), sencillamente nos metemos al pasajero y añadimos otro asiento.*/

		//Registro el asiento vendido.
		//vuelo.registrarAsiento(asiento, cliente);
		
		return codigo;
		
	}
	
	
	/* IREP: Recibe Fechas con el formato "dd/mm/aaaa".
	 * - 11. 
	 * devuelve una lista de códigos de vuelos. que estén entre fecha dada y hasta una semana despues. La lista estará vacía si no se encuentran vuelos similares. La Fecha es la fecha de salida.
	 * 
	 * Genero un nuevo array para devolver
	 * Convierto la fecha en un objeto para despues calcular la diferencia entre aca y una semana
	 * En este array meto todos los vuelos cuyo origen y destino matcheen con los parametros 
	*/
	@Override
	public List<String> consultarVuelosSimilares(String origen, String destino, String Fecha) 
	{	
		//Realizo validaciones, si no pasa tiro runtimeexception. 
		stringInvalido(origen, "origen"); stringInvalido(destino, "destino"); stringInvalido(Fecha, "fecha"); 
		
		//Genero la lista de vuelos vacia. 
		List <String> codVuelosSimilares = new ArrayList<String>();
		
		//Convierto el String a un objeto fecha para manipularlo
		LocalDate fecha = obtenerFecha(Fecha);
		
		return verificarVuelosSimilares(codVuelosSimilares, origen, destino, fecha);
	}
	
	
	
	/*
	 * Añade a la lista los vuelos que cumplan con mismo destino, origen y estar a una semana de la fecha.
	 * */
	public List<String> verificarVuelosSimilares(List <String> codVuelosSimilares, String origen, String destino, LocalDate fechaAComparar)
	{
		//Genero un iterador de vuelos
		Iterator<Map.Entry<String, Vuelo>> it = vuelos.entrySet().iterator();
		
		//Itero sobre todos los vuelos viendo si cumplen o no las condiciones. Si los cumplen, los añado a la lista a retornar.
		while (it.hasNext()) {
			
			Vuelo vueloActual = (Vuelo) it.next();
			
			//Si el vuelo cumple con los parametros, sumo su codigo al array de vuelos similares. 
			if(vueloEsSimilar(vueloActual, origen, destino, fechaAComparar)) codVuelosSimilares.add(vueloActual.getCodigo());
		}
		
		return codVuelosSimilares;
	}
	
	
	
	/*
	 * Verifica cada vuelo individual para ver si cumple con las condiciones (que tenga el mismo origen, mismo destino, 
	 * y que su fecha de salida este a maximo 1 semana de la fecha a comparar). Si esto se da, retorna true. 
	 * */
	private boolean vueloEsSimilar(Vuelo vuelo, String origen, String destino, LocalDate fechaAComparar) 
	{
		//Si el destino del vuelo es el mismo que el dado
		if(vuelo.getDestino().getDireccion().equals(destino) &&
			
				//Y el origen del vuelo es el mismo que el dado
				vuelo.getOrigen().getDireccion().equals(origen) &&
					
					//Y el vuelo esta a una semana (o menos) de partir
					estaAUnaSemana(fechaAComparar, obtenerFecha(vuelo.getFechaSalida())))
		{
			return true;
		}
		
		return false;
	}
	
	
	
	/*
	 * Verifica si 2 fechas dadas se encuentran a una semana, en cuyo caso retorna true. Si no, false.
	 * Importante, solo se retorna true si fechaSalida es posterior a fecha. 
	 * Es decir, si fechaSalida es el 1/1/2000 y fecha es 2/1/2000, retorna false, 
	 * porque lo que nos interese es que fechaSalida sea hasta una semana despues de fecha.  
	 * */
	private boolean estaAUnaSemana(LocalDate fecha, LocalDate fechaSalida) 
	{
		long diasEntreFechas = ChronoUnit.DAYS.between(fecha, fechaSalida);

		//Retorna true si la diferencia entre fecha y fechaSalida es menor a una semana CUANDO fechaSalida es posterior a fecha. 
        return Math.abs(diasEntreFechas) < 7 && diasEntreFechas > 0;
	}
	
	
	
	/*
	 * Se borra el pasaje y se libera el lugar para que pueda comprarlo otro cliente.
	 * IMPORTANTE: Se debe resolver en O(1).
	 *  
	 * 1) Conseguir el vuelo con el codigo desde la variable local "vuelos"
	 * 2) Acceder al pasajero con el dni en el diccionario pasajeros
	 * 3) Guardar el asiento y setearlo para reutilizarlo
	 * 4) Borrar el asiento 
	 * 5) Agregar el asiento en el diccionario "AsientosDisponiblesPorVuelo" con el codigo de vuelo
	 * 6) Restar el precio del asiento al total por destino en el diccionario "facturacionPorDestino", utilizando el destino guardado
	 * */
	@Override
	public void cancelarPasaje(int dni, String codVuelo, int nroAsiento) 	
	{
		
		intInvalidoCero(dni, "DNI"); stringInvalido(codVuelo, "Codigo de vuelo"); intInvalidoCero(nroAsiento, "Numero asiento");
		
		//1)
		Vuelo vuelo = vuelos.get(codVuelo); //O(1)
		
		//2)
		Pasajero pasajero = vuelo.getPasajeros().get(dni);
		
		//3)
		Asiento asiento = pasajero.getAsiento(nroAsiento);
		
		asiento.liberarAsiento();
		
		//4)
		vuelo.eliminarAsiento(dni, nroAsiento);
		
		//5)
		Aeropuerto aeropuertoDestino = vuelo.getDestino();
		
		String destino = aeropuertoDestino.getLocacion();
		asientosDisponiblesPorVuelo.get(codVuelo).put(nroAsiento, asiento);
		
		//6)
		double precioAsiento = asiento.getPrecio();
		double facturado = facturacionPorDestino.get(destino);
		
		facturacionPorDestino.put(destino, facturado - precioAsiento);
		
		int cantAsientos = pasajero.getCantAsientos(); //O(1)
		if(cantAsientos == 0) vuelo.eliminarPasajero(dni);
		
	}

	
	
	/** 12-B
	* Se cancela un pasaje dado el codigo de pasaje. 
	* NO es necesario que se resuelva en O(1).
	* 
	* Para cancelar un pasaje (asiento), debo:
	* Ubicar el vuelo del pasaje. 
	* Ubicar el cliente, por su dni. 
	* Finalmente, ubicar el pasaje en particular, por su codigo. Un cliente puede tener multiples pasajes (cada pasaje representa un asiento, como un ticket en el cine).
	* Para esto ultimo, recorro todos los asientos posibles que puede tener el cliente. 
	*/
	@Override
	public void cancelarPasaje(int dni, int codPasaje) {
		
		//Recorro todo el diccionario de vuelos
		Iterator<Map.Entry<String, Vuelo>> it = vuelos.entrySet().iterator();
		
		/* Como estoy cancelando un pasaje, ni bien encuentre al cliente que lo tiene, invoco a eliminarPasaje.
		 * Si recorro todo y no encuentro al cliente, bueno, el pasaje ya estaba eliminado.*/
		while (it.hasNext()) {
			
			Vuelo vueloActual = (Vuelo) it.next();

			//Si encuentro al cliente, le elimino el pasaje y termino.  
			if(vueloActual.getPasajero(dni) != null) vueloActual.eliminarPasaje(dni, codPasaje);
		}
		
	}

	
	
	/** - 13
	* Cancela un vuelo completo conociendo su codigo.
	* Los pasajes se reprograman a vuelos con igual destino, no importa el numero del asiento pero 
	* si a igual seccion o a una mejor, y no importan las escalas.
	* Devuelve los codigos de los pasajes que no se pudieron reprogramar.
	* Los pasajes no reprogramados se eliminan. Y se devuelven los datos de la cancelación, indicando 
	* los pasajeros que se reprogramaron y a qué vuelo,  y los que se cancelaron por no tener lugar.
	* Devuelve una lista de Strings con este formato : “dni - nombre - telefono - [Codigo nuevo vuelo|CANCELADO]”
	* --> Ejemplo: 
	*   . 11111111 - Juan - 33333333 - CANCELADO
	*   . 11234126 - Jonathan - 33333311 - 545-PUB
	*   
	* Busco el vuelo en cuestion. 
	* Guardo su destino.
	* Hago una lista con todos los clientes del vuelo y la seccion de su asiento. 
	* Itero sobre todos los vuelos con el mismo destino. 
	* Uso asientosDisponiblesPorVuelo para buscar asientos en dichos vuelos que sean iguales o mejores que los de los clientes.
	* De ahi invoco a venderPasaje para asignar ese asiento. 
	* Conforme voy vendiendo pasajes voy sacando de la lista de clientes y voy añadiendolos a una lista nueva, con su toString y vuelo
	* Los clientes que quedan en la lista anterior los paso a la lista nueva con su toString y CANCELADO
	* 
	*/
	@Override
	public List<String> cancelarVuelo(String codVuelo) {
		
		//FUNCION INCOMPLETA
		
		stringInvalido(codVuelo, "Codigo de vuelo"); 
		
		Vuelo vueloACancelar = vuelos.get(codVuelo);
		
		String destino = vueloACancelar.getDestino().getLocacion();
		
		Iterator<Map.Entry<String, Vuelo>> it = vuelos.entrySet().iterator();
		
		while (it.hasNext()) {
			
			Vuelo vueloActual = (Vuelo) it.next();
			
			if(vueloActual.getDestino().getLocacion() == destino) {
				
				String codigo = vueloActual.getCodigo();
				
			}

			
		}
		return null;
	}

	
	/** - 14
	* devuelve el total recaudado por todos los viajes al destino pasado por parámetro. 
	* IMPORTANTE: Se debe resolver en O(1).
	*/
	@Override
	public double totalRecaudado(String destino) {
		return facturacionPorDestino.get(destino);
	}


	
	
	@Override
	public String detalleDeVuelo(String codVuelo) {
		// TODO Auto-generated method stub
		return null;
	}
	
	//Auxiliar
	
	public HashMap<Integer, Cliente> getClientes()
	{
		return clientes;
	}
	
}
