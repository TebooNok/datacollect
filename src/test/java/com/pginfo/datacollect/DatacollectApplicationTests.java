package com.pginfo.datacollect;

import com.pginfo.datacollect.controller.QueryDataController;
import com.pginfo.datacollect.domain.AlarmInfo;
import com.pginfo.datacollect.domain.MongoSinkData;
import com.pginfo.datacollect.domain.SinkData;
import com.pginfo.datacollect.dto.QueryDataRequest;
import com.pginfo.datacollect.dto.QueryDataResponse;
import com.pginfo.datacollect.service.QuerySinkDataService;
import com.pginfo.datacollect.util.ConvertUtil;
import com.pginfo.datacollect.util.LocalUtils;
import io.swagger.models.auth.In;
import org.apache.commons.lang3.LocaleUtils;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.function.Consumer;

// @RunWith(SpringRunner.class)
@SpringBootTest
public class DatacollectApplicationTests {

    @Autowired
    QuerySinkDataService querySinkDataService;

    @Test
    public void contextLoads() {
        System.out.println(true);
    }

    @Test
    public void testToString() {
        String jsonString = "{\"deviceId\":123,\"height\":1024,\"timeMills\":1993102804}";
        SinkData sinkData = new SinkData();
        sinkData.setDeviceId(123);
        sinkData.setHeight(1024);
        sinkData.setDateTime(Timestamp.valueOf(LocalDateTime.now()));
        if (sinkData.toString().equals(jsonString)) {
            System.out.println(true);
        } else {
            System.out.println(false);
        }
        System.out.println(sinkData.getDateTime().toString());
        System.out.println(jsonString);
        System.out.println(sinkData.toString());
    }

