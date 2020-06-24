package kr.co.cmtinfo.seal.app.web;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "project")
@Getter
@Setter
public class WebProperties {
    private String artifactId;
    private String name;
    private String description;

}
