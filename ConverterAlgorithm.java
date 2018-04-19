/**
 * For the purposes of the Radic Lab: program takes contents of a .txt file
 * and prints them to a pdb file in a format readable to 3D visualizers
 * such that the data points are the first two columns of the .txt with the
 * first as the X coordinate and the second as a Y coordinate
 */

//Get program to read column titles and give them as options to user. Have user pick. Do this by printing multiple choice options of letters.
//Have range of Z dynamically calculated to have nice fitting graph. B factor column need to be 0 to 200, Occupancy needs to be 0 to 1. 
//Make Axes Chain X and Y
//Make chains carbons, not H, if spacing smaller than 1.5 angstrom then will automatically make line. 

import java.io.*;
import java.util.*;

public class ConverterAlgorithm {

	private static int scaleFactor = 10;

    public static void main(String[] args) {
    	Scanner userChoices = new Scanner(System.in);
        System.out.println("First make sure that the file to convert is in the same folder as this app.\n" +
            "Output file will also be made in the folder. " +
        	"Please enter the name of the file you wish to convert: ");
        String infileName = userChoices.nextLine();
        System.out.println("Please enter the total number of residues: ");
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
            printAxisOptionsToConsole(txtFileReader);

            System.out.println("Which column would you like on the X axis? (Type the number and hit enter)");
            int xChoice = userChoices.nextInt();

            System.out.println("Which column would you like on the Y axis? (Type the number and hit enter)");
            int yChoice = userChoices.nextInt();

            System.out.println("Which column would you like on the Z axis? (Type the number and hit enter)");
            int zChoice = userChoices.nextInt();



        	PrintWriter writer = new PrintWriter("Txt_to_pdb_" + infileName.substring(0, infileName.length()-4) + ".pdb", "utf-8");
        	printPointsToFile(txtFileReader, writer, numResidues);
        	writer.flush();
        	txtFileReader.close();
        	System.out.println("Conversion has been completed. \n.pdb file should now be in the same folder as this program.");
        } catch (IOException e){
            System.err.println("Could not write file");
            System.exit(-1);
        }
        userChoices.close();
    }

    public static void printAxisOptionsToConsole(Scanner s) {
    	System.out.println("The options for the columns in your .txt file are printed below.\n" +
    		"Please type in the number corresponding to each when prompted.\n");
    	String options = s.nextLine();
    	String[] choices = options.split("\t\t");
    	for(int i = 0; i < choices.length; i++){
    		System.out.print((i+1) + ". " + choices[i] + "\n");
    	}
    }

	public static void printPointsToFile(Scanner s, PrintWriter writer, int numResidues) {
		int atomNum = 1;

		while(s.hasNextLine()) {
			double res1 = s.nextDouble();
			double res2 = s.nextDouble();
			s.nextLine();

		    try {
		        //double res1 = Double.parseDouble(residue1);
		        //double res2 = Double.parseDouble(residue2);

		        //scaling needs work to be dynamic
		    	writer.println("HETATM" + atomNumRightAlign(""+atomNum) + "  " + "H   OAA A   1    " + 
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
	    double x = 0;
	    while(x < numResidues) {
	        pw.println("HETATM" + atomNumRightAlign(""+atomNum) + "  " + "C   OAA X   1    " + 
	    		coordRightAlign(calcPoint(x)) + coordRightAlign("0.000") + coordRightAlign("0.000") + 
	    		"  1.00  20.00           C");
	        x+=1;
	        atomNum++;
	    }

	    double y = 0;
	    while(y < numResidues) {
	        pw.println("HETATM" + atomNumRightAlign(""+atomNum) + "  " + "C   OAA Y   1    " + 
	    		coordRightAlign("0.000") + coordRightAlign(calcPoint(y)) + coordRightAlign("0.000") + 
	    		"  1.00  20.00           C");
	        y+=1;
	        atomNum++;
	    }

	    double z = 0;
	    while(z < numResidues) {
	        pw.println("HETATM" + atomNumRightAlign(""+atomNum) + "  " + "C   OAA Y   1    " + 
	    		coordRightAlign("0.000") + coordRightAlign(calcPoint(z)) + coordRightAlign("0.000") + 
	    		"  1.00  20.00           C");
	        z+=1;
	        atomNum++;
	    }
	}

	private static String calcPoint(double d) {
		double value = d/scaleFactor;
		return "" + String.format("%.03f", value);
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