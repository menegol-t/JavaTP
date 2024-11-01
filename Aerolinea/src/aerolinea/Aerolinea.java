package aerolinea;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class Aerolinea implements IAerolinea 
{

		private String nombre;
		private String cuit;
		private HashMap<Integer, Vuelo> Vuelos;
		private LinkedList<Aeropuerto> Aeropuertos;
		private HashMap<Integer, Cliente> clientes;
	
	
	public void Aereolinea(String nombre, String cuit) throws Exception
	{
		
		if(!(nombre != null && nombre.length() > 0 && cuit != null && cuit.length() > 0)) throw new Exception("Valor de parametros invalido!!");
		
		this.nombre = nombre;
		this.cuit = cuit;
		this.Vuelos = new HashMap<>();
		this.Aeropuertos = new LinkedList<>();
		this.clientes = new HashMap<>();
	}
	

	@Override
	public void registrarCliente(int dni, String nombre, String telefono) 
	{
		Integer Dni = new Integer(Integer.valueOf(dni));
		
		Cliente cliente = new Cliente(Dni, nombre, telefono);
		
		((HashMap<Integer, Cliente>) clientes).put(cliente.consultarDni(), cliente);
		
	}

	@Override
	public void registrarAeropuerto(String nombre, String pais, String provincia, String direccion) 
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public String registrarVueloPublicoNacional(String origen, String destino, String fecha, int tripulantes,
			double valorRefrigerio, double[] precios, int[] cantAsientos) 
	{
		// TODO Auto-generated method stub
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
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int venderPasaje(int dni, String codVuelo, int nroAsiento, boolean aOcupar) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public List<String> consultarVuelosSimilares(String origen, String destino, String Fecha) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void cancelarPasaje(int dni, String codVuelo, int nroAsiento) {
		// TODO Auto-generated method stub
		
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