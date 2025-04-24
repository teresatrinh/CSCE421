# Homework 5
## Teresa Trinh

This file contains java code to read in an .xml file of a csp and conduct searching algorithms, namely BT, CBJ, and FC according to Prosser's paper. 

Use the runProgram.sh file with -f flag for the file name, -s flag for the search algorithm, and -u flag for ordering heuristic.

## Performance of Various Ordering Heuristics on FC
For smaller problems, the performanc (CPU Time) of static ordering compared to dynamic ordering is about the same. It mostly depends on the problem itself rather than the ordering type. However, for larger problems, static ordering is better on a performance standpoint. 

For individual ordering heuristics like lexicographic, least domain, degree, and degree domain, the performance (CPU Time) depends on the problem. However, the most commonly expensive ordering heuristic is degree. Usually, the performance is close to another ordering heuristics which again the specific heuristic is dependent on the problem itself. 

## Performance of Various Ordering Heuristics on FCCBJ
For static variable ordering, for most instances, the least domain and the degree ordering it more computationally expensive (CPUtime) compared to the other two orderings. For smaller instances, the difference is not that great, but for larger instances, the difference is more noticeable. 

For dynamic variable ordering, degree ordering timed out the most compared to the other orderings. However, the least domain and degree-domain also did timeout depending on the instance. This may be due to calculating degree to be computationally expensive compared to calculating other heuristics. 

## Comparing BT, CBJ, FC, and FCCBJ for each instance
### 3 queens
For the 3 queens instance in both conflicts and intension, BT and CBJ were both more computationally expensive compared to FC and FC-CBJ. BT and CBJ have similar #CC, #NV, and #BT, while the same can be said for FC and FC-CBJ. The implementation of forward checking decreases computation needed for this instance. 

Comparing the dynamic ordering between FC and FC-CBJ, the #CC, #NV, and #BT are the same for the same ordering heuristics. However, the CPUtime for FC-CBJ is greatly reduced compared to FC. 

### 4 queens
For the 4 queens instance with static ordering, BT and CBJ were computationally less expensive than FC and FC-CBJ, especially for finding one solution. As for finding all solutions, the better algorithm is determined by how the constraints are modeled (conflicts vs. supports). Throughout all algorithms implemented, the number of solutions for the 4queens-support instance is wrong. 

For dynamic ordering, FC and FC-CBJ are computationally very similar especially for finding one solution and all solutions for the 4queens-conflicts. For the 4queens-supports instance, FC-CBJ is better at quickly finding the solutions (even though it did find more solutions than true).

### 5 queens
For static ordering 5 queens problem, FC was the best at finding one and all solutions. BT is slightly better for this instance than CBJ, but still worse than FC-CBJ. FC-CBJ had stats exactly like FC. However, it took more CPUtime to fully go through the entire FC-CBJ algorithm compared to FC. 

For dynamic ordering 5 queens problem, FC and FC-CBJ were comparable for all ordering except for the degree. FC-CBJ timedout for degree ordering. However, FC was still able to find the same number of solutions (albeit less than the true number of solutions) in a much shorter time. 

### 6 queens
For finding one solution, CBJ was the best for all orderings and both conflicts and intension instances. However, for finding all solutions, FC-CBJ is slightly better as it found them in significantly smaller stats. However, FC was close to FC-CBJ, but lost out with more constraint checks, nodes visited, and backtracks (the CPUtime was less than FC-CBJ). 

For dynamic ordering with both FC and FC-CBJ, lexicographic and degree-domain ordering were both able to quickly terminate while finding the correct number of solutions. For FC, degree and least domain were both incorrect with the number of solutions. On the other hand, for FC-CBJ the degree and least domain ordering both timed out with finding only 1 solution. However, the first solution was found relatively quickly but the second solution was could not be found in FC-CBJ for these two orderings. 

### 20_8_100_20
Only FC and BT were able to find the correct number of solutions for this instance with static ordering. In this case, FC was computationally better than BT. 

For dynamic ordering, only FC with lexicographic ordering was able to get to the correct number of solutions. However with other orderings, FC was not able to find any solutions, so in this aspect FC-CBJ was slightly better finding 3 solutions before timing out for the degree-domain ordering (timing out for the other two orderings). This means FC-CBJ had the prospect of finding the other solutions if given enough time or computational power. 

### Chain 4
For static ordering, BT was the best at finding both one and all solutions. FC and FC-CBJ were almost exactly the same. Lastly, CBJ was the worst, with extra constraint checks, nodes visited, and backtracks to only find all solutions to be 1. 

For dynamic ordering, FC and FC-CBJ were virtually the same for all orderings except for degree ordering. For FC, it was not able to find a solution, while FC-CBJ timed out without finding a single solution. 

### Color Australia
For static ordering, only FC and BT were able to find all solutions, with BT being slightly better except for the degree ordering. 

For dynamic ordreing, FC was able to correctly find all solutions for lexicographic ordering. FC-CBJ was not able to find the correct number of solutions, but it did not time out for any ordering or constraint model. 

### Color K4
BT was the best at finding one and all solutions for all 4 static orderings. FC and FC-CBJ were very similar with FC-CBJ being slightly better. 

For dynamic ordering, FC-CBJ was notably better than FC. 

### Zebra
For static version of zebra, the only instances where the correct number of solutions was FC and BT for the intension binary instance. This may be due to supports and extension being nonbinary, which is not strongly supported by the parser and solver. 

For dynamic ordering, FC timed out half of the time, especially with the supports instance. FC-CBJ only timed out less than half of the time in both the extension and intension binary constraint instances. The correct number of solutions found was for FC with the intension binary constraint model with lexicographic ordering. The other times the problem was ran through both FC and FC-CBJ, not all solutions were found (or more than total solutions).

