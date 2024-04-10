import java.io.IOException;

public class TesteDeCarga {

	public synchronized void incrementarErroConnect(){
		erroConnect++;
	}

	public synchronized void incrementarErroSend(){
		erroSend++;
	}

	public synchronized void incrementarErroRecev(){
		erroRecev++;
	}

	public synchronized void incrementarErroClose(){
		erroClose++;
	}

	public synchronized void incrementarErroJoin(){
		erroJoin++;
	}

	public synchronized void incrementarSucesso(){
		sucesso++;
	}

	int erroSend = 0;
	int erroRecev = 0;
	int erroConnect = 0;
	int erroClose = 0;
	int erroJoin = 0;
	double sucesso = 0; 
	
	public long executar(String ipServer, int portServer, int rodadas) {
		
		erroSend = erroRecev = erroConnect = erroClose = erroJoin = 0;
		sucesso = 0; 
		
		Thread[] threads = new Thread[rodadas];
		
		long tempoInicialST = System.currentTimeMillis();
		
		for (int i = 0; i < rodadas; i++) {
			threads[i] = new Thread(new Runnable() {
				public void run(){
					ClienteTcp clientTest = null;

					try {
						clientTest = new ClienteTcp	(ipServer, portServer);
					} catch (Exception e) {
						incrementarErroConnect();
					}

					if (clientTest != null){
						try {
							clientTest.sendRequest("add,10,11");
						} catch (Exception e) {
							incrementarErroSend();
						}
					}

					try {
						if (clientTest != null) {
							clientTest.getResponse();
							incrementarSucesso();
						}
					} catch (Exception e) {
						incrementarErroRecev();

					} finally{
						if (clientTest != null) {
							try {
								clientTest.close();
							} catch (Exception e) {
								incrementarErroClose();
							} 
						}
					}
				}
			});
			threads[i].start();
		}
		for (int i = 0; i < rodadas; i++) {
			try {
				threads[i].join();
			} catch (InterruptedException e) {
 				incrementarErroJoin();
			}
		}
		
		long tempoFinalST = System.currentTimeMillis() - tempoInicialST;
		
		System.out.println("Erros de Connect: " + erroConnect);
		System.out.println("Erros de Send: " + erroSend);
		System.out.println("Erros de Recv: " + erroRecev);
		System.out.println("Erros de Close: " + erroClose);
		System.out.println("Erros de Join: " + erroJoin);
		System.out.println("Sucesso: " + sucesso );
		double taxaDeSucesso = sucesso/rodadas;
		System.out.println("Taxa de Sucesso: " + taxaDeSucesso*100 +"%" );

		return tempoFinalST;
		
	}

	public static void main(String args[])  {
		
		int rodadas = Integer.parseInt("100");
		
		TesteDeCarga testeDeCarga = new TesteDeCarga();
		
		System.out.println("Testando SingleThread");
		System.out.println("TempoST: " + testeDeCarga.executar("172.25.243.93", 8000, rodadas) + "ms");
		
		System.out.println();

		System.out.println("Testando MultiThread");
		System.out.println("TempoMT: " + testeDeCarga.executar("localhost", 7896, rodadas) + "ms");
	}
}
