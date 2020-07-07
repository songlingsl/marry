package com.songlingadmin.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import java.time.LocalDate;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 
 * </p>
 *
 * @author songling
 * @since 2020-07-03
 */
@Data
  @EqualsAndHashCode(callSuper = false)
  @Accessors(chain = true)
public class MarryActivity implements Serializable {

    private static final long serialVersionUID=1L;

      @TableId(value = "activity_id", type = IdType.AUTO)
      private Integer activityId;

      /**
     * 活动名
     */
      private String activityName;

      /**
     * 活动地
     */
      private String activityAddr;

      /**
     * 活动时间
     */
      private LocalDate activityTime;

      /**
     * 活动展示图片url
     */
      private String activityImage;

      /**
     * 活动内容
     */
      private String activityContent;

      /**
     * 活动创建者
     */
      private Long activityCreater;

      /**
     * 创建时间
     */
      private LocalDateTime activityCreateTime;


}
