package com.api.service.auth.sign.service.impl;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import com.api.service.DateUtils;
import com.api.service.RedisLockUtil;
import com.api.service.UUID;
import com.api.service.auth.sign.entity.AfterSignDto;
import com.api.service.auth.sign.service.IApiSignService;
import com.api.service.db.entity.*;
import com.api.service.db.mysql.service.ITApiUserService;
import com.api.service.db.mysql.service.ITSignBaseConfigService;
import com.api.service.db.mysql.service.ITSignDateConfigService;
import com.api.service.db.mysql.service.ITSignLogService;
import com.api.service.db.mysql.util.PageUtils;
import com.api.service.db.mysql.util.Query;
import com.api.service.db.mysql.util.beanToMap.BeanMapUtil;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service("IApiSignService")
public class ApiSignServiceImpl implements IApiSignService {

    @Autowired
    private ITSignLogService itSignLogService;
    @Autowired
    private ITSignBaseConfigService itSignBaseConfigService;
    @Autowired
    private ITSignDateConfigService itSignDateConfigService;
    @Autowired
    private ITApiUserService itApiUserService;


    @Override
    public List<TSignLogEntity> signLog(String userId) {
        DateTime dateTime = DateUtil.lastWeek();
        DateTime beginDate = DateUtil.beginOfWeek(dateTime);//?????????????????????
        String firstDate = DateUtil.formatDate(beginDate);
        String lastDate = DateUtil.formatDate(new Date());

        Wrapper<TSignLogEntity> wrapper = new EntityWrapper<TSignLogEntity>()
                .where("1=1")
                .eq("user_id", userId)
                .eq("account_trend", "0")
                .and(" date_format(sign_date,'%Y-%m-%d') >= date_format('" + firstDate + "','%Y-%m-%d')")
                .and(" date_format(sign_date,'%Y-%m-%d') <= date_format('" + lastDate + "','%Y-%m-%d')")
                .eq("score_type", "0")
                .orderBy("sign_date", true);
        List<TSignLogEntity> logList = itSignLogService.selectList(wrapper);
        if (!CollectionUtils.isEmpty(logList)) {
            for (int i = 0; i < logList.size(); i++) {
                TSignLogEntity logEntity = logList.get(i);
                String signDate = logEntity.getSignDate();
                Date date1 = DateUtils.stringToDate(signDate, DateUtils.DATE_PATTERN);
                String format = DateUtils.format(date1, "yyyy-MM-dd");
                logEntity.setSignDate(format);
            }
        }
        return logList;
    }

    /**
     * ??????30??????????????????????????????
     *
     * @return
     */
    @Override
    public Integer signDays(String userId) {
        TSignBaseConfigEntity tSignBaseConfigEntity = itSignBaseConfigService.selectById("1");

        Wrapper<TSignLogEntity> wrapper1 = new EntityWrapper<TSignLogEntity>()
                .where("1=1")
                .eq("user_id", userId)
                .eq("account_trend", "0")
                .eq("score_type", "0")
                .orderBy("sign_date", false);
        List<TSignLogEntity> signInList = itSignLogService.selectList(wrapper1);
        Integer continuousSignInDay = this.getContinuousSignInDay(signInList);
        if (continuousSignInDay > 0) {
            continuousSignInDay = continuousSignInDay % tSignBaseConfigEntity.getSignBaseDay() > 0 ? continuousSignInDay % tSignBaseConfigEntity.getSignBaseDay() : tSignBaseConfigEntity.getSignBaseDay();
        } else {
            continuousSignInDay = 0;
        }
        return continuousSignInDay;
    }

    /**
     * ???????????????????????????
     *
     * @param userId
     * @return
     */
    @Override
    public Integer signTotalCount(String userId) {
        Wrapper<TSignLogEntity> wrapper1 = new EntityWrapper<TSignLogEntity>()
                .where("1=1")
                .eq("account_trend", "0")
                .eq("score_type", "0")
                .eq("user_id", userId);
        Integer count = itSignLogService.selectCount(wrapper1);
        return count;
    }

