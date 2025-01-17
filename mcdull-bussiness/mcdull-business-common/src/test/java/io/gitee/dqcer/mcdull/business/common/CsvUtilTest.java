package io.gitee.dqcer.mcdull.business.common;

import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class CsvUtilTest {

//    @Test
    void parse() throws IOException {
        ClassPathResource resource = new ClassPathResource("demo.csv");
        Map<String, String> searchMap = new HashMap<>();
        searchMap.put("ID", "005");
        List<String> displayFields = new ArrayList<>();
        displayFields.add("Id");
        displayFields.add("Code");
        displayFields.add("ID");
        List<List<String>> data = CsvUtil.parse(resource.getInputStream(), searchMap, displayFields);
        System.out.println(data);
    }
}