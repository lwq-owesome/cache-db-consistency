package cn.jorden.mapper;

import cn.jorden.model.ProductInventory;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author yangfan
 * @date 2018/02/19
 */

@Mapper
public interface ProductInventoryMapper {

  /**
   * 更新库存数量
   */
  void updateProductInventory(ProductInventory productInventory);

  /**
   * 根据
   * @param productId
   * @return
   */
  ProductInventory findProductInventory(Integer productId);
}
