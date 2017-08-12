package thread;

public class MultiThreadEx {

	public static void main(String[] args) {
		
		Thread th1 = new AlphabetThread();
		Thread th2 = new Thread(new DigitThread());
		
		new Thread(new Runnable() {

			@Override
			public void run() {

				for(char c='A'; c<='Z'; c++) {
					System.out.print(c);
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
			
		}).start();
		
		th1.start();
		th2.start();
		
		/*try {
			th1.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		for(int i=0; i<=9; i++) {
			System.out.print(i);
		}*/
		
		/*for(char c='a'; c<='z'; c++) {
			System.out.print(c);
		}*/
	}

}
