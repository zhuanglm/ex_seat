package com.example.ex_seat;

/**
 * Created by Zhuang on 2016-02-23.
 */
public class DataCollector {
    public static int  m_nTimes=0;
    public static int  m_nAverageRPM=0;
    private static int  m_nMaxRPM;

    public static int getAverageRPM(int nCurRPM){
        int nAverage;

        m_nTimes++;
        nAverage = (m_nAverageRPM*(m_nTimes-1)+nCurRPM) / m_nTimes;
        m_nAverageRPM = nAverage;
        m_nMaxRPM = nCurRPM>m_nMaxRPM?nCurRPM:m_nMaxRPM;

        return nAverage;
    }

    public  static int getMaxRPM(){
        return  m_nMaxRPM;
    }

    public static void init(){
        m_nTimes = 0;
        m_nAverageRPM = 0;
        m_nMaxRPM = 0;
    }
}
