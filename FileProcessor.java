import java.io.*;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;

public class FileProcessor {

    /* the "hashtagMap" hashmap contains the hashtag name as the key and value
    as the node corresponding to the node for this hashtag in the fibonacci heap
     */
    LinkedHashMap<String, Node>
            hashtagMap = new LinkedHashMap<>();
    final String compareToHash = "#";
    final String compareToStop = "stop";

    FibonacciHeapImpl fibonacci;
    FileOutputStream fos = null;
    BufferedWriter bw = null;

    BufferedReader br = null;

    File f = null;

    /**
     * reads the input file line by line
     * @param writeToFile : boolean indicating if we need to write output to file or not
     * @param inputPath: input file
     * @param outputPath: output  file
     * @throws IOException
     */
    public void read(boolean writeToFile, String inputPath, String outputPath) throws IOException {
        try {
            f = new File(inputPath);

            String temp;
            try {
                br = new BufferedReader(new java.io.FileReader(f));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            // reading the input file line by line
            temp = br.readLine();
            String input[];
            Node node;

            while (temp != null && !temp.isEmpty()) {

                // if the input line starts with hash
                if (temp.startsWith(compareToHash)) {

                    input = temp.split("\\s+");
                    String key = input[0];

                    // check if the "hashtagMap" has already this hashtag name
                    if (hashtagMap.containsKey(input[0])) {

                        /* if the "hashtagMap" already has this hashtag name as key of the hashmap, then call the
                         increase key operation rather than insert */
                        fibonacci.increaseKey(hashtagMap.get(key), Integer.valueOf(input[1]));

                    } else {
                        // if it is a new hashtag name, call insert node operation
                        node = fibonacci.insert(key, Integer.valueOf(input[1]));

                        // put the newly created node in the hashmap for checking in future
                        hashtagMap.put(key, node);
                    }
                } else if (temp.equalsIgnoreCase(compareToStop)) {

                    //"stop" keyword is encountered, stop the program
                    System.exit(0);

                } else {

                    // this else logic takes care when the input line in the file is a integer "n"
                    int count = Integer.valueOf(temp);

                    // returns a list of removed max nodes by calling remove max multiple times
                    LinkedHashSet<Node>
                            toBeInserted = getListOfMaxValues(count);

                    StringBuffer sb = new StringBuffer();
                    int size = toBeInserted.size();
                    for (Node val : toBeInserted) {
                        size--;
                        if (size != 0) {
                            sb.append(val.key.replace("#", "") + ",");
                        } else {
                            sb.append(val.key.replace("#", ""));
                        }
                    }
                    // write output to file
                    if (writeToFile) {
                        writeToFile(sb, outputPath);

                    } else {
                        // write output to console
                        System.out.println(sb);
                    }

                    // reinsert into the fibonacci heap the removed nodes
                    for (Node tempNode : toBeInserted) {
                        node = fibonacci.insert(tempNode.key, tempNode.valueCount);
                        hashtagMap.put(node.key, node);
                    }
                }
                // read next line in a while loop
                temp = br.readLine();
            }
        } catch (Exception ex) {
            ex.printStackTrace();

        } finally {
            br.close();
        }
    }

    /**
     * calls the remove max operation "count" times
     * @param count : the number of times remove max has to be called
     * @return : return a list of removed max nodes from the fibonacci heap
     */
    private LinkedHashSet<Node> getListOfMaxValues(int count) {
        LinkedHashSet<Node>
                nodeSet = new LinkedHashSet<>();
        Node node;
        // calls remove max n (count times)
        while (count != 0) {
                node = fibonacci.removeMax();
                if(node == null)
                {
                    break;
                }
                nodeSet.add(node);
                hashtagMap.remove(node.key);
                count--;
            }
        return nodeSet;
    }

    /**
     * writes the ouptut to the file
     * @param line : the line to be written to the file
     * @param fileLocation : the output file location including the name of the file (by default the location of output file is current directory)
     */
    private void writeToFile(StringBuffer line, String fileLocation) {
        try {
            if(fos == null || bw == null) {
                fos = new FileOutputStream(fileLocation);
                bw = new BufferedWriter(new OutputStreamWriter(fos));
            }
            bw.write(line.toString());
            bw.newLine();
        } catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            try {
                bw.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