    /**
     * ????????????????????????
     *
     * @param logEntity
     * @return
     */
    @Override
    public PageUtils detail(TSignLogEntity logEntity) {
        Wrapper<TSignLogEntity> wrapper = new EntityWrapper<TSignLogEntity>()
                .where("1=1")
                .eq(StringUtils.isNotBlank(logEntity.getUserId()), "user_id", logEntity.getUserId())
                .eq(StringUtils.isNotBlank(logEntity.getSignDate()), "sign_date", logEntity.getSignDate())
                .eq(null != logEntity.getSignDays(), "sign_days", logEntity.getSignDays())
                .orderBy("create_time desc");
        Page<TSignLogEntity> page = itSignLogService.selectPage(new Query<TSignLogEntity>(BeanMapUtil.toPageQuery(logEntity)).getPage(), wrapper);
        return new PageUtils(page);
    }

    /**
     * ????????????
     *
     * @return
     */
    @Override
    public BigDecimal sign(String userId) {
        //1???????????????????????????
        Wrapper<TSignLogEntity> wrapper = new EntityWrapper<TSignLogEntity>()
                .where("1=1")
                .eq("account_trend", "0")
                .eq("score_type", "0")
                .eq("user_id", userId)
                .eq("sign_date", DateUtil.formatDate(new Date()));
        int count = itSignLogService.selectCount(wrapper);
        if (count > 0) {
            throw new RuntimeException("???????????????");
        }
        //??????????????????
        Wrapper<TSignDateConfigEntity> wrapper3 = new EntityWrapper<TSignDateConfigEntity>()
                .where("1=1")
                .eq("status", "0")
                .orderBy("sign_days", false);
        List<TSignDateConfigEntity> configList = itSignDateConfigService.selectList(wrapper3);
        if (CollectionUtils.isEmpty(configList)) {
            throw new RuntimeException("???????????????????????????");
        }
        BigDecimal scoreNum = this.save(configList, userId);
        return scoreNum;
    }

    @Override
    @Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public BigDecimal afterSign(@RequestBody AfterSignDto signDto, String userId) {
        //????????????
        String day = signDto.getAfterSignDate();
        Date date1 = DateUtils.stringToDate(day, DateUtils.DATE_PATTERN);
        day = DateUtils.format(date1, "yyyy-MM-dd");

        //????????????????????????????????????????????????
        long l = DateUtil.betweenDay(new Date(), DateUtil.parseDate(day), true);
        if (l > 3) {
            throw new RuntimeException("????????????3???????????????");
        }
        Wrapper<TSignLogEntity> wrapper = new EntityWrapper<TSignLogEntity>()
                .where("1=1")
                .eq("account_trend", "0")
                .eq("score_type", "0")
                .eq("user_id", userId)
                .eq("sign_date", day);
        int count = itSignLogService.selectCount(wrapper);
        if (count > 0) {
            throw new RuntimeException("???????????????,????????????!");
        }
        BigDecimal scoreNum = this.afterSave(day, userId);
        return scoreNum;
    }


