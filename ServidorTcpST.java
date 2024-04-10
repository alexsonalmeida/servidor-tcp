import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ServidorTcpST {
	static Socket s;
	static Calculadora calculadora = Calculadora.getInstance();
	public static void main(String argv[]) throws Exception {
		ServerSocket welcomeSocket = new ServerSocket(8000);

		while (true) {
			s = welcomeSocket.accept();
			String request = getRequest();
			String[] parts = request.split(",");
			String operation = parts[0];
			double operand1 = Double.parseDouble(parts[1]);
			double operand2 = Double.parseDouble(parts[2]);
			double result = 0;
			Thread.sleep(100);
			switch (operation) {
				case "add":
					result = calculadora.add(operand1, operand2);
					break;
				case "sub":
					result = calculadora.sub(operand1, operand2);
					break;
				case "mul":
					result = calculadora.mult(operand1,operand2);
					break;
				case "div":
					result = calculadora.div(operand1, operand2);
					break;
			}
			sendResponse(Double.toString(result));
		}
	}
	
	public static String getRequest() throws IOException {
		DataInputStream in = new DataInputStream(s.getInputStream());
		return in.readUTF();
	}
	
	public static void sendResponse(String response) throws IOException {
		DataOutputStream out = new DataOutputStream(s.getOutputStream());
		out.writeUTF(response);
	}
}