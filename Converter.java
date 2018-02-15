/**
 * For the purposes of the Radic Lab: program takes contents of a .txt file
 * and prints them to a pdb file in a format readable to 3D visualizers
 * such that the data points are the first two columns of the .txt with the
 * first as the X coordinate and the second as a Y coordinate
 */

import java.io.*;

public class Converter() {
	public static String txtName;

	JPanel[] row = new JPanel[15];
    int[] dimW = {300,45,100,90};
    int[] dimH = {35, 50};
    Dimension displayDimension = new Dimension(dimW[0], dimH[0]);
    Dimension regularDimension = new Dimension(dimW[0], dimH[1]);
    Dimension rColumnDimension = new Dimension(dimW[2], dimH[1]);
    Dimension zeroButDimension = new Dimension(dimW[3], dimH[1]);
    //double[] temporary = {0, 0};

    //JTextField file1 = new JTextField("",10);
    //JTextField file2 = new JTextField("",10);
    //JTextField refPoint = new JTextField("",10);
    static JLabel message = new JLabel("");
    static String option1 = new String("");
    static String option2 = new String("");
    static String option3 = new String("");
    static String option4 = new String("");
    static String option5 = new String("");
    static String option6 = new String("");
    JButton button = new JButton("Convert");
    JButton button2 = new JButton("Upload txt file");

    
    Font font = new Font("Times new Roman", Font.BOLD, 14);

    JTextField inputOpt1 = new JTextField("",10);   
    JTextField inputOpt2 = new JTextField("",10);  
    
    JLabel opt3 = new JLabel("max length?");
    JTextField inputOpt3 = new JTextField("",10);  
    
    JLabel fi1 = null;

    public static ArrayList<dist> topList;
	Calculator() {
        super("Angle and Distance Calculator (Average)");
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

        button2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {

            		JFileChooser jfc = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
            		File workingDirectory = new File(System.getProperty("user.dir"));
            		jfc.setCurrentDirectory(workingDirectory);

            		int returnValue = jfc.showOpenDialog(null);

            		if (returnValue == JFileChooser.APPROVE_OPTION) {
            			
            			curArgs[0]= jfc.getSelectedFile().getAbsolutePath();
            			txtName = jfc.getSelectedFile().getName();
            			
            			fi1 = new JLabel(txtName);
            			
            			row[0].add(fi1);
            			add(row[0]);
            			setVisible(true);
            		}     
            }
        }); 

        inputOpt1.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent event) {
                String content = inputOpt1.getText();
                option1 = content;
            }           
        });

        inputOpt2.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent event) {
                String content = inputOpt2.getText();
                option2 = content;
            }           
        });
        
        inputOpt3.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent event) {
                String content = inputOpt3.getText();
                maxLength = Integer.parseInt(content);
            }           
        });

        button.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent event) {
            	
                try{
                	PrintWriter pw = new PrintWriter(new FileWriter("Txt_to_pdb_" + txtName + ".txt", false));
                	pw.println("This is the .pdb file to be printed to");

                	try{
                		printToFile(txtName, pw);
                	}

                    System.out.println("write done");

                	}finally{
                		pw.close();
                        graph2.setVisible(true);
                	}
                		
                } catch (IOException e){
                	                	
                    System.exit(-1);
                }
            }
        });   

        button2.setForeground(Color.BLACK);
        button2.setBackground(Color.WHITE);
        button3.setForeground(Color.BLACK);
        button3.setBackground(Color.WHITE);

        inputOpt1.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
        inputOpt1.setPreferredSize(regularDimension);
        inputOpt2.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
        inputOpt2.setPreferredSize(regularDimension);
        inputOpt3.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
        inputOpt3.setPreferredSize(regularDimension);
        message.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
        message.setPreferredSize(regularDimension);

        button.setForeground(Color.BLACK);
        button.setBackground(Color.WHITE);
        Border line = new LineBorder(Color.BLACK);
        Border margin = new EmptyBorder(5, 15, 5, 15);
        Border compound = new CompoundBorder(line, margin);
        button2.setBorder(compound);
        button3.setBorder(compound);
        button.setBorder(compound);
        
        row[2].add(button2);
        row[3].add(button3);

        row[6].add(opt1);
        row[7].add(inputOpt1);
        row[8].add(opt2);
        row[9].add(inputOpt2);
        row[10].add(opt3);
        row[11].add(inputOpt3);
        row[12].add(button);

        row[13].add(message);
        for(int i=2;i<14;i++){
            add(row[i]);
        } 
        setVisible(true);
    }

    public static void printToFile(String txtName, Printwriter pw) {
    	try {
    		File pdbfile = new File (pdb1);
            Scanner s = new Scanner (pdbfile);

            int atomNum = 1;

            while(s.hasNextLine()) {
            	String line = s.nextLine();
            	String residue1 = line.substring(0, s.firstIndexOf());
            	line = line.substring(s.firstIndexOf()+1);
            	String residue2 = line.substring(0, s.firstIndexOf());
            	pw.println("ATOM\t" + atomNum + "\t" + "H\tASP\t1\t" + residue1 + "\t" + residue2);
            }
    	}
    }
}