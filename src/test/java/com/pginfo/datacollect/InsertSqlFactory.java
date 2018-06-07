//package com.pginfo.datacollect;
//
//import java.util.Random;
//
//public class InsertSqlFactory {
//    public static void main(String[] args) {
//
//        StringBuilder stringBuilder = new StringBuilder();
//        String origin = "insert into yk_api values([id], [height], [temperature], current_timestamp);";
//
//        for(int i = 1; i <= 42; i++)
//        {
//            Random random = new Random();
//            int height = -1 * (500 + random.nextInt(2500));
//            int abs = random.nextInt(4);
//            if(abs == 0) height = Math.abs(height);
//            double temperature = (2000 + random.nextInt(1000)) / 100.0;
//            stringBuilder.append(origin.replace("[id]", String.valueOf(i)).replace("[height]", String.valueOf(height)).replace("[temperature]", String.valueOf(temperature))).append("\n");
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
//        System.out.println(stringBuilder.toString());
//    }
//}
