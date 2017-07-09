#!/bin/bash
# Execute family approach

programName=$1
iterations=$2
entry=$3
counter=1

while [ ${counter} -le ${iterations} ]
do
    java -cp /Users/mvelezce/Documents/Programming/Java/Projects/performance-mapper-evaluation/family/${programName}/bin:/Users/mvelezce/Documents/Programming/Java/Projects/performance-mapper/lib/aspectjrt.jar:/Users/mvelezce/Documents/Programming/Java/Projects/performance-mapper/lib/choco-solver-2.1.4.jar:/Users/mvelezce/Documents/Programming/Java/Projects/performance-mapper/lib/java-cup-11a.jar:/Users/mvelezce/Documents/Programming/Java/Projects/performance-mapper/lib/jpf-bdd-annotations.jar:/Users/mvelezce/Documents/Programming/Java/Projects/performance-mapper/lib/jpf-classes.jar:/Users/mvelezce/Documents/Programming/Java/Projects/performance-mapper/lib/junit-4.11.jar:/Users/mvelezce/Documents/Programming/Java/Projects/performance-mapper/lib/xstream-1.4.4.jar ${entry}
    mv measurement.txt /Users/mvelezce/Documents/Programming/Java/Projects/performance-mapper/src/main/resources/performance-model/java/programs/family/${programName}_${counter}.txt
    counter=$(($counter + 1))
done

