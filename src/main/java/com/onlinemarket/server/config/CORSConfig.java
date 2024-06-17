//package com.onlinemarket.server.config;
//
//import org.springframework.context.annotation.Configuration;
//import org.springframework.web.servlet.config.annotation.CorsRegistry;
//import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
//
//@Configuration
//public class CORSConfig implements WebMvcConfigurer {
//
//    @Override
//    public void addCorsMappings(CorsRegistry registry) {
//        registry.addMapping("/**")
//                .allowedOrigins("*")
//                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
//                .allowedHeaders("Access-Control-Allow-Origin", "Content-Type", "Authorization")
////                .allowCredentials(true)
//                .maxAge(3600);
//
//////        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
////        CorsConfiguration config = new CorsConfiguration();
////        config.setAllowCredentials(true);
////        config.addAllowedOrigin("*");
////        config.addAllowedHeader("Access-Control-Allow-Headers, Content-Type, Authorization");
////        config.addAllowedMethod("GET, POST, PUT, DELETE, OPTIONS");
////
////        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
////
////        source.registerCorsConfiguration("/**", config);
////
////        return new CorsFilter(source);
//    }
//
////    @Bean
////    public FilterRegistrationBean<TokenFilter> myCustomFilter() {
////        FilterRegistrationBean<TokenFilter> registrationBean = new FilterRegistrationBean<>();
////        registrationBean.setFilter(new TokenFilter());
////        registrationBean.setOrder(Ordered.LOWEST_PRECEDENCE - 1); // Adjust the order
////        registrationBean.addUrlPatterns("/**"); // Specify URL patterns for your filter
////        return registrationBean;
////    }
//
//}
