package pl.edu.pwr.chrono.webui.infrastructure.components.results;

import com.google.common.collect.Maps;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.data.util.converter.StringToDateConverter;
import com.vaadin.server.*;
import com.vaadin.shared.ui.grid.HeightMode;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import lombok.extern.slf4j.Slf4j;
import pl.edu.pwr.chrono.readmodel.dto.ConcordanceDTO;
import pl.edu.pwr.chrono.webui.infrastructure.components.ChronoTheme;
import pl.edu.pwr.configuration.properties.DbPropertiesProvider;

import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
public class ConcordanceList implements CalculationResult {

    private final HorizontalSplitPanel panel = new HorizontalSplitPanel();

    private final Grid grid = new Grid();

    private DbPropertiesProvider provider;

    private BeanItemContainer<ConcordanceDTO> container = new BeanItemContainer<>(ConcordanceDTO.class);

    private  FileDownloader fileDownloader;
    private final Button downloadCSV = new Button("Pobierz CSV", FontAwesome.DOWNLOAD);

    public ConcordanceList(DbPropertiesProvider provider) {
        this.provider = provider;
        initializeGrid();
        panel.setWidth(100, Sizeable.Unit.PERCENTAGE);

        VerticalLayout wrapper = new VerticalLayout();
        wrapper.addComponent(grid);

        HorizontalLayout btn = new HorizontalLayout();
        btn.addComponent(downloadCSV);
        btn.addStyleName(ChronoTheme.SMALL_MARGIN);
        downloadCSV.addStyleName(ValoTheme.BUTTON_TINY);

        wrapper.addComponent(btn);

        panel.setFirstComponent(wrapper);
        panel.setSplitPosition(75, Sizeable.Unit.PERCENTAGE);
        panel.setCaption(provider.getProperty("label.lexeme.concordance.list"));

    }

    @Override
    public String getType() {
        return "concordance";
    }

    @Override
    public Component showResult() {
        return panel;
    }

    public Resource createExportContent(List<ConcordanceDTO> data) throws IOException {
        final String date = LocalDate.now().toString();
            java.io.File file  = java.io.File.createTempFile("konkordancje-"+date , ".csv");
            file.deleteOnExit();
            FileWriter writer = new FileWriter(file);
            data.forEach(i -> {
                try {
                    writer.append(i.getLeft() + "\t" + i.getLemma() + "\t" + i.getRight() + "\t" + i.getPublicationDate() + "\t" + i.getJournalTitle()+"\t\n");
                } catch (IOException e) {
                    log.debug("Export to csv", e);
                }
            });
            writer.flush();
            writer.close();
        return new FileResource(file);
    }

    public void addData(List<ConcordanceDTO> data) {
        container.removeAllItems();
        container.addAll(data);
        TreeTable tt = buildStatTable(data);
        tt.setWidth(100, Sizeable.Unit.PERCENTAGE);
        panel.setSecondComponent(tt);

        try {
            fileDownloader = new FileDownloader(createExportContent(data));
        } catch (IOException e) {
            log.debug("Export to csv", e);
        }
        fileDownloader.extend(downloadCSV);
        panel.setCaption(panel.getCaption() + " ("+ data.size() +")");
    }

