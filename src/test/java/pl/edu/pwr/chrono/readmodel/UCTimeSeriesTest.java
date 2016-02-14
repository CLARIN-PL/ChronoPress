package pl.edu.pwr.chrono.readmodel;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import pl.edu.pwr.Application;
import pl.edu.pwr.configuration.DataSourceConfig;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = {Application.class, DataSourceConfig.class})
public class UCTimeSeriesTest {

    @Autowired
    private UCTimeSeries service;

    @Test
    public void test() {
    }

}
