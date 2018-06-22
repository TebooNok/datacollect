package com.pginfo.datacollect.config;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.pginfo.datacollect.dao.*;
//import com.pginfo.datacollect.dao.SinkDataDao;
import com.pginfo.datacollect.domain.*;
import com.pginfo.datacollect.util.Constants;
import com.pginfo.datacollect.util.ConvertUtil;
import org.apache.ibatis.datasource.DataSourceException;
import org.apache.ibatis.io.Resources;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.CollectionUtils;

import java.io.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Bean配置类
 * 1. 记录即时的沉降值 cacheDataMap
 * 2. 记录一小时内每秒数据 cacheDataQueueMap
 * 3. 记录告警阈值
 * 4. 记录当前告警状态
 * 5. 记录当前设备状态
 * <p>
 * 缓存关键是区分：数据、配置、状态三项。数据和状态从mysql同步，配置从mongo同步
 */
@Configuration
public class BeanConfigure {

    private Logger logger = LoggerFactory.getLogger(BeanConfigure.class);

    // 从mysql读取沉降数值
    private final SinkDataMapper sinkDataMapper;

    // 从mysql读取传感器信息
    private final MonitorDeviceMapper monitorDeviceMapper;

    // 从mysql读取解调仪信息
    private final DemoDeviceMapper demoDeviceMapper;

    // 向mongo存取告警信息
    private final MongoAlarmInfoDao mongoAlarmInfoDao;

    // 向mongo存取告警配置
    private final MongoAlarmThreDao mongoAlarmThreDao;

    // 向mongo存取传感器配置
    private final MongoMonitorDeviceSettingDao mongoMonitorDeviceSettingDao;

    private final UserDao userDao;
    @Autowired
    public BeanConfigure(SinkDataMapper sinkDataMapper, MonitorDeviceMapper monitorDeviceMapper, DemoDeviceMapper demoDeviceMapper, MongoAlarmInfoDao mongoAlarmInfoDao, MongoAlarmThreDao mongoAlarmThreDao, MongoMonitorDeviceSettingDao mongoMonitorDeviceSettingDao, UserDao userDao) {
        this.mongoMonitorDeviceSettingDao = mongoMonitorDeviceSettingDao;
        this.userDao = userDao;

        logger.debug("Application context initialize");
        this.monitorDeviceMapper = monitorDeviceMapper;
        this.demoDeviceMapper = demoDeviceMapper;
        this.sinkDataMapper = sinkDataMapper;
        this.mongoAlarmInfoDao = mongoAlarmInfoDao;
        this.mongoAlarmThreDao = mongoAlarmThreDao;

    }

//    @Bean
//    public SinkDataDao sinkDataDao() {
//        SinkDataDao sinkDataDao = new SinkDataDao();
//        sinkDataDao.setConf(MYBATIS_PATH);
//        return sinkDataDao;
//    }

    // 状态，和mongo保持同步
    // 缓存的当前告警信息，每个设备只有一个记录，系统重启时，缓存告警从Mongodb读取；
    @Bean
    Map<Integer, AlarmInfo> alarmInfoMap() {

        List<AlarmInfo> alarmInfoList = mongoAlarmInfoDao.getCurrentAlarmInfo();
        Map<Integer, AlarmInfo> alarmInfoMap = new ConcurrentHashMap<>();

        if (!CollectionUtils.isEmpty(alarmInfoList)) {
            for (AlarmInfo alarmInfo : alarmInfoList) {
                alarmInfoMap.put(alarmInfo.getAlarmDeviceId(), alarmInfo);
            }
        }
        // 初始化所有设备的告警状态为无告警
        else {
            // 如果数据库没有配置，则初始化默认配置并存入数据库
            List<MonitorDevice> monitorDeviceList = monitorDeviceMapper.selectAllMonitorDevice();
            for (MonitorDevice monitorDevice : monitorDeviceList) {
                AlarmInfo alarmInfo = new AlarmInfo();

                // 配置告警的位置信息
                MonitorDeviceSetting setting = monitorDeviceSettingMap().get(alarmInfo.getAlarmDeviceId());
                alarmInfo.setAlarmDevicePosition(setting.getDevicePosition());
                alarmInfo.setAlarmDeviceDirection(setting.getDeviceDirection());

                alarmInfo.setAlarmDeviceId(monitorDevice.getDeviceId());
                // 无告警
                alarmInfo.setAlarmStatus(4);

                alarmInfo.setAlarmDateTime(null);
                alarmInfo.setAlarmLevel(0);
                alarmInfo.setAlarmProcessUser("");
                alarmInfo.setAlarmProcessTime(null);
                alarmInfo.setAlarmProcessMessage(null);
                alarmInfo.setAlarmConfirmUser("");
                alarmInfo.setAlarmConfirmTime(null);
                alarmInfo.setAlarmConfirmMessage(null);

                alarmInfoMap.put(monitorDevice.getDeviceId(), alarmInfo);
                mongoAlarmInfoDao.saveCurrentAlarmInfo(alarmInfo);
            }
        }


        return alarmInfoMap;
    }


