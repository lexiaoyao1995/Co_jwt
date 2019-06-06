package com.swjtu;

import com.swjtu.model.History;
import com.swjtu.service.HistoryService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TestDemoJarApplicationTests {

	@Autowired
	private HistoryService historyService;

	@Test
	public void contextLoads() {
        List<History> list = historyService.getAll();
        for (History i : list){
            System.out.println(list);
        }
    }

    @Test
    public void test2(){
	    History history = new History();
	    history.setAnswer("asdasd");
	    history.setQuestion("asdasd");
        historyService.insert(history);
    }

}
