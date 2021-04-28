package com.songlingadmin.entity;

import com.baomidou.mybatisplus.annotation.IdType;
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
 * @since 2020-07-08
 */
@Data
  @EqualsAndHashCode(callSuper = false)
  @Accessors(chain = true)
public class CarRecorder implements Serializable {

    private static final long serialVersionUID=1L;

      @TableId(value = "recorder_id", type = IdType.AUTO)
      private Long recorderId;

      /**
     * 预约id
     */
      private Long subscribeId;

      /**
     * 记录人id
     */
      private Long recorderUserId;

      /**
     * 记录时间
     */
      private LocalDateTime recorderTime;


}
