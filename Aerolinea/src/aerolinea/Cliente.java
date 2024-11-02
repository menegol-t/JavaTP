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
	
	public String consultarNombre() 
	{
		return nombre;
	}
	
	public String consultarTelefono() 
	{
		return telefono;
	}
	
	public String toString() 
	{
		StringBuilder st = new StringBuilder();
		
		st.append(dni);
		
		st.append(nombre);
		
		st.append(telefono);
		
		return st.toString();
	}
}
