import java.io.IOException;

public class hits1432 {
	static int edges;
	static int vertices;
	static int[][] adjMtrx;
	static int[][] trnsMtrx;
	static double[] u;
	static double [] v;
	static FileReader1432 fr;

	public static void main(String[] args) throws IOException {

		fr = new FileReader1432("s.txt");

		vertices = fr.getVerticeSize();
		adjMtrx = new int[vertices][vertices];
		u = v = fillVector(vertices, 1);
		edges = fr.getEdgeSize();

		adjMtrx = initMtrx(adjMtrx.length);
		trnsMtrx = transpose(adjMtrx,adjMtrx.length);
		
		v = mtrxMulti(v, trnsMtrx);
		u = mtrxMulti(v,adjMtrx);
		double[] dash = computeUVDash(u,v);
		
		u = mtrxDiv(dash[0],u);
		v = mtrxDiv(dash[1],v);
		
		
		for(double j : v) {
			System.out.println(j);
		}

//		for(int j : u) {
//			System.out.println(j);
//		}
		//printMtrx(trnsMtrx);
		fr.close();
		//printMtrx(adjMtrx);
	}
	static double[] mtrxDiv(double num, double[] mtrx) {
		int size = mtrx.length;
		for(int i=0; i<size; i++) 
			mtrx[i] = Math.round((mtrx[i]/ num) * 10000000d) / 10000000d  ;
		return mtrx;
	}
	
	static double[] computeUVDash(double[] u, double[] v) {
		int sum1 = 0;
		int sum2 = 0;
		for(int i=0; i<u.length; i++) {
			sum1 += u[i] * u[i];
			sum2 += v[i] * v[i];
		}
		return new double[]{ Math.round((Math.sqrt(sum1)) * 10000000d) / 10000000d,Math.round((Math.sqrt(sum2)) * 10000000d) / 10000000d };
	}

	static int[][] transpose(int[][] mtrx, int size){
		int[][] trnsMtrx = new int[size][size];
		for(int i=0; i<size; i++) 
			for(int j=0; j<size; j++) 
				trnsMtrx[j][i] = mtrx[i][j];
		return trnsMtrx;
	}

	static double[] mtrxMulti(double mtrx[], int mtrx2[][]) {
		int size = mtrx.length;
		double[] temp = new double[size];
		for(int i=0; i<size; i++) {
			int sum = 0;
			for(int j=0; j<size; j++) {
				sum += mtrx2[i][j] * mtrx[j];
			}
			temp[i] = sum;
		}	
		return temp;
	}
	
	static int[][] initMtrx(int size) throws NumberFormatException, IOException {
		int i;
		int[][] mtrx = new int[size][size];
		while ( (i = fr.getNextValue()) != -1 ){ 
			int prev = i;
			if((i = fr.getNextValue()) != -1) 
				mtrx[Integer.parseInt(((char)prev)+"")][Integer.parseInt((char)i+"")] = 1;
			else break;
		}
		return mtrx;
	}

	static double[] fillVector(int size, int initValue) {
		double[] vec = new double[size];
		for(int j=0; j<size; j++) vec[j] = initValue;
		return vec;
	}

	static void printMtrx(int[][] mtrx) {
		for(int l=0; l<mtrx.length; l++) {
			for(int y=0; y<mtrx[l].length; y++) 
				System.out.print(mtrx[l][y]);
			System.out.println();
		}
	}

}
