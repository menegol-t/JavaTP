package aerolinea;

public class Aeropuerto 
{
	String nombre;
	String pais;
	String estado;
	String direccion;
	boolean esNacional;
	
	//Acordate que tiene que ser runtimeException
	
	public Aeropuerto(String nombre, String pais, String estado, String direccion, boolean esNacional)
	{	
		this.nombre = nombre;
		this.pais = pais;
		this.estado = estado;
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
}
