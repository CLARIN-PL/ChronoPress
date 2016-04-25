package pl.edu.pwr.chrono.webui.infrastructure.components.results;

import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.server.*;
import com.vaadin.shared.data.sort.SortDirection;
import com.vaadin.shared.ui.grid.HeightMode;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import lombok.extern.slf4j.Slf4j;
import pl.edu.pwr.chrono.readmodel.dto.WordFrequencyDTO;
import pl.edu.pwr.chrono.webui.infrastructure.components.ChronoTheme;
import pl.edu.pwr.configuration.properties.DbPropertiesProvider;

import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.util.List;

@Slf4j
public class FrequencyList implements CalculationResult {

    private final VerticalLayout panel = new VerticalLayout();

    private final Grid grid = new Grid();

    private DbPropertiesProvider provider;

    private BeanItemContainer<WordFrequencyDTO> container = new BeanItemContainer<WordFrequencyDTO>(WordFrequencyDTO.class);

    private final Button downloadCSV = new Button("Pobierz CSV", FontAwesome.DOWNLOAD);
    private FileDownloader fileDownloader;

    public FrequencyList(DbPropertiesProvider provider) {
        this.provider = provider;
        initializeGrid();
        panel.setWidth(100, Sizeable.Unit.PERCENTAGE);
        panel.setMargin(true);
        panel.addComponent(grid);
        panel.setCaption(provider.getProperty("label.lexeme.frequency.list"));

        HorizontalLayout download = new HorizontalLayout();
        download.addStyleName(ChronoTheme.SMALL_MARGIN);
        downloadCSV.addStyleName(ValoTheme.BUTTON_TINY);
        download.addComponent(downloadCSV);

        panel.addComponent(download);
    }

    @Override
    public String getType() {
        return "frequency";
    }

    @Override
    public Component showResult() {
        return panel;
    }

    public void addData(List<WordFrequencyDTO> data) {
        container.removeAllItems();
        container.addAll(data);
        grid.sort("count", SortDirection.DESCENDING);
        try {
            fileDownloader = new FileDownloader(createExportContent(data));
        } catch (IOException e) {
            log.debug("Export to csv", e);
        }
        fileDownloader.extend(downloadCSV);
    }

    private void initializeGrid() {
        grid.setWidth(100, Sizeable.Unit.PERCENTAGE);
        grid.setHeightMode(HeightMode.ROW);
        grid.setHeightByRows(10);
        grid.addStyleName(ChronoTheme.GRID);
        grid.setContainerDataSource(container);
        grid.setColumnOrder("word", "partOfSpeech", "count", "percentage");
        grid.getColumn("word").setHeaderCaption(provider.getProperty("label.word"));
        grid.getColumn("partOfSpeech").setHeaderCaption(provider.getProperty("label.part.of.speech"));
        grid.getColumn("count").setHeaderCaption(provider.getProperty("label.frequency.count"));
        grid.getColumn("percentage").setHeaderCaption(provider.getProperty("label.percentage"));
    }

    public Resource createExportContent(List<WordFrequencyDTO> data) throws IOException {
        final String date = LocalDate.now().toString();
        java.io.File file  = java.io.File.createTempFile("frequency-list-"+date , ".csv");
        file.deleteOnExit();
        FileWriter writer = new FileWriter(file);

        DecimalFormat df = new DecimalFormat();
        df.setMaximumFractionDigits(3);

        data.forEach(i -> {
            try {
                writer.append(i.getWord() + "\t" + i.getPartOfSpeech() + "\t" + Long.toString(i.getCount()) + "\t" + df.format(i.getPercentage()) + "\t\n");
            } catch (IOException e) {
                log.debug("Export to csv", e);
            }
        });
        writer.flush();
        writer.close();
        return new FileResource(file);
    }


}
