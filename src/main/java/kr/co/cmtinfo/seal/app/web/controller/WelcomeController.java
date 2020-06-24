package kr.co.cmtinfo.seal.app.web.controller;

import kr.co.cmtinfo.seal.domain.member.service.SimpleMemberService;
import kr.co.cmtinfo.seal.app.web.WebProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.util.Map;

@Controller
public class WelcomeController {

    final WebProperties webProperties;

    SimpleMemberService simpleMemberService;

    @Autowired
    private RequestMappingHandlerMapping handlerMapping;

    public WelcomeController(WebProperties webProperties, SimpleMemberService simpleMemberService) {
        this.webProperties = webProperties;
        this.simpleMemberService = simpleMemberService;
    }

    @GetMapping({"/", "/welcome"})
    String welcome() {
        String artifactId = webProperties.getArtifactId();

        Map<RequestMappingInfo, HandlerMethod> mappings = handlerMapping.getHandlerMethods();
//        Set<RequestMappingInfo> keys = mappings.keySet();
//        Iterator<RequestMappingInfo> iterator = keys.iterator();
//
//        List<String> urlList = new ArrayList<String>();
//        while (iterator.hasNext()) {
//            RequestMappingInfo key = iterator.next();
//            HandlerMethod value = mappings.get(key);
//            urlList.add(key.getPatternsCondition().toString());
//
//            logger.info(key.getPatternsCondition()  + "[" + key.getMethodsCondition()  + "] : " + value);
//        }

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        boolean a = authentication.isAuthenticated();

        return "welcome";
    }


    @GetMapping({"/admin", "/admin/welcome"})
    String admin_welcome() {

        return "admin/welcome";
    }
}