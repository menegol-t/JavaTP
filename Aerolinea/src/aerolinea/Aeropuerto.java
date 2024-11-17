package aerolinea;

public class Aeropuerto 
{
	String nombre;
	String locacion;
	String direccion;
	String pais;
	boolean esNacional;
	
	//Acordate que tiene que ser runtimeException
	
	public Aeropuerto(String nombre, String locacion, String direccion, String pais, boolean esNacional)
	{
		if(!(nombre.length() > 0 && locacion.length() > 0 && direccion.length( )> 0)) throw new RuntimeException("Valor de parametros invalido!!");
		
		this.nombre = nombre;
		this.locacion = locacion;
		this.direccion = direccion;
		this.pais = pais;
		this.esNacional = esNacional;
	}
	
	public String getNombre() 
	{
		return nombre;
	}
	
	public boolean getNacional()
	{
		return esNacional;
	}
	
	public String getDireccion()
	{
		return direccion;
	}
	
	public String getLocacion()
	{
		return locacion;
	}
}
