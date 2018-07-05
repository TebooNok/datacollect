package com.pginfo.datacollect.service.timer;

import com.pginfo.datacollect.dao.MongoAlarmInfoDao;
import com.pginfo.datacollect.domain.AlarmInfo;
import com.pginfo.datacollect.domain.AlarmThre;
import com.pginfo.datacollect.domain.MongoSinkData;
import com.pginfo.datacollect.util.LocalUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Map;

@Component
public class AlarmTimerService {

    Logger logger = LoggerFactory.getLogger(AlarmTimerService.class);
    private final Map<Integer, MongoSinkData> cacheDataMap;

    private final AlarmThre alarmThre;

    private final Map<Integer, AlarmInfo> alarmInfoMap;

    private final MongoAlarmInfoDao mongoAlarmInfoDao;

    @Autowired
    public AlarmTimerService(Map<Integer, MongoSinkData> cacheDataMap, AlarmThre alarmThre, Map<Integer, AlarmInfo> alarmInfoMap, MongoAlarmInfoDao mongoAlarmInfoDao) {

        this.cacheDataMap = cacheDataMap;
        this.alarmThre = alarmThre;
        this.alarmInfoMap = alarmInfoMap;
        this.mongoAlarmInfoDao = mongoAlarmInfoDao;
    }

    // 每10秒检查一次当前是否存在告警
    @Scheduled(cron = "0/10 * * * * ?")
    public void checkCurrentAlarm() {

        logger.info(Thread.currentThread().toString() + ",checkCurrentAlarm," + LocalDateTime.now().toString());

        int id;
        long height;
        for (Map.Entry<Integer, MongoSinkData> entry : cacheDataMap.entrySet()) {

            MongoSinkData mongoSinkData = entry.getValue();
            height = mongoSinkData.getHeight();
            id = mongoSinkData.getDeviceId();
            AlarmInfo alarmInfo = alarmInfoMap.get(id);

            // 只有设备处于未报警时才检查
            if (alarmInfo.getAlarmStatus() == 4) {
                // 用绝对值和阈值比较
                if (Math.abs(height) > alarmThre.getAlarmLevel1() * 1000) {
                    AlarmInfo info = new AlarmInfo();
                    info.setAlarmDeviceId(id);
                    info.setAlarmLevel(1);
                    info.setHeight(height);
                    info.setAlarmDateTime(LocalUtils.formatCurrentTime());
                    // 正数上浮，负数下沉
                    info.setAlarmType(height>0?1:2);
                    info.setAlarmStatus(1);
                    alarmInfoMap.put(id, info);

                    mongoAlarmInfoDao.saveCurrentAlarmInfo(info);
                }
                else if(Math.abs(height) > alarmThre.getAlarmLevel2() * 1000)
                {
                    AlarmInfo info = new AlarmInfo();
                    info.setAlarmDeviceId(id);
                    info.setAlarmLevel(2);
                    info.setHeight(height);
                    info.setAlarmDateTime(LocalUtils.formatCurrentTime());
                    // 正数上浮，负数下沉
                    info.setAlarmType(height>0?1:2);
                    info.setAlarmStatus(1);
                    alarmInfoMap.put(id, info);

                    mongoAlarmInfoDao.saveCurrentAlarmInfo(info);
                }
                else if(Math.abs(height) > alarmThre.getAlarmLevel3() * 1000)
                {
                    AlarmInfo info = new AlarmInfo();
                    info.setAlarmDeviceId(id);
                    info.setAlarmLevel(3);
                    info.setHeight(height);
                    info.setAlarmDateTime(LocalUtils.formatCurrentTime());
                    // 正数上浮，负数下沉
                    info.setAlarmType(height>0?1:2);
                    info.setAlarmStatus(1);
                    alarmInfoMap.put(id, info);

                    mongoAlarmInfoDao.saveCurrentAlarmInfo(info);
                }
            }
        }
    }
}
