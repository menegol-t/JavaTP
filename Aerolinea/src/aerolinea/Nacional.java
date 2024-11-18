package aerolinea;

public class Nacional extends Vuelo {
	
	int refrigeriosPorPasajero;
	double precioPorRefrigerio;
	int limitePasajerosEconomica;
	int pasajerosEconomica;
	int limitePasajerosEjecutivo;
	int pasajerosEjecutivo;

	public Nacional(String codigo, Aeropuerto origen, Aeropuerto destino, int totalAsientos, int totalTripulantes, String fechaSalida, int porcentajeImpuesto, int refrigeriosPorPasajero, double precioPorRefrigerio, String tipoDeVuelo) 
	{	
		//Se crea la clase padre vuelo, por lo que su irep se mantiene. Se le pasa "porcentajeImpuesto" como 20
		super(codigo, origen, destino, totalAsientos, totalTripulantes, fechaSalida, porcentajeImpuesto, tipoDeVuelo);
		
		validarParametros(refrigeriosPorPasajero, precioPorRefrigerio);
		
		this.refrigeriosPorPasajero = refrigeriosPorPasajero;
		this.precioPorRefrigerio = precioPorRefrigerio;
		
		//De momento no tiene pasajeros, la logica de aumentar este numero es de venderPasaje, al igual que incrementar agregar el pasajero a la lista
		pasajerosEconomica = 0;
		pasajerosEjecutivo = 0;
		
		//El limite de pasajeros aparentemente no me lo pasan, solo lo sacamos nosotros??????
		limitePasajerosEconomica = 0;
		limitePasajerosEjecutivo = 0;
		
	}
	
	private void validarParametros(int refrigeriosPorPasajero, double precioPorRefrigerio) 
	{
		if(refrigeriosPorPasajero < 0) throw new RuntimeException("VueloNacional: No puede haber una cantidad negativa de refrigerios.");
		if(precioPorRefrigerio < 0) throw new RuntimeException("VueloNacional: Los refrigerios no pueden tener un precio negativo.");
	}
	
	/*
	 * Registrar asientos no deberia registrar COMO MAXIMO el limitePasajerosPrimera y limitePasajerosEconomica?????
	 * */
	@Override
	public void registrarAsientosDisponibles(int[]cantAsientos, double[]precios)
	{
		//Al registrar los asientos, registramos cual es el limite de asientos por clase, algo que solicitaba la etapa de diseño pero en la segunda etapa no se especifica. 
		setLimitePasajerosEconomica(cantAsientos[0]);
		
		setLimitePasajerosEjecutivo(cantAsientos[1]);
		
		//Se recorre el array de la cantidad de secciones que habra en el vuelo: cantiAsientos: [Economica, Pimera]  
		for(int i = 0; i < cantAsientos.length; i++)
		{
			//El contador sirve para numerar los asientos y darles un codigo unico en el vuelo
			int contador = 0;
			
			//En cada posicion del array de secciones, hay un array con los asientos de esa seccion. Aca se recorre ese sub array: cantiAsientos: [ [asiento0, asiento1, asiento2], [asiento3, asiento4, asiento6] ]
			for(int j = 0; j<cantAsientos[i]; j++)
			{
				//incrementamos el numero de asiento
				contador += 1;
				
				//Calculamos el pasaje con impuestos
				double precioFinal = precios[i] + precios[i] * super.getPorcentajeImpuesto()/100;
				
				Asiento asientoNuevo;
				
				if(i == 0) //Si estamos en la primera seccion
				{
					//Se crea un asiento de clase economica
					asientoNuevo = new Asiento(contador, precioFinal, "Economica");
				}
				
				else //Si estamos en la segunda seccion
				{
					//Se crea un asiento de primera clase
					asientoNuevo = new Asiento(contador, precioFinal, "Ejecutivo");
				}
				
				super.registrarAsientoDisponible(asientoNuevo);
			}
		}
	}
	
	//Hacemos este setter por la subclase internacional
	public void setLimitePasajerosEconomica(int cant) 
	{
		limitePasajerosEconomica = cant;
	}
	
	//Hacemos este setter por la subclase internacional
	public void setLimitePasajerosEjecutivo(int cant) 
	{
		limitePasajerosEjecutivo = cant;
	}
	
	
}
