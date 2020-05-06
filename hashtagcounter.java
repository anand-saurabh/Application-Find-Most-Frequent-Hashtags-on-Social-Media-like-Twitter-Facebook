import java.io.IOException;

public class hashtagcounter {

    /**
     *  the main entry point of the program
     * @param args: refer to the arguments passed to the program
     * @throws IOException
     */

    public static  void main(String args[]) throws IOException {
        FibonacciHeapImpl
                ob = new FibonacciHeapImpl();
        boolean writeToFile = false;
        String inputFile = "";
        String outputFile = "";
        if(args.length == 0)
        {
            // give error when no input file or no arguments is passed in the program arguments
            System.out.println("No Input File Specified");
        }
        // only when input filename is passed in the program arguments i.e. no of args = 1
        else if(args.length == 1) {
            writeToFile = false;
            inputFile = args[0];

        } else {
            // when both input file and output file name is passed in the program arguments
            writeToFile = true;
            inputFile = args[0];
            outputFile = args[1];
        }

        FileProcessor fileProcessor = new FileProcessor();
        fileProcessor.fibonacci = ob;
        // call the file processor to read the input file
        fileProcessor.read(writeToFile, inputFile, outputFile);
    }

}
