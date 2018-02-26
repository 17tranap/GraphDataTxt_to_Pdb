/**
 * For the purposes of the Radic Lab: program takes contents of a .txt file
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
import java.lang.Number;
import java.lang.Math;
import javax.swing.border.*;
import javax.swing.filechooser.FileSystemView;

import java.lang.Object;


public class Converter extends JFrame{
	public static String txtName;
    public static int numResidues;

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
        setDesign();
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

            		JFileChooser jfc = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
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
            		}     
            }
        }); 

        convertButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent event) {
            	
                try{
                	PrintWriter pw = new PrintWriter(new FileWriter("Txt_to_pdb_" + txtName + ".txt", false));
                	pw.println("This is the .pdb file to be printed to");
                    try {
                        String numRes = inputNumResidues.getText();
                        numResidues = Integer.parseInt(numRes);
                        printPointsToFile(txtName, pw);
                    }
                    catch(NumberFormatException e) {
                        System.exit(-1);
                    }

                	printPointsToFile(txtName, pw);
                    System.out.println("write done");
                } 
                catch (IOException e) {                	
                    System.exit(-1);
                }
                finally{
                        pw.close();
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

    public static void printPointsToFile(String txtName, Printwriter pw) {
    	try {
    		File txtFile = new File (txtName);
            Scanner s = new Scanner (txtFile);

            int atomNum = 1;

            while(s.hasNextLine()) {
            	String line = s.nextLine();
            	String residue1 = line.substring(0, line.indexOf(" "));
            	line = line.substring(line.indexOf(" ")+1);
            	String residue2 = line.substring(0, line.indexOf(" "));
                try {
                    int res1 = Integer.parseInt(residue1);
                    int res2 = Integer.parseInt(residue2);
                	pw.println("HETATM\t" + atomNum + "\t" + "H\tOAA A\t1\t" + (res1/3) + "\t" + (res2/3) + "\n");
                    atomNum++;
                }
                catch (NumberFormatException e) {
                    System.err.println("Could not print because of unexpected .txt format");
                    System.exit(-1);
                }
            }

            printAxesToFile(pw, atomNum);
    	}
        catch (IOException e){
            System.err.println("Could not write to file");
            System.exit(-1);
        }
    }

    public static void printAxesToFile(Printwriter pw, int atomNum) {
        try{
            int x = 0;
            while(x < numResidues/3) {
                pw.println("HETATM\t" + atomNum + "\t" + "H\tOAA A\t1\t" + (x/3) + "\t0\t0\t1\t18\tH");
                x+=1;
            }

            int y = 0;
            while(y < numResidues/3) {
                pw.println("HETATM\t" + atomNum + "\t" + "H\tOAA A\t1\t" + "0\t" +  (y/3) + "\t0\t1\t18\tH");
                x+=1;
            }

            /*int z = 0;
            while(z < ) {
                pw.println("HETATM\t" + atomNum + "\t" + "H\tOAA A\t1\t" + x/3 + "\t0\t0\t1\t18\tH");
                x+=1;
            }*/
        }
        catch (IOException e){
            System.err.println("Could not write axes to file");
            System.exit(-1);
        }
    }
}