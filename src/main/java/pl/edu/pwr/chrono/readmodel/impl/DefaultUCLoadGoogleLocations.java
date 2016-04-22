package pl.edu.pwr.chrono.readmodel.impl;

import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.edu.pwr.chrono.readmodel.UCLoadGoogleLocations;
import pl.edu.pwr.chrono.readmodel.dto.DataSelectionDTO;
import pl.edu.pwr.chrono.readmodel.dto.SimpleGeolocation;
import pl.edu.pwr.chrono.repository.TextRepository;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@Slf4j
public class DefaultUCLoadGoogleLocations implements UCLoadGoogleLocations{


    @Autowired
    private TextRepository repository;

    @Override
    public List<SimpleGeolocation> load(DataSelectionDTO dto) {
        return  findCount(repository.findProperNames(dto));
    }

    private List<SimpleGeolocation> findCount(final List<SimpleGeolocation> list){
         Map<SimpleGeolocation, Long> group = list.stream()
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));

        List<SimpleGeolocation> tmp = Lists.newArrayList();
        group.forEach((geo, count) -> {
            geo.setFreq(count);
            tmp.add(geo);
        });
        return tmp;
    }

}
