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
		
		private Integer codigoBase;		//Los codigos numericos se obtienen en base a esta variable.
		
	
	public Aerolinea(String nombre, String cuit)
	{
		stringInvalido(nombre, "Nombre aerolonia");
		stringInvalido(cuit, "CUIT"); 

		this.nombre = nombre;
		this.cuit = cuit;
		this.vuelos = new HashMap<>();
		this.aeropuertos = new HashMap<>();
		this.clientes = new HashMap<>();
		this.facturacionPorDestino  = new HashMap<>();	
		this.codigoBase = 1;
	}
	

	/*
	 * Con esta funcion genero IDs unicos en todo el codigo, solo voy sumando un int.
	 * */
	private Integer obtenerCodigo()
	{
		codigoBase = codigoBase + 1;
		
		return codigoBase;
	}

	public String crearCodigoPublico()
	{
		Integer parteNumerica = obtenerCodigo();
		StringBuilder cod = new StringBuilder();
		cod.append(parteNumerica);
		cod.append("-PUB");
		String codigo = cod.toString();
		
		return codigo;
	}
	
	private Aeropuerto getAeropuerto(String nombre)
	{
		Aeropuerto aeropuerto = aeropuertos.get(nombre);
		
		if(aeropuerto == null) throw new RuntimeException("getAeropuerto: El nombre dado no corresponde a ningun aeropuerto registrado.");
		
		return aeropuerto;
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
	
	
	
	/**
	* - 2
	* Se registran los clientes de la Aerolínea, compren o no pasaje. 
	* Cuando un cliente compre un pasaje es un Cliente (pasajero) y queda registrado en el vuelo correspondiente.
	* 
	* Para esto, convertimos el dni que nos pasaron en Integer, dado que el diccionario de clientes usa como clave su dni.
	* Despues mandamos a crear el cliente y lo guardamos en el array de clientes. Si nos pasaron algun valor invalido (dni == 0)
	* el constructor de cliente debe tirar exception.
    */
	@Override
	public void registrarCliente(int dni, String nombre, String telefono) 
	{	
		Integer Dni = dni;
		
		//Añado el nuevo cliente a la lista de clientes. Si los datos que me pasaron son invalidos, el constructor de cliente rebota
		clientes.put(Dni, new Cliente(dni, nombre, telefono));
	}

	
	
	/**
	* - 3 
	* Se ingresa un aeropuerto con los datos que lo identifican. Estos aeropuertos son los que deberán corresponder
	* al origen y destino de los vuelos.
	* El nombre es único por aeropuerto en todo el mundo.
	* 
	* Para esto, primero verificamos si el pais del aeropuerto es o no es argentina.
	* Posterior, guardamos el aeropuerto en el diccionario de aeropuertos. 
	* Si alguno de sus datos es invalido (por ej direccion == null) el constructor de aeropuerto tira runtimeException. 
	*/
	@Override
	public void registrarAeropuerto(String nombre, String pais, String estado, String direccion) 
	{	
		//Verifico si pais es null antes de utilizarla para determinar esNacional. El resto de variables se verifican en el constructor de aeropuerto.
		if(pais == null) throw new RuntimeException("registrarAeropuerto: ni el nombre ni el pais del aeropuerto pueden ser vacios.");
		
		//Verifico si el pais es o no es argentina (es igual de valido que me pasen Argentina, argentina, aRgEnTiNa, etc)
		boolean esNacional = pais.equalsIgnoreCase("Argentina");
		
		//Añado el nuevo aeropuerto en el diccionario. Si los datos que me pasaron son invalidos, el constructor del aeropuerto tira excepcion. 
		aeropuertos.put(nombre, new Aeropuerto(nombre, pais, estado, direccion, esNacional));
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
	* Primero verificamos que los aeropuertos esten registrados en la compañia.
	* Posterior, generamos el nuevo vuelo. El constructor de este debe tirar runtimeException si algun dato es invalido (lso tripulantes no pueden ser 0, etc)
	* Si todo esta bien, retornamos el codigo guardado. 
	*/
	
	//El constructor de esto deberia validar que los datos no sean invalidos
	@Override
	public String registrarVueloPublicoNacional(String origen, String destino, String fecha, int tripulantes,
			double valorRefrigerio, double[] precios, int[] cantAsientos) 
	{
		//Creamos un codigo
		String codigo = crearCodigoPublico();
		
		//Obtengo los aeropuertos de origen y destino por su nombre. Si no estaban registrados, tira una excepcion.
		Aeropuerto Origen = getAeropuerto(origen);
		Aeropuerto Destino = getAeropuerto(destino);
		
		//Calculo del total de asientos entre la primera y la segunda clase
		int totalAsientos = cantAsientos[0] + cantAsientos[1];
		
		//Mando a generar el vuelo nacional, y a registrar todos sus asientos.
		return registrarNacional(codigo, Origen, Destino, totalAsientos, tripulantes, fecha, cantAsientos[0], cantAsientos[1], valorRefrigerio, precios[0], precios[1], cantAsientos, precios);
	}
	
	private String registrarNacional(String codigo, Aeropuerto origen, Aeropuerto destino, int totalAsientos, int totalTripulantes, String fechaSalida, int limitePasajerosEconomica, int limitePasajerosPrimera, double valorRefrigerio, double precioEconomica, double precioPrimera, int[] asientos, double[] precios) 
	{
		//Generamos nuevo vuelo nacional. Si algun dato es incorrecto, tira runtimeException
		Nacional nuevoNacional = new Nacional(codigo, origen, destino, totalAsientos, totalTripulantes, fechaSalida, limitePasajerosEconomica, limitePasajerosPrimera, valorRefrigerio, precioEconomica, precioPrimera);
		
		//Asignamos sus asienos al nuevo vuelo
		nuevoNacional.registrarAsientosDisponibles(asientos, precios);
		
		//Ponemos el nuevo vuelo en el diccionario de vuelos
		vuelos.put(codigo, nuevoNacional);
		
		//Para retornar el codigo, buscamos al nuevoNacional en el diccionario de vuelos donde lo acabamos de registrar, y devolvemos su codigo. Con esto validamos que se guardo bien. 
		return vuelos.get(codigo).getCodigo();
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
		//Validaciones. Si alguna falla se tira excepcion. 
		stringInvalido(origen, "Origen"); stringInvalido(destino, "Destino"); stringInvalido(origen, "Fecha");
		intInvalidoCero(tripulantes, "Tripulantes"); doubleInvalidoCero(valorRefrigerio, "Valor refrigerio");  
		arrayDoubleInvalido(precios, "Precios asientos"); arrayIntInvalido(cantAsientos, "Cantidad Asientos"); arrayInvalido(escalas, "Escalas"); 
		
		
		
		//Creacion del codigo
		String codigo = crearCodigoPublico();
		
		//Obtencion de origen y destino
		Aeropuerto Origen = getAeropuerto(origen);
		Aeropuerto Destino = getAeropuerto(destino);
		
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
		Nacional nuevoInternacional = new Internacional(codigo,Origen,Destino,totalAsientos,tripulantes, fecha, 20,cantAsientos[0],cantAsientos[1],cantAsientos[2],valorRefrigerio, precios[0],precios[1], precios[2], listaEscalas, hayEscalas);
		
		//2)
		vuelos.put(codigo, nuevoInternacional);
		
		//3) y 4)
		
		//Se crean los asientos y se agregan al vuelo
		nuevoInternacional.registrarAsientosDisponibles(cantAsientos, precios);
		
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

	
	
	/** - 7 
	*  Dado el código del vuelo, devuelve un diccionario con los asientos aún disponibles para la venta 
	*  --> clave:  el número de asiento
	*  --> valor:  la sección a la que pertenecen los asientos.
	*  
	*  Buscamos el vuelo por su codigo en el diccionario de vuelos.
	*  Obtenemos todos los asientos libres de dicho vuelo. Dado que el vuelo nos devuelve un hashMap <Integer, Asiento>, 
	*  y lo que nos piden es un Map<Integer, Asiento.clase>, necesitamos armar otro diccionario que reemplace Asiento por Asiento.getClase().
	*/
	@Override
	public Map<Integer, String> asientosDisponibles(String codVuelo) {
		
		//Obtenemos el vuelo por su codigo
		Vuelo vuelo = vuelos.get(codVuelo);
		
		if(vuelo == null) throw new RuntimeException("asientosDisponibles: El codigo de vuelo no corresponde a ningun vuelo registrado.");
		
		//Obtenemos un hashMap de los asientos disponibles en el vuelo
		HashMap<Integer, Asiento> asientosDisponibles = vuelo.getAsientosDisponibles();
		
		//Generamos un nuevo diccionario vacio, el que retornaremos como resultado, que sera <Integer, Asiento.getClase()>
		Map<Integer, String> diccionarioNroAsientoSeccion = new HashMap<>();
		
		return numeroDeAsintoYSeccion(diccionarioNroAsientoSeccion, asientosDisponibles);
	}

	/*
	 * Dado un hashmap de asientos, retorna un Map con sus clases (economica, turista, primera).
	 * */
	public Map <Integer, String> numeroDeAsintoYSeccion(Map<Integer, String> diccionarioNroAsientoSeccion, HashMap<Integer, Asiento> asientosDisponibles)
	{
		//Generamos un iterador sobre todos los asientosDisponibles
		Iterator<Map.Entry<Integer, Asiento>> iterador = asientosDisponibles.entrySet().iterator();
		
		while (iterador.hasNext()) {
			Asiento asientoActual = (Asiento) iterador.next();
			
			//En el diccionario que vamos a devolver, guardamos el codigo del asiento y su clase
			diccionarioNroAsientoSeccion.put(asientoActual.getCodigo(), asientoActual.getClase());
		}
		
		return diccionarioNroAsientoSeccion;
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
		intInvalidoCero(dni, "DNI"); 
		
		intInvalidoCero(nroAsiento, "Numero de asiento"); 
		
		stringInvalido(codVuelo, "Codigo de vuelo");
		
		//Nos pasaron el dni como int asi que lo convertimos en Integer, porque el DNI es una clave en los diccionarios que usamos, y como tal necesita ser Integer.
		Integer Dni = dni;
		
		return validarCliente(Dni, codVuelo, nroAsiento, aOcupar);
	}
	
	private int validarCliente(Integer dni, String codVuelo, int nroAsiento, boolean aOcupar) 
	{
		//Busco el cliente y verifico si existe
		Cliente pasajero = clientes.get(dni);
		
		//Si el cliente no existe, lanzo excepcion
		if(pasajero == null) throw new RuntimeException("venderPasaje: El DNI provisto no pertenece a un cliente registrado.");
		
		return verificarVuelo(pasajero, codVuelo, nroAsiento, aOcupar);
	}
	
	private int verificarVuelo(Cliente pasajero, String codVuelo, int nroAsiento, boolean aOcupar) 
	{
		//Busco el vuelo y verifico si esta registrado en la aerolinea. 
		Vuelo vuelo = vuelos.get(codVuelo);
		
		//Si no esta registrado en la aeolina lanzo una excepcion.
		if(vuelo == null) throw new RuntimeException("venderPasaje: El codigo de vuelo provisto no pertenece a un vuelo registrado.");
		
		return  venderAsiento(pasajero, vuelo, nroAsiento, aOcupar);
	}
	
	private int venderAsiento(Cliente pasajero, Vuelo vuelo, int nroAsiento, boolean aOcupar) 
	{
		//Busco el asiento disponible dentro del vuelo. El vuelo sabe que si el asiento que busco no es disponible, tira una excepcion.
		Asiento asiento = vuelo.getAsientoDisponible(nroAsiento);
 
		/*
		 * Importante: En nuestro diseño que fue aprobado, no habia un "codigo de pasaje". Cada pasajero es ubicado por su DNI en su vuelo, y cada 
		 * asiento es ubicado por su numero, que debe ser unico por vuelo. Entonces decidimos que el numero de pasaje sera solo un numero, unico
		 * en toda la aerolinea, asociado al asiento. De esta manera, el asiento tiene un numero unico por vuelo (su codigo de asiento) y un numero
		 * unico en toda la aerolinea en general (su codigo de pasaje). 
		 * */
		
		//Genero un codigo unico en toda la aerolinea para el pasaje.
		int codigoPasaje = obtenerCodigo();
		
		//Le asigno al asiento si esta ocupado o no
		asiento.setOcupado(aOcupar);
		
		//Le digo al vuelo que venda el asiento. Si todo salio bien, se le asigna el numero de pasaje al asiento y este es retornado 
		return vuelo.registrarAsiento(pasajero, asiento, codigoPasaje);
		
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
		
		/* !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
		 * 
		 * 
		 * 
		 * 
		 *          NO EXISTE MAS ASIENTOS DISPONIBLES POR VUELO A NIVEL BONDIJET
		 * 
		 * 
		 * 
		 * 
		 * !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
		 * */
		//asientosDisponiblesPorVuelo.get(codVuelo).put(nroAsiento, asiento);
		
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
		
		Integer pasaje = codPasaje;
		/* Como estoy cancelando un pasaje, ni bien encuentre al cliente que lo tiene, invoco a eliminarPasaje.
		 * Si recorro todo y no encuentro al cliente, bueno, el pasaje ya estaba eliminado.*/
		while (it.hasNext()) {
			
			Vuelo vueloActual = (Vuelo) it.next();

			//Si encuentro al cliente, le elimino el pasaje y termino.  
			if(vueloActual.getPasajero(dni) != null) vueloActual.eliminarPasaje(dni, pasaje);
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
	
}
