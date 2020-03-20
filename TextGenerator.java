import java.io.FileInputStream;

/**
 * This class is used to generated text using a Markov Model
 */
public class TextGenerator {

    // For testing, we will choose different seeds
    private static long seed;

    // Sets the random number generator seed
    public static void setSeed(long s)
    {
        seed = s;
    }

    /**
     * buildModel rads in the file and builds the MarkovModel
     * @param order
     * @param fileName
     * @param model
     * @return
     */
    public static String buildModel(int order, String fileName, MarkovModel model)
    {
        // Get ready to parse the file.
        //StringBuffer is used instead of String as appending character to String is slow
        StringBuffer text = new StringBuffer();

        // Loop through the text
        try {
            FileInputStream inputStream = new FileInputStream(fileName);

            // Determine the size of the file, in bytes
            int fileSize = inputStream.available();

            // Read in the file, one character at a time.
            for (int i=0; i<fileSize; i++)
            {
                // Read a character
                char c = (char)inputStream.read();
                text.append(c);
            }

            //Make sure that length of input text is longer than requested Markov order
            if(text.length() < order)
            {
                System.out.println("Text is shorter than specified Markov Order.");
                return null;
            }
        } catch(Exception e){
            System.out.println("Problem reading file " + fileName + ".");
            return null;
        }

        // Build Markov Model of order from text
        model.initializeText(text.toString());
        return text.substring(0, order);
    }

    /**
     * generateText outputs to stdout text of the specified length based on the specified seedText
     * using the given Markov Model.
     * @param model
     * @param seedText
     * @param order
     * @param length
     */
    public static void generateText(MarkovModel model, String seedText, int order, int length){
        // Use the first order characters of the text as the starting string
        StringBuffer kgram = new StringBuffer();
        kgram.append(seedText);

        // Generate length characters
        char charToAppend = MarkovModel.NOCHARACTER;
        int outLength = kgram.length();
        while (outLength < length)
        {
            // Get the next character from kgram sequence. The kgram sequence to use
            // is the sequence starting from ith position.
            charToAppend = model.nextCharacter(kgram.substring(outLength - order));

            //if there is no next character, restart generation with initial kgram value which
            //starts from 0th position.
            if(charToAppend != MarkovModel.NOCHARACTER) {
                kgram.append(charToAppend);
                outLength++;
            }
            else{
                // This prefix has never appeared in the text.
                // Give up?
                System.out.println(kgram);
                return;
            }
        }

        //output the generated characters, not including the initial seed
        System.out.println(kgram);
    }

    /**
     * The main routine.  Takes 3 arguments:
     * 1. the order of the Markov Model
     * 2. the length of the text to generate
     * 3. the filename for the input text
     * @param args
     */
    public static void main(String[] args)
    {
        // Check that we have three parameters
        if (args.length != 3)
        {
            System.out.println("Number of input parameters are wrong.");
        }

        // Get the input:
        int order = Integer.parseInt(args[0]);
        int length = Integer.parseInt(args[1]);
        String fileName = args[2];

        // Create the model
        MarkovModel markovModel = new MarkovModel(order, seed);
        String seedText = buildModel(order, fileName, markovModel);

        // Generate text
        generateText(markovModel, seedText, order, length);
    }
}