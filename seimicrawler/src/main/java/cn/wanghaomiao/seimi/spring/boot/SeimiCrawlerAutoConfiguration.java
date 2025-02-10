package cn.wanghaomiao.seimi.spring.boot;

import cn.wanghaomiao.seimi.Constants;
import cn.wanghaomiao.seimi.annotation.EnableSeimiCrawler;
import cn.wanghaomiao.seimi.spring.common.CrawlerCache;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;

import jakarta.annotation.PostConstruct;

/**
 * @author: github.com/zhegexiaohuozi seimimaster@gmail.com
 * @since 2018/5/7.
 */
@AutoConfiguration
@ConditionalOnProperty(name = {Constants.SEIMI_CRAWLER_BOOTSTRAP_ENABLED})
@EnableConfigurationProperties({CrawlerProperties.class})
@ComponentScan({"**/crawlers", "**/queues", "**/interceptors", "cn.wanghaomiao.seimi"})
@EnableSeimiCrawler
public class SeimiCrawlerAutoConfiguration {
}
