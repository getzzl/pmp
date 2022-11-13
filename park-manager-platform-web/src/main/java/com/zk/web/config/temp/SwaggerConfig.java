//package com.zk.web.config.temp;
//
//import io.swagger.annotations.ApiOperation;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
//import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
//import springfox.documentation.builders.ApiInfoBuilder;
//import springfox.documentation.builders.ParameterBuilder;
//import springfox.documentation.builders.PathSelectors;
//import springfox.documentation.builders.RequestHandlerSelectors;
//import springfox.documentation.schema.ModelRef;
//import springfox.documentation.service.ApiInfo;
//import springfox.documentation.service.Contact;
//import springfox.documentation.service.Parameter;
//import springfox.documentation.spi.DocumentationType;
//import springfox.documentation.spring.web.plugins.Docket;
//import springfox.documentation.swagger2.annotations.EnableSwagger2;
//
//import java.util.ArrayList;
//import java.util.List;
//
///**
// * @author: ouyangxu
// * @Date: 2019年12月09日 上午 09:36
// */
//@Configuration
//@EnableSwagger2
//public class SwaggerConfig implements WebMvcConfigurer {
//
//    public static final String SWAGGER_SCAN_ADMIN_PACKAGE = "com.gx.rvf.mgr.web.system.controller";
//    public static final String SWAGGER_SCAN_STATIS_PACKAGE = "com.gx.rvf.mgr.web.statistics.controller";
//    public static final String SWAGGER_SCAN_DEVICE_PACKAGE = "com.gx.rvf.mgr.web.device.controller";
//    public static final String SWAGGER_SCAN_WZ_PACKAGE = "com.gx.rvf.mgr.web.wz.controller";
//    public static final String ADMIN_VERSION = "1.0.0";
//
//    @Bean
//    public Docket swaggerSpringMvcPlugin() {
//        //添加head参数start
//        ParameterBuilder tokenPar = new ParameterBuilder();
//        List<Parameter> pars = new ArrayList<>();
//        tokenPar.name("Authorization").description("访问授权码")
//                .modelRef(new ModelRef("string")).parameterType("header").required(false).build();
//        pars.add(tokenPar.build());
//
//        return new Docket(DocumentationType.SWAGGER_2)
//                .apiInfo(apiInfo())
//                .select()
//                .apis(RequestHandlerSelectors.withMethodAnnotation(ApiOperation.class))
//                .build().globalOperationParameters(pars);
//    }
//
//    @Bean
//    public Docket createAdminRestApi() {
//        return new Docket(DocumentationType.SWAGGER_2)
//                .groupName("系统管理接口")
//                .apiInfo(new ApiInfoBuilder().title("系统管理接口").description("系统管理").version(ADMIN_VERSION).build())
//                .select()
//                .apis(RequestHandlerSelectors.basePackage(SWAGGER_SCAN_ADMIN_PACKAGE))
//                .paths(PathSelectors.any())
//                .build();
//    }
//
//    @Bean
//    public Docket createStatisticsRestApi() {
//        return new Docket(DocumentationType.SWAGGER_2)
//                .groupName("统计查询接口")
//                .apiInfo(new ApiInfoBuilder().title("统计查询").description("统计查询").version(ADMIN_VERSION).build())
//                .select()
//                .apis(RequestHandlerSelectors.basePackage(SWAGGER_SCAN_STATIS_PACKAGE))
//                .paths(PathSelectors.any())
//                .build();
//    }
//
//    @Bean
//    public Docket createDeviceRestApi() {
//        return new Docket(DocumentationType.SWAGGER_2)
//                .groupName("设备管理接口")
//                .apiInfo(new ApiInfoBuilder().title("设备管理").description("设备管理").version(ADMIN_VERSION).build())
//                .select()
//                .apis(RequestHandlerSelectors.basePackage(SWAGGER_SCAN_DEVICE_PACKAGE))
//                .paths(PathSelectors.any())
//                .build();
//    }
//
//    @Bean
//    public Docket createWzRestApi() {
//        return new Docket(DocumentationType.SWAGGER_2)
//                .groupName("温州接口")
//                .apiInfo(new ApiInfoBuilder().title("温州接口").description("温州接口").version(ADMIN_VERSION).build())
//                .select()
//                .apis(RequestHandlerSelectors.basePackage(SWAGGER_SCAN_WZ_PACKAGE))
//                .paths(PathSelectors.any())
//                .build();
//    }
//
//    @Override
//    public void addResourceHandlers(ResourceHandlerRegistry registry) {
//        registry.addResourceHandler("swagger-ui.html")
//                .addResourceLocations("classpath:/META-INF/resources/");
//
//        registry.addResourceHandler("/webjars/**")
//                .addResourceLocations("classpath:/META-INF/resources/webjars/");
//
//        registry.addResourceHandler("/swagger-resources/**")
//                .addResourceLocations("classpath:/META-INF/resources/swagger-resources/");
//    }
//
//    private ApiInfo apiInfo() {
//        return new ApiInfoBuilder()
//                .title("Rvf Web Service RESTful APIs")
//                .contact(new Contact("广信研发中心", "", ""))
//                .version(ADMIN_VERSION)
//                .build();
//    }
//}
