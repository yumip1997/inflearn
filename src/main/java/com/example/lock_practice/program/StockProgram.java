package com.example.lock_practice.program;

import com.example.lock_practice.com.utils.ResTemplateUtils;
import com.example.lock_practice.com.vo.RestTemplateVO;
import com.example.lock_practice.product.entity.Product;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.gson.reflect.TypeToken;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;
import java.util.List;

public class StockProgram {

    private static final String BASE_URL = "http://localhost:8080";
    private static final String ORDER_URL =  BASE_URL + "/order";
    private static final String PRODUCT_ALL_URL = BASE_URL + "/product/all";
    private static final String BLANK = " ";
    private static final String LONG_BLANK = "                ";

    public static void main(String[] args){
        ResTemplateUtils resTemplateUtils = new ResTemplateUtils(createRestTemplate());
        printAllProduct(getAllProduct(resTemplateUtils));
    }

    private static RestTemplate createRestTemplate() {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getMessageConverters()
                .add(0, new StringHttpMessageConverter(StandardCharsets.UTF_8));
        return restTemplate;
    }

    private static List<Product> getAllProduct(ResTemplateUtils resTemplateUtils) {
        RestTemplateVO productRestTemplateVO = RestTemplateVO.builder().url(PRODUCT_ALL_URL).build();
        return resTemplateUtils.callAPIByGet(productRestTemplateVO, new TypeToken<List<Product>>(){}.getType());
    }

    private static void printAllProduct(List<Product> allProduct) {
        System.out.println("상품번호" + BLANK + "상품이름" + LONG_BLANK + "판매가격" + BLANK+ "재고수");
        for (Product product : allProduct) {
            System.out.println(product.getProductId() + BLANK + product.getProductName() + BLANK + product.getSalePrice() + BLANK + product.getQuantity());
        }
    }

}
