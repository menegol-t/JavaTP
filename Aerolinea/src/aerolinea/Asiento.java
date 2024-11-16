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
	
	public void setOcupado(boolean aOcupar) 
	{
		ocupado = aOcupar;
	}
	
	public void setPrecio(double val)
	{
		precio = val;
	}
	
	public double getPrecio()
	{
		return precio;
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
	
	public void setCodPasaje(int codigo) 
	{
		codPasaje = codigo;
	}
	
	public void liberarAsiento()
	{
		ocupado = false;
	}
}