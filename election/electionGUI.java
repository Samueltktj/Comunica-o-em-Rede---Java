package election;

import java.awt.*;
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.Socket;

import javax.swing.*;


public class electionGUI extends JFrame implements ActionListener {

    private static final long serialVersionUID = 5924196330829656017L;

    
    private final String title = "Projeto II - Eleicao em Servidor";
    private final String [] voteOptions = {"Candidato 1", "Candidato 2", "Candidato 3", "Candidato 4"};
    private final String question = "Digite seu CPF para votar";

    private final JPanel connectPanel   = new JPanel();
        private final JButton connectButton = new JButton("Conectar");


    private final JPanel votePanel      = new JPanel();
        private final JLabel guideLabel  = new JLabel(question);
        private final JTextArea documentArea = new JTextArea();
        private final JPanel voteButtonsPanel = new JPanel();

    private final JPanel resultsPanel   = new JPanel();
        private final JTextArea resultsText = new JTextArea();
    
    private final JSplitPane documentButtonsSplitPanel = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
    private final JSplitPane voteResultsSplitPanel = new JSplitPane(JSplitPane.VERTICAL_SPLIT);

    private final int screenWidth   = (int) (Toolkit.getDefaultToolkit().getScreenSize().getWidth() * 0.35);
    private final int screenHeight  = (int) (Toolkit.getDefaultToolkit().getScreenSize().getHeight() * 0.35);

    ElectionData electionData;


    
    public void setupElectionInGUI() {
        electionData = new ElectionData(question, voteOptions);
    
        for (int i = 0; i < electionData.getVoteOptions().length; i++) {
            JButton newCandidateButton = new JButton(electionData.getVoteOptions()[i]);
            int voteIndex = i;
            
            newCandidateButton.addActionListener(e -> {
                String cpf = documentArea.getText();
                if (cpf.length() == 11) { 
                    String result = sendVoteToServer(cpf, voteIndex);
                    resultsText.setText(result);
                    updateResults();
                } else {
                    resultsText.setText("CPF inválido. Insira um CPF com 11 dígitos.");
                }
            });
            voteButtonsPanel.add(newCandidateButton);
        }
    
        
    }
        

    public void SetupGUI() {

        setupElectionInGUI();

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        this.setTitle(title);
        this.setSize(screenWidth,screenHeight);
        this.setLocationRelativeTo(null);

        connectButton.setBounds((int) (screenWidth*0.35),(int) (screenHeight*0.425), (int) (screenWidth*0.25),(int) (screenHeight*0.15));
        
        connectPanel.setLayout(null);
        connectPanel.add(connectButton);
        this.add(connectPanel);

        documentArea.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                char c = e.getKeyChar();
                if (!Character.isDigit(c) || documentArea.getText().length() >= 11) {
                    e.consume(); 
                }
            }
        });


        votePanel.setLayout(null);
        guideLabel.setBounds(10, 10, 200,20);
        documentArea.setBounds(10, 30, 80,20);
        votePanel.add(guideLabel);
        votePanel.add(documentArea);

        resultsPanel.setLayout(new BorderLayout());
        resultsPanel.add(resultsText, BorderLayout.EAST);

        documentButtonsSplitPanel.setTopComponent(votePanel);
        documentButtonsSplitPanel.setBottomComponent(voteButtonsPanel);
        documentButtonsSplitPanel.setDividerLocation(175);

        voteResultsSplitPanel.setLeftComponent(documentButtonsSplitPanel);
        voteResultsSplitPanel.setRightComponent(resultsPanel);
        voteResultsSplitPanel.setDividerLocation(100);
        

        this.setVisible(true);

        connectButton.addActionListener(this);
    }

    private String sendVoteToServer(String cpf, int voteIndex) {
        String serverResponse;
        try (Socket socket = new Socket(ElectionServer.IP, ElectionServer.PORT);
             ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
             ObjectInputStream in = new ObjectInputStream(socket.getInputStream())) {

            out.writeObject(cpf);         
            out.writeObject(voteIndex);   

            serverResponse = (String) in.readObject();  

            if (serverResponse != null) {
                electionData = (ElectionData) in.readObject();  
                resultsText.setText(serverResponse); 
            }
        } catch (IOException | ClassNotFoundException e) {
            serverResponse = "Erro ao conectar com o servidor.";
            e.printStackTrace();
        }

        updateResults();  
        return serverResponse;
    }

    private void updateResults() {
        resultsText.setText(""); 
        for (int i = 0; i < electionData.getVoteOptions().length; i++) {
            resultsText.append(electionData.getVoteOptions()[i] + " = " + electionData.getTotalVotes()[i] + "\n");
        }
    }

    
        

    
    @Override
    public void actionPerformed(ActionEvent e) {
        
        if(e.getSource() == this.connectButton){
            this.remove(connectPanel);
            this.add(voteResultsSplitPanel);

            this.revalidate();
            this.repaint();
        }
        //throw new UnsupportedOperationException("Not supported yet.");
    }
    
}
