/**
 * For the purposes of the Radic Lab: program takes contents of a .txt file 
 * through a GUI that allows user to pick file from file picker, takes data points,
 * and prints them to a pdb file in a format readable to 3D visualizers
 * such that the data points are the first two columns of the .txt with the
 * first as the X coordinate and the second as a Y coordinate
 */
import java.awt.*;
import java.awt.Color;
import javax.swing.*;
import java.awt.event.*;
import javafx.application.*;

import javafx.scene.*;
import javafx.scene.paint.*;
import javafx.scene.shape.*;

import javafx.stage.*;
import java.util.*;
import java.util.List;
import java.io.*;
import java.io.PrintWriter;
import java.lang.Number;
import java.lang.Math;
import javax.swing.border.*;
import javax.swing.filechooser.FileSystemView;
import javax.swing.filechooser.FileNameExtensionFilter;

import java.lang.Object;


public class Converter extends JFrame{
	public static String txtName;
    public static int numResidues = -1;
    public static final int scaleFactor = 10;
    public static Scanner txtFile = null;

	JPanel[] row = new JPanel[15];
    int[] dimW = {300,45,100,90};
    int[] dimH = {35, 50};
    Dimension displayDimension = new Dimension(dimW[0], dimH[0]);
    Dimension regularDimension = new Dimension(dimW[0], dimH[1]);
    Dimension rColumnDimension = new Dimension(dimW[2], dimH[1]);
    Dimension zeroButDimension = new Dimension(dimW[3], dimH[1]);

    static JButton convertButton = new JButton("Convert");
    static JButton uploadFileButton = new JButton("Upload txt file");

    
    Font font = new Font("Times new Roman", Font.BOLD, 14);

    JTextField inputNumResidues = new JTextField("",10);   
    JLabel infileName = null;

    String[] curArgs = new String[3];

	Converter() {
        super(".txt Data Points to .pdb Format Converter");
        //super.setDesign();
        setSize(500, 500);
        setResizable(true);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        GridLayout grid = new GridLayout(7,5);
        setLayout(grid);

        
        FlowLayout f1 = new FlowLayout(FlowLayout.CENTER);
        FlowLayout f2 = new FlowLayout(FlowLayout.CENTER,1,1);
        for(int i = 0; i < 15; i++)
            row[i] = new JPanel();
        for(int i = 0; i < 14; i++)
            row[i].setLayout(f2);

        row[14].setLayout(f1);

        uploadFileButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {

            		/*JFileChooser jfc = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
            		File workingDirectory = new File(System.getProperty("user.dir"));
            		jfc.setCurrentDirectory(workingDirectory);

            		int returnValue = jfc.showOpenDialog(null);

            		if (returnValue == JFileChooser.APPROVE_OPTION) {
            			
            			curArgs[0]= jfc.getSelectedFile().getAbsolutePath();
            			txtName = jfc.getSelectedFile().getName();
            			
            			infileName = new JLabel(txtName);
            			
            			row[0].add(infileName);
            			add(row[0]);
            			setVisible(true);
            		}    */
                    JFileChooser chooser = new JFileChooser(FileSystemView.getFileSystemView());
                    FileNameExtensionFilter filter = new FileNameExtensionFilter(
                        ".txt files", "txt");
                    chooser.setFileFilter(filter);
                    int returnVal = chooser.showOpenDialog(null);
                    if(returnVal == JFileChooser.APPROVE_OPTION) {
                       System.out.println("You chose to open this file: " +
                            chooser.getSelectedFile().getName());
                       txtFile = new Scanner(chooser.getSelectedFile());
                    } 
            }
        }); 

        convertButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent event) {
            	
                try{
                	PrintWriter pw = new PrintWriter(new FileWriter("Txt_to_pdb_" + txtName + ".txt", false));

                    try {
                        String numRes = inputNumResidues.getText();
                        numResidues = Integer.parseInt(numRes);
                    }
                    catch(NumberFormatException e) {
                        System.exit(-1);
                    }

                	printPointsToFile(txtFile, pw, numResidues);
                    pw.close();
                    System.out.println("write done");
                } 
                catch (IOException e) {                	
                    System.exit(-1);
                } 
            }
        });   

        uploadFileButton.setForeground(Color.BLACK);
        uploadFileButton.setBackground(Color.WHITE);
        convertButton.setForeground(Color.BLACK);
        convertButton.setBackground(Color.WHITE);

        Border line = new LineBorder(Color.BLACK);
        Border margin = new EmptyBorder(5, 15, 5, 15);
        Border compound = new CompoundBorder(line, margin);
        uploadFileButton.setBorder(compound);
        convertButton.setBorder(compound);
        
        row[2].add(uploadFileButton);
  
        row[12].add(convertButton);

        for(int i=2;i<14;i++){
            add(row[i]);
        } 
        setVisible(true);
    }

    public static void printPointsToFile(Scanner s, PrintWriter writer, int numResidues) {
        int atomNum = 1;

        while(s.hasNextLine()) {
            String residue1 = "" + (s.nextInt());
            String residue2 = "" + (s.nextInt());
            s.nextLine();

            try {
                double res1 = Double.parseDouble(residue1);
                double res2 = Double.parseDouble(residue2);

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
            pw.println("HETATM" + atomNumRightAlign(""+atomNum) + "  " + "H   OAA X   1    " + 
                coordRightAlign(calcPoint(x)) + coordRightAlign("0.000") + coordRightAlign("0.000") + 
                "  1.00  20.00           H");
            x+=1;
            atomNum++;
        }

        double y = 0;
        while(y < numResidues) {
            pw.println("HETATM" + atomNumRightAlign(""+atomNum) + "  " + "H   OAA Y   1    " + 
                coordRightAlign("0.000") + coordRightAlign(calcPoint(y)) + coordRightAlign("0.000") + 
                "  1.00  20.00           H");
            y+=1;
            atomNum++;
        }

        /*double z = 0.0;
        while(z < numResidues/scaleFactor) {
            pw.println("HETATM" + atomNumRightAlign(""+atomNum) + "  " + "H  OAA A\t1" + 
                coordRightAlign("0.000") + coordRightAlign("0.000") + coordRightAlign(calcPoint(z)) + 
                "  1.00  40.00           H");
            z+=1;
            atomNum++;
        }*/
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