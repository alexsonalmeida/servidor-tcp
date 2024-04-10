public final class Calculadora {
	
	public static Calculadora instancia;
	
	private Calculadora() {}
	
	public static synchronized Calculadora getInstance() {
		if (instancia == null) {
			instancia = new Calculadora();
		}

		return instancia;
	}
		
	public double add(double op1, double op2) {
		return op1 + op2;
	}
	
	public double sub(double op1, double op2) {
		return op1 - op2;
	}
	
	public double mult(double op1, double op2) {
		return op1 * op2;
	}
	
	public double div(double op1, double op2) {
		return op1 / op2;
	}
}
