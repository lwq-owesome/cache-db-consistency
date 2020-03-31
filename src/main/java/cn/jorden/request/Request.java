package cn.jorden.request;

/**
 * <p>
 *
 * </p>
 *
 * @author jorden.li
 * @since 2020-03-30 16:21
 */
public interface Request {

  void process();


  Integer getProductId();


  boolean isForceFefresh();


}