    @Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public BigDecimal save(List<TSignDateConfigEntity> configList, String userId) {
        TSignBaseConfigEntity tSignBaseConfigEntity = itSignBaseConfigService.selectById("1");
        //??????????????????
        TSignLogEntity logEntity = new TSignLogEntity();
        logEntity.setUserId(userId);
        logEntity.setSignDate(DateUtil.formatDate(new Date()));
        logEntity.setCreateTime(new Date());
        logEntity.setSignId(UUID.randomUUID());
        logEntity.setIsAfterSign("1");
        logEntity.setAccountTrend("0");
        logEntity.setScoreType("0");
        itSignLogService.insert(logEntity);
        //??????????????????????????????
        BigDecimal scoreNum = tSignBaseConfigEntity.getSignBaseScoreNum();
        Integer signBaseDay = tSignBaseConfigEntity.getSignBaseDay();

        //2?????????????????????????????????????????????30?????????????????????
        Wrapper<TSignLogEntity> wrapper1 = new EntityWrapper<TSignLogEntity>()
                .where("1=1")
                .eq("user_id", userId)
                .eq("account_trend", "0")
                .eq("score_type", "0")
                .orderBy("sign_date", false);
        List<TSignLogEntity> signInList = itSignLogService.selectList(wrapper1);
        Integer continuousSignInDay = this.getContinuousSignInDay(signInList);

        if (continuousSignInDay > 0) {
            continuousSignInDay = continuousSignInDay % signBaseDay > 0 ? continuousSignInDay % signBaseDay : tSignBaseConfigEntity.getSignBaseDay();
        } else {
            continuousSignInDay = 0;
        }

        for (int i = 0; i < configList.size(); i++) {
            TSignDateConfigEntity config = configList.get(i);
            Integer signDays = config.getSignDays();
            if (continuousSignInDay.compareTo(signDays) == 0) {
                scoreNum = scoreNum.add(config.getScoreNum());
                logEntity.setSignDays(config.getSignDays());
                logEntity.setConfigId(config.getConfigId());
                break;
            }
        }
        logEntity.setScoreNum(scoreNum);
        itSignLogService.updateById(logEntity);

        //????????????????????????
        StringBuilder builder3 = new StringBuilder();
        builder3.append("amount_signin_points = amount_signin_points + " + scoreNum);
        itApiUserService.updateForSet(builder3.toString(), new EntityWrapper<TApiUserEntity>().eq("user_id", userId));
        return scoreNum;
    }


