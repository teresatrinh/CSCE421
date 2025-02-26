for i in $(seq 2 9); do
    for j in $(seq 0 49); do
        ./runProgram.sh -f input/v32_d8_p20/v32_d8_p20_t${i}0/v32_d8_p20_t${i}0_${j}.xcsp -a ac3 >> output2.csv
    done
done
