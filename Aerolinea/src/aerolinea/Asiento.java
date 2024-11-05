package aerolinea;

public class Asiento 
{

	private int codigo;
	private int codPasaje;
	private int seccion;
	private String clase;
	private boolean ocupado;
	private double precio;
	
	public Asiento(int codigo, int seccion, double precio, String clase, boolean ocupado)
	{
		if (!(codigo > 0 && seccion > 0 && seccion <= 3 && clase != null)) 
			
			throw new RuntimeException("Valor de parametros invalido!!");
		
		this.codigo = codigo;
		this.seccion = seccion;
		this.clase = clase;
		this.precio = precio;
		this.ocupado = ocupado;
	}
	
	
	public int getSeccion()
	{
		return seccion;
	}
	
	public int getCodigo()
	{
		return codigo;
	}
	
	public int getCodPasaje() 
	{
		return codPasaje;
	}
	
	public String getClase()
	{
		return clase;
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