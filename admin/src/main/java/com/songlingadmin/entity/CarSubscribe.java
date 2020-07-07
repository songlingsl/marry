package com.songlingadmin.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

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
      private String carNumber;

      /**
     * 创建日期
     */
      private LocalDateTime createTime;

      /**
     * 预约时间
     */
      private LocalDate subscribeTime;

      /**
     * 预约码
     */
      private Integer subscribeCode;

      /**
     * 录入人
     */
      private Long sysUserId;

      /**
     * 预约者电话
     */
      private String subscribePhone;

      /**
     * 预约者姓名
     */
      private String subscribeName;

      /**
     * 当前状态0,正常 1.取消
     */
      private Integer subscribeStatus;

      /**
     * 预约时间段
     */
      private String subscribeTimePhase;


      /** 开始时间 */
      @JsonIgnore
      @TableField(exist = false)
      private String beginTime;

      /** 结束时间 */
      @JsonIgnore
      @TableField(exist = false)
      private String endTime;
}
