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
