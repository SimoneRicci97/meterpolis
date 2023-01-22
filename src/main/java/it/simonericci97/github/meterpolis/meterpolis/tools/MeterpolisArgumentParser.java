package it.simonericci97.github.meterpolis.meterpolis.tools;

import org.apache.commons.cli.*;

import java.util.regex.Pattern;

public class MeterpolisArgumentParser {

    private static final String METROPOLIS_NAME_ARGUMENT_KEY_SHORT = "mp";
    private static final String METROPOLIS_NAME_ARGUMENT_KEY_LONG = "metropolis";

    private Options options;

    public MeterpolisArgumentParser() {
        this.options = new Options();

        Option input = new Option(METROPOLIS_NAME_ARGUMENT_KEY_SHORT, METROPOLIS_NAME_ARGUMENT_KEY_LONG, true, "comma separated metropolis to analyze ");
        input.setRequired(true);
        options.addOption(input);
    }

    public String[] parse(String[] args) {
        CommandLineParser parser = new DefaultParser();
        HelpFormatter formatter = new HelpFormatter();
        CommandLine cmd = null;//not a good practice, it serves it purpose

        try {
            cmd = parser.parse(options, args);
        } catch (ParseException e) {
            System.out.println(e.getMessage());
            formatter.printHelp("meterpolis-batch", options);

            System.exit(1);
        }

        String meterpolis = cmd.getOptionValue(METROPOLIS_NAME_ARGUMENT_KEY_LONG);

        if(!Pattern.matches("(\\w+,)*(\\w+)", meterpolis)) {
            throw new IllegalArgumentException(meterpolis);
        }

        return meterpolis.split(",");
    }
}
