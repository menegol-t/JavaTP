package aerolinea;

public class Asiento 
{

	private int codigo;
	private int seccion;
	
	public Asiento(int codigo, int seccion) throws Exception
	{
		if (!(codigo > 0 && seccion > 0)) throw new Exception("Valor de parametros invalido!!");
		
		this.codigo = codigo;
		this.seccion = seccion;
	}
	
	
	public int consultarSeccion()
	{
		return seccion;
	}
	
	public int consultarCodigo()
	{
		return codigo;
	}
	
}

