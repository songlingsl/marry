package com.songlingadmin.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
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
public class CarSubscribeCode implements Serializable {

    private static final long serialVersionUID=1L;

      @TableId(value="code_id",type=IdType.AUTO)
      private Long codeId;

    private Integer codeStatus;
}
