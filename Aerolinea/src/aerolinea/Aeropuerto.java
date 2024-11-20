package aerolinea;

public class Aeropuerto 
{
	private String nombre;
	
	private String pais;
	
	private String estado;
	
	private String direccion;
	
	private boolean esNacional;
	
	public Aeropuerto(String nombre, String pais, String estado, String direccion)
	{	
		verificarParametros(nombre, pais, estado, direccion);
		
		this.nombre = nombre;
		this.pais = pais;
		this.estado = estado;
		this.direccion = direccion;
		
		//Es nacional va a ser True si el pais que nos pasan es "Argentina", "argentina" o "ArGeNTiNa"
		this.esNacional = pais.equalsIgnoreCase("Argentina");;
	}
	
	private void verificarParametros(String nombre, String pais, String estado, String direccion) {
		if(nombre.length() == 0) throw new RuntimeException("Aeropuerto: El nombre del aeropuerto no puede ser vacio.");
		if(pais.length() == 0) throw new RuntimeException("Aeropuerto: El pais del aeropuerto no puede ser vacio.");
		if(estado.length() == 0) throw new RuntimeException("Aeropuerto: El estado del aeropuerto no puede ser vacio.");
		if(direccion.length() == 0) throw new RuntimeException("Aeropuerto: La direccion del aeropuerto no puede ser vacio.");
	}

	public String getNombre() 
	{
		return nombre;
	}
	
	public boolean esNacional()
	{
		return esNacional;
	}
}
