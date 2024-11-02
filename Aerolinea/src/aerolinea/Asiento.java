package aerolinea;

public class Asiento 
{

	private int codigo;
	private int seccion;
	private String clase;
	private boolean ocupado;
	
	//Validar si precio entra aca
	private double precio;
	
	public Asiento(int codigo, int seccion, String clase, boolean ocupado) throws Exception
	{
		if (!(codigo > 0 && seccion > 0 && clase != null)) throw new Exception("Valor de parametros invalido!!");
		
		this.codigo = codigo;
		this.seccion = seccion;
		this.clase = clase;
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
	
	@Override
	public String toString()
	{
		return "-------------------\nCodigo: "+codigo + "\nPrecio: "+precio+"\nClase: "+clase+"\nEn uso: "+ocupado+"\n-------------------";
	}
}
