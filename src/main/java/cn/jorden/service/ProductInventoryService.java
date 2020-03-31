package cn.jorden.service;

import cn.jorden.model.ProductInventory;

/**
 * <p>
 *
 * </p>
 *
 * @author jorden.li
 * @since 2020-03-30 15:50
 */
public interface ProductInventoryService {

  /**
   * 更新商品库存
   *
   * @param productInventory 商品库存
   */
  void updateProductInventory(ProductInventory productInventory);

  /**
   * 删除商品redis的商品的缓存
   */
  void removeProductInventoryCache(ProductInventory productInventory);


  /**
   * 获取商品库存redis的商品的缓存
   */
  ProductInventory getProductInventoryCache(Integer productId);


  /**
   * 根据商品id获取商品库存
   *
   * @param productId 商品ID
   * @return 商品库存
   */
  ProductInventory findProductInventoryById(Integer productId);


  /**
   * 设置商品库存的缓存数据
   *
   * @param productInventory 商品库存
   */
  void setProductInventoryCache(ProductInventory productInventory);


}
