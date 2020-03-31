package cn.jorden.request;

import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Callable;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.ObjectUtils;

/**
 * <p>
 * 异步消费内存队列里面的请求
 * </p>
 *
 * @author jorden.li
 * @since 2020-03-31 10:18
 */
@AllArgsConstructor
@Slf4j
public class RequestProcessorThread implements Callable<Boolean> {

  private ArrayBlockingQueue<Request> queue;

  @Override
  public Boolean call() throws Exception {

    while (true) {
      //如果内存队列满了，那么此段代码块会卡住。
      Request request = queue.take();
      //是否是读请求
      boolean isForceFefresh = request.isForceFefresh();

      //做读请求读去重-- 读请求是ture
      if (!isForceFefresh) {
        RequestQueue requestQueue = RequestQueue.getInstance();
        Map<Integer, Boolean> flagMap = requestQueue.getFlagMap();

        if (request instanceof ProductInventoryDBUpdateRequest) {
          flagMap.put(request.getProductId(), true);

        } else if (request instanceof ProductInventoryCacheRefreshRequest) {
          Boolean productReadRequest = flagMap.get(request.getProductId());

          // 之前没有这个product请求
          if (ObjectUtils.isEmpty(productReadRequest)) {
            flagMap.put(request.getProductId(), false);
          }

          //如果product 之前是写请求
          if (!ObjectUtils.isEmpty(productReadRequest)&&productReadRequest){
            flagMap.put(request.getProductId(), false);

          }

          //这个之前也是读请求
          if (!ObjectUtils.isEmpty(productReadRequest)&&!productReadRequest){
            //直接不做次请求
            return  true;
          }


        }

      }

      log.info("工作线程处理 productId {}",request.getProductId()+"====开始处理");

      request.process();


    }
  }
}
