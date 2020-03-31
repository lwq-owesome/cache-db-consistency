package cn.jorden.request;

/**
 * <p>
 *      请求异步的处理器
 * </p>
 *
 * @author jorden.li
 * @since 2020-03-30 16:18
 */
public interface RequestAsyncProcessor {

  /**
   * 异步处理
   * @param request
   */
  void process(Request request);



}
