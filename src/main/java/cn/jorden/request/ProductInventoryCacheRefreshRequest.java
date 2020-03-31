package cn.jorden.request;


import cn.jorden.model.ProductInventory;
import cn.jorden.service.ProductInventoryService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 重新加载商品库存的缓存
 *
 * @author yangfan
 * @date 2018/02/19
 */
@AllArgsConstructor
@Slf4j
public class ProductInventoryCacheRefreshRequest implements Request {

  /**
   * 商品ID
   */
  private Integer productId;

  /**
   * 商品库存Service
   */
  private ProductInventoryService productInventoryService;

  /**
   * 是否强制刷新缓存
   */
  private boolean forceRefresh;



  @Override
  public void process() {
    // 从数据库中查询最新的商品库存数量
    ProductInventory productInventory = productInventoryService.findProductInventoryById(productId);
    log.info(
        "===========日志===========: 数据库更新请求开始执行，商品id= {} 商品库存数量 {}", productInventory.getProductId(),
        productInventory.getInventoryCnt());

    // 将最新的商品库存数量刷新到redis缓存中去
    productInventoryService.setProductInventoryCache(productInventory);

  }

  @Override
  public Integer getProductId() {
    return productId;
  }

  @Override
  public boolean isForceFefresh() {
    return forceRefresh;
  }
}
