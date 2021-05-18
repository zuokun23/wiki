package com.carlos.wiki.util;

import org.springframework.stereotype.Component;

import java.text.ParseException;

/**
 * Twitter的分布式自增ID雪花算法
 */
@Component
public class SnowFlake {

    /**
     * 时间戳
     */
    private static final long START_STAMP = 1609459200000L;//"2021-01-01 08:00:00"

    /**
     * 每一部分占用的位数
     */
    private static final long SEQUENCE_BIT = 12;//序列号占用的位数
    private static final long MACHINE_BIT = 5;//机器标识占用的位数
    private static final long DATACENTER_BIT = 5;//数据中心占用的位数

    /**
     * 每一个部分的最大值
     */
    private static final long MAX_DATACENTER_NUM = -1L ^ (-1L << DATACENTER_BIT);
    private static final long MAX_MACHINE_NUM = -1L ^ (-1L << MACHINE_BIT);
    private static final long MAX_SEQUENCE = -1L ^ (-1L << SEQUENCE_BIT);

    /**
     * 每一部分向左的位移
     */
    private static final long MACHINE_LEFT = SEQUENCE_BIT;
    private static final long DATACENTER_LEFT = MACHINE_LEFT + MACHINE_BIT;
    private static final long TIMESTAMP_LEFT = DATACENTER_LEFT + DATACENTER_BIT;

    private long datacenterId = 1;  //数据中心
    private long machineId = 1;     //机器标识
    private long sequence = 0L; //序列号
    private long lastStmp = -1L;//上一次时间戳



    public SnowFlake() {
    }

    public SnowFlake(long datacenterId, long machineId) {
        if (datacenterId > MAX_DATACENTER_NUM || datacenterId < 0) {
            throw new IllegalArgumentException("datacenterId can't be greater than MAX_DATACENTER_NUM or less than 0");
        }
        if (machineId > MAX_MACHINE_NUM || machineId < 0) {
            throw new IllegalArgumentException("machineId can't be greater than MAX_MACHINE_NUM or less than 0");
        }
        this.datacenterId = datacenterId;
        this.machineId = machineId;
    }

    /**
     * 产生下一个ID
     * @return
     */
    public synchronized long nextId(){
        long currStmp = getNewstmp();
        if (currStmp < lastStmp) {
            throw new RuntimeException("Clock moved backwards.  Refusing to generate id");
        }

        if(currStmp == lastStmp){
            //相同毫秒内，序列号自增
            sequence = (sequence + 1) % MAX_SEQUENCE;
            //同一毫秒的序列数已经达到最大
            if(sequence == 0){
                currStmp = getNextMill();
            }
        }else{
            //不同毫秒内，序列号置0
            sequence = 0;
        }
        lastStmp = currStmp;
//        System.out.println("准确时间：" + currStmp);

        return sequence//12序列号部分
                | machineId << MACHINE_LEFT//5机器标识部分
                | datacenterId << DATACENTER_LEFT//5数据中心部分
                | (currStmp - START_STAMP) << TIMESTAMP_LEFT;//42时间戳部分
    }

    private long getNextMill(){
        long mill = getNewstmp();
        while(mill <= lastStmp){
            mill = getNewstmp();
        }
        return mill;
    }
    private long getNewstmp(){
        return System.currentTimeMillis();
    }

    public static void main(String[] args) throws ParseException {
//        //时间戳,"1970-01-01 08:00:00"到现在时间的毫秒数
//        System.out.println(System.currentTimeMillis());
//        System.out.println(new Date().getTime());
//
//        String dateTime = "2021-01-01 08:00:00";
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//        System.out.println(sdf.parse(dateTime).getTime());


        SnowFlake snowFlake = new SnowFlake(1,1);

        long start = System.currentTimeMillis();
        for (int i = 0; i < 10; i++) {
            System.out.println(snowFlake.nextId());
        }
    }
}
