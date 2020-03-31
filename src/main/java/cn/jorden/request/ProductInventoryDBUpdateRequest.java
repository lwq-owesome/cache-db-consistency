package cn.jorden.request;

import cn.jorden.model.ProductInventory;
import cn.jorden.service.ProductInventoryService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 *
 * 更新商品库存
 *
 * </p>
 *
 * @author jorden.li
 * @since 2020-03-30 16:56
 */
@AllArgsConstructor
@Slf4j
public class ProductInventoryDBUpdateRequest implements Request {


  /**
   * 商品库存 参数
   */
  private ProductInventory productInventory;

  /**
   * 商品库存Service 组件
   */
  private ProductInventoryService productInventoryService;


  @Override
  public void process() {
    log.info(
        "===========日志===========: 数据库更新请求开始执行，商品id= {} 商品库存数量 {}", productInventory.getProductId(),
        productInventory.getInventoryCnt());

    //1 先删除redis缓存
    productInventoryService.removeProductInventoryCache(productInventory);

    //2 更新数据库
    productInventoryService.updateProductInventory(productInventory);

  }

  @Override
  public Integer getProductId() {
    return productInventory.getProductId();
  }

  @Override
  public boolean isForceFefresh() {
    return false;
  }
}
