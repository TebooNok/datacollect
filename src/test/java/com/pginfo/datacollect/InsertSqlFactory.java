package com.pginfo.datacollect;

import java.util.Random;

public class InsertSqlFactory {
    public static void main(String[] args) {

        StringBuilder stringBuilder = new StringBuilder();
        StringBuilder stringBuilder2 = new StringBuilder();
        String origin = "insert into yk_api values([id], [height], [temperature], current_timestamp);";
        String origin2 = "insert into yk_parameter(id,devices_name,state,light_d,light_kt,light_kd,devices_id,light_rt0,light_rt,device_number) values([id],[name], [state],94,98,43,17112700,1540,1543,30);";

        for(int i = 1; i <= 42; i++)
        {
            Random random = new Random();
            int height = -1 * (500 + random.nextInt(2500));
            int abs = random.nextInt(4);
            if(abs == 0) height = Math.abs(height);
            double temperature = (2000 + random.nextInt(1000)) / 100.0;
            stringBuilder.append(origin.replace("[id]", String.valueOf(i)).replace("[height]", String.valueOf(height)).replace("[temperature]", String.valueOf(temperature))).append("\n");

        }
        System.out.println(stringBuilder.toString());
//        for(int i = 1; i <= 42; i++)
//        {
//            stringBuilder2.append(origin2.replace("[id]", String.valueOf(i)).replace("[name]", "\"name" + String.valueOf(i) + "\"").replace("[state]","1")).append("\n");
//        }
//
//        Random random = new Random();
//        int height = -1 * (500 + random.nextInt(2500));
//        System.out.println(height);
//        double temperature = (2000 + random.nextInt(1000)) / 100.0;
//        System.out.println(temperature);
//        int int12 = random.nextInt(4);
//        System.out.println(int12);
//
//        System.out.println(stringBuilder2.toString());
    }
}
