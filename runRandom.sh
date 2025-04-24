for i in $(seq 1 9); do
    for j in $(seq 0 2); do
        for k in $(seq 0 9); do 
            ./runProgram.sh -f input/random-n20-a6-d25/t${i}/${j}${k}-n20-a6-d25-t${i}-k2.xml -s FC -u LX >> outputFC-FCCBJ.csv
        done
    done
done
