package cn.jorden.service.impl;

import cn.hutool.json.JSONUtil;
import cn.jorden.mapper.ProductInventoryMapper;
import cn.jorden.model.ProductInventory;
import cn.jorden.service.ProductInventoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;

/**
 * <p>
 *
 * </p>
 *
 * @author jorden.li
 * @since 2020-03-30 15:50
 */
@RequiredArgsConstructor
@Slf4j
@Service
public class ProductInventoryServiceImpl implements ProductInventoryService {

  private final ProductInventoryMapper productInventoryMapper;

  private final Jedis jedis;


  private final String PRODUCT_ID_CACHE_KEY = "product_id_cache_key:";

  /**
   * 更新商品库存
   *
   * @param productInventory 商品库存
   */
  @Override
  public void updateProductInventory(ProductInventory productInventory) {
    log.info("更新数据库商品库存入参：{}", productInventory);
    productInventoryMapper.updateProductInventory(productInventory);


  }

  /**
   * 删除商品redis的商品的缓存
   */
  @Override
  public void removeProductInventoryCache(ProductInventory productInventory) {
    jedis.del(PRODUCT_ID_CACHE_KEY + productInventory.getProductId());
    log.info("删除商品redis的商品的缓存 product id {}", productInventory.getProductId());

  }

  /**
   * 获取商品库存redis的商品的缓存
   */
  @Override
  public ProductInventory getProductInventoryCache(Integer productId) {
    return JSONUtil.toBean(jedis.get(PRODUCT_ID_CACHE_KEY + productId), ProductInventory.class);
  }


  /**
   * 根据商品id获取商品库存
   *
   * @param productId 商品ID
   * @return 商品库存
   */
  @Override
  public ProductInventory findProductInventoryById(Integer productId) {
    log.info("根据商品id获取商品库存入参：{}", productId);
    ProductInventory productInventory = productInventoryMapper.findProductInventory(productId);
    log.info("根据商品id获取商品库存返回：{}", productInventory);
    return productInventory;
  }

  /**
   * 设置商品库存的缓存数据
   *
   * @param productInventory 商品库存
   */
  @Override
  public void setProductInventoryCache(ProductInventory productInventory) {
    String key = PRODUCT_ID_CACHE_KEY + productInventory.getProductId();
    jedis.set(key, JSONUtil.toJsonStr(productInventory));
    log.info("设置商品库存的缓存数据 key ：{}", key);

  }
}
