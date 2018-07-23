package pl.clarin.chronopress.presentation.page.dataanalyse.result;

import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.server.*;
import com.vaadin.shared.data.sort.SortDirection;
import com.vaadin.shared.ui.grid.HeightMode;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import pl.clarin.chronopress.business.property.boundary.DbPropertiesProvider;
import pl.clarin.chronopress.presentation.shered.dto.FrequencyItem;
import pl.clarin.chronopress.presentation.shered.theme.ChronoTheme;

@Slf4j
public class FrequencyList implements CalculationResult {

    private final VerticalLayout panel = new VerticalLayout();

    private final Grid grid = new Grid();

    @Inject
    private DbPropertiesProvider provider;

    private BeanItemContainer<FrequencyItem> container = new BeanItemContainer<FrequencyItem>(FrequencyItem.class);

    private final Button downloadCSV = new Button(FontAwesome.DOWNLOAD);
    private FileDownloader fileDownloader;

    @PostConstruct
    public void init() {
        downloadCSV.setCaption(provider.getProperty("label.download"));

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

    public void addData(List<FrequencyItem> data) {
        container.removeAllItems();
        container.addAll(data);
        grid.sort("value", SortDirection.DESCENDING);
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
        grid.setColumnOrder("word", "partOfSpeech", "value", "percentage");
        grid.getColumn("word").setHeaderCaption(provider.getProperty("label.word"));
        grid.getColumn("partOfSpeech").setHeaderCaption(provider.getProperty("label.part.of.speech"));
        grid.getColumn("value").setHeaderCaption(provider.getProperty("label.frequency.count"));
        grid.getColumn("percentage").setHeaderCaption(provider.getProperty("label.percentage"));
    }

    public Resource createExportContent(List<FrequencyItem> data) throws IOException {
        final String date = LocalDate.now().toString();

        Collections.sort(data, (Object o1, Object o2) -> {
            Long x1 = ((FrequencyItem) o1).getValue();
            Long x2 = ((FrequencyItem) o2).getValue();
            return x1.compareTo(x2);
        });
        Collections.reverse(data);

        java.io.File file = java.io.File.createTempFile("frequency-list-" + date, ".csv");
        file.deleteOnExit();
        try (FileWriter writer = new FileWriter(file)) {
            DecimalFormat df = new DecimalFormat();
            df.setMaximumFractionDigits(3);

            data.forEach(i -> {
                try {
                    writer.append(i.getWord() + ";" + Long.toString(i.getValue()) + ";\n");
                } catch (IOException e) {
                    log.debug("Export to csv", e);
                }
            });
            writer.flush();
        }
        return new FileResource(file);
    }

}
