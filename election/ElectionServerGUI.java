package election;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ElectionServerGUI extends JFrame {

    private static final long serialVersionUID = 1L;
    private ElectionServer server;

    public ElectionServerGUI(ElectionServer server){
        this.server = server;
    }

    public void start() {
        this.setTitle("Status do Servidor");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(400, 200);
        this.setLocationRelativeTo(null);
        this.setLayout(new BorderLayout());

        JLabel statusLabel = new JLabel("Servidor Ativo", SwingConstants.CENTER);
        statusLabel.setFont(new Font("Arial", Font.BOLD, 24));
        statusLabel.setForeground(Color.GREEN);
        this.add(statusLabel, BorderLayout.NORTH);

        JButton closeButton = new JButton("Encerrar Servidor");
        closeButton.setFont(new Font("Arial", Font.BOLD, 20));
        closeButton.setPreferredSize(new Dimension(400, 150));
        closeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                server.closeServer(); 
                System.exit(0);      
            }
        });

        this.add(closeButton, BorderLayout.CENTER);

        this.setVisible(true);
        server.start();
    }
}
