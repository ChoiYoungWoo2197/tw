package kr.co.cmtinfo.seal;

import kr.co.cmtinfo.seal.core.application.SealApplication;
import kr.co.cmtinfo.seal.app.web.WebProperties;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages = {"kr.co.cmtinfo.seal"})
@EntityScan(basePackages = {"kr.co.cmtinfo.seal"})
@EnableJpaRepositories(basePackages = {"kr.co.cmtinfo.seal"})
@EnableConfigurationProperties({WebProperties.class})
public class WebSiteApplication {
    public static void main(String[] args) {
        SealApplication.run(WebSiteApplication.class, args);
    }
}
