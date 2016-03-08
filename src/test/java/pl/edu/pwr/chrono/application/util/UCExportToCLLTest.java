package pl.edu.pwr.chrono.application.util;

import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import pl.edu.pwr.Application;
import pl.edu.pwr.chrono.repository.TextRepository;
import pl.edu.pwr.configuration.datasource.DataSourceConfig;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = {Application.class, DataSourceConfig.class})
public class UCExportToCLLTest {


    @Autowired
    private TextRepository repository;

/*    @Test
    public void process_Test_ReturnsCCl(){

        List<Integer> ids = repository.findAllTexts();
        UCExportToCLL ccl =  new UCExportToCLL();

        ids.forEach(id -> {
            List<WordToCllDTO> w = repository.findWordCCL(id);
            ccl.process(w);
        });
    }*/
}