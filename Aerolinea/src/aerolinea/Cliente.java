package aerolinea;

public class Cliente
{

	private Integer dni;
	private String nombre;
	private String  telefono;
	
	
	public Cliente(Integer dni, String nombre, String telefono)
	{
		validarParametros(dni, nombre, telefono);
		
		this.dni = dni;
		this.nombre = nombre;
		this.telefono =telefono; 
	} 
	
	private void validarParametros(Integer dni, String nombre, String telefono) 
	{
		if(dni <= 0 ) throw new RuntimeException("Cliente: El DNI no puede ser menor a 1.");
		if(nombre.length() == 0) throw new RuntimeException("Cliente: El nombre no puede ser vacio");
		if(telefono.length() == 0) throw new RuntimeException("Cliente: El telefono no puede ser vacio");
	}
	
	public Integer getDni()
	{
		return dni;
	}
	
	public String getNombre() 
	{
		return nombre;
	}
	
	public String getTelefono() 
	{
		return telefono;
	}
	
	public String toString() 
	{
		StringBuilder st = new StringBuilder("Cliente: ");
		
		st.append(dni);
		st.append(" - ");
		
		st.append(nombre);
		st.append(" - ");
		
		st.append(telefono);
		st.append("\n");
		
		return st.toString();
	}
}