    public BigDecimal afterSave(String day,String userId) {
        String signId = UUID.randomUUID();
        TSignBaseConfigEntity tSignBaseConfigEntity = itSignBaseConfigService.selectById("1");
        //??????????????????
        TSignLogEntity logEntity = new TSignLogEntity();
        logEntity.setUserId(userId);
        logEntity.setSignDate(day);
        logEntity.setCreateTime(new Date());
        logEntity.setSignId(signId);
        logEntity.setIsAfterSign("0");
        logEntity.setAccountTrend("0");
        logEntity.setScoreType("0");

        //??????????????????
        BigDecimal scoreNum = tSignBaseConfigEntity.getSignBaseScoreNum();

        //???????????????????????????????????????
        Wrapper<TSignLogEntity> wrapper1 = new EntityWrapper<TSignLogEntity>()
                .where("1=1")
                .eq("user_id", userId)
                .eq("score_type", "0")
                .eq("account_trend", "0");
        List<TSignLogEntity> signInList = itSignLogService.selectList(wrapper1);
        //???????????????????????????????????????????????????
        signInList.add(logEntity);
        signInList = signInList.stream().sorted(Comparator.comparing(TSignLogEntity::getSignDate).reversed()).collect(Collectors.toList());

        Integer continuousSignInDay = this.getContinuousSignInDay(signInList);

        Integer signBaseDay = tSignBaseConfigEntity.getSignBaseDay();

        if (continuousSignInDay % signBaseDay <= 3 && continuousSignInDay >= signBaseDay) {
            //????????????3??????????????????30?????????????????????????????????????????????.
            Date date = DateUtils.addDateDays(new Date(), -3);
            Wrapper<TSignLogEntity> wrapper = new EntityWrapper<TSignLogEntity>()
                    .where("1=1")
                    .eq("user_id", userId)
                    .eq("account_trend", "0")
                    .eq("score_type", "0")
                    .and(" date_format(sign_date,'%Y-%m-%d') >= date_format('" + DateUtil.formatDateTime(date) + "','%Y-%m-%d')")
                    .and(" date_format(sign_date,'%Y-%m-%d') <= date_format('" + DateUtil.now() + "','%Y-%m-%d')")
                    .and(" sign_days = " + signBaseDay);

            int count = itSignLogService.selectCount(wrapper);
            //???????????????,????????????????????????,??????????????????????????????30?????????
            if (count > 0) {
                continuousSignInDay = continuousSignInDay % signBaseDay;
            }
        } else {
            if (continuousSignInDay > 0) {
                continuousSignInDay = continuousSignInDay % signBaseDay > 0 ? continuousSignInDay % signBaseDay : signBaseDay;
            } else {
                continuousSignInDay = 0;
            }
        }


        //??????????????????
        Wrapper<TSignDateConfigEntity> wrapper3 = new EntityWrapper<TSignDateConfigEntity>()
                .where("1=1")
                .eq("status", "0")
                .orderBy("sign_days", false);
        List<TSignDateConfigEntity> configList = itSignDateConfigService.selectList(wrapper3);
        if (CollectionUtils.isEmpty(configList)) {
            throw new RuntimeException("???????????????????????????");
        }
        for (int i = 0; i < configList.size(); i++) {
            TSignDateConfigEntity config = configList.get(i);
            Integer signDays = config.getSignDays();
            if (continuousSignInDay >= signDays) {
                //????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????
                String beginDate = DateUtil.formatDate(DateUtils.addDateDays(DateUtil.parseDate(DateUtil.now()), -signDays));
                String endDate = DateUtil.formatDate(DateUtil.parseDate(DateUtil.now()));

                Wrapper<TSignLogEntity> wrapper4 = new EntityWrapper<TSignLogEntity>()
                        .where("1=1")
                        .eq("config_id", config.getConfigId())
                        .eq("account_trend", "0")
                        .eq("score_type", "0")
                        .and(" date_format(sign_date,'%Y-%m-%d') >= date_format('" + beginDate + "','%Y-%m-%d')")
                        .and(" date_format(sign_date,'%Y-%m-%d') <= date_format('" + endDate + "','%Y-%m-%d')")
                        .eq("user_id", userId);
                int count = itSignLogService.selectCount(wrapper4);
                if (count == 0) {
                    logEntity.setConfigId(config.getConfigId());
                    scoreNum = scoreNum.add(config.getScoreNum());
                    itSignLogService.updateById(logEntity);
                }
                break;
            }
        }
        StringBuilder builder3 = new StringBuilder();
        builder3.append("amount_signin_points = amount_signin_points + " + scoreNum);
        itApiUserService.updateForSet(builder3.toString(), new EntityWrapper<TApiUserEntity>().eq("user_id", userId));

        logEntity.setScoreNum(scoreNum);
        itSignLogService.insert(logEntity);

        return scoreNum;
    }

    /**
     * ????????????????????????
     *
     * @param signInList
     * @return
     */
    private int getContinuousSignInDay(List<TSignLogEntity> signInList) {
        //continuousDay ???????????????
        int continuousDay = 1;
        boolean todaySignIn = false;
        // today ????????????
        Date today = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String todays = sdf.format(today);

        for (int i = 0; i < signInList.size(); i++) {
            TSignLogEntity signIn = signInList.get(i);
            int intervalDay = 0;
            try {
                intervalDay = distanceDay(sdf.parse(todays), DateUtil.parseDate(signIn.getSignDate()));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            //????????????
            if (intervalDay == 0 && i == 0) {
                todaySignIn = true;
            } else if (intervalDay == continuousDay) {
                continuousDay++;
            } else {
                //????????????????????????
                break;
            }
        }
        if (!todaySignIn) {
            continuousDay--;
        }
        return continuousDay;
    }

    //?????????????????? ?????????????????????????????????
    private static int distanceDay(Date largeDay, Date smallDay) {
        int day = (int) ((largeDay.getTime() - smallDay.getTime()) / (1000 * 60 * 60 * 24));
        return day;
    }

}
