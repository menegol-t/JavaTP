package aerolinea;

import java.util.HashMap;

public class Internacional extends Vuelo{

	private int refrigeriosPorPasajero;
	private double precioPorRefrigerio;
	private boolean vueloDirecto;
	private HashMap<String, Aeropuerto> escalas;
	
	
	public Internacional(String codigo, Aeropuerto origen, Aeropuerto destino, int totalAsientos, int totalTripulantes, String fechaSalida, int cantidadRefrigerios, double precioRefrigerio, HashMap <String, Aeropuerto> escalas)
	{
		//Generamos el vuelo
		super(codigo, origen, destino, totalAsientos, totalTripulantes, fechaSalida, 20);
		
		validarParametros(cantidadRefrigerios, precioRefrigerio);
		
		this.refrigeriosPorPasajero = cantidadRefrigerios;
		this.precioPorRefrigerio = precioRefrigerio;
		this.escalas = escalas;
		
		//Si el diccionario de escalas mide "0", es un vuelo directo, porque no tiene escalas
		this.vueloDirecto = escalas.size() == 0;
		
	}
	
	
	
	private void validarParametros(int cantidadRefrigerios, double precioRefrigerio) 
	{
		if(cantidadRefrigerios < 0) throw new RuntimeException("VueloPublico: La cantidad de refrigerios no puede ser negativa.");
		if(precioRefrigerio < 0) throw new RuntimeException("VueloPublico: El precio de los refrigerios no puede ser negativo.");
	}
	
	/*
	 * Dado un array de ints, siendo la primer posicion la cantidad de asientos de clase Economica,
	 * la segunda posicion la cantidad de asientos de clase turista y la tercera la cantidad de asientos
	 * de clase ejecutivo, con otro array de precios siguiendo la misma logica, crea los asientos
	 * del vuelo con estos datos y los añade a asientos disponibles.
	 * */
	@Override
	public void registrarAsientosDisponibles(int[]cantAsientos, double[]precios)
	{	
		//El contador sirve para numerar los asientos y darles un codigo unico dentro del vuelo
		int contador = 0;
		
		//Se recorre el array de la cantidad de secciones que habra en el vuelo: cantiAsientos: [Economica, Turista, Pimera]  
		for(int i = 0; i < cantAsientos.length; i++)
		{
			
			//Se itera la cantidad de veces que determine la seccion. Por ejemplo:[15, 10, 2] si estoy en clase Turista, iterare 10 veces, creando 10 asientos.   
			for(int j = 0; j<cantAsientos[i]; j++)
			{
				//incrementamos el numero de asiento
				contador ++;

				//Establecemos su precio segun la seccion en la que nos encontramos.
				double precio = precios[i];
				
				//Guardo una variable de tipo asiento. Dependiendo su clase generare uno u otro.
				Asiento asientoNuevo;
				
				if(i == 0) 
				{
					//Se crea un asiento de clase economica
					asientoNuevo = new Asiento(contador, precio, "Economica");
				}
				
				if(i == 1) //Si estamos en la segunda seccion
				{
					//Se crea un asiento de clase turista
				    asientoNuevo = new Asiento(contador, precio, "Turista");
				}
				
				else //Si estamos en la tercera seccion. Segun la consigna, para un vuelo internacional, NO HAY mas de 3 secciones. 
				{	
					//Se crea un asiento de primera clase 
					asientoNuevo = new Asiento(contador, precio, "Ejecutivo");	
				}
				
				//Agregamos el asiento al diccionario de asientos disponibles. 
				super.registrarAsientoDisponible(asientoNuevo);
			}
		}
	}


//	public HashMap<String, Aeropuerto> getEscalas() {
//		return escalas;
//	}
//
//	public boolean esVueloDirecto() {
//		return vueloDirecto;
//	}
	
	@Override
	public String toString() {
		return super.getCodigo()+ " - " + super.getOrigen().getNombre() + " - " + super.getDestino().getNombre() + " - " + super.getFechaSalida()+ " - " + "INTERNACIONAL";
	}

	/*
	 * El precio se obtiene de la cantidad de refrigerios que consumieron los pasajeros + la sumatoria de todos los 
	 * precios de sus asientos. A eso se le suma el 20% de impuesto
	 * */
	@Override
	public double getPrecio() 
	{
		//El precio base es cantidadDePasajeros * refrigeriosPorPasajero * precioPorRefrigerio
		double precio =  (super.getAllPasajeros().size() * refrigeriosPorPasajero) * precioPorRefrigerio;
		
		//Recorro todos los pasajeros y voy sumando el precio de su costo total (la sumatoria de todos los asientos que tienen)
		for(Pasajero pasajeroaActual: super.getAllPasajeros()) 
		{
			precio = precio + pasajeroaActual.getCosto();
		}
		
		//Al precio final le sumo el 20%. O sea "precio * 1.20"
		return precio * (1 + (super.getPorcentajeImpuesto()/100));
	}
	
}
