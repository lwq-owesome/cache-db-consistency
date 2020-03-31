package cn.jorden.contorller;

import cn.jorden.common.Result;
import cn.jorden.model.ProductInventory;
import cn.jorden.request.ProductInventoryCacheRefreshRequest;
import cn.jorden.request.ProductInventoryDBUpdateRequest;
import cn.jorden.request.Request;
import cn.jorden.request.RequestAsyncProcessor;
import cn.jorden.service.ProductInventoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 商品库存控制器
 * </p>
 *
 * @author jorden.li
 * @since 2020-03-30 15:42
 */
@RequestMapping("product/inventory")
@RestController
@RequiredArgsConstructor
@Slf4j
public class ProductInventoryContorller {

  private final ProductInventoryService productInventoryService;

  private final RequestAsyncProcessor requestAsyncProcessor;


  @PutMapping("update")
  public Result updateProductInventory(@RequestBody ProductInventory productInventory) {
    log.info(
        "===========日志===========: 数据库更新请求开始执行，商品id= {} 商品库存数量 {}", productInventory.getProductId(),
        productInventory.getInventoryCnt());

    Request request = new ProductInventoryDBUpdateRequest(productInventory,
        productInventoryService);

    requestAsyncProcessor.process(request);

    return Result.ofSuccess();

  }

  @GetMapping("get/{productId}")
  public Result getProductInventory(@PathVariable int productId) {

    log.info(
        "===========日志===========:接收到一个商品库存的读请求 商品id= {} ",
        productId);

    ProductInventory productInventoryReponse = null;

    Request request = new ProductInventoryCacheRefreshRequest(productId,
        productInventoryService, false);

    // 将请求扔给service异步去处理以后，就需要while(true)一会儿，在这里hang住
    // 去尝试等待前面有商品库存更新的操作，同时缓存刷新的操作，将最新的数据刷新到缓存中

    requestAsyncProcessor.process(request);

    long startTime = System.currentTimeMillis();
    long endTime = 0L;

    long waitTime = 0;

    while (true) {
      if (waitTime > 200) {
        break;
      }
      //从缓存里面拿
      productInventoryReponse = productInventoryService
          .getProductInventoryCache(productId);

      if (!ObjectUtils.isEmpty(productInventoryReponse)) {
        return Result.ofSuccess(productInventoryReponse);

      } else {
        try {
          Thread.sleep(20);
        } catch (InterruptedException e) {
          e.printStackTrace();
          log.error("现场休眠异常", e);
        }
        endTime = System.currentTimeMillis();
        waitTime = endTime - startTime;
      }

    }

    // 如果在200ms之内还没有查到。就直接从数据库里面拿hang
    productInventoryReponse = productInventoryService.findProductInventoryById(productId);

    if (!ObjectUtils.isEmpty(productInventoryReponse)) {
      //重新进入队列来处理
      request = new ProductInventoryCacheRefreshRequest(productId, productInventoryService, true);
      requestAsyncProcessor.process(request);
    }

    return Result.ofSuccess(productInventoryReponse);
  }


  public static void main(String[] args) {
  }


}