    private TreeTable buildStatTable(List<ConcordanceDTO> data) {

        final Map<String, Map<String, Long>> stats = calculateConcordanceStats(data);
        final TreeTable ttable = new TreeTable();
        ttable.addStyleName(ValoTheme.TREETABLE_SMALL);
        ttable.addStyleName(ValoTheme.TREETABLE_NO_HEADER);
        ttable.addStyleName(ValoTheme.TREETABLE_NO_VERTICAL_LINES);

        ttable.addContainerProperty(provider.getProperty("label.category"), String.class, "");
        ttable.addContainerProperty(provider.getProperty("label.occurrence.count"), String.class, "");
        ttable.addItem(new Object[]{provider.getProperty("label.article.title"), ""}, 0);
        ttable.addItem(new Object[]{provider.getProperty("label.status"), ""}, 1);
        ttable.addItem(new Object[]{provider.getProperty("label.period"), ""}, 2);
        ttable.addItem(new Object[]{provider.getProperty("label.style"), ""}, 3);

        final int[] nextItem = {4};
        stats.get("article").forEach((s, aLong) -> {
            ttable.addItem(new Object[]{s, Long.toString(aLong)}, nextItem[0]);
            ttable.setChildrenAllowed(nextItem[0], false);
            ttable.setParent(nextItem[0], 0);
            nextItem[0]++;
        });
        stats.get("status").forEach((s, aLong) -> {
            ttable.addItem(new Object[]{s, Long.toString(aLong)}, nextItem[0]);
            ttable.setChildrenAllowed(nextItem[0], false);
            ttable.setParent(nextItem[0], 1);
            nextItem[0]++;
        });
        stats.get("style").forEach((s, aLong) -> {
            ttable.addItem(new Object[]{s, Long.toString(aLong)}, nextItem[0]);
            ttable.setChildrenAllowed(nextItem[0], false);
            ttable.setParent(nextItem[0], 2);
            nextItem[0]++;
        });
        stats.get("period").forEach((s, aLong) -> {
            ttable.addItem(new Object[]{s, Long.toString(aLong)}, nextItem[0]);
            ttable.setChildrenAllowed(nextItem[0], false);
            ttable.setParent(nextItem[0], 3);
            nextItem[0]++;
        });

        return ttable;
    }

    private void initializeGrid() {
        grid.setWidth(100, Sizeable.Unit.PERCENTAGE);
        grid.setHeightMode(HeightMode.ROW);
        grid.setHeightByRows(15);
        grid.addStyleName(ChronoTheme.GRID);
        grid.setContainerDataSource(container);
        grid.setColumnOrder("left", "word", "right", "publicationDate", "journalTitle");

        grid.getColumn("left").setHeaderCaption(provider.getProperty("label.left.context"));
        grid.getColumn("word").setHeaderCaption(provider.getProperty("label.searched.word"));
        grid.getColumn("right").setHeaderCaption(provider.getProperty("label.right.context"));
        grid.getColumn("publicationDate").setHeaderCaption(provider.getProperty("label.publication.date"));
        grid.getColumn("journalTitle").setHeaderCaption(provider.getProperty("label.journal.title"));

        grid.getColumn("sentence").setHidden(true);
        grid.getColumn("lemma").setHidden(true);
        grid.getColumn("status").setHidden(true);
        grid.getColumn("style").setHidden(true);
        grid.getColumn("period").setHidden(true);
        grid.getColumn("articleTitle").setHidden(true);
        grid.getColumn("textId").setHidden(true);

        grid.getColumn("publicationDate").setConverter(new StringToDateConverter() {
            @Override
            public DateFormat getFormat(Locale locale) {

                return new SimpleDateFormat("dd-MM-yyyy");
            }
        });
        grid.setStyleName("ch-grid");
        grid.setCellStyleGenerator(cell -> {
            if ("left".equals(cell.getPropertyId())) return "leftcol";
            if ("word".equals(cell.getPropertyId())) return "centercol";
            return null;
        });

        grid.getColumn("left").setMaximumWidth(380);
        grid.getColumn("right").setMaximumWidth(380);
        grid.getColumn("publicationDate").setMaximumWidth(100);

    }

    public Grid getGrid(){
        return  grid;
    }

    public Map<String, Map<String, Long>> calculateConcordanceStats(final List<ConcordanceDTO> list) {
        Map<String, Map<String, Long>> stats = Maps.newHashMap();

        Map<String, Long> articleTitles = list.stream()
                .collect(Collectors.groupingBy(ConcordanceDTO::getArticleTitle, Collectors.counting()));
        Map<String, Long> status = list.stream()
                .collect(Collectors.groupingBy(ConcordanceDTO::getStatus, Collectors.counting()));
        Map<String, Long> style = list.stream()
                .collect(Collectors.groupingBy(ConcordanceDTO::getStyle, Collectors.counting()));
        Map<String, Long> period = list.stream()
                .collect(Collectors.groupingBy(ConcordanceDTO::getPeriod, Collectors.counting()));

        stats.put("article", articleTitles);
        stats.put("status", status);
        stats.put("style", style);
        stats.put("period", period);

        return stats;
    }


}
