import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

public class ClienteTcp {

	Socket s = null;
	DataOutputStream out;
	DataInputStream in;

	public ClienteTcp(String ipServer, int portServer) throws UnknownHostException, IOException {
        s = new Socket(ipServer, portServer);
        out = new DataOutputStream(s.getOutputStream());
        in = new DataInputStream(s.getInputStream());
	}

	public void sendRequest(String request) throws IOException {
		out.writeUTF(request);
	}
	

	public String getResponse() throws IOException {
		String response = in.readUTF();
		return response;
	}

	public void close() {
        try {
            if (s != null)
                s.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
	}
}