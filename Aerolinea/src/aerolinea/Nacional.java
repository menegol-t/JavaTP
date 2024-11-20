package aerolinea;

import java.util.HashMap;

public class Nacional extends Internacional {
	

	public Nacional(String codigo, Aeropuerto origen, Aeropuerto destino, int totalAsientos, int totalTripulantes, String fechaSalida, double precioPorRefrigerio) 
	{			
		//Se crea la clase padre vuelo internacional, por lo que mantiene cosas como que el impuesto es del 20
		super(codigo, origen, destino, totalAsientos, totalTripulantes, fechaSalida, 1, precioPorRefrigerio, new HashMap<>());
		
		validarParametros(destino);
		
	}
	
	private void validarParametros(Aeropuerto destino) 
	{
		if(!destino.esNacional()) throw new RuntimeException("VueloNacional: Los vuelos nacionales solo pueden ir a destinos nacionales.");
	}
	
	/*
	 * Registrar asientos no deberia registrar COMO MAXIMO el limitePasajerosPrimera y limitePasajerosEconomica?????
	 * */
	@Override
	public void registrarAsientosDisponibles(int[]cantAsientos, double[]precios)
	{
		
		//El contador sirve para numerar los asientos y darles un codigo unico en el vuelo
		int contador = 0;
		
		//Se recorre el array de la cantidad de secciones que habra en el vuelo: cantiAsientos: [Economica, Pimera]  
		for(int i = 0; i < cantAsientos.length; i++)
		{	
			//En cada posicion del array de secciones, hay un array con los asientos de esa seccion. Aca se recorre ese sub array: cantiAsientos: [ [asiento0, asiento1, asiento2], [asiento3, asiento4, asiento6] ]
			for(int j = 0; j<cantAsientos[i]; j++)
			{
				//incrementamos el numero de asiento
				contador++;
				
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
	

	@Override
	public String toString() {
		return super.getCodigo()+ " - " + super.getOrigen().getNombre() + " - " + super.getDestino().getNombre() + " - " + super.getFechaSalida()+ " - " + "NACIONAL";
	}

	@Override
	public double getPrecio() {
		// TODO Auto-generated method stub
		return 0;
	}
	
	
}
