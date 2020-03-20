/**
 * Contains static routines for solving the problem of balancing m jobs on p processors
 * with the constraint that each processor can only perform consecutive jobs.
 */
public class LoadBalancing {

    /**
     * Checks if it is possible to assign the specified jobs to the specified number of processors such that no
     * processor's load is higher than the specified query load.
     *
     * @param jobSize the sizes of the jobs to be performed
     * @param queryLoad the maximum load allowed for any processor
     * @param p the number of processors
     * @return true iff it is possible to assign the jobs to p processors so that no processor has more than queryLoad load.
     */
    public static boolean feasibleLoad(int[] jobSize, int queryLoad, int p) {
        // TODO: Implement this
        boolean feasibility = true;
        for(int i = 0; i < jobSize.length; i++){
            if(jobSize[i] > queryLoad){
                feasibility = false;
                break;
            }
        }
        if(feasibility){
            int a = 0;
            int processorCount = 0;
            int soFar = jobSize[a];
            do {
                if(jobSize.length == 1){
                    feasibility = true;
                    break;
                } else if (a + 2 == jobSize.length){
                        if(soFar + jobSize[ a + 1] <= queryLoad){
                            a = a + 2;
                            processorCount++;
                        } else {
                            a = a + 2;
                            processorCount = processorCount + 2;
                        }
                    } else if (soFar + jobSize[a+1] <=queryLoad){
                        soFar =soFar + jobSize[a+1];
                        a++;
                    } else {
                        processorCount++;
                        soFar = 0;
                    }
                } while (a < jobSize.length);

                if (processorCount <= p){
                    return true;
                } else {
                    return false;
                }
            } else {
                return false;
            }
    }

    /**
     * Returns the minimum achievable load given the specified jobs and number of processors.
     *
     * @param jobSize the sizes of the jobs to be performed
     * @param p the number of processors
     * @return the maximum load for a job assignment that minimizes the maximum load
     */
    public static int findLoad(int[] jobSize, int p) {
        if(p == 1){
            return summation (jobSize, 0, jobSize.length - 1);
        } else {
            int start = 0;
            int end = jobSize.length - 1;
            int sumEnd = summation(jobSize, start, end);
            int sumStart = 0;
            do{
                int mid = sumStart + (sumEnd - sumStart) / 2;
                if(feasibleLoad(jobSize, mid, p)){
                    sumEnd = (sumStart + sumEnd) / 2;
                } else {
                    sumStart = ((sumEnd + sumStart) / 2) + 1;
                }
            } while (sumStart < sumEnd);
            System.out.println(sumStart);
            return sumStart;

        }
    }
    public static int summation(int[] arr, int pos1, int pos2){
        int res = 0;
        for(int i = pos1; i<=pos2; i++){
            res +=arr[i];
        }
        return res;
    }

    // These are some arbitrary testcases.
    public static int[][] testCases = {
            {1, 3, 5, 7, 9, 11, 10, 8, 6, 4},
            {67, 65, 43, 42, 23, 17, 9, 100},
            {4, 100, 80, 15, 20, 25, 30},
            {2, 3, 4, 5, 6, 7, 8, 100, 99, 98, 97, 96, 95, 94, 93, 92, 91, 90, 89, 88, 87, 86, 85, 84, 83},
            {7}
    };

    /**
     * Some simple tests for the findLoad routine.
     */
    public static void main(String[] args) {
        for (int p = 1; p < 30; p++) {
            System.out.println("Processors: " + p);
            for (int[] testCase : testCases) {
                System.out.println(findLoad(testCase, p));
            }
        }
    }
}