    // 状态，和mysql保持同步
    // 缓存当前传感器信息(状态和设备名)，不向mongo同步
    @Bean
    Map<Integer, MonitorDevice> monitorDeviceMap() {
        Map<Integer, MonitorDevice> monitorDeviceMap = new ConcurrentHashMap<>();

        List<MonitorDevice> monitorDeviceList = monitorDeviceMapper.selectAllMonitorDevice();

        int id;
        if (!CollectionUtils.isEmpty(monitorDeviceList)) {
            for (MonitorDevice monitorDevice : monitorDeviceList) {
                id = monitorDevice.getDeviceId();
                MonitorDevice device = new MonitorDevice();
                device.setDeviceId(id);
                device.setDeviceName(monitorDevice.getDeviceName());
                device.setStatus(monitorDevice.getStatus());
                monitorDeviceMap.put(id, device);
            }
        } else {
            throw new DataSourceException("Select all monitor device info from mysql but result is null!");
        }
        return monitorDeviceMap;
    }

    // 状态，和mysql保持同步
    // 缓存当前解调仪信息（时间戳），不向mongo同步, 定时检查时间戳
    @Bean
    Map<Integer, DemoDevice> demoDeviceMap() {

        Map<Integer, DemoDevice> demoDeviceMap = new ConcurrentHashMap<>();

        List<DemoDevice> demoDeviceList = demoDeviceMapper.selectAllDemoDevice();

        int id;
        if (!CollectionUtils.isEmpty(demoDeviceList)) {
            for (DemoDevice demoDevice : demoDeviceList) {
                id = demoDevice.getDeviceId();
                DemoDevice device = new DemoDevice();
                device.setDeviceId(id);
                device.setStatus(1);
                device.setTimestamp(demoDevice.getTimestamp());
                demoDeviceMap.put(id, device);
            }
        } else {
            throw new DataSourceException("Select all demo device info from mysql but result is null!");
        }
        return demoDeviceMap;
    }

    // 配置，和mongo保持同步
    // 缓存当前传感器配置(位置、类型、关系)，启动时从mongo同步
    @Bean
    Map<Integer, MonitorDeviceSetting> monitorDeviceSettingMap() {
        Map<Integer, MonitorDeviceSetting> monitorDeviceSettingMap = new ConcurrentHashMap<>();
        List<MonitorDeviceSetting> monitorDeviceSettingList = mongoMonitorDeviceSettingDao.getMonitorDeviceSetting();

        if (!CollectionUtils.isEmpty(monitorDeviceSettingList)) {
            for (MonitorDeviceSetting monitorDeviceSetting : monitorDeviceSettingList) {
                monitorDeviceSettingMap.put(monitorDeviceSetting.getDeviceId(), monitorDeviceSetting);
            }
        } else {
            // 如果数据库没有配置，则初始化默认配置并存入数据库
            List<MonitorDevice> monitorDeviceList = monitorDeviceMapper.selectAllMonitorDevice();
            for (MonitorDevice monitorDevice : monitorDeviceList) {
                MonitorDeviceSetting monitorDeviceSetting = new MonitorDeviceSetting();
                monitorDeviceSetting.setDeviceId(monitorDevice.getDeviceId());
                // 都是数据传感器
                monitorDeviceSetting.setDeviceType(2);
                // 1、2号->桥墩1 ..... 41、42号->桥墩21
                monitorDeviceSetting.setDevicePosition((monitorDevice.getDeviceId() + 1) / 2);
                // 奇数传感器上行、偶数传感器下行
                monitorDeviceSetting.setDeviceDirection(monitorDevice.getDeviceId() % 2 == 0 ? 2 : 1);
                // 默认无基准传感器，无关联解调仪
                monitorDeviceSetting.setDeviceBase(0);
                monitorDeviceSetting.setDemoBase(0);
                monitorDeviceSetting.setDeviceName(monitorDevice.getDeviceName());
                monitorDeviceSettingMap.put(monitorDevice.getDeviceId(), monitorDeviceSetting);

            }
            mongoMonitorDeviceSettingDao.saveMonitorDeviceSetting(monitorDeviceSettingMap);
        }

        return monitorDeviceSettingMap;
    }

