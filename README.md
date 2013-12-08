#### What is this?

Code for this article : article link
 
It measures the performance of task processors **Serial** vs **Thread Pool** vs **ForkJoin**.
 
We read tasks types with TaskProducer from tasks.txt file, and generate random data for them.
Then tasks are fed into a processor implementation.

-TaskProcessorFJ
 
-TaskProcessorPool
 
-TaskProcessorSimple
 

Processors execute the task solving it using a TaskSolver. Results are signaled to a TaskResultHandler that counts down on a latch as Tasks get solved. The latch then continues with a performance counter that measures how much it took to solve the tasks.

Main entry point : com/javaadvent/dec9/TestRunner.java

#### To run
`ant clean-build`
 
`java -jar javaadvent-forkjoin.jar`

#### Results on my machine 
i5-2500 CPU @ 3.30GHz



