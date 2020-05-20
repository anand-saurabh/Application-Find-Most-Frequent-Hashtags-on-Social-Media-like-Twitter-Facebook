# Application-Find-Most-Frequent-Hashtags-on-Social-Media-like-Twitter-Facebook


output for input file "sampleInput.txt" (present in  input_output files folder) :

&nbsp;

![](output/fibo_output.JPG)

Problem description:

You are required to implement a system to find the n most popular hashtags that appear on social media such as Facebook or Twitter. For the scope of this project hashtags will be given from an input file.
Basic idea for the implementation is to use a max priority structure to find out the most popular hashtags.
You must use the following structures for the implementation.
1. Max Fibonacci heap: use to keep track of the frequencies of hashtags.
2. Hash table: The key for the hash table is the hashtag, and the value is the pointer to the corresponding node in the Fibonacci heap.
You can assume there will be a large number of hashtags appearing in the stream and you need to perform increase key operation many times. Max Fibonacci heap is recommended because it has an amortized complexity of O(1) for the increase key operation. You should implement all Fibonacci heap functions discussed in class. For the hash table, you can use an implementation from any library that your programming languages supports (e.g., STL)

To run the program in Java type below command:
$java hashtagcounter <input_file_name> <output_file_name>

Input Format:

Hashtags appear one per line in the input file and start with # sign. After the hashtag an integer will appear and that is the count of the hashtag (There is a space between hashtag and the integer). You need to increment the hashtag frequency by that count. Queries will also appear in the input file and once a query appears you should append the answer to the query to the output file. A query appears as an integer number (n) without # sign in the beginning. The answer to the query n is n hashtags with the highest frequency. These should be written to the output file. An input line with the word “stop” (without hashtag symbol) causes the program to terminate. The following is an example of an input file.
#saturday 5
#sunday 3
#saturday 10
#monday 2
#reading 4
#playing_games 2
#saturday 6
#sunday 8
#friday 2
#tuesday 2
#saturday 12
#sunday 11
#monday 6
3
#saturday 12
#monday 2
#stop 3
#playing 4
#reading 15
#drawing 3
#friday 12
#school 6
#class 5
5
stop

Output Format:
For each query n, you need to write the n most popular hashtags (i.e., highest frequency) to the output file in descending order of frequency (ties may be broken arbitrarily). The output for a query should be a comma separated list occupying a single line in the output “output_file.txt”. There should be no space character after the commas.
Following is the output file for the above input file.
saturday,sunday,monday
saturday,sunday,reading,friday,monday
