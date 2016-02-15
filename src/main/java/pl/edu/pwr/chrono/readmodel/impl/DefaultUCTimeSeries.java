package pl.edu.pwr.chrono.readmodel.impl;

import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListeningExecutorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.edu.pwr.chrono.readmodel.UCTimeSeries;
import pl.edu.pwr.chrono.readmodel.dto.DataSelectionDTO;
import pl.edu.pwr.chrono.readmodel.dto.TimeSeriesDTO;
import pl.edu.pwr.chrono.readmodel.dto.TimeSeriesResult;
import pl.edu.pwr.chrono.repository.TextRepository;

import javax.persistence.EntityManager;

@Service
public class DefaultUCTimeSeries implements UCTimeSeries {

    @Autowired
    private EntityManager em;

    @Autowired
    private ListeningExecutorService service;

    @Autowired
    private TextRepository repository;

    @Override
    @Transactional(readOnly = true)
    public ListenableFuture<TimeSeriesResult> calculate(final DataSelectionDTO selection, final TimeSeriesDTO dto) {
        return service.submit(() -> repository.findTimeSeries(selection, dto));
    }


}
