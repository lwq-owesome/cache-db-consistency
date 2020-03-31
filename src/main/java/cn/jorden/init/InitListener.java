package cn.jorden.init;

import cn.jorden.request.RequestProcessorThreadPool;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 * <p>
 *
 * </p>
 *
 * @author jorden.li
 * @since 2020-03-31 13:56
 */
public class InitListener implements ServletContextListener
{

  @Override
  public void contextInitialized(ServletContextEvent servletContextEvent) {
    RequestProcessorThreadPool.init();
  }

  @Override
  public void contextDestroyed(ServletContextEvent servletContextEvent) {

  }
}
