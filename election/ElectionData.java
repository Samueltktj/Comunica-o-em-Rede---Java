package election;

import java.io.Serializable;

import java.util.Arrays;

public class ElectionData implements Serializable {
    private static final long serialVersionUID = 1L;
    @SuppressWarnings("FieldMayBeFinal")
    private String question;
    @SuppressWarnings("FieldMayBeFinal")
    private String[] voteOptions;
    @SuppressWarnings("FieldMayBeFinal")
    private int[] totalVotes; 

    public ElectionData(String question, String[] voteOptions) {
        this.question = question;
        this.voteOptions = voteOptions;
        this.totalVotes = new int[voteOptions.length];
        Arrays.fill(totalVotes, 0);
    }

    public String getQuestion() {
        return question;
    }

    public String[] getVoteOptions() {
        return voteOptions;
    }

    public void addVote(int indexOption) {
        if (indexOption >= 0 && indexOption < totalVotes.length) {
            totalVotes[indexOption]++;
        }
    }

    public int[] getTotalVotes() {
        return totalVotes;
    }
}

