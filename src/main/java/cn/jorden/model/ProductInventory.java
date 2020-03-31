package cn.jorden.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * <p>
 *
 * </p>
 *
 * @author jorden.li
 * @since 2020-03-30 15:43
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductInventory {

  /**
   * 商品ID
   */
  private Integer productId;

  /**
   * 库存数量
   */
  private Integer inventoryCnt;

}
