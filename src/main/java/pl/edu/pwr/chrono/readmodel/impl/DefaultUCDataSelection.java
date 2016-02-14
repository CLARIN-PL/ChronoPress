package pl.edu.pwr.chrono.readmodel.impl;

import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListeningExecutorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.edu.pwr.chrono.readmodel.UCDataSelection;
import pl.edu.pwr.chrono.readmodel.dto.DataSelectionDTO;
import pl.edu.pwr.chrono.readmodel.dto.DataSelectionResult;
import pl.edu.pwr.chrono.repository.TextRepository;

import java.util.Optional;

@Service
public class DefaultUCDataSelection implements UCDataSelection {

    @Autowired
    private TextRepository textRepository;

    @Autowired
    private ListeningExecutorService service;

    @Override
    @Transactional(readOnly = true)
    public ListenableFuture<Optional<DataSelectionResult>> search(DataSelectionDTO dto) {
        return service.submit(() -> textRepository.countSamplesByCriteria(dto));
    }
}
