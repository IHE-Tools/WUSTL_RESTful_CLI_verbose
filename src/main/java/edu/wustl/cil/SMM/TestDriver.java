package edu.wustl.cil.SMM;

import edu.wustl.cil.SMM.jackson.LinkedTransactionTestEngine;

public class TestDriver {

    public static void main(String[] args) {
        if (args.length == 0) printUsageAndDie();
        String[] cropped = cropArguments(args);

        switch (args[0]) {
            case "360X":
            case "360x":
                execute360X(cropped);
                break;
            default:
                printUsageAndDie();
                break;
        }

    }

    public static void printUsageAndDie() {
        System.out.println("" +
                "Usage: TEST-VERB [options] [arguments] \n" +
                "       where TEST-VERBS are: \n" +
                "         360X"
        );
        System.exit(1);
    }

    public static String[] cropArguments(String[] args) {
        String[] newArray = new String[args.length-1];
        for (int i = 1; i < args.length; i++) {
            newArray[i-1] = args[i];
        }
        return newArray;
    }

    public static void execute360X(String[] args) {
        LinkedTransactionTestEngine engine = new LinkedTransactionTestEngine();
        engine.main(args);
    }
}
