/**
 * For the purposes of the Radic Lab: program takes contents of a .txt file
 * and prints them to a pdb file in a format readable to 3D visualizers
 * such that the data points are the first two columns of the .txt with the
 * first as the X coordinate and the second as a Y coordinate
 */

import java.io.*;
import java.util.*;

public class ConverterAlgorithm {

	private static int scaleFactor = 20;

    public static void main(String[] args) {
    	Scanner userChoices = new Scanner(System.in);
        System.out.println("Please enter the name of the file you wish to convert: \n");
        String infileName = userChoices.nextLine();
        System.out.println("Please enter the total number of residues: \n");
		int numResidues = -1;
		try {
			numResidues = Integer.parseInt(userChoices.nextLine());
			if(numResidues < 0) {
				System.err.println("Invalid number was chosen");
			}
		} catch (NumberFormatException e) {
			System.err.println("Invalid number was chosen");
			System.exit(-1);
		}

        try {
            Scanner txtFileReader = new Scanner(new File(infileName));
        	PrintWriter writer = new PrintWriter("Txt_to_pdb_" + infileName.substring(0, infileName.length()-4) + ".pdb", "utf-8");
        	printPointsToFile(txtFileReader, writer, numResidues);
        	txtFileReader.close();
        } catch (IOException e){
            System.err.println("Could not write file");
            System.exit(-1);
        }
        userChoices.close();
    }

	public static void printPointsToFile(Scanner s, PrintWriter writer, int numResidues) {
		int atomNum = 1;

		while(s.hasNextLine()) {
			/*String line = s.nextLine();
			String residue1 = line.substring(0, line.indexOf(" "));
			line = line.substring(line.indexOf(" ")+1);
			String residue2 = line.substring(0, line.indexOf(" "));*/
			String residue1 = "" + (s.nextInt()-200);
			String residue2 = "" + (s.nextInt()-200);
			s.nextLine();

		    try {
		        double res1 = Double.parseDouble(residue1);
		        double res2 = Double.parseDouble(residue2);

		        //scaling needs work to be dynamic
		    	writer.println("HETATM" + atomNumRightAlign(""+atomNum) + "  " + "H  OAA A\t1" + 
		    		coordRightAlign(calcPoint(res1)) + coordRightAlign(calcPoint(res2)) + coordRightAlign("0.000") + 
		    		"  1.00  40.00           H"); 
		        atomNum++;
		    }
		    catch (NumberFormatException e) {
		        System.err.println("Could not print because of unexpected .txt format");
		        System.exit(-1);
		    }
		}

		printAxesToFile(writer, atomNum, numResidues);
	}

	public static void printAxesToFile(PrintWriter pw, int atomNum, int numResidues) {
		//try{
		    double x = 0.0;
		    while(x < numResidues/scaleFactor) {
		        pw.println("HETATM" + atomNumRightAlign(""+atomNum) + "  " + "H  OAA A\t1" + 
		    		coordRightAlign(calcPoint(x)) + coordRightAlign("0.000") + coordRightAlign("0.000") + 
		    		"  1.00  40.00           H");
		        x+=1;
		        atomNum++;
		    }

		    double y = 0.0;
		    while(y < numResidues/scaleFactor) {
		        pw.println("HETATM" + atomNumRightAlign(""+atomNum) + "  " + "H  OAA A\t1" + 
		    		coordRightAlign("0.000") + coordRightAlign(calcPoint(y)) + coordRightAlign("0.000") + 
		    		"  1.00  40.00           H");
		        y+=1;
		        atomNum++;
		    }

		    /*int z = 0;
		    while(z < ) {
		        pw.println("HETATM\t" + atomNum + "\t" + "H\tOAA A\t1\t" + x/scaleFactor + "\t0\t0\t1\t18\tH");
		        x+=1;
		    }*/
		/*} catch (IOException e){
		    System.err.println("Could not write axes to file");
		    System.exit(-1);
		}*/
	}

	private static String calcPoint(double d) {
		double value = d/scaleFactor;
		return "" + String.format("%.02f", d);
	}

	private static String atomNumRightAlign(String s) {
		int l = s.length();
		int toInsert = 0;
		switch(l) {
			case(1):
				toInsert = 4;
				break;
			case(2):
				toInsert = 3;
				break;
			case(3):
				toInsert = 2;
				break;
			case(4):
				toInsert = 1;
				break;
			case(5):
				toInsert = 0;
				break;
		}
		for(int i = 0; i < toInsert; i++) {
			s = " " + s;
		}
		return s;
	}

	private static String coordRightAlign(String s) {
		int l = s.length();
		int toInsert = 0;
		switch(l) {
			case(1):
				toInsert = 7;
				break;
			case(2):
				toInsert = 6;
				break;
			case(3):
				toInsert = 5;
				break;
			case(4):
				toInsert = 4;
				break;
			case(5):
				toInsert = 3;
				break;
			case(6):
				toInsert = 2;
				break;
			case(7):
				toInsert = 1;
				break;
		}
		for(int i = 0; i < toInsert; i++) {
			s = " " + s;
		}
		return s;
	}
}