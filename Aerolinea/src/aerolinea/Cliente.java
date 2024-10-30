package aerolinea;

public class Cliente
{

	private int dni;
	private String nombre;
	private int telefono;
	
	
	public Cliente(int dni, String nombre, int telefono)
	{
		this.dni = dni;
		this.nombre = nombre;
		this.telefono =telefono; 
	}
	
	public int consultarDni()
	{
		return dni;
	}
}
