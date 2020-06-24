package kr.co.cmtinfo.seal.app.web.config;

import kr.co.cmtinfo.seal.app.web.converter.StringToEnvironmentVariableGroupConverter;
import kr.co.cmtinfo.seal.core.web.servlet.mvc.resolver.FilterableHandlerMethodArgumentResolver;
import kr.co.cmtinfo.seal.core.web.servlet.mvc.method.SealRequestMappingInfoHandlerMethodMappingNamingStrategy;
import kr.co.cmtinfo.seal.core.web.view.*;
import org.springframework.beans.BeansException;
import org.springframework.boot.autoconfigure.web.servlet.WebMvcRegistrations;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.MessageSource;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.format.FormatterRegistry;
import org.springframework.http.MediaType;
import org.springframework.validation.Validator;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.accept.ContentNegotiationManager;
import org.springframework.web.accept.HeaderContentNegotiationStrategy;
import org.springframework.web.accept.ParameterContentNegotiationStrategy;
import org.springframework.web.filter.HiddenHttpMethodFilter;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.*;
import org.springframework.web.servlet.i18n.CookieLocaleResolver;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;
import org.springframework.web.servlet.view.ContentNegotiatingViewResolver;
import org.springframework.web.servlet.view.ResourceBundleViewResolver;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;
import org.springframework.web.servlet.view.xml.MappingJackson2XmlView;

import java.nio.charset.StandardCharsets;
import java.util.*;

@Configuration
//@EnableWebMvc
public class WebConfig implements WebMvcConfigurer, WebMvcRegistrations, ResourceLoaderAware, ApplicationContextAware {

    private ResourceLoader resourceLoader;
    private ApplicationContext applicationContext;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/images/**").addResourceLocations("classpath:/static/images/");
        registry.addResourceHandler("/css/**").addResourceLocations("classpath:/static/css/");
        registry.addResourceHandler("/js/**").addResourceLocations("classpath:/static/js/");
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        LocaleChangeInterceptor localeChangeInterceptor = new LocaleChangeInterceptor();
        localeChangeInterceptor.setParamName("lang");
        registry.addInterceptor(localeChangeInterceptor);
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(new FilterableHandlerMethodArgumentResolver());
    }

    @Override
    public Validator getValidator() {
        LocalValidatorFactoryBean factoryBean = new LocalValidatorFactoryBean();
        factoryBean.setValidationMessageSource(messageSource());
        return factoryBean;
    }

    @Override
    public RequestMappingHandlerMapping getRequestMappingHandlerMapping() {
        RequestMappingHandlerMapping requestMappingHandlerMapping
                = new RequestMappingHandlerMapping();
        requestMappingHandlerMapping.setHandlerMethodMappingNamingStrategy(new SealRequestMappingInfoHandlerMethodMappingNamingStrategy());

        return requestMappingHandlerMapping;
    }

    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addConverter(new StringToEnvironmentVariableGroupConverter());
    }

    @Bean
    public LocaleResolver localeResolver() {
        CookieLocaleResolver cookieLocaleResolver = new CookieLocaleResolver();
        cookieLocaleResolver.setDefaultLocale(Locale.KOREAN);
        return cookieLocaleResolver;
    }

    @Bean
    public MessageSource messageSource() {
        ResourceBundleMessageSource messageSource
                = new ResourceBundleMessageSource();
        messageSource.setBasename("messages");
        messageSource.setDefaultEncoding(StandardCharsets.UTF_8.name());
        return messageSource;
    }

    @Bean
    public FilterRegistrationBean<HiddenHttpMethodFilter> registerHiddenHttpMethodFilter() {
        FilterRegistrationBean<HiddenHttpMethodFilter> registrationBean
                = new FilterRegistrationBean<>();

        registrationBean.setFilter(new HiddenHttpMethodFilter());
        registrationBean.addUrlPatterns("/*");

        return registrationBean;
    }

    @Override
    public void configureContentNegotiation(ContentNegotiationConfigurer configurer) {
        configurer.ignoreAcceptHeader(true).defaultContentType(MediaType.TEXT_HTML);

        Map<String, MediaType> mediaTypes = new HashMap<>();
        mediaTypes.put("html", MediaType.TEXT_HTML);
        mediaTypes.put("pdf", MediaType.valueOf("application/pdf"));
        mediaTypes.put("xls", MediaType.valueOf("application/vnd.ms-excel"));
        mediaTypes.put("xlsx", MediaType.valueOf("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"));
        mediaTypes.put("xml", MediaType.APPLICATION_XML);
        mediaTypes.put("json", MediaType.APPLICATION_JSON);

        configurer.mediaTypes(mediaTypes);
        configurer.strategies(Arrays.asList(
                new ParameterContentNegotiationStrategy(mediaTypes),
                new HeaderContentNegotiationStrategy()
        ));
    }

    @Bean
    public ContentNegotiatingViewResolver contentNegotiatingViewResolver
            (ContentNegotiationManager contentNegotiationManager) {
        ContentNegotiatingViewResolver viewResolver = new ContentNegotiatingViewResolver();
        viewResolver.setContentNegotiationManager(contentNegotiationManager);
        return viewResolver;
    }

    @Bean
    public ResourceBundleViewResolver resourceBundleViewResolver() {
        ResourceBundleViewResolver viewResolver = new ResourceBundleViewResolver();
        viewResolver.setBasename("views");
        viewResolver.setOrder(Integer.MIN_VALUE);
        return viewResolver;
    }

    @Override
    public void setResourceLoader(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

//    @Bean
//    ViewResolver jsonViewResolver() {
//        return new ViewResolver() {
//            @Override
//            public View resolveViewName(String viewName, Locale locale) throws Exception {
//                MappingJackson2JsonView view = new MappingJackson2JsonView();
//                view.setPrettyPrint(true);
//                return view;
//            }
//        };
//    }

    @Override
    public void configureViewResolvers(ViewResolverRegistry registry) {
        MappingJackson2XmlView xmlView = new SealMappingJackson2XmlView();
        xmlView.setPrettyPrint(true);
        xmlView.setModelKey("content");

        MappingJackson2JsonView jsonView = new SealMappingJackson2JsonView();
        jsonView.setPrettyPrint(true);
        jsonView.setModelKey("content");

        registry.enableContentNegotiation(xmlView, jsonView, new SealMappingXlsxView(), new SealMappingPdfView());
    }

//    @Bean
//    ModelMapper modelMapper() {
//        ModelMapper modelMapper = new ModelMapper();
//        return modelMapper;
//    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}