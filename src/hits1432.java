// Dhrumil Shah cs610 1432 prp
import java.io.IOException;
import java.text.DecimalFormat;

public class hits1432 {
	static int edges;
	static int vertices;
	static int[][] adjMtrx;
	static double[] hub;
	static double [] auth;
	static Reader1432 fr;
	static int initialValue;
	static int iterations;
	static double[] prevHub;
	static double[] prevAuth;
	static String fileName;

	public static void main(String[] args) throws IOException {
		try {

			if (args.length < 3) {
				System.out.println("Please enter valid number of arguments!");
				return;
			}

			iterations = Integer.parseInt(args[0]);
			initialValue = Integer.parseInt(args[1]);
			fileName = args[2];

			if(fileName.trim().length()==0 || initialValue > 1 || initialValue<-2 ) {
				System.out.println("Please enter authentic arguments!");
				return;
			}

			fr = new Reader1432(fileName);

			vertices = fr.getVerticeSize();
			adjMtrx = new int[vertices][vertices];

			edges = fr.getEdgeSize();

			adjMtrx = initMtrx(adjMtrx.length);

			DecimalFormat numFmt = new DecimalFormat("0.0000000");

			if (vertices > 10) {
				iterations = 0;
				initialValue = -1;
			}		

			prevHub = prevAuth = new double[vertices];
			hub = auth = fillVector(vertices, initialValue, vertices);

			int i=0;
			while((iterations <= 0 ? true : i!=(iterations+1) )) {	

				if(i == 0) {
					System.out.print("Base: 0 : ");
					for(int g=0; g<hub.length; g++) {
						System.out.print("A/H["+g+"] = "+numFmt.format(Math.floor(auth[g] * 1e7)/1e7)+" / "
								+numFmt.format(Math.floor(hub[g] * 1e7)/1e7)+" ");
					}
					System.out.println();
				}else {
					if(vertices < 11) {
						System.out.print("Iter: "+i+" :");
						for(int g=0; g<hub.length; g++) {
							System.out.print("A/H["+g+"] = "+numFmt.format(Math.floor(auth[g] * 1e7)/1e7)+" / "
									+numFmt.format(Math.floor(hub[g] * 1e7)/1e7)+" ");

						}
						System.out.println();
					}
				}

				prevAuth = auth;
				prevHub = hub;

				auth = computeAuth(auth, hub, adjMtrx);
				hub = computeHub(hub, auth, adjMtrx);		

				double[] dash = computeUVDash(hub,auth);//0=hub, 1=auth

				auth = scaleMtrx(dash[1],auth);

				hub = scaleMtrx(dash[0],hub);

				if((vertices>10 || iterations<0) 
						&& didItConverge((iterations <= 0) ? iterations : i,prevAuth, prevHub, auth, hub)) { 
					if(vertices<11) {
						System.out.print("Iter: "+(++i)+" :");
						for(int g=0; g<hub.length; g++) {
							System.out.print("A/H["+g+"] = "+numFmt.format(Math.floor(auth[g] * 1e7)/1e7)+" / "
									+numFmt.format(Math.floor(hub[g] * 1e7)/1e7)+" ");

						}
						System.out.println();
					}
					break;
				}

				i++;
			}

			if(vertices>10) {
				System.out.println("Iter: "+(++i)+" :");
				for(int g=0; g<hub.length; g++) {
					System.out.println("A/H["+g+"] = "+numFmt.format(Math.floor(auth[g] * 1e7)/1e7)+" / "
							+numFmt.format(Math.floor(hub[g] * 1e7)/1e7)+" ");
				}
			}
			fr.close();

		}catch(Exception e) {
			System.err.println(e.getMessage());
		}
	}

	static boolean didItConverge(int iterations, double[] prevA, double[] prevH, double[] au, double[] hu) {
		double errRate = 0;
		errRate = (iterations == 0)? 100000 : Math.pow(10, (iterations * -1));

		for (int i = 0; i < au.length; i++) {
			if ((int)Math.floor(au[i] * errRate) != (int)Math.floor(prevA[i] * errRate)) {
				return false;
			}
		}

		for (int i = 0; i < hu.length; i++) {
			if ((int)Math.floor(hu[i] * errRate) != (int)Math.floor(prevH[i] * errRate)) {
				return false;
			}
		}
		//System.out.println("Converged!");
		return true;
	}

	static double[] computeHub(double[] mtrx, double[] mtrx2, int[][] adjMtrx) {
		double sum;
		double[] temp = new double[mtrx.length];
		for(int i=0; i <mtrx.length; i++) {
			sum = 0.0;
			for(int a=0; a<adjMtrx.length; a++) {
				if(adjMtrx[i][a] == 1)	{
					sum += mtrx2[a];
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
			temp[i] = mtrx[i] / num;
		}
		return temp;
	}

	static double[] computeUVDash(double[] u, double[] v) {
		double sum1 = 0.0;
		double sum2 = 0.0;
		for(int i=0; i<u.length; i++) {
			sum1 += (double)Math.pow(u[i],2);
			sum2 += (v[i] * v[i]);
		}
		return new double[] {Math.sqrt(sum1),Math.sqrt(sum2)};	
	}

	static int[][] initMtrx(int size){
		try {
			int i;
			int[][] mtrx = new int[size][size];
			int edgeCounter = 0;
			while ( (i = fr.getNextValue()) != -1 ){ 
				int prev = i;
				if((i = fr.getNextValue()) != -1) {
					mtrx[prev][i] = 1;
					edgeCounter++;
				}
				else break;
			}
			//System.out.println(edgeCounter);
			if(edgeCounter != edges) {
				throw new Exception("Improper Data Format..!");
			}
			return mtrx;
		}catch(Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	static double[] fillVector(int size, int initValue, int vertices) {
		double temp = initValue;
		if(initValue == -1) {
			temp = (1/(double)vertices);
		}else if(initValue == -2) {
			temp = 1/(double)Math.sqrt(vertices);
		}
		double[] vec = new double[size];
		for(int j=0; j<size; j++) vec[j] = temp;
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
