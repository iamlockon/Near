package com.example.jay.fragmentbasics;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * Created by armorsun on 2015/8/2.
 * this is a data provider
 * its stored the professional field data
 */
public class proFieldDataProvider {

    public static LinkedHashMap<String,List<String>> getInfo(){

        LinkedHashMap<String,List<String>> proFieldData=new LinkedHashMap<String,List<String>>();

        List<String> ComputerSciences=new ArrayList<String>();
        ComputerSciences.add("C++/C Programming");
        ComputerSciences.add("Swift Programming");
        ComputerSciences.add("Java Programming");
        ComputerSciences.add("Perl Programming");
        ComputerSciences.add("Web Design");
        ComputerSciences.add("Big Data");
        ComputerSciences.add("Arduino");
        ComputerSciences.add("System Analysis");

        List<String> Design=new ArrayList<String>();
        Design.add("Graphic Design");
        Design.add("3D Modeling");
        Design.add("Product Design");

        List<String> Economics=new ArrayList<String>();
        Economics.add("Microeconomics");
        Economics.add("Macroeconomics");

        List<String> Mechanics=new ArrayList<String>();
        Mechanics.add("Kinematics Analysis");

        List<String> Psychology=new ArrayList<String>();
        Psychology.add("Psychological Assessment");

        List<String> Photography=new ArrayList<String>();
        Photography.add("Chromatology");

        List<String> Others=new ArrayList<String>();

        //put those List into Hashmap
        proFieldData.put("Computer Sciences", ComputerSciences);
        proFieldData.put("Design", Design);
        proFieldData.put("Economics", Economics);
        proFieldData.put("Mechanics", Mechanics);
        proFieldData.put("Psychology", Psychology);
        proFieldData.put("photography", Photography);
        proFieldData.put("others", Others);

        return proFieldData;
    }
}
