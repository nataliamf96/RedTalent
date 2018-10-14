package src.redtalent.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.validation.MessageCodesResolver;
import org.springframework.validation.Validator;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.HandlerMethodReturnValueHandler;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.config.annotation.*;

import java.util.List;

@Configuration
public class PageConfig implements WebMvcConfigurer {

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        return bCryptPasswordEncoder;
    }

    public void configurePathMatch(PathMatchConfigurer pathMatchConfigurer) {

    }

    public void configureContentNegotiation(ContentNegotiationConfigurer contentNegotiationConfigurer) {

    }

    public void configureAsyncSupport(AsyncSupportConfigurer asyncSupportConfigurer) {

    }

    public void configureDefaultServletHandling(DefaultServletHandlerConfigurer defaultServletHandlerConfigurer) {

    }

    public void addFormatters(FormatterRegistry formatterRegistry) {

    }

    public void addInterceptors(InterceptorRegistry interceptorRegistry) {

    }

    public void addResourceHandlers(ResourceHandlerRegistry resourceHandlerRegistry) {

    }

    public void addCorsMappings(CorsRegistry corsRegistry) {

    }

    public void addViewControllers(ViewControllerRegistry registry) {

        registry.addViewController("/home").setViewName("home");
        registry.addViewController("/").setViewName("home");
        registry.addViewController("/dashboard").setViewName("dashboard");
        registry.addViewController("/login").setViewName("login");
        registry.addViewController("/user/index").setViewName("user/index");

    }

    public void configureViewResolvers(ViewResolverRegistry viewResolverRegistry) {

    }

    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> list) {

    }

    public void addReturnValueHandlers(List<HandlerMethodReturnValueHandler> list) {

    }

    public void configureMessageConverters(List<HttpMessageConverter<?>> list) {

    }

    public void extendMessageConverters(List<HttpMessageConverter<?>> list) {

    }

    public void configureHandlerExceptionResolvers(List<HandlerExceptionResolver> list) {

    }

    public void extendHandlerExceptionResolvers(List<HandlerExceptionResolver> list) {

    }

    public Validator getValidator() {
        return null;
    }

    public MessageCodesResolver getMessageCodesResolver() {
        return null;
    }
}
