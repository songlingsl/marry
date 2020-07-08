package com.songlingapp.entity;

import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 
 * </p>
 *
 * @author songling
 * @since 2020-07-01
 */
@Data
  @EqualsAndHashCode(callSuper = false)
  @Accessors(chain = true)
public class Songling implements Serializable {

    private static final long serialVersionUID=1L;
    @TableId(type= IdType.AUTO)
      private Long id;

    private String name;

    private Integer number;


}
