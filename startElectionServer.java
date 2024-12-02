import election.*;

import election.ElectionData;
import election.ElectionServer;
import election.ElectionServerGUI;

public class startElectionServer {

    public static void main(String[] args) {

        final String [] voteOptions = {"Candidato 1", "Candidato 2", "Candidato 3", "Candidato 4"};
        final String question = "Digite seu CPF para votar";
        ElectionData electionData = new ElectionData(question, voteOptions);
        ElectionServer server = new ElectionServer(electionData);
        ElectionServerGUI serverGUI = new ElectionServerGUI(server);
        serverGUI.start();
    }
}
