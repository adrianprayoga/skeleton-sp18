public class DrawTriangle {

	public static void drawTriangle(int n) {
		int row = 0;

		while (row <= n) {
			for (int i=0; i < row; i++) {
				System.out.print("*");
			}
			System.out.println();
			row = row +1;
		}
	}



	public static void main(String[] args) {
		if (args.length != 1) {
        	    System.out.println("Please enter command line arguments.");
		}

		try {
			drawTriangle(Integer.parseInt(args[0]));

		} catch (Exception e) {
			System.out.println("Casting error");
		}

	}

}
