package byog.Core;

public class Input {
    public long seed;
    public String actionSequence = "";
    public boolean quitAndSave = false;

    public Input(String inputString) {
        String seed = "";

        int i = 1;
        while(i < inputString.length()) {
            char inputChar = inputString.charAt(i);
            if (Character.isDigit(inputChar)) {
                seed += inputChar;
            } else {
                break;
            }
            i++;
        }

        while (i < inputString.length()) {
            char inputChar = inputString.charAt(i);
            if (inputChar == 'W' || inputChar == 'A'
                    || inputChar == 'S' || inputChar == 'D') {
                this.actionSequence += inputChar;
            } else if (inputChar == ':') {
                if(i+i < inputString.length()
                        && inputString.charAt(i+1) == 'Q') {
                    this.quitAndSave = true;
                }
            }
            i++;
        }

        this.seed = Long.valueOf(seed);
    }
}
