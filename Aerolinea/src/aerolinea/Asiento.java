package aerolinea;

public class Asiento 
{

	private Integer codigo;
	private int codPasaje;
	private String seccion;
	private boolean ocupado;
	private double precio;
	
	public Asiento(int codigo, double precio, String seccion)
	{	
		Integer cod = codigo;
		
		this.codigo = cod;
		this.seccion = seccion;
		this.precio = precio;
		this.ocupado = false;
		this.codPasaje = 0;
	}
	
	public Integer getCodigo()
	{
		return codigo;
	}
	
	public String getSeccion()
	{
		return seccion;
	}
	
	public int getCodPasaje() 
	{
		return codPasaje;
	}
	
	public boolean getOcupado() 
	{
		return ocupado;
	}
	
	public double getPrecio()
	{
		return precio;
	}
	
	public void setOcupado(boolean aOcupar) 
	{
		ocupado = aOcupar;
	}
	
	public void setPrecio(double val)
	{
		precio = val;
	}
	
	public void setCodPasaje(int codigo) 
	{
		codPasaje = codigo;
	}
	
	@Override //La reescribimos en cada clase vuelo para agregar la logica
	public String toString()
	{
		StringBuilder retorno = new StringBuilder();
		
		retorno.append("Codigo: ");
		retorno.append(codigo);
		retorno.append("Seccion: ");
		retorno.append(seccion);
		
		return retorno.toString();

	}
}