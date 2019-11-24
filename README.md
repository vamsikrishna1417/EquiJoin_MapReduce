# EquiJoin_MapReduce
This is a map-reduce program that will perform EquiJoin.

• The code is in Java using Hadoop Framework.

• The code takes two inputs, one is the hdfs location of the file on which the equijoin should be performed and other is the hdfs location of the file, where the output should be stored.

## Approach:

MAPPER : The input is read from the file loaded into HDFS line by line and creates a key-value pair. Here the key is the join column and the value has the whole line.

REDUCER : we split the tuples into relations in this phase based on the table name. Then based on joining key, if key matches, the tuples of S are added to tuples of R and if they exist in only one of the tuple, it is ignored. The final output of EquiJoin is written in output hdfs file.

DRIVER : Driver is the function which calls the required classes and gets the input directory and optput directory from the input arguments and runs the code. The result file can be downloaded from the hadoop localhost webpage.

## Instructions to test the code:

Add the necessary Hadoop dependencies in the project before running the program.<br />
copy the .java file to a folder, open a terminal and follow the below commands to test the application:<br />
mkdir equijoin_classes <br />
mkdir inputMapReduce <br />
cp input inputMapReduce/ <br />
javac -d equijoin_classes/ equijoin.java -cp $(hadoop classpath)                         ---- To compile <br />
jar -cvf equijoin.jar -C equijoin_classes/ .                                             ---- creating a jar file <br />
$HADOOP_HOME/sbin/start-dfs.sh                                                           ---- starting the cluster <br />
$HADOOP_HOME/sbin/start-yarn.sh     <br />
$HADOOP_HOME/bin/hdfs dfs -copyFromLocal inputMapReduce/ /                               ---- copying folder that contains input to hadoop filesystem <br />
$HADOOP_HOME/bin/hadoop jar equijoin.jar equijoin /inputMapReduce /output_equijoin       ---- running the application ( {pathtohadoopExec} jar {jarfilename} {classwithmainfunction} {inputfolder}                                                                                                                         {outputfolder} ) <br />
$HADOOP_HOME/bin/hdfs dfs -cat /output_equijoin/part-00000                               ---- see the Output <br />

/* Stopping the cluster and deleting the files created */  <br />
$HADOOP_HOME/sbin/stop-dfs.sh   <br />
$HADOOP_HOME/sbin/stop-yarn.sh   <br />
rm -rf equijoin.jar equijoin_classes/   <br />
hadoop dfs -rm -r -f /output_equijoin    <br />
