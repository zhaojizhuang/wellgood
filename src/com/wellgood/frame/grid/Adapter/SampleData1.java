package com.wellgood.frame.grid.Adapter;

import java.util.ArrayList;
import java.util.List;

public class SampleData1 {

    public static final int SAMPLE_DATA_ITEM_COUNT = 30;

    public static ArrayList<String> generateSampleData() {
        final ArrayList<String> data = new ArrayList<String>(SAMPLE_DATA_ITEM_COUNT);

        for (int i = 0; i < SAMPLE_DATA_ITEM_COUNT; i++) {
            data.add("世贸中心 ipc编号 #0021");
        }

        return data;
    }

}
