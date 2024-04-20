import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Scanner;

public class TextEditor extends JFrame implements ActionListener {

    JTextArea textArea;
    JScrollPane scroll;
    JLabel font;
    JButton color;
    JComboBox fontBox;
    String[] fonts;
    JMenuBar menuBar;
    JMenuItem save;
    JMenuItem open;
    JMenuItem exit;
    JSpinner size; //contains a single line of input which might be a number or a object from an ordered sequence
    public TextEditor(){
        this.setSize(new Dimension(600, 600));
        this.setTitle("Fancy Text");
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.setLayout(new FlowLayout()); //to arrange components within a container
        this.setLocationRelativeTo(null);

        textArea = new JTextArea();
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        textArea.setBackground(Color.lightGray);
        textArea.setFont(new Font("Arial",Font.PLAIN,32));

        //------- menu
        menuBar = new JMenuBar();
        menuBar.setMargin(new Insets(10, 10, 10, 10));
        menuBar.setBackground(Color.LIGHT_GRAY);

        open = new JMenuItem("OPEN");
        save = new JMenuItem("SAVE");
        exit = new JMenuItem("EXIT");

        open.setBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2));
        open.setMargin(new Insets(2, 2, 2, 2));
        save.setBackground(Color.GRAY);
        
        open.addActionListener(this);
        save.addActionListener(this);
        exit.addActionListener(this);


        menuBar.add(open);
        menuBar.add(save);
        menuBar.add(exit);

        //------- menu


        scroll = new JScrollPane(textArea);
        scroll.setPreferredSize(new Dimension(500, 500));
        scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        scroll.setAlignmentX(100);
        size = new JSpinner();
        size.setPreferredSize(new Dimension(70, 20));
        size.addChangeListener(new ChangeListener() { // to change font size
            @Override
            public void stateChanged(ChangeEvent e) {
                textArea.setFont(new Font(textArea.getFont().getFamily(), Font.PLAIN, (int) size.getValue()));
            }
        });

        this.setJMenuBar(menuBar);

        fonts = GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();
        fontBox = new JComboBox(fonts);
        fontBox.addActionListener(this);
        fontBox.setSelectedItem("Arial");

        font = new JLabel("Font: ");
        color = new JButton("Color: ");
        color.addActionListener(this);
        this.add(font);
        this.add(size);
        this.add(scroll);
        this.add(color);
        this.add(fontBox);
        this.setVisible(true);

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == color){
            Color c = JColorChooser.showDialog(null, "Color", Color.BLACK);
            textArea.setForeground(c);
        }
        if(e.getSource() == fontBox){
            textArea.setFont(new Font((String)fontBox.getSelectedItem(), Font.PLAIN, (int)textArea.getFont().getSize()));
            fontBox.setSelectedItem(fonts[0]);
        }

        if(e.getSource() == open){
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setCurrentDirectory(new File("."));
            int response = fileChooser.showOpenDialog(null);
            Scanner fileIn = null;
            if(response == JFileChooser.APPROVE_OPTION){
                File file = new File(fileChooser.getSelectedFile().getAbsolutePath());
                try {
                    fileIn = new Scanner(file);
                    if(file.isFile()){
                        while(fileIn.hasNextLine()) {
                            textArea.append(fileIn.nextLine() + "\n");
                        }
                    }
                    fileIn.close();
                } catch (FileNotFoundException ex) {
                    throw new RuntimeException(ex);
                } finally {
                    fileIn.close();
                }


            }
        }
        if(e.getSource() == save){
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setCurrentDirectory(new File("."));

            int response = fileChooser.showSaveDialog(null);
            if(response == JFileChooser.APPROVE_OPTION){
                File file;

                file = new File(fileChooser.getSelectedFile().getAbsolutePath());
                try (PrintWriter fileOut = new PrintWriter(file)) {
                    fileOut.println(textArea.getText());
                } catch (FileNotFoundException ex) {
                    ex.printStackTrace();
                    throw new RuntimeException(ex);
                }
            }

        }
        if(e.getSource() == exit){
            System.exit(0);

        }
    }
}
