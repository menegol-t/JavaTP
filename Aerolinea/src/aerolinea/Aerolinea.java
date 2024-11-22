package aerolinea;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
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
		stringInvalido(nombre, "Nombre aerolonea");
		stringInvalido(cuit, "CUIT"); 

		this.nombre = nombre;
		this.cuit = cuit;
		this.vuelos = new HashMap<String, Vuelo>();
		this.aeropuertos = new HashMap<String, Aeropuerto>();
		this.clientes = new HashMap<Integer, Cliente>();
		this.facturacionPorDestino  = new HashMap<String, Double>();	
		this.codigoBase = 100;
	}
	

	/*
	 * Con esta funcion genero IDs unicos en todo el codigo, solo voy sumando un int.
	 * */
	private Integer obtenerCodigo()
	{
		codigoBase = codigoBase + 1;
		
		return codigoBase;
	}

	private String obtenerCodigoPublico()
	{
		return obtenerCodigo() + "-PUB";
	}
	
	private String obtenerCodigoPrivado()
	{
		return obtenerCodigo() + "-PRI";
	}
	
	/*
	 * Si encuentra el aeropuerto buscandolo por su nombre, lo retorna. Si no lo encuentra, tira excepcion
	 * */
	private Aeropuerto getAeropuerto(String nombre)
	{	
		Aeropuerto aeropuerto = aeropuertos.get(nombre);
		
		if(aeropuerto == null) throw new RuntimeException("getAeropuerto: El nombre dado no corresponde a ningun aeropuerto registrado.");
		
		return aeropuerto;
	}
	
	/*
	 * Si encuentra el cliente buscandolo por su dni, lo retorna. Si no lo encuentra, tira excepcion
	 * */
	private Cliente getCliente(int dni) 
	{
		Cliente cliente = clientes.get(dni);
		
		if(cliente == null) throw new RuntimeException("getCliente: El cliente solicitado no esta registrado en la compañia");
		
		return cliente;
	}
	
	/*
	 * Si encuentra el vuelo buscandolo por su codigo, lo retorna. Si no lo encuentra, tira excepcion
	 * */
	private Vuelo getVuelo(String codVuelo) 
	{
		Vuelo vuelo = vuelos.get(codVuelo);
		
		if(vuelo == null) throw new RuntimeException("getVuelo: El vuelo solicitado no esta registrado en el aeropuerto");
		
		return vuelo;
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
	 * Con esta funcion valido si me pasaron un String vacio y tiro un error
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
		
		Cliente clienteYaRegistrado = clientes.get(Dni);
		
		//Si el cliente existia en clientes, tiro runtimeException
		if(clienteYaRegistrado != null) throw new RuntimeException("registrarCliente: El cliente ya estaba registrado. ");
		
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
		Aeropuerto aeropuertoYaRegistrado = aeropuertos.get(nombre);
		
		//Si el aeropuerto existia en clientes, tiro runtimeException
		if(aeropuertoYaRegistrado != null) throw new RuntimeException("registrarAeropuerto: El aeropuerto ya estaba registrado.");
		
		//Creo el nuevo aeropuerto y lo guardo en el diccionario aeropuertos. Si los datos del aeropurto son invalidos, el constructor del mismo tira excepcion. 
		aeropuertos.put(nombre, new Aeropuerto(nombre, pais, estado, direccion));
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
	* IREP: NO PUEDE HABER MAS ASIENTOS QUE TRIPULANTES?????? En el diseño original no habia valor de refrigerio. 
	* 
	* Primero verificamos que los aeropuertos esten registrados en la compañia.
	* Posterior, generamos el nuevo vuelo. El constructor de este debe tirar runtimeException si algun dato es invalido (lso tripulantes no pueden ser 0, etc)
	* Si todo esta bien, retornamos el codigo guardado. 
	*/
	@Override
	public String registrarVueloPublicoNacional(String origen, String destino, String fecha, int tripulantes, double valorRefrigerio, double[] precios, int[] cantAsientos) 
	{
		//Creamos un codigo
		String codigo = obtenerCodigoPublico();
		
		//Obtengo los aeropuertos de origen y destino por su nombre. Si no estaban registrados, tira una excepcion.
		Aeropuerto Origen = getAeropuerto(origen);
		Aeropuerto Destino = getAeropuerto(destino);
		
		//Calculo del total de asientos entre la primera y la segunda clase
		int totalAsientos = cantAsientos[0] + cantAsientos[1];
		
		//Mando a generar el vuelo nacional, y a registrar todos sus asientos.
		return registrarDatosNacional(codigo, Origen, Destino, totalAsientos, tripulantes, fecha, valorRefrigerio, cantAsientos, precios);
	}
	
	/*
	 * Dados los datos necesarios, genera un vuelo publico nacional, registra sus asientos disponibles y guarda dicho vuelo en el diccionario vuelos de Aerolinea.
	 * */
	private String registrarDatosNacional(String codigo, Aeropuerto origen, Aeropuerto destino, int totalAsientos, int totalTripulantes, String fechaSalida, double valorRefrigerio, int[] asientos, double[] precios) 
	{
		//Generamos nuevo vuelo nacional. Si algun dato es incorrecto, tira runtimeException.
		Nacional nuevoNacional = new Nacional(codigo, origen, destino, totalAsientos, totalTripulantes, fechaSalida, valorRefrigerio);
		
		//Asignamos sus asienos al nuevo vuelo
		nuevoNacional.registrarAsientosDisponibles(asientos, precios);
		
		//Ponemos el nuevo vuelo en el diccionario de vuelos
		vuelos.put(codigo, nuevoNacional);
		
		//Para retornar el codigo, buscamos al nuevoNacional en el diccionario de vuelos donde lo acabamos de registrar, y devolvemos su codigo. Con esto validamos que se guardo bien. 
		return getVuelo(codigo).getCodigo();
	}

	
	
	/* Pueden ser vuelos con escalas o sin escalas. 
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
	 *IREP: HAY QUE VALIDAR QUE LA FECHA DE SALIDA SEA POSTERIOR A LA ACTUAL???? GUATAFAK
	 *
	 * Devuelve el código del vuelo.  Con el formato: {Nro_vuelo_publico}-PUB, por ejemplo--> 103-PUB
	 * Si al validar los datos no se puede registrar, se deberá lanzar una excepción.
	 * 
	 * Para esto, primero verificamos que los aeropuertos que nos pasaron existan en la compañia. 
	 * Posterior, verificamos que todas las escalas que nos pasaron son aeropuertos validos. Para esto, convertimos
	 * el array de nombres de aeropuertos en un diccionario de aeropuertos. Si algun aeropuerto no se encuentra, 
	 * toda la funcion tira runtimeException. Una vez realizado el diccionario, se llama al constructor del vuelo,
	 * y registramos todos sus asientos. Por ultimo, retornamos el codigo del vuelo. 
	 */
	@Override
	public String registrarVueloPublicoInternacional(String origen, String destino, String fecha, int tripulantes, double valorRefrigerio, int cantRefrigerios, double[] precios, int[] cantAsientos, String[] escalas) 
	{		
		 //Creamos el codigo del vuelo, unico en toda la aerolinea. 
		String codigo = obtenerCodigoPublico();
		
		//Obtengo los aeropuertos de origen y destino por su nombre. Si no estaban registrados, tira una excepcion.
		Aeropuerto Origen = getAeropuerto(origen);
		Aeropuerto Destino = getAeropuerto(destino);
		
		//Calculo total de asientos entre la primera y la segunda clase.
		int totalAsientos = cantAsientos[0] + cantAsientos[1] + cantAsientos[2]; 
		
		//Obtengo un hashMap de todas las escalas
		HashMap<String, Aeropuerto> Escalas = verificarEscalas(escalas);
		
		return registrarDatosInternacional(codigo, Origen, Destino, totalAsientos, tripulantes, fecha, valorRefrigerio, cantRefrigerios, precios, cantAsientos, Escalas);
	}
	
	/*
	 * Verifica que las escalas sean validas antes de pasarlas al vuelo.
	 * */
	private HashMap<String, Aeropuerto> verificarEscalas(String[] aeropuertos) 
	{
		//Realizamos un nuevo hashMap para guardar las escalas
		HashMap<String, Aeropuerto> escalas = new HashMap<>(); 
		
		//Si el vuelo que nos mandaron a crear siquiera TIENE escalas, buscamos el nombre de los aeropuertos uno por uno, y metemos los aeropuertos en el diccionario de escalas del vuelo
		if(aeropuertos.length > 0) 
		{
			for(String nombre: aeropuertos) 
			{
				Aeropuerto aeropuerto = getAeropuerto(nombre);
				
				escalas.put(nombre, aeropuerto);
			}
		}//Es posible que nos pidan hacer un vuelo internacional SIN escalas, en cuyo caso mandamos el diccionario "escalas" va vacio al vuelo. 
		
		return escalas;
	}
	
	/*
	 * Dados todos los datos necesarios, genera un vuelo publico internacional, registra sus asientos disponibles y guarda dicho vuelo en el diccionario vuelos de Aerolinea.
	 * */
	private String registrarDatosInternacional(String codigo, Aeropuerto origen, Aeropuerto destino, int totalAsientos, int tripulantes, String fecha, double valorRefrigerio, int cantRefrigerios, double[] precios, int[] cantAsientos, HashMap<String, Aeropuerto> escalas) 
	{
		//Generamos un nuevo vuelo internacional. Si algun dato es incorrecto, el constructor tira runtimeException
		Internacional nuevoInternacional = new Internacional(codigo, origen, destino, totalAsientos, tripulantes, fecha, cantRefrigerios, valorRefrigerio, escalas);
		
		//Registramos todos los asientos del vuelo internacional
		nuevoInternacional.registrarAsientosDisponibles(cantAsientos, precios);
		
		//Guardamos el vuelo en el diccionario de vuelos
		vuelos.put(codigo, nuevoInternacional);
		
		//Para retornar el codigo, buscamos al nuevoInternacional en el diccionario de vuelos donde lo acabamos de registrar, y devolvemos su codigo. Con esto validamos que se guardo bien. 
		return getVuelo(codigo).getCodigo();
	}
	
	
	
	/** 6 y 10 **** Se reune en esta firma ambos puntos de la especificación.
	 * 
	* Origen y destino son los Aeropuertos de donde parte y al que llega el jet. 
	* Fecha es la fecha de salida y debe ser posterior a la fecha actual.
	* Tripulantes es la cantidad de tripulantes del vuelo. 
	* Precio es el de un(1) jet. 
	* Se supone que se cuenta con todos los jets necesarios para trasladar todos los acompañantes. 
	* Se usara la cantidad de jets (necesarios) para el calculo del costo total del Vuelo.
	* IMPORTANTE; Se toma un sólo código para la compra aunque se necesiten mas de un jet. 
	* No se sirven refrigerios
	* 
	* Devuelve el código del vuelo. Con el formato: {Nro_vuelo_privado}-PRI, por ejemplo: 103-PRI
	* 
	* Respecto a lo que decia la etapa de diseño, no nos pasan los datos para registrar Pasajeros realmente. 
	*/
	@Override
	public String VenderVueloPrivado(String origen, String destino, String fecha, int tripulantes, double precio, int dniComprador, int[] acompaniantes) 
	{
		//Generamos el codigo para el vuelo
		String codigo = obtenerCodigoPrivado();
		
		//Obtenemos los aeropuertos de origen y destino. Si son incorrectos, tira runtimeexception
		Aeropuerto Origen = getAeropuerto(origen);
		Aeropuerto Destino = getAeropuerto(destino);
		
		return verificarPasajerosPrivado(codigo, Origen, Destino, fecha, tripulantes, precio, dniComprador, acompaniantes);
	}

	/*
	 * Verifica que el comprador sea un cliente ya registrado, asi como sus acompaniantes antes de generar el vuelo
	 * */
	private String verificarPasajerosPrivado(String codigo, Aeropuerto origen, Aeropuerto destino, String fecha, int tripulantes, double precio, int dniComprador, int[] acompaniantes) {
		
		//El total de asientos son los acompañantes mas el comprador
		int totalAsientos = acompaniantes.length + 1;
		
		//Obtengo el comprador como cliente. Si no estaba registrado en la aerolina, patea
		Cliente comprador = getCliente(dniComprador);
		
		//Buscamos a todos los acompaniantes como clientes registrados .Si alguno no esta registrado, tira excepcion 
		ArrayList<Cliente> pasajeros = validarAcompaniantes(acompaniantes);
		
		return registrarDatosPrivado(codigo, origen, destino, fecha, totalAsientos, tripulantes, precio, comprador, pasajeros, acompaniantes);
	}

	/*
	 * Dado un array de dnis, devuelve un array de clientes. Si algun dni no corresponde a un clinete, tira exception
	 * */
	private ArrayList<Cliente> validarAcompaniantes(int[] acompaniantes)
	{	
		//Generamos un array list de los clientes a registrar
		ArrayList<Cliente> pasajeros = new ArrayList<>();
		
		for(int i = 0; i<acompaniantes.length; i++)
		{
			//Buscamos al pasajero puntiual como cliente
			Cliente pasajeroActual = getCliente(acompaniantes[i]);
			
			//Si no, lo añado a la lista de pasajeros
			pasajeros.add(pasajeroActual);
		}
		
		return pasajeros;
	}
	
	/*
	 * Dados todos los datos necesarios, crea un nuevo vuelo privado, genera todos sus asientos disponibles, asigna y pone el vuelo en el diccionario de vuelos.
	 */
	private String registrarDatosPrivado(String codigo, Aeropuerto origen, Aeropuerto destino, String fecha, int totalAsientos, int tripulantes, double precio, Cliente comprador, ArrayList<Cliente> pasajeros, int[] acompaniantes) 
	{
		//Genero un nuevo vuelo privado
		Privado nuevoPrivado = new Privado(codigo, origen, destino, totalAsientos, tripulantes, fecha, 30, precio, comprador);
		
		//Registra la cantidad de asientos disponibles segun la cantidad de acompañantes. Como el precio del vuelo privado es por vuelo y no por asiento, como precio envio 0
		nuevoPrivado.registrarAsientosDisponibles(acompaniantes, new double[] {0.0});
		
		return registrarPasajerosPrivado(comprador, pasajeros, nuevoPrivado);
	}
	
	/*
	 * Dado el cliente comprador, el array de clientes passajeros y un vuelo privado, vende todos los asientos del vuelo privado
	 * */
	private String registrarPasajerosPrivado(Cliente comprador, ArrayList<Cliente> pasajeros, Privado nuevoPrivado) 
	{		
		//Le asigna a cada pasajero y al comprador sus asientos
		nuevoPrivado.registrarPasajeros(pasajeros, comprador);
		
		//Calculo el precio total del vuelo privado
		double precioPrivado = nuevoPrivado.getPrecio();
		
		//Pongo el vuelo privado en el diccionario de vuelos
		vuelos.put(nuevoPrivado.getCodigo(), nuevoPrivado);
		
		//Actualizo la facturacion, sumando el precio de este nuevo vuelo a facturacionPorDestino
		agregarFacturacion(0.0, precioPrivado, nuevoPrivado);
		
		//Devuelvo el codigo del vuelo privado, asegurandome que se haya guardado bien en el diccionario. Si no se guardo bien, getVuelo explota
		return getVuelo(nuevoPrivado.getCodigo()).getCodigo();
	}
	
	
	/** - 7 
	*  Dado el código del vuelo, devuelve un diccionario con los asientos aún disponibles para la venta 
	*  --> clave:  el número de asiento
	*  --> valor:  la sección a la que pertenecen los asientos.
	*  
	*  Buscamos el vuelo por su codigo en el diccionario de vuelos.
	*  Obtenemos todos los asientos libres de dicho vuelo. Dado que el vuelo nos devuelve un hashMap <Integer, Asiento>, 
	*  y lo que nos piden es un Map<Integer, Asiento.clase>, necesitamos armar otro diccionario que reemplace Asiento por Asiento.getSeccion().
	*/
	@Override
	public Map<Integer, String> asientosDisponibles(String codVuelo) {
		
		//Obtenemos el vuelo por su codigo
		Vuelo vuelo = getVuelo(codVuelo);
		
		
		//Obtenemos un hashMap de los asientos disponibles en el vuelo
		ArrayList<Asiento> asientosDisponibles = vuelo.getAsientosDisponibles();
		
		//Generamos un nuevo diccionario vacio, el que retornaremos como resultado, que sera <Integer, Asiento.getClase()>
		Map<Integer, String> diccionarioNroAsientoSeccion = new HashMap<>();
		
		return numeroDeAsientoYSeccion(diccionarioNroAsientoSeccion, asientosDisponibles);
	}

	/*
	 * Dado un hashmap <Integer, Asiento>, retorna un Map <Integer, asiento.getSeccion()>
	 * */
	private Map <Integer, String> numeroDeAsientoYSeccion(Map<Integer, String> diccionarioNroAsientoSeccion, ArrayList<Asiento> asientosDisponibles)
	{
		
		for(Asiento asientoActual: asientosDisponibles) {
			
			//En el diccionario que vamos a devolver, guardamos el codigo del asiento y su clase
			diccionarioNroAsientoSeccion.put(asientoActual.getCodigo(), asientoActual.getSeccion());
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
		
		return validarCliente(dni, codVuelo, nroAsiento, aOcupar);
	}
	
	/*
	 * Valida si el cliente existen en la aerolinea antes de vender el asiento. 
	 * */
	private int validarCliente(int dni, String codVuelo, int nroAsiento, boolean aOcupar) 
	{
		//Busco el cliente y verifico si existe en la aerolinea
		Cliente cliente = getCliente(dni);
		
		//Busco el vuelo y verifico si esta registrado en la aerolinea. 
		Vuelo vuelo = getVuelo(codVuelo);
		
		//Genero un codigo unico en toda la aerolinea para el pasaje.
		int codigoPasaje = obtenerCodigo();  
		
		return venderPasaje(cliente, vuelo, nroAsiento, aOcupar, codigoPasaje);	
	}
	
	/*
	 *Dado un cliente, un vuelo y los datos de un asiento, se encarga de vender el asiento al cliente.
	 * Esto significa, si el cliente ya era un pasajero del vuelo y solo esta comprando un asiento mas, solo se le suma ese asiento
	 * Si el cliente es nuevo en el vuelo, se lo guarda como pasajero con su nuevo asiento. 
	 */
	private int venderPasaje(Cliente cliente, Vuelo vuelo, int nroAsiento, boolean aOcupar, int codigoPasaje) 
	{
		//Conseguimos la recaudacion antes de vender el pasaje
		double antes = vuelo.getPrecio();
		
		//Realizamos la venta, lo que devuelve el codigo de pasaje si se asigno bien el asiento.
		int codPasaje = vuelo.venderPasaje(cliente, nroAsiento, aOcupar, codigoPasaje);
		
		//Conseguimos la recaudacion despues de vender el pasaje
		double despues = vuelo.getPrecio();
		
		//Actualizamos la facturacion
		agregarFacturacion(antes, despues, vuelo);
		
		//Retornamos el codigo de pasaje obtenido de la venta.
		return codPasaje;
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
	private List<String> verificarVuelosSimilares(List <String> codVuelosSimilares, String origen, String destino, LocalDate fechaAComparar)
	{
		//Itero sobre todos los vuelos en el diccionario de vuelos, viendo si cumplen o no las condiciones. Si los cumplen, los añado a la lista a retornar.
		for(Vuelo vueloActual: vuelos.values()){
			
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
		if(vuelo.getDestino().getNombre().equals(destino) &&
			
				//Y el origen del vuelo es el mismo que el dado
				vuelo.getOrigen().getNombre().equals(origen) &&
					
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
	 * Busco el vuelo indicado.
	 * En el vuelo, busco al pasajero por su dni.
	 * Al pasajero le digo que elimine su asiento: Busca el asiento por el numero, lo saca de su array de asientos, setOcupado(false), setPasaje(false) y lo retorna
	 * El vuelo recibe este retorno, y añade el asiento ahora liberado a su diccionario de asientos liberados.
	 * */
	@Override
	public void cancelarPasaje(int dni, String codVuelo, int nroAsiento) 	
	{
		//Valido que no me hayan pasado datos vacios
		intInvalidoCero(dni, "DNI"); stringInvalido(codVuelo, "Codigo de vuelo"); intInvalidoCero(nroAsiento, "Numero asiento");
		
		//Obtengo el vuelo
		Vuelo vuelo = getVuelo(codVuelo); //O(1)
		
		//mando a que el vuelo cancele el pasaje
		double costoPasajeCancelado = vuelo.cancelarPasaje(dni, nroAsiento); //O(1)
	
		//Actualizamos la facturacion a ese destino quitando el pasaje
		quitarFacturacion(costoPasajeCancelado, vuelo);
	}

	

	/** 12-B
	* Se cancela un pasaje dado el codigo de pasaje. 
	* NO es necesario que se resuelva en O(1).
	* 
	* Para cancelar un pasaje (asiento), debo:
	* Ubicar el vuelo del cliente, para eto recorro todos los vuelos a mano y les pregunto si lo contienen. 
	* Una vez encontrado, busco al cliente por su DNI dentro del vuelo.
	* Dentro del cliente, debo encontrar el pasaje, de vuelta recorriendo todos sus asientos a mano hasta encontrar el asiento que tiene el codigo que busco
	* Por ulimo, elimino ese asiento, ya que no nos pide que lo liberemos creo.  
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

			//Si encuentro al cliente, voy al vuelo a eliminar el pasaje  
			if(vueloActual.contienePasajero(dni)) {
				
				//Mando a eliminar el pasaje dentro del cliente. Esto me retorna el costo de ese pasaje
				double costoPasajeCancelado = vueloActual.eliminarPasaje(dni, pasaje);
			
				//Actualizamos la facturacion a ese destino (le resto el precio del pasaje que acabo de cancelar)
				quitarFacturacion(costoPasajeCancelado, vueloActual);
				
			}//Si no encontre al cliente, o sigo buscando, o el pasaje ya estaba eliminado.
		}
	}

	
	
	/** - 13
	* Cancela un vuelo completo conociendo su codigo.
	* Los pasajes se reprograman a vuelos con igual destino, no importa el numero del asiento pero 					
	* si a igual seccion o a una mejor, y no importan las escalas.			|En los vuelos similares, tengo que ver sus asientosDisponibles, y llamar a venderPasaje por numero de asiento a aquellos que tengan mejor asiento									
	* Devuelve los codigos de los pasajes que no se pudieron reprogramar.	|Devuelve los codigos de los pasajes?????									
	* Los pasajes no reprogramados se eliminan. Y se devuelven los datos de la cancelación, indicando 			
	* los pasajeros que se reprogramaron y a qué vuelo,  y los que se cancelaron por no tener lugar.			
	* Devuelve una lista de Strings con este formato : “dni - nombre - telefono - [Codigo nuevo vuelo|CANCELADO]”
	* 																												
	* --> Ejemplo: 
	*   . 11111111 - Juan - 33333333 - CANCELADO
	*   . 11234126 - Jonathan - 33333311 - 545-PUB
	* 
	* Hacemos un listado con todos los vuelos que tengan el mismo destino que el a cancelar.
	* Hacemos un listado con todos los pasajeros a reprogramar.
	* Generamos un listado vacio que vamos a ir rellenando con los pasajes que se pudieron o no reprogramar. 
	* Recordemos que un mismo pasajero puede tener multiples asientos, entonces capas se le pueden reprogramar
	* algunos pero otros no.
	* Nos metemos pasajero por pasajero, en todos sus asientos.
	* Nos metemos en todos los vuelos con destino similar, a todos sus asientos disponibles. 
	* Si el asiento disponible es de igual o mejor clase que el del pasajero, lo vendemos y sumamos eso a la lista.
	* Si no, entonces sumammos a la lista que ese pasaje particular no se pudo revender. 
	*/
	@Override
	public List<String> cancelarVuelo(String codVuelo) {
		
		//Busco el vuelo a cancelar
		Vuelo vuelo = getVuelo(codVuelo);
		
		//Genero un array list de todos los vuelos que tengan el mismo nombre de aeropuerto de destino
		ArrayList<Vuelo> vuelosDestinoSimilar = vuelosSimilaresPorDestino(vuelo); 
		
		//Busco todos los pasajeros del vuelo
		ArrayList<Pasajero> pasajeros = vuelo.getAllPasajeros();
		
		return buscarAsientosAReprogramar(vuelosDestinoSimilar, pasajeros);
	}
	
	/*
	 * Devuelve la lista completa de todos los pasajes reprogramados y todos los cancelados.
	 * Para esto, itera sobre todos los pasajeros del vuelo a cancelar, y dentro de estos, 
	 * itera sobre todos sus asientos.
	 * Le pasa un asiento por vez a buscarAsientosDisponibles
	 * */
	private List<String> buscarAsientosAReprogramar(ArrayList<Vuelo> vuelosDestinoSimilar, ArrayList<Pasajero> pasajeros)
	{
		//En este listado vamos a meter todos los datos que necesitemos
		List<String> listadoReprogramacion = new ArrayList<>();
		
		//Itero por todos los pasajeros a reprogramar
		for(Pasajero pasajeroAReprogramar: pasajeros) 
		{
			ArrayList<Asiento> asientosAReprogramar = pasajeroAReprogramar.getAsientos();
			
			//Itero por todos los asientos de los pasajeros
			for(Asiento asientoAReprogramar: asientosAReprogramar) 
			{
				buscarAsientosDisponibles(asientoAReprogramar, vuelosDestinoSimilar, pasajeroAReprogramar, listadoReprogramacion);
			}
		}
		
		return listadoReprogramacion;
	}
	
	/*
	 * Itera sobre todos los vuelos con destino similar al vuelo a cancelar. Dentro de cada vuelo, itera sobre sus asientos disponibles.
	 * Compara los asientos disponibles con un asiento dado, y si se cumple que el asiento disponible tiene clase 
	 * mejor o igual que el asiento dado, lo vende. Si no, cancela el pasaje.
	 * */
	private void buscarAsientosDisponibles(Asiento asientoAReprogramar, ArrayList<Vuelo> vuelosDestinoSimilar, Pasajero pasajeroAReprogramar, List<String> listadoReprogramacion)
	{
		//Itero por todos los vuelos con el mismo destino que el vuelo a cancelar
		for(Vuelo vueloActual: vuelosDestinoSimilar) 
		{
			//Busco los asientos disponibles del vuelo actual. 
			ArrayList<Asiento> asientosDisponibles = vueloActual.getAsientosDisponibles();
			
			//Itero todos los asientos disponibles que tienen los vuelos con el mismo destino
			for(Asiento asientoDisponible: asientosDisponibles) 
			{
				//Si encuentro un asiento disponible, de igual o mejor seccion que el que tengo que reprogramar, lo vendo.
				if(esAceptable(asientoAReprogramar.getSeccion(), asientoDisponible.getSeccion())) 
				{
					//Vende el asiento y lo suma al listadoReprogramacion
					reprogramarPasaje(asientoAReprogramar, asientoDisponible, pasajeroAReprogramar, vueloActual.getCodigo(), listadoReprogramacion);
					
					//Una vez vendi el asiento corto el ciclo para que me brindern el siguiente asientoAReprogramar
					return;
				}//Si no vendi el asiento porque no era aceptable, sigo buscando asientosDisponibles que cumplan la condicion
			}
		}
		
		//Si busque en todos los vuelos y nunca encontre un asientoDisponible que asignarle al pasajero a reprogramar, sumo el asiento a la lista de cancelados
		decartarPasaje(pasajeroAReprogramar, asientoAReprogramar, listadoReprogramacion);
	}
	
	/*
	 * Llama a eliminar el pasaje adentro del vuelo a cancelar, y añade el pasaje a la lista de pasajes cancelados. 
	 * */
	private void decartarPasaje(Pasajero pasajeroAReprogramar, Asiento asientoAReprogramar, List<String> listadoReprogramacion) 
	{
		//Genero los datos del pasaje que no pude reprogramar
		String datosPasajero = pasajeroAReprogramar.getCliente().toString() + " - " + "CANCELADO";
		
		/*
		 * Puede darse, que para un mismo pasajero, algunos asientos se pudieron reprogramar y otros no. 
		 * Habria que indicar el numero de pasaje para saber cual es cual.
		 * Para esto, habria que sumar lo siguiente a datosPasajero
		 * */ 
		
		//Sumo estos datos al lsitado de reprogramaciones
		listadoReprogramacion.add(datosPasajero);
		
		//Llamo a cancelar passaje, que se va a encargar de destruir el pasaje en el vuelo a cancelar. No se pq hacemos esto si total al vuelo lo vamos a destruir pero bueno ponele onda
		cancelarPasaje(pasajeroAReprogramar.getDniCliente(), asientoAReprogramar.getCodPasaje());
	}
	
	/*
	 * Dado un numero de asiento disponible y un codigo de vuelo, vende un pasaje, y lo suma al listado de pasajes reprogramados.
	 * */
	private void reprogramarPasaje(Asiento asientoAReprogramar, Asiento asientoDisponible, Pasajero pasajeroAReprogramar, String codVuelo, List<String> listadoReprogramacion)
	{
		//Mando a vender el pasaje con todos los datos del cliente, el vuelo al que debe ir y los datos del asiento
		venderPasaje(pasajeroAReprogramar.getDniCliente(), codVuelo, asientoDisponible.getCodigo(), asientoAReprogramar.getOcupado());
		
		//Guardo la informacion del pasajero que fue reprogramado como string
		String datosPasajero = pasajeroAReprogramar.getCliente().toString() + " - " + codVuelo;
		
		//Sumo la informacion del pasajero a la lista que indica el estatus de todos los pasajeros
		listadoReprogramacion.add(datosPasajero);
	}
	
	/*
	 * Dados dos asientos, determina si la clase de uno es igual o mejor a la clase del otro. 
	 * */
	private boolean esAceptable(String seccionDelPasajeroAReprogramar, String seccionDisponible) 
	{
		/*
		 * Por un lado, el pasajero tenia un asiento a reprogramar, el cual tenia una cierta seccion.
		 * El nuevo asiento que le estamos consiguiendo tiene otra seccion. 
		 * Si el asiento a reprogramar era de clase "Ejecutivo", pero el nuevo asiento es de clase 
		 * "Economica", entonces retorno false, porque ejecutivo es una clase mas alta que economica. 
		 * */
		
		//Si la seccion del pasajero antes era economica, sin importar el caso, un asiento en caulqueir seccion es aceptable
		if(seccionDelPasajeroAReprogramar.equals("Economica")) return true;
		
		//Si la seccion del pasajero era turista, solo seria aceptable que su nueva seccion sea o turista o ejecutivo
		if(seccionDelPasajeroAReprogramar.equals("Turista") && (seccionDisponible.equals("Turista") || seccionDisponible.equals("Ejecutivo"))) return true;
		
		//Si la seccion del pasajero era ejecutivo, solo seria aceptable que su nueva seccion sea ejecutivo
		if(seccionDelPasajeroAReprogramar.equals("Ejecutivo") && seccionDisponible.equals("Ejecutivo")) return true;
		
		return false;
	}

	/*
	 * Devuelve una lista de todos los vuelos cuyos destinos sean iguales al de un vuelo dado.
	 * */
	private ArrayList<Vuelo> vuelosSimilaresPorDestino(Vuelo vuelo) 
	{
		//Genero una lista donde voy a poner los codigos de todos los vuelos con el mismo destino
		ArrayList <Vuelo> codVuelosSimilares = new ArrayList<Vuelo>();
		
		//Itero sobre todos los vuelos de la aerolinea
		for(Vuelo vueloActual : vuelos.values()) {
			
			//Si el vuelo actual y el vuelo que me dieron tienen el mismo aerpuerto de destino (pero no son el mismo vuelo), sumo el vuelo actual a la lista 
			if(mismoDestino(vuelo, vueloActual) && ! vuelo.getCodigo().equals(vueloActual.getCodigo())) codVuelosSimilares.add(vueloActual);
		}
		return codVuelosSimilares;
	}
	
	/*
	 * Verifica si los destinos de dos vuelos son iguales (el nombre de su aeropuerto)
	 * */
	private boolean mismoDestino(Vuelo vueloReferencia, Vuelo vueloAComparar) 
	{
		//Obtengo el nombre del aeropuerto de destino
		String destinoReferencia = vueloReferencia.getDestino().getNombre();
		
		//Obtengo el nombre del vueloActual
		String destino = vueloAComparar.getDestino().getNombre();
		
		//Si son iguales, retorno true.
		if(destinoReferencia.equals(destino)) return true;
		
		return false;
	}
	
	
	
	/** - 14
	* devuelve el total recaudado por todos los viajes al destino pasado por parámetro. 
	* IMPORTANTE: Se debe resolver en O(1).
	*/
	@Override
	public double totalRecaudado(String destino) {
		
		Double facturacion = facturacionPorDestino.get(destino);
		
		if(facturacion == null) return 0;
		
		return facturacion;
	}

	/*
	 * Dado un vuelo y dos precios, calcula la diferencia entre ellos y agrega la diferencia
	 * (es decir, el valor de vender 1 pasaje) al diccionario de facturacionPorDestino.
	 * */
	private void agregarFacturacion(double antes, double despues, Vuelo vuelo)
	{
		//Conseguimos el destino
		String destino = vuelo.getDestino().getNombre();
		
		//Conseguimos la diferencia entre antes y despues de vender el pasaje 
		double diferencia = despues - antes;
		
		//Si el destin ya estaba registrado, sumamos el valor del pasaje recien vendido
		if(facturacionPorDestino.containsKey(destino))
		{
			
			//Conseguimos la factuacion actual
			double recaudacionActual = facturacionPorDestino.get(destino);
			
			//Actualizamos la facturacion, sumandole el precio del pasaje que se acaba de vender.
			recaudacionActual += diferencia;
			
			//Agregamos a facturacionPorDestino
			facturacionPorDestino.put(destino, recaudacionActual);
			
		}
		//Si es la primera vez que se compra un pasaje a ese destino, ponemos el precio de ese unico pasaje
		else
		{
			facturacionPorDestino.put(destino, diferencia);
		}	
	}
	
	/*
	 * Dado el precio de un pasaje cancelado y un vuelo, actualiza facturacionPorDestino
	 * quitandole el precio de ese pasaje que se cancelo.
	 * */
	private void quitarFacturacion(double costoPasjeCancelado, Vuelo vuelo)
	{
		//Conseguimos el destino
		String destino = vuelo.getDestino().getNombre();
		
		if(facturacionPorDestino.containsKey(destino)) 
		{
			//Obtengo la facturacion anterior
			double facturacionAnterior = facturacionPorDestino.get(destino);
			
			//Pongo la facturacion actualizada en facturacion por destino. 
			double facturacionActualizada = facturacionAnterior - costoPasjeCancelado;
			
			facturacionPorDestino.put(destino, facturacionActualizada);
		}
	}
	
	
	
	/** - 15 
	* Detalle de un vuelo
	* devuelve un texto con el detalle un vuelo en particular.
	* Formato del String: CodigoVuelo - Nombre Aeropuerto de salida - Nombre Aeropuerto de llegada - 
	*                     fecha de salida - [NACIONAL /INTERNACIONAL / PRIVADO + cantidad de jets necesarios].
	* --> Ejemplo:
	*   . 545-PUB - Bariloche - Jujuy - 10/11/2024 - NACIONAL
	*   . 103-PUB - Ezeiza  - Madrid -  15/11/2024 - INTERNACIONAL
	*   . 222-PRI - Ezeiza - Tierra del Fuego - 3/12/2024 - PRIVADO (3)
	*/
	@Override
	public String detalleDeVuelo(String codVuelo) {
		
		Vuelo vuelo = getVuelo(codVuelo);
		
		return vuelo.toString();
	}
	
	
	
	public String toString() 
	{
		StringBuilder st = new StringBuilder("Aerolinea: ");
		
		st.append(nombre);
		
		st.append(" - ");
		
		st.append(cuit);
		
		st.append("\n");
		
		for(Cliente clienteActual: clientes.values()) 
		{
			st.append(clienteActual.toString() + "\n");
		}
		
		for(Aeropuerto aeropuertoActual: aeropuertos.values()) 
		{
			st.append(aeropuertoActual.toString() + "\n");
		}
		
		for(Vuelo vueloActual: vuelos.values()) 
		{
			st.append(detalleDeVuelo(vueloActual.getCodigo()).toString() + "\n");
		}
		
		return st.toString();
	}
}
