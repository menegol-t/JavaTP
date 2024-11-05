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
		//Che hay que convertir todas estas validaciones en una funcion...
		if(!(nombre != null && nombre.length() > 0 && cuit != null && cuit.length() > 0))
			throw new RuntimeException("Valor de parametros invalido!!");
			
			this.nombre = nombre;
			this.cuit = cuit;
			this.vuelos = new HashMap<>();
			this.aeropuertos = new HashMap<>();
			this.clientes = new HashMap<>();
			this.asientosDisponiblesPorVuelo = new HashMap<>();
			this.codigoBase = 1;
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
			throw new RuntimeException("obtenerFecha: La fecha es invalida, favor de proveer una fecha en formato 'dd/mm/aaaa'.");
		}
		
	}

	/*
	 * Con esta funcion detectamos si un numero es invalido, es decir si vale 0 o vale negativo.
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
				nuevo = new Aeropuerto(nombre, provincia, direccion, pais, esNacional);	
			}
			catch(Exception Exception) {
//IMPORTANTE: Acordarse que TODAS las excepciones ahora son RuntimeException
				 Exception.printStackTrace();
			}
			aeropuertos.put(direccion,nuevo);
		}
		else
		{
			try {
				nuevo = new Aeropuerto(nombre, provincia, direccion, pais, esNacional);
			}
			catch(Exception Exception) {
				Exception.printStackTrace();
			}
			aeropuertos.put(direccion,nuevo);
		}
	}
	

	
	@Override
	public String registrarVueloPublicoNacional(String origen, String destino, String fecha, int tripulantes,
			double valorRefrigerio, double[] precios, int[] cantAsientos) 
	{
		
		/*Tenemos que: 1) crear un codigo Nacional y un Nacional
		 * 			   2) agregarlo a la lista de vuelos (polimorfismo),
		  			   3) Crear los asientos y asignarles un precio
		 			   4) almacenar <codigo, asientosLibres>
		 			   5) retornar el codigo
		*/
		
		//1)
		Integer parteNumerica = obtenerCodigo();
		StringBuilder cod = new StringBuilder();
		cod.append(parteNumerica);
		cod.append("-NAC");
		
		String codigo = cod.toString();
		
		Aeropuerto Origen = aeropuertos.get(origen);
		Aeropuerto Destino = aeropuertos.get(destino);
		
		
		int totalAsientos = cantAsientos[0] + cantAsientos[1]; //pendiente de para que lo necesitamos
		
		//lista vacia de pasajeros, total despues se iran agregando cuando se venda el pasaje
		HashMap<Integer, Pasajero> pasajerosVuelo = new HashMap<>();
		
		Nacional nuevoNacional = new Nacional(codigo,Origen,Destino,totalAsientos,tripulantes,pasajerosVuelo, fecha, 
											  20, clientes,cantAsientos[0],cantAsientos[1], valorRefrigerio, precios[0],precios[1]);
		
		//2)
		vuelos.put(codigo, nuevoNacional);
		
		//3) y 4)
		for(int i = 0; i < cantAsientos.length; i++)
		{
			int contador = 0;
			for(int j = 0; j<cantAsientos[i]; j++)
			{
				if(i == 0)
				{
					contador += 1;
					Asiento asiento = new Asiento(contador, 1, precios[i], "Economica", false);
					asientosDisponiblesPorVuelo.get(destino).put(asiento.getCodigo(), asiento);
				}
				
				else
				{
					contador += 1;
					Asiento asiento = new Asiento(contador, 2, precios[i], "Primera Clase", false);
					asientosDisponiblesPorVuelo.get(destino).put(asiento.getCodigo(), asiento);
				}
			}		
		}
		
		//5)
		return codigo;
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

	public List<String> verificarVuelosSimilares(List <String> codVuelosSimilares, String origen, String destino, LocalDate fechaAComparar)
	/*
	 * Añade a la lista los vuelos que cumplan con mismo destino, origen y estar a una semana de la fecha.
	 * */
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
	
	private boolean vueloEsSimilar(Vuelo vuelo, String origen, String destino, LocalDate fechaAComparar) 
	/*
	 * Verifica cada vuelo individual para ver si cumple con las condiciones (que tenga el mismo origen, mismo destino, 
	 * y que su fecha de salida este a maximo 1 semana de la fecha a comparar). Si esto se da, retorna true. 
	 * */
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
	 * Actualizacion (Leo):
	 * 
	 * Ahora los asientos conocen sus precios, cuando se registra un vuelo Nacional o Internacional se le asigna
	 * a cada asiento de cada seccion se le agrega el precio correspondiente, con ello se podra restar la diferencia a la recaudacion
	 * 
	 *  - Hay una funcion en la clase vuelo que realiza la logica de eliminar el asiento, de este modo el coedigo es mas limpio
	 *  
	 * 1) Conseguir el vuelo con el codigo desde la variable local "vuelos"
	 * 2) Acceder al pasajero con el dni en el diccionario pasajeros
	 * 3) Guardar el asiento y setearlo para reutilizarlo
	 * 4) Borrar el asiento 
	 * 5) Agregar el asiento en el diccionario "AsientosDisponiblesPorVuelo" con el codigo de vuelo
	 * 6) Restar el precio del asiento al total por destino en el diccionario "facturacionPorDestino", utilizando el destino guardado
	 * */
	
	{
		
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
		
		/* Tom: Acordate que el "codigo" unico de cada aeropuerto es su nombre.
		 * Podrias hacer solo un:
		 * String destino = vuelo.getDestino().getNombre(); 
		 * 
		 * Leo: No se, el destino es necesario para la resta del precio del pasaje,
		 * el diccionario facturacionPorDestino tiene como key el destino, no el nombre del aeropuerto
		 * 
		 * Entiendo que no dira Aeroparque --> 1000, sera algo como, Buenos aires, --> 1000 
		 * 
		 */
		String destino = aeropuertoDestino.getLocacion();
		asientosDisponiblesPorVuelo.get(codVuelo).put(nroAsiento, asiento);
		
		//6)
		double precioAsiento = asiento.getPrecio();
		double facturado = facturacionPorDestino.get(destino);
		
		/*
		 * TOM: Aca le estas restando a facturacionPorDestino el precio del pasaje ANTES de sacarle el asiento 
		 * 
		 * LEO: Ahora se resta DESPUES de eliminar el asiento
		 * */
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
		
		//Genero una variable para no tener que recorrer absolutamente todos los vuelos una vez encontrado el cliente. 
		boolean buscando = true;
		
		/* Como estoy cancelando un pasaje, ni bien encuentre al cliente que lo tiene, invoco a eliminarPasaje.
		 * Si recorro todo y no encuentro al cliente, bueno, el pasaje ya estaba eliminado.*/
		while (it.hasNext() && buscando) {
			
			Vuelo vueloActual = (Vuelo) it.next();

			//Si encuentro al cliente, le elimino el pasaje y termino.  
			if(vueloActual.getPasajero(dni) != null) 
			{
				vueloActual.eliminarPasaje(dni, codPasaje);
				
				buscando = false;
			}
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
