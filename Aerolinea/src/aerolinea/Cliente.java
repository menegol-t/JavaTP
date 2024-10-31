package aerolinea;

public class Cliente
{

	private Integer dni;
	private String nombre;
	private int telefono;
	
	
	public Cliente(Integer dni, String nombre, int telefono)
	{
		this.dni = dni;
		this.nombre = nombre;
		this.telefono =telefono; 
	}
	
	public Integer consultarDni()
	{
		return dni;
	}
}
