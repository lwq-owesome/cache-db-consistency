package cn.jorden.request;

import static java.util.concurrent.Executors.newFixedThreadPool;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;

/**
 * <p>
 *
 * </p>
 *
 * @author jorden.li
 * @since 2020-03-31 13:59
 */
public class RequestProcessorThreadPool {


  /**
   * 线程池
   */
  private ExecutorService threadPool = newFixedThreadPool(10);

  public RequestProcessorThreadPool() {
    RequestQueue requestQueue = RequestQueue.getInstance();
    for (int i = 0; i < 10; i++) {
      ArrayBlockingQueue<Request> queue = new ArrayBlockingQueue<>(100);
      requestQueue.addQueue(queue);
      threadPool.submit(new RequestProcessorThread(queue));
    }
  }

  private static class Singleton {

    private static RequestProcessorThreadPool instance;

    static {
      instance = new RequestProcessorThreadPool();
    }

    public static RequestProcessorThreadPool getInstance() {
      return instance;
    }
  }

  /**
   * jvm的机制去保证多线程并发安全
   * <p>
   * 内部类的初始化，一定只会发生一次，不管有多少个线程并发去初始化。
   */
  private static RequestProcessorThreadPool getInstance() {
    return Singleton.getInstance();
  }

  /**
   * 初始化的便捷方法
   */
  public static void init() {
    getInstance();
  }


}
