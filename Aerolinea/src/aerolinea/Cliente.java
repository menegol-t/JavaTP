package aerolinea;

public class Cliente
{

	private Integer dni;
	private String nombre;
	private String  telefono;
	
	
	public Cliente(Integer dni, String nombre, String telefono)
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
