package com.kivotos.fairdivision.util;

import java.util.ArrayList;
import java.util.List;

public class MaximumMatching {

    private static boolean bpm(boolean bpGraph[][], int u, boolean seen[], int matchR[], int goods)
    {
        // Try every job one by one
        for (int v = 0; v < goods; v++)
        {
            // If applicant u is interested
            // in job v and v is not visited
            if (bpGraph[u][v] && !seen[v])
            {

                // Mark v as visited
                seen[v] = true;

                // If job 'v' is not assigned to
                // an applicant OR previously
                // assigned applicant for job v (which
                // is matchR[v]) has an alternate job available.
                // Since v is marked as visited in the
                // above line, matchR[v] in the following
                // recursive call will not get job 'v' again
                if (matchR[v] < 0 || bpm(bpGraph, matchR[v], seen, matchR, goods))
                {
                    matchR[v] = u;
                    return true;
                }
            }
        }
        return false;
    }

    // Returns maximum number
    // of matching from M to N
    public static List<int[]> maxBPM(boolean adjacencyMatrix[][], int agents, int goods)
    {

        List<int[]> resultList =  new ArrayList<>();

        // An array to keep track of the
        // applicants assigned to jobs.
        // The value of matchR[i] is the
        // applicant number assigned to job i,
        // the value -1 indicates nobody is assigned.
        int matchR[] = new int[goods];

        // Initially all jobs are available
        for(int i = 0; i < goods; ++i)
            matchR[i] = -1;

        // Count of jobs assigned to applicants
        int result = 0;
        for (int u = 0; u < agents; u++)
        {
            // Mark all jobs as not seen
            // for next applicant.
            boolean seen[] =new boolean[goods] ;
            for(int i = 0; i < goods; ++i)
                seen[i] = false;

            // Find if the applicant 'u' can get a job
            if (bpm(adjacencyMatrix, u, seen, matchR,goods)) {
                result++;
            }
        }


        for (int v = 0; v < goods; v++) {
            if (matchR[v] != -1) {
                resultList.add(new int[]{matchR[v], v});
            }
        }

        // Print the matching pairs
        System.out.println("Matching pairs:");
        for (int v = 0; v < goods; v++) {
            if (matchR[v] != -1) {
                System.out.println("Applicant " + matchR[v] + " is matched with Good " + v);
            }
        }

        return resultList;
    }
}
