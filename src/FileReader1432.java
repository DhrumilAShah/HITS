import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class FileReader1432 {
	
	private static BufferedInputStream br;
	
	FileReader1432(String fileName){
		try {
			br = new BufferedInputStream(new FileInputStream(fileName));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	int getEdgeSize() throws IOException{
		return Integer.parseInt( (char)getNextValue()+"");
	}
	
	int getVerticeSize() throws NumberFormatException, IOException{
		return Integer.parseInt( (char)getNextValue()+"");
	}
	
	int getNextValue() throws IOException{
		int i;
		if((i = br.read()) != -1) {
			if(i != 32 && i != 10)//32 is space, 10 is new line
				return i;
			else return getNextValue();
		}
		return -1;
	}
	
	
	void close(){
		try {
			br.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
