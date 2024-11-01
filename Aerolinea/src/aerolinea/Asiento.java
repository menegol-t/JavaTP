package aerolinea;

public class Asiento 
{

	private int codigo;
	private int seccion;
	private String clase;
	
	//Validar si precio entra aca
	private double precio;
	
	public Asiento(int codigo, int seccion, String clase) throws Exception
	{
		if (!(codigo > 0 && seccion > 0 && clase != null)) throw new Exception("Valor de parametros invalido!!");
		
		this.codigo = codigo;
		this.seccion = seccion;
		this.clase = clase;
	}
	
	
	public int consultarSeccion()
	{
		return seccion;
	}
	
	public int consultarCodigo()
	{
		return codigo;
	}
	
	public String consultarClase()
	{
		return clase;
	}
	
	@Override
	public String toString()
	{
		return "-------------------\nCodigo: "+codigo + "\nPrecio: "+precio+"\nClase: "+clase+"\n-------------------";
	}
}
