package kr.co.cmtinfo.seal.app.web.converter;

import kr.co.cmtinfo.seal.domain.environmentvariable.entity.EnvironmentVariableGroup;
import kr.co.cmtinfo.seal.domain.environmentvariable.service.EnvironmentVariableGroupService;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.convert.converter.Converter;

/**
 * @author Yongsu Son
 */
public class StringToEnvironmentVariableGroupConverter implements Converter<String, EnvironmentVariableGroup>, ApplicationContextAware {

    private EnvironmentVariableGroupService service;

    @Override
    public EnvironmentVariableGroup convert(String groupId) {
        return service.findById(Long.parseLong(groupId));
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        service = applicationContext.getBean(EnvironmentVariableGroupService.class);
    }
}
