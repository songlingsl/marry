package com.songlingadmin.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.io.Serializable;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.ruoyi.framework.aspectj.lang.annotation.Excel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * <p>
 * 
 * </p>
 *
 * @author songling
 * @since 2020-07-07
 */
@Data
  @EqualsAndHashCode(callSuper = false)
  @Accessors(chain = true)
public class CarSubscribe implements Serializable {

    private static final long serialVersionUID=1L;
      @JsonSerialize(using= ToStringSerializer.class)
      @TableId(value = "subscribe_id")
      private Long subscribeId;

      /**
     * 车牌
     */
      @Excel(name = "预约号牌")
      private String carNumber;

      /**
     * 创建日期
     */
      @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
      private LocalDateTime createTime;

      /**
     * 预约时间
     */
      @Excel(name = "预约日期",dateFormat = "yyyy-MM-dd")
      @JsonFormat(pattern = "yyyy-MM-dd") //前台展示用的轉換
      @DateTimeFormat(pattern = "yyyy-MM-dd")//後台接前台時間的時候轉換
      private Date subscribeTime;

      /**
     * 预约码
     */
      @Excel(name = "预约码")
      private Integer subscribeCode;

      /**
     * 录入人
     */
      private Long sysUserId;

      /**
       * 录入人名
       */
      private String sysNickName;

      /**
     * 预约者电话
     */
      private String subscribePhone;

      /**
     * 预约者姓名
     */
      @Excel(name = "预约人")
      private String subscribeName;

      /**
     * 当前状态0,正常 1.取消
     */
      private Integer subscribeStatus;

      /**
     * 预约时间段
     */
      @Excel(name = "预约时段")
      private String subscribeTimePhase;


      private Integer importFlag;
  @Excel(name = "预约状态")
  @TableField(exist = false)
  private String status12123;


      /** 开始时间 */
      @JsonIgnore
      @TableField(exist = false)
      private String beginTime;

      /** 结束时间 */
      @JsonIgnore
      @TableField(exist = false)
      private String endTime;
}