    @Test
    public void dateTime2SqlDate() {
        LocalDateTime localDateTime = LocalDateTime.now();
        //Date date = Date.valueOf(localDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));

        Timestamp timestamp = Timestamp.valueOf(localDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        System.out.println(timestamp);
    }

    @Test
    public void testQueryAllData() {
    }

    @Test
    public void testLog4j() {
        Logger logger = LoggerFactory.getLogger(QuerySinkDataService.class);
        logger.debug("This is a debug");
        logger.error("This is a error");
        logger.info("This is a info");
    }

    @Test
    public void testConvert() {
        SinkData sinkData = new SinkData();
        sinkData.setDeviceId(123);
        sinkData.setHeight(1024);
        sinkData.setDateTime(Timestamp.valueOf(LocalDateTime.now()));
        sinkData.setTemperature(12.34);
        System.out.println("Sink: " + sinkData.toString());

        MongoSinkData mongoSinkData = ConvertUtil.convertFromSinkData(sinkData);
        System.out.println("Query: " + mongoSinkData.toString());

        MongoSinkData mongoSinkData1 = new MongoSinkData(mongoSinkData);
        System.out.println("Query1: " + mongoSinkData1.toString());

        mongoSinkData.setDateTime("I change this! ");
        System.out.println("Query: " + mongoSinkData.toString());
        System.out.println("Query1: " + mongoSinkData1.toString());

    }

    @Test
    public void hashMapDuplicatePut() throws InterruptedException {
        Map<Integer, String> duplicateDataMap = new HashMap<>(10);

        SinkData sinkData = new SinkData();
        sinkData.setDeviceId(123);
        sinkData.setHeight(1024);
        sinkData.setDateTime(Timestamp.valueOf(LocalDateTime.now()));
        sinkData.setTemperature(12.34);

        Thread.sleep(1000);

        SinkData sinkData1 = new SinkData();
        sinkData1.setDeviceId(123);
        sinkData1.setHeight(1024);
        sinkData1.setDateTime(Timestamp.valueOf(LocalDateTime.now()));
        sinkData1.setTemperature(12.34);

        System.out.println("Sink: " + sinkData.toString());

        MongoSinkData mongoSinkData = ConvertUtil.convertFromSinkData(sinkData);
        System.out.println("Query: " + mongoSinkData.toString());

        duplicateDataMap.put(mongoSinkData.getDeviceId(), new StringBuilder().append(mongoSinkData.getDeviceId()).append(mongoSinkData.getDateTime()).toString());
        System.out.println("size: " + duplicateDataMap.size());
        System.out.println(duplicateDataMap.get(mongoSinkData.getDeviceId()));

        MongoSinkData mongoSinkData1 = ConvertUtil.convertFromSinkData(sinkData1);
        duplicateDataMap.put(mongoSinkData1.getDeviceId(), new StringBuilder().append(mongoSinkData1.getDeviceId()).append(mongoSinkData1.getDateTime()).toString());

        System.out.println("size: " + duplicateDataMap.size());
        System.out.println(duplicateDataMap.get(mongoSinkData.getDeviceId()));
    }

    @Test
    public void testSplite() {
        String str = "45";
        String[] strArr = str.split("\\|");
        for (String s : strArr) {
            System.out.println(s);
        }

        String sss = "Custom_60";
        int i = Integer.parseInt(sss.split("_")[1]);
        System.out.println(i);
        long l1 = 50;
        long l2 = 60;
        double b1 = 0.51;
        long l3 = l2 + (int) (b1 + 0.5);
        System.out.println();
        System.out.println(b1);
        System.out.println(l3);
    }

    @Test
    public void listQueue() {
        LinkedBlockingQueue<String> strings = new LinkedBlockingQueue<>(5);
        strings.offer("first");
        strings.offer("second");
        strings.offer("third");
        strings.offer("forth");
        strings.offer("fifth");
//        strings.poll();
//        strings.offer("seventh");
//        strings.poll();
//        strings.offer("eighth");
        for (String str : strings) {
            System.out.print(str + " ");
        }

        System.out.println();

        Object[] stringArr = strings.toArray();
        List<Object> stringList = Arrays.asList(stringArr);
        Collections.reverse(stringList);

        stringArr = stringList.toArray();

        List<String> list = new ArrayList<>();
        for(Object str: stringArr){
            System.out.print(str + " ");
            list.add((String) str);
        }

        list.add(null);
        System.out.println(list);
        System.out.println(list.size());

    }

    @Test
    public void localDateTime()
    {
        LocalDateTime startDateTime = LocalDateTime.now();
        System.out.println(startDateTime);
        startDateTime = startDateTime.plusSeconds((long)(1.05 * 60));
        System.out.println(startDateTime);
    }

    @Test
    public void testFormatIgnoreSec()
    {
        String dateTime = "2018-06-06 23:59:33";
        System.out.println(LocalUtils.formatIgnoreSeconds(dateTime));
        System.out.println(LocalUtils.formatDataTimeIgnoreSec(LocalDateTime.now()));
    }

    @Test
    public void testSortLambda()
    {
        MongoSinkData o1 = new MongoSinkData();
        MongoSinkData o2 = new MongoSinkData();
        MongoSinkData o3 = new MongoSinkData();
        MongoSinkData o4 = new MongoSinkData();
        MongoSinkData o5 = new MongoSinkData();
        o1.setDeviceId(1);
        o2.setDeviceId(2);
        o3.setDeviceId(3);
        o4.setDeviceId(4);
        o5.setDeviceId(5);

        o1.setDateTime("2018-06-07 01:50:23");
        o2.setDateTime("2018-06-07 02:40:23");
        o3.setDateTime("2018-07-07 01:40:23");
        o4.setDateTime("2018-06-03 01:40:23");
        o5.setDateTime("2018-06-03 01:40:23");

        List<MongoSinkData> mongoSinkDataList = new ArrayList<>();
        mongoSinkDataList.add(o1);
        mongoSinkDataList.add(o2);
        mongoSinkDataList.add(o3);
        mongoSinkDataList.add(o4);
        mongoSinkDataList.add(o5);

        for(MongoSinkData mongoSinkData:mongoSinkDataList){
            System.out.println(mongoSinkData.getDeviceId() + ", time = " + mongoSinkData.getDateTime());
        }

        System.out.println();
        mongoSinkDataList.sort((MongoSinkData ob1, MongoSinkData ob2) ->{
            return ob1.getDateTime().compareTo(ob2.getDateTime())>0?-1:0;
        });

        for(MongoSinkData mongoSinkData:mongoSinkDataList){
            System.out.println(mongoSinkData.getDeviceId() + ", time = " + mongoSinkData.getDateTime());
        }
    }

    @Test
    public void testSubList(){
        List<String> strings = new ArrayList<>();
        strings.add("str1");
        strings.add("str2");
        strings.add("str3");
        strings.add("str4");
        strings.add("str5");
        strings.add("str6");
        strings.add("str7");
        strings.add("str8");
        if(strings.size() > 6){
            List<String> ret = strings.subList(0,6);
            for(String str:ret)
            {
                System.out.println(str);
            }
        }

        if(null == strings.subList(1,1)){
            System.out.println("null");
        }

        System.out.println(strings.subList(1,1).size());
    }

    @Test
    public void testAddAll(){
        List<String> strings1 = new ArrayList<>();
        List<String> strings2 = new ArrayList<>();
        strings1.add("str1");
        strings1.add("str2");
        strings1.add("str3");
        strings1.add("str4");
        strings2.add("str5");
        strings2.add("str6");
        strings2.add("str7");
        strings2.add("str8");

        strings1.addAll(strings2);

        for(String str:strings1){
            System.out.println(str);
        }
    }

    @Test
    public void testPage(){

        List<String> returnList = new ArrayList<>();
        for(int i = 1; i <= 0; i++){
            returnList.add("str" + String.valueOf(i));
        }

        int page = 1;
        int dataNum = 10;

        if (page > 0) {
            int endIndex = page * dataNum;
            int size = returnList.size();

            System.out.println("endIndex = " + endIndex);
            System.out.println("dataNum = " + dataNum);
            if ((page - 1) * dataNum > size) {
                System.out.println("error");
                return;
            }else {
                System.out.println((page - 1) * dataNum);
                System.out.println(endIndex > size ? size : endIndex);
                for (String str : returnList.subList((page - 1) * dataNum, endIndex > size ? size : endIndex)) {
                    System.out.println(str);
                }
                return;
            }
        }

        // 不启用分页，返回前N条
        if (returnList.size() > dataNum) {

            for(String str:returnList.subList(0, dataNum)){
                System.out.println(str);
            }
            return;
        } else {
            for(String str:returnList){
                System.out.println(str);
            }
            return;
        }
    }

    @Test
    public void Timestamp(){
        long timestamp = System.currentTimeMillis();
        long timestamp1 = LocalDateTime.now().toInstant(ZoneOffset.of("+8")).toEpochMilli();
        Instant instant = Instant.ofEpochMilli(timestamp);
        Instant instant1 = Instant.ofEpochMilli(timestamp1);
        LocalDateTime localDateTime = LocalDateTime.ofInstant(instant, ZoneId.of("+8"));
        LocalDateTime localDateTime1 = LocalDateTime.ofInstant(instant1, ZoneId.of("+8"));
        System.out.println(timestamp);
        System.out.println(timestamp1);
        System.out.println(instant);
        System.out.println(instant1);
        System.out.println(localDateTime);
        System.out.println(localDateTime1);

        Timestamp ts = new Timestamp(timestamp);
        String dateTime = LocalUtils.convertTimestamp2String(ts);
        System.out.println(dateTime);

        String sTime = String.valueOf(System.currentTimeMillis());
        System.out.println(sTime);
        sTime = LocalUtils.convertTimestamp2String(new Timestamp(Long.parseLong(sTime)));
        System.out.println(sTime);

        String dateTime1 = "2018-06-19 01:23:34";
        dateTime = String.valueOf(LocalUtils.convertString2LocalDataTime(dateTime1).toInstant(ZoneOffset.of("+8")).toEpochMilli());
        System.out.println(dateTime);
        String dateTime2 = LocalUtils.convertTimestamp2String(new Timestamp(Long.parseLong(dateTime)));
        System.out.println(dateTime2);
    }

    @Test
    public void test(){
        QueryDataRequest request = new QueryDataRequest();
        String sTime = "1529417985000";
        request.setStartDateTime(sTime);
        request.setEndDateTime(sTime);
        request.setDeviceId("1");
        request.setMode(2);
        request.setTemplateType("Custom_10");

        sTime = LocalUtils.convertTimestamp2String(new Timestamp(Long.parseLong(sTime)));
        System.out.println(sTime);
    }

    @Test
    public void testEqualDay(){
        String ald = "2018-07-06 12:10:55";
        String td  = LocalUtils.formatCurrentTime();
        System.out.println(ald.substring(0, 10));
        System.out.println(td.substring(0, 10));
        System.out.println(ald.substring(0, 10).equals(td.substring(0, 10)));
    }

    @Test
    public void testSort()
    {
        List<AlarmInfo> returnList = new ArrayList<>();

        AlarmInfo ali1 = new AlarmInfo();
        AlarmInfo ali2 = new AlarmInfo();
        AlarmInfo ali3 = new AlarmInfo();
        AlarmInfo ali4 = new AlarmInfo();
        AlarmInfo ali5 = new AlarmInfo();

        ali1.setAlarmStatus(1);
        ali2.setAlarmStatus(1);
        ali3.setAlarmStatus(1);
        ali4.setAlarmStatus(4);
        ali5.setAlarmStatus(4);

        ali1.setAlarmDeviceId(1);
        ali2.setAlarmDeviceId(2);
        ali3.setAlarmDeviceId(3);
        ali4.setAlarmDeviceId(4);
        ali5.setAlarmDeviceId(5);

        System.out.println(LocalUtils.formatCurrentTime());

        ali1.setAlarmDateTime("2018-08-09 04:39:00");
        ali2.setAlarmDateTime("2018-08-10 10:44:00");
        ali3.setAlarmDateTime("2018-08-09 01:39:00");
        ali4.setAlarmDateTime("2018-08-10 10:39:00");
        ali5.setAlarmDateTime("2018-08-09 20:39:00");

        returnList.add(ali1);
        returnList.add(ali2);
        returnList.add(ali3);
        returnList.add(ali4);
        returnList.add(ali5);

        returnList.sort((AlarmInfo o1, AlarmInfo o2) -> {
            if(null == o1.getAlarmDateTime() || null == o2.getAlarmDateTime()){
                return 0;
            }
            else return o1.getAlarmDateTime().compareTo(o2.getAlarmDateTime()) > 0 ? -1 : 0;
        });

        returnList.forEach(
                System.out::println
        );
    }
}
