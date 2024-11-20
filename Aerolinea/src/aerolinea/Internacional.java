package aerolinea;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Iterator;

public class Internacional extends Vuelo{

	private int refrigeriosPorPasajero;
	private double precioPorRefrigerio;
	private int pasajerosEconomica;
	private int pasajerosTurista;
	private int pasajerosEjecutivo;
	private int limitePasajerosEconomica;
	private int limitePasajerosTurista;
	private int limitePasajerosEjecutivo;
	private boolean vueloDirecto;
	private HashMap<String, Aeropuerto> escalas;
	
	
	public Internacional(String codigo, Aeropuerto origen, Aeropuerto destino, int totalAsientos, int totalTripulantes, String fechaSalida, int cantidadRefrigerios, double precioRefrigerio, HashMap <String, Aeropuerto> escalas)
	{
		//Generamos el vuelo
		super(codigo, origen, destino, totalAsientos, totalTripulantes, fechaSalida, 20);
		
		validarParametros(cantidadRefrigerios, precioRefrigerio, fechaSalida);
		
		this.refrigeriosPorPasajero = cantidadRefrigerios;
		this.precioPorRefrigerio = precioRefrigerio;
		
		//El numero de pasajeros empieza vacio, se van sumando conforme se suman pasajes.
		this.pasajerosEconomica = 0;
		this.pasajerosTurista = 0;
		this.pasajerosEjecutivo = 0;
		
		//El limite de pasajeros aparentemente no me lo pasan, solo lo sacamos nosotros??????
		this.limitePasajerosEconomica = 0;
		this.limitePasajerosTurista = 0;
		this.limitePasajerosEjecutivo = 0;
		
		this.escalas = escalas;
		
		//Si el diccionario de escalas mide "0", es un vuelo directo, porque no tiene escalas
		this.vueloDirecto = escalas.size() == 0;
		
	}
	
	//Validacion de la fecha segun indica la Interfaz
	
	private void compararFecha(String fecha) 
	{
		LocalDate fechaSalida = obtenerFecha(fecha);
		
		LocalDate fechaActual = LocalDate.now();
		
		if (fechaSalida.isBefore(fechaActual)) {
	        throw new RuntimeException("Vuelo: La fecha de salida no puede ser en el pasado");
	    }
	}
	
	private LocalDate obtenerFecha(String fecha) 
	{	
		DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd/MM/yyyy");
		
		try {
            LocalDate objetoFecha= LocalDate.parse(fecha, formato);
            return objetoFecha;
            
		}catch(Exception e){
			throw new RuntimeException("Vuelo: La fecha de salida es invalida, favor de proveer una fecha en formato 'dd/mm/aaaa'.");
		}	
	}
	
	
	private void validarParametros(int cantidadRefrigerios, double precioRefrigerio, String fecha) 
	{
		if(cantidadRefrigerios != 3) throw new RuntimeException("VueloPublico: La cantidad de refrigerios no puede ser negativa.");
		if(precioRefrigerio < 0) throw new RuntimeException("VueloPublico: El precio de los refrigerios no puede ser negativo.");
		compararFecha(fecha);
	}
	
	@Override
	public void registrarAsientosDisponibles(int[]cantAsientos, double[]precios)
	{	
		//Al registrar los asientos, registramos cual es el limite de asientos por clase, algo que solicitaba la etapa de diseño pero en la segunda etapa no se especifica. 
		limitePasajerosEconomica = cantAsientos[0];
		
		limitePasajerosTurista = cantAsientos[1];
		
		limitePasajerosEjecutivo = cantAsientos[2];
		
		//El contador sirve para numerar los asientos y darles un codigo unico en el vuelo
		int contador = 0;
		
		//Se recorre el array de la cantidad de secciones que habra en el vuelo: cantiAsientos: [Economica, Turista, Pimera]  
		for(int i = 0; i < cantAsientos.length; i++)
		{
			
			//En cada posicion del array de secciones, hay un array con los asientos de esa seccion. Aca se recorre ese sub array: cantiAsientos: [ [asiento0, asiento1, asiento2], [asiento3, asiento4, asiento6], [asiento7, asiento8] ]
			for(int j = 0; j<cantAsientos[i]; j++)
			{
				//incrementamos el numero de asiento
				contador ++;

				//Calculamos el pasaje con impuestos
				double precioFinal = precios[i] + precios[i] * super.getPorcentajeImpuesto()/100;
				
				Asiento asientoNuevo;
				
				if(i == 0) 
				{
					//Se crea un asiento de clase economica
					asientoNuevo = new Asiento(contador, precioFinal, "Economica");
				}
				
				if(i == 1) //Si estamos en la segunda seccion
				{
					//Se crea un asiento de clase turista
				    asientoNuevo = new Asiento(contador, precioFinal, "Turista");
				}
				
				else //Si estamos en la tercera seccion
				{	
					//Se crea un asiento de primera clase 
					asientoNuevo = new Asiento(contador, precioFinal, "Ejecutivo");	
				}
				
				//Agregamos el asiento al diccionario de asientos disponibles. 
				super.registrarAsientoDisponible(asientoNuevo);
			}
		}
	}


	public HashMap<String, Aeropuerto> getEscalas() {
		return escalas;
	}

	public boolean isVueloDirecto() {
		return vueloDirecto;
	}
	
	@Override
	public String toString() {
		return super.getCodigo()+ " - " + super.getOrigen().getNombre() + " - " + super.getDestino().getNombre() + " - " + super.getFechaSalida()+ " - " + "INTERNACIONAL";
	}

	@Override
	public double getPrecio() 

	/* Retorna la recaudacion total del vuelo
	 * 
	 * 1) recorremos pasajero a pasajero
	 * 2) sumar el precio de todos sus asientos + el precio de los refrigerios
	 * 3) sumar el procentaje de impuesto
	 * 4) retornar el total
	 * 
	 */
	
	
	{
		
		//Obtenemos los pasajeros
		HashMap<Integer, Pasajero> pasajeros = super.getPasajeros();
		
		//no hay pasajeros, no se recaudo nada
		if(pasajeros.size() == 0) return 0;
		
		double retorno = 0;
		
		//Calculamos el total por los refrigerios por pasajero
		double refrigerios = precioPorRefrigerio * refrigeriosPorPasajero;
		
		//Creamos el iterador, este es por valores, es decir por pasajeros
		Iterator<Pasajero> iterator = pasajeros.values().iterator();
		
		//Recorremos los pasajeros
		while(iterator.hasNext())
		{
			Pasajero pasajero = iterator.next();
			
			//Sumamos el costo total de los pasajes + impuestos
			retorno += pasajero.calcularCosto();
			
			retorno += refrigerios;
			
		}
	 
		
		//agregamos el porcentaje de impuesto
		retorno += (retorno * (super.getPorcentajeImpuesto() / 100));
		
		//retornamos
		return retorno;
		
	}
}
