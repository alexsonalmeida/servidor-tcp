import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ServidorTcpMT {
	public static void main(String args[]) {
		Socket clientSocket;

		try {
			int serverPort = 7896;
			ServerSocket listenSocket = new ServerSocket(serverPort);

			while (true) {
				clientSocket = listenSocket.accept();
				Connection connection = new Connection(clientSocket);
				connection.start();
			}

		} catch (IOException e) {
			System.out.println("Listen :" + e.getMessage());
		}
	}
}

class Connection extends Thread {
	Socket clientSocket;
	Calculadora calculadora = Calculadora.getInstance();
	DataInputStream in = null;
	DataOutputStream out = null;

	public Connection(Socket aClientSocket) throws IOException {
		clientSocket = aClientSocket;
		in = new DataInputStream(clientSocket.getInputStream());
		out = new DataOutputStream(clientSocket.getOutputStream());
	}

	public String getRequest() throws IOException {
		return in.readUTF();
	}

	public void sendResponse(String response) throws IOException {
		out.writeUTF(response);
	}
	
	public void run() {
		try {
			String request = getRequest();
			String[] parts = request.split(",");
			String operation = parts[0];
			double operand1 = Double.parseDouble(parts[1]);
			double operand2 = Double.parseDouble(parts[2]);
			Thread.sleep(100);

			double result = 0;
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
		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
		} finally {
			try {
				clientSocket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}