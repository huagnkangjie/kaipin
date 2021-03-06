package com.kaipin.search.index.build;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import com.alibaba.fastjson.JSON;
import com.kaipin.search.JUnitActionBase;
import com.kaipin.search.constant.SearchTask;
import com.kaipin.search.service.AppSearchService;
import com.kaipin.search.service.SearchLuceneTasksService;

public class LuceneTaskSchoolActionTest extends JUnitActionBase {
 
    @Test
    public void test(){
        try {
 
            Map<String, Object> map = new HashMap<>();

            map.put(SearchLuceneTasksService.TASK_OBJ_ID, "488c6e05-264d-4653-8eae-ee69d8320693");
            map.put(SearchLuceneTasksService.TASK_OBJ_TYPE, SearchTask.ObjType.school);
            map.put(SearchLuceneTasksService.TASK_OPT, SearchTask.OptType.add);
            String json = JSON.toJSONString(map);
            System.out.println(json);

            MvcResult result = mockMvc.perform(post("/lucene/task/operation").contentType(MediaType.APPLICATION_JSON_UTF8)
                    .accept(MediaType.APPLICATION_JSON_UTF8_VALUE).content(json)

            ).andDo(MockMvcResultHandlers.print()).andReturn();

            System.out.println(status().isNoContent());

            System.out.println(content().toString());
            
            
        }catch(Exception e){
            
        }
    }
}
