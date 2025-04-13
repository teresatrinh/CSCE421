# Homework 5
## Teresa Trinh

This file contains java code to read in an .xml file of a csp and conduct searching algorithms, namely BT, CBJ, and FC according to Prosser's paper. 

Use the runProgram.sh file with -f flag for the file name, -s flag for the search algorithm, and -u flag for ordering heuristic.

### Performance of Various Ordering Heuristics on FC
For smaller problems, the performanc (CPU Time) of static ordering compared to dynamic ordering is about the same. It mostly depends on the problem itself rather than the ordering type. However, for larger problems, static ordering is better on a performance standpoint. 

For individual ordering heuristics like lexicographic, least domain, degree, and degree domain, the performance (CPU Time) depends on the problem. However, the most commonly expensive ordering heuristic is degree. Usually, the performance is close to another ordering heuristics which again the specific heuristic is dependent on the problem itself. 

### Comparing BT, CBJ, and FC
By far, CBJ and BT are much less computationally expensive compared to FC. Even with bigger problems, BT and CBJ are able to solve them accurately within milliseconds, while FC timed out with larger problems. 

For small and large problems, FC is significantly more expensive than BT and CBJ. However, for moderately sized problems, FC is comparable and sometimes even better (less CC, shorter CPU Time) compared to BT and CBJ. 

