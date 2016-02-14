package pl.edu.pwr.chrono.readmodel;

import com.google.common.util.concurrent.ListenableFuture;
import org.springframework.stereotype.Service;
import pl.edu.pwr.chrono.readmodel.dto.DataSelectionDTO;
import pl.edu.pwr.chrono.readmodel.dto.QuantitativeAnalysisDTO;
import pl.edu.pwr.chrono.readmodel.dto.QuantitativeAnalysisResult;

@Service
public interface UCQuantitativeAnalysis {

     ListenableFuture<QuantitativeAnalysisResult> calculate(DataSelectionDTO selection, QuantitativeAnalysisDTO dto);
}
