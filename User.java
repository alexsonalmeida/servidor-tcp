
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.UnknownHostException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class User {
	public static void main(String args[]) throws IOException {
		int numClients = 100;
		ExecutorService executor = Executors.newFixedThreadPool(numClients);

		long startTime = System.currentTimeMillis();

		for (int i = 0; i < numClients; i++) {
			executor.submit(() -> {
				try {
					ClienteTcp clienteTcp = new ClienteTcp("localhost", 7896);
					clienteTcp.sendRequest("sub,10,6");
					System.out.println("Response: " + clienteTcp.getResponse());
					clienteTcp.close();
				} catch (UnknownHostException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			});
		}

		executor.shutdown();
		try {
			if (!executor.awaitTermination(60, TimeUnit.SECONDS)) {
				executor.shutdownNow();
			}
		} catch (InterruptedException ex) {
			executor.shutdownNow();
			Thread.currentThread().interrupt();
		}

		long endTime = System.currentTimeMillis();
		System.out.println("Total execution time: " + (endTime - startTime) + "ms");
	}
}

