import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;

public class hits1432 {
	static int edges;
	static int vertices;
	static int[][] adjMtrx;
	
	public static void main(String[] args) throws IOException {
		BufferedInputStream br = new BufferedInputStream(new FileInputStream("s.txt"));
		StringBuilder str = new StringBuilder();
		int i = 0;
		vertices = Integer.parseInt( (char)br.read()+"");
		adjMtrx = new int[vertices][vertices];
		
		br.read();
		edges = Integer.parseInt( (char)br.read()+"");

		//System.out.println(vertices +"--"+ edges);
		while ( (i = br.read()) != -1 ){ 
			System.out.println((char)i);
		} 
		br.close();

	}

}