    // 配置,和mongo保持同步
    // 缓存当前的告警设置，启动时从mongo同步，为空则用默认值初始化
    @Bean
    AlarmThre alarmThre() {
        AlarmThre alarmThre = mongoAlarmThreDao.getAlarmThre();

        // 如果数据库没数值，则默认值初始化
        if (null == alarmThre)
            alarmThre = new AlarmThre();
        if (alarmThre.getAlarmLevel1() == 0)
            alarmThre.setAlarmLevel1(Constants.DEFAULT_ALARMTHRE_LV1);
        if (alarmThre.getAlarmLevel2() == 0)
            alarmThre.setAlarmLevel2(Constants.DEFAULT_ALARMTHRE_LV2);
        if (alarmThre.getAlarmLevel3() == 0)
            alarmThre.setAlarmLevel3(Constants.DEFAULT_ALARMTHRE_LV3);

        mongoAlarmThreDao.saveAlarmThre(alarmThre);
        return alarmThre;
    }

    // 数据,和mysql保持同步

    /**
     * 用于缓存的MAP对象,系统初始化时从mysql读取，该缓存只保存每个传感器一条数据，与mysql数据库同步
     *
     * @return 查询结果
     */
    @Bean
    Map<Integer, MongoSinkData> cacheDataMap() {

        Map<Integer, MongoSinkData> cacheDataMap = new ConcurrentHashMap<>();

        try {
            List<SinkData> sinkDataList = sinkDataMapper.selectAllSinkData();

            MongoSinkData mongoSinkData;
            if (!CollectionUtils.isEmpty(sinkDataList)) {
                for (SinkData sinkData : sinkDataList) {
                    // 时间用我方系统时间代替
                    mongoSinkData = ConvertUtil.convertFromSinkData(sinkData);
                    cacheDataMap.put(mongoSinkData.getDeviceId(), mongoSinkData);
                }
            } else {
                throw new DataSourceException("Select all data from mysql but result is null!");
            }
        } catch (Exception e) {
            logger.error("Init data from front db fail,! Error: " + e.getMessage());
            throw e;
        }
//
//        // 加载monitor device info、状态等
//        try {
//            List<MonitorDevice> monitorDeviceList = monitorDeviceMapper.selectAllMonitorDevice();
//            if (CollectionUtils.isEmpty(monitorDeviceList)) {
//                throw new DataSourceException("Select all monitor device info from mysql but result is null!");
//            } else {
//                for (MonitorDevice monitorDevice : monitorDeviceList) {
//                    MonitorDevice device = new MonitorDevice();
//                    device.setDeviceId(monitorDevice.getDeviceId());
//                    device.setStatus(monitorDevice.getStatus());
//                    device.setDeviceName(monitorDevice.getDeviceName());
//                    monitorDeviceMap.put(monitorDevice.getDeviceId(), device);
//                }
//            }
//        } catch (Exception e) {
//            logger.error("Init monitor device info from front db fail,! Error: " + e.getMessage());
//            throw e;
//        }
//
//        //
//        try {
//            for (MongoSinkData mongoSinkData : cacheDataList) {
//                int id = mongoSinkData.getDeviceId();
//                // 默认不基于基准传感器和解调仪，需要后面设置
//                mongoSinkData.setDeviceBase(0);
//                mongoSinkData.setDemoBase(0);
//
//                // 默认位置为桥墩1，上行
//                mongoSinkData.setDevicePosition(1);
//                mongoSinkData.setDeviceDirection(1);
//
//                // 默认为监测传感器
//                mongoSinkData.setDeviceType(2);
//
//                // 初始化设置状态
//                mongoSinkData.setDeviceStatus(monitorDeviceMap.get(id).getStatus());
//            }
//        } catch (Exception e) {
//            logger.error("Seems data number different from yk_api and yk_parameter! Error: " + e.getMessage());
//            throw e;
//        }

        // 刷新mongoDB
//        for (Map.Entry<Integer, MongoSinkData> entry : cacheDataMap.entrySet()) {
//            mongoSinkDataDao.upsertCurrentSinkData(entry.getValue());
//        }

        logger.info("Init data from front mysql successful!\n" + JSON.toJSONString(cacheDataMap));
        return cacheDataMap;
    }

    // 数据
    // 每秒一条存一个小时
    @Bean
    public Map<Integer, Queue<MongoSinkData>> cacheDataQueueMap() {
        return new ConcurrentHashMap<>();
    }

    @Bean
    public User userInit(){
        try {
            InputStream inputStream = Resources.getResourceAsStream("conf/userinit.properties");
            Scanner scanner = new Scanner(inputStream);
            while(scanner.hasNextLine()){
                String input = scanner.nextLine();
                String[] userInfo = input.split("\\|");
                User user = new User();
                user.setId(userInfo[0]);
                user.setName(userInfo[1]);
                user.setPassword(userInfo[2]);
                user.setRole(userInfo[3]);

                if(CollectionUtils.isEmpty(userDao.getData(userInfo[1]))){
                    userDao.addUser(user);
                    logger.debug("Insert a user : " + user.getName());
                }
                else{
                    logger.debug("Exist a user : " + user.getName());
                }

            }
        } catch (IOException e) {
            logger.error("File userinit.properties not found." + e.getMessage());
            return null;
        }

        return null;
    }
}
