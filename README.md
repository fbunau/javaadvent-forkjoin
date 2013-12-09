#### What is this?

Code for [this article](http://www.javaadvent.com/2013/12/applying-forkjoin-from-optimal-to-fast.html)
 
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

#### Results on my FC16 machine after a clean reboot:
i5-2500 CPU @ 3.30GHz
 
JDK7_u45
 
```
Doing 4 runs for each of the 3 processors. Pls wait ...
TaskProcessorSimple: 7963
TaskProcessorSimple: 7757
TaskProcessorSimple: 7748
TaskProcessorSimple: 7744
TaskProcessorPool: 3933
TaskProcessorPool: 2906
TaskProcessorPool: 4477
TaskProcessorPool: 4160
TaskProcessorFJ: 2498
TaskProcessorFJ: 2498
TaskProcessorFJ: 2524
TaskProcessorFJ: 2511
Test completed.
```


