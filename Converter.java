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

	JPanel[] row = new JPanel[8];

    static JButton convertButton = new JButton("Convert");
    static JButton uploadFileButton = new JButton("Upload txt file");

    
    Font font = new Font("Times new Roman", Font.BOLD, 14);

    public static JLabel numResLabel = new JLabel("Enter size of x and y axes here: ");
    public static JTextField inputNumResidues = new JTextField("",10);   
    public static JLabel infileName = new JLabel("");

    public static JComboBox<String> columnOptions = new JComboBox<String>();
    public static JLabel choicesLabel = new JLabel("Pick the column you want for the Z axis: ");



    String[] curArgs = new String[3];

	Converter() {
        super(".txt Data Points to .pdb Format Converter");
        setSize(400, 300);
        setResizable(true);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        GridLayout grid = new GridLayout(6,3, 0, 10);
        setLayout(grid);

        
        FlowLayout f1 = new FlowLayout(FlowLayout.LEADING);
        FlowLayout f2 = new FlowLayout(FlowLayout.CENTER,1,1);
        for(int i = 0; i < 6; i++)
            row[i] = new JPanel();
        for(int i = 0; i < 5; i++)
            row[i].setLayout(f1);

        row[5].setLayout(f2);

        uploadFileButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {

                    JFileChooser chooser = new JFileChooser(FileSystemView.getFileSystemView());
                    FileNameExtensionFilter filter = new FileNameExtensionFilter(
                        ".txt files", "txt");
                    chooser.setFileFilter(filter);
                    int returnVal = chooser.showOpenDialog(null);
                    if(returnVal == JFileChooser.APPROVE_OPTION) {
                        txtName = chooser.getSelectedFile().getName();
                        infileName.setText(txtName);
                        try {
                            txtFile = new Scanner(chooser.getSelectedFile());
                            getColumnOptions(txtFile);
                        } catch(IOException e) {
                            System.exit(-1);
                        }

                    } 
            }
        }); 

        convertButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent event) {
            	
                try{
                	PrintWriter pw = new PrintWriter(
                        new FileWriter("Txt_to_pdb_" + 
                        txtName.substring(0, txtName.length()-4) + ".pdb", false));

                    try {
                        String numRes = inputNumResidues.getText();
                        numResidues = Integer.parseInt(numRes);
                    }
                    catch(NumberFormatException e) {
                        System.exit(-1);
                    }

                	printPointsToFile(txtFile, pw, numResidues);
                    pw.flush();
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

        Border line = new BevelBorder(1);
        Border margin = new EmptyBorder(5, 15, 5, 15);
        Border compound = new CompoundBorder(line, margin);
        uploadFileButton.setBorder(compound);
        convertButton.setBorder(compound);
        
        row[4].add(choicesLabel);
        row[4].add(columnOptions);
        row[2].add(uploadFileButton);
        row[2].add(infileName);
        row[3].add(numResLabel);
        row[3].add(inputNumResidues);
        row[5].add(convertButton);

        for(int i=1;i<6;i++){
            add(row[i]);
        } 
        setVisible(true);
    }

    public static void getColumnOptions(Scanner s) {
        String options = s.nextLine();
        String[] choices = options.split("\t\t");
        for(int i = 0; i < choices.length; i++){
            System.out.print((i+1) + ". " + choices[i] + "\n");
            columnOptions.insertItemAt(choices[i], i);
        }
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

    public static void main(String[] args) {
        Converter runner = new Converter();
    }
}