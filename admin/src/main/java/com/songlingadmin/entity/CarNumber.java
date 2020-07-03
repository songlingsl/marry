package com.songlingadmin.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import java.time.LocalDate;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
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
 * @since 2020-07-02
 */
@Data
  @EqualsAndHashCode(callSuper = false)
  @Accessors(chain = true)
public class CarNumber implements Serializable {

    private static final long serialVersionUID=1L;
      @TableId(value = "car_id", type = IdType.AUTO)
      private Integer carId;

      /**
     * 车牌
     */
      @Excel(name = "车牌")
      private String carNumber;

      /**
     * 导入日期
     */
      private LocalDateTime importTime;

      /**
     * 进场有效期
     */
      @Excel(name = "有效时间",dateFormat = "yyyy-MM-dd")

      @JsonFormat(pattern = "yyyy-MM-dd") //前台展示用的轉換
      @DateTimeFormat(pattern = "yyyy-MM-dd")//後台接前台時間的時候轉換
      private  Date validTime;


}
