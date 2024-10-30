package aerolinea;

public class Aeropuerto 
{
	String nombre;
	String locacion;
	String direccion;
	boolean esNacional;
	
	public Aeropuerto(String nombre, String locacion, String direccion, boolean esNacional) throws Exception
	{
		if(!(nombre.length() > 0 && locacion.length() > 0 && direccion.length( )> 0)) throw new Exception("Valor de parametros invalido!!");
		
		this.nombre = nombre;
		this.locacion = locacion;
		this.direccion = direccion;
		this.esNacional = esNacional;
	}
	
	public boolean consultarNacional()
	{
		return esNacional;
	}
	
	public String consultarDireccion()
	{
		return direccion;
	}
}
