import java.io.IOException;
import java.text.DecimalFormat;

public class hits1432 {
	static int edges;
	static int vertices;
	static int[][] adjMtrx;
	//static int[][] trnsMtrx;
	static double[] hub;
	static double [] auth;
	static FileReader1432 fr;

	public static void main(String[] args) throws IOException {

		fr = new FileReader1432("s.txt");

		vertices = fr.getVerticeSize();
		adjMtrx = new int[vertices][vertices];
		hub = auth = fillVector(vertices, 1.0, vertices);
		edges = fr.getEdgeSize();

		adjMtrx = initMtrx(adjMtrx.length);
		//trnsMtrx = transpose(adjMtrx,adjMtrx.length);

		//auth = mtrxMulti(hub, trnsMtrx);
		//hub = mtrxMulti(auth,adjMtrx);

		//auth = computeAuth(auth, hub, adjMtrx);
		//		for(int o=0; o<auth.length; o++) {
		//			System.out.println(auth[o]);
		//		}

		//hub = computeHub(hub, auth, adjMtrx);
		//		for(int k=0; k<hub.length; k++) {
		//			System.out.println(hub[k]);
		//		}
		DecimalFormat numberFormat = new DecimalFormat("0.0000000");
		

		for(int i=0; i<11; i++) {

			System.out.print("Iter: "+i+" :");
			for(int g=0; g<hub.length; g++) {
				System.out.print("A/H["+g+"] = "+numberFormat.format(Math.floor(auth[g] * 1e7)/1e7)+" / "
						+numberFormat.format(Math.floor(hub[g] * 1e7)/1e7)+" ");
			}

			auth = computeAuth(auth, hub, adjMtrx);
			hub = computeHub(hub, auth, adjMtrx);

			double[] dash = computeUVDash(hub,auth);//0=hub, 1=auth


			System.out.println();

			//System.out.println(dash[1]+"--"+dash[0]);

			auth = scaleMtrx(dash[1],auth);
			//			for(int o=0; o<auth.length; o++) {
			//				System.out.println(auth[o]);
			//			}
			hub = scaleMtrx(dash[0],hub);
			//			for(int g=0;g<hub.length;g++) {
			//				System.out.println(u[g]+" "+v[g]);
			//			}
			//System.out.println();
		}
		//printMtrx(trnsMtrx);
		fr.close();
		//printMtrx(adjMtrx);
	}


	static double[] computeHub(double[] mtrx, double[] mtrx2, int[][] adjMtrx) {
		double sum;
		double[] temp = new double[mtrx.length];
		for(int i=0; i <mtrx.length; i++) {
			sum = 0.0;
			for(int a=0; a<adjMtrx.length; a++) {
				if(adjMtrx[i][a] == 1)	{
					sum += mtrx2[a];
					//sum = Math.round((sum) * 10000000d) / 10000000d;
					//System.out.println("i>"+i+" sum>"+Math.round((sum) * 10000000d) / 10000000d+" a>"+a+" val>"+mtrx2[a]+" auth>"+auth[a]);
				}
			}
			temp[i] = sum;
		}
		return temp;
	}

	static double[] computeAuth(double[] mtrx, double[] mtrx2, int[][] adjMtrx) {
		double sum;
		double[] temp = new double[mtrx.length];
		for(int i=0; i <mtrx.length; i++) {
			sum = 0.0;
			for(int a=0; a<adjMtrx.length; a++) {
				if(adjMtrx[a][i] == 1) {
					sum += mtrx2[a];
					//sum = Math.round((sum) * 10000000d) / 10000000d;
					//System.out.println("i>"+i+" sum>"+Math.round((sum) * 10000000d) / 10000000d+" a>"+a+" val>"+mtrx2[a]+" auth>"+auth[a]);
				}
			}
			temp[i] =sum;
		}
		return temp;
	}



	static double[] scaleMtrx(double num, double[] mtrx) {
		int size = mtrx.length;
		double[] temp = new double[size];
		for(int i=0; i<size; i++) { 
			//System.out.println(mtrx[i]+"/"+num+"-->"+Math.round((mtrx[i] / num) * 10000000d) / 10000000d);
			//temp[i] = Math.round((mtrx[i] / num) * 10000000d) / 10000000d;
			temp[i] = mtrx[i] / num;
		}
		return temp;
	}

	static double[] computeUVDash(double[] u, double[] v) {
		double sum1 = 0.0;
		double sum2 = 0.0;
		for(int i=0; i<u.length; i++) {
			sum1 += (double)Math.pow(u[i],2);
			//System.out.println("sum: "+sum1);
			sum2 += (v[i] * v[i]);
		}
		//System.out.println(sum1+" sum->"+Math.sqrt(sum1));
		return new double[] {Math.sqrt(sum1),Math.sqrt(sum2)};	
		//return new double[]{ Math.round((Math.sqrt(sum1)) * 10000000d) / 10000000d,
			//	Math.round((Math.sqrt(sum2)) * 10000000d) / 10000000d };
	}

//	static int[][] transpose(int[][] mtrx, int size){
//		int[][] trnsMtrx = new int[size][size];
//		for(int i=0; i<size; i++) 
//			for(int j=0; j<size; j++) 
//				trnsMtrx[j][i] = mtrx[i][j];
//		return trnsMtrx;
//	}
//
//	static double[] mtrxMulti(double mtrx[], int mtrx2[][]) {
//		int size = mtrx.length;
//		double[] temp = new double[size];
//		for(int i=0; i<size; i++) {
//			int sum = 0;
//			for(int j=0; j<size; j++) {
//				sum += mtrx2[i][j] * mtrx[j];
//			}
//			temp[i] = sum;
//		}	
//		return temp;
//	}

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

	static double[] fillVector(int size, double initValue, int vertices) {
		if(initValue == -1.0) {
			initValue = 1/vertices;
		}else if(initValue == -2.0) {
			initValue = 1/Math.sqrt(vertices);
		}
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
