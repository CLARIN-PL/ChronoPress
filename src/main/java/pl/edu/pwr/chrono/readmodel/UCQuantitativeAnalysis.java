package pl.edu.pwr.chrono.readmodel;

import org.springframework.stereotype.Service;
import pl.edu.pwr.chrono.readmodel.dto.DataSelectionResult;
import pl.edu.pwr.chrono.readmodel.dto.QuantitativeAnalysisDTO;
import pl.edu.pwr.chrono.readmodel.dto.QuantitativeAnalysisResult;

/**
 * Created by tnaskret on 08.02.16.
 */

@Service
public interface UCQuantitativeAnalysis {

     QuantitativeAnalysisResult calculate(DataSelectionResult result, QuantitativeAnalysisDTO dto);
}
