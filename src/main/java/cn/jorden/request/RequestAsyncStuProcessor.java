package cn.jorden.request;

import java.util.Optional;
import java.util.concurrent.ArrayBlockingQueue;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * <p>
 *
 * </p>
 *
 * @author jorden.li
 * @since 2020-03-30 16:25
 */
@Component
@Slf4j
public class RequestAsyncStuProcessor implements RequestAsyncProcessor {

  @Override
  public void process(Request request) {
    ArrayBlockingQueue<Request> routingQueue = getRoutingQueue(request.getProductId());
    try {
      routingQueue.put(request);
      log.info("异步put到内存队列 ");
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }

  private ArrayBlockingQueue<Request> getRoutingQueue(Integer productId) {
    RequestQueue requestQueue = RequestQueue.getInstance();

    // 先获取productId的hash值
    String key = String.valueOf(productId);

    // @See HashMap  这个参照HashMap 的hash寻址 的算法
    int h;
    int hash = (key == null) ? 0 : (h = key.hashCode()) ^ (h >>> 16);

    int index = (requestQueue.queueSize() - 1) & hash;
    log.info("根据Producthash路由到 {}", index);

    return Optional.ofNullable(requestQueue.getQueue(index)).orElseThrow(RuntimeException::new);
  }
}
