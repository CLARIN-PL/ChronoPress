package pl.edu.pwr.chrono.webui.infrastructure.components.results;

import com.google.common.collect.Maps;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.data.util.converter.StringToDateConverter;
import com.vaadin.server.Sizeable;
import com.vaadin.shared.ui.grid.HeightMode;
import com.vaadin.ui.Component;
import com.vaadin.ui.Grid;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.TreeTable;
import pl.edu.pwr.chrono.readmodel.dto.ConcordanceDTO;
import pl.edu.pwr.chrono.webui.infrastructure.components.ChronoTheme;
import pl.edu.pwr.configuration.properties.DbPropertiesProvider;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;


public class ConcordanceList implements CalculationResult {

    private final HorizontalLayout panel = new HorizontalLayout();

    private final Grid grid = new Grid();

    private DbPropertiesProvider provider;

    private BeanItemContainer<ConcordanceDTO> container = new BeanItemContainer<>(ConcordanceDTO.class);

    public ConcordanceList(DbPropertiesProvider provider) {
        this.provider = provider;
        initializeGrid();
        panel.setWidth(100, Sizeable.Unit.PERCENTAGE);
        panel.setMargin(true);
        panel.addComponent(grid);
        panel.setCaption(provider.getProperty("label.lexeme.concordance.list"));
    }

    @Override
    public Component showResult() {
        return panel;
    }

    public void addData(List<ConcordanceDTO> data) {
        container.removeAllItems();
        container.addAll(data);
        panel.addComponent(buildStatTable(data));
    }

    private TreeTable buildStatTable(List<ConcordanceDTO> data) {

        final Map<String, Map<String, Long>> stats = calculateConcordanceStats(data);
        final TreeTable ttable = new TreeTable();
        ttable.addContainerProperty(provider.getProperty("label.category"), String.class, "");
        ttable.addContainerProperty(provider.getProperty("label.occurrence.count"), String.class, "");

        ttable.addItem(new Object[]{provider.getProperty("label.article.title"), ""}, 0);
        ttable.addItem(new Object[]{provider.getProperty("label.status"), ""}, 1);
        ttable.addItem(new Object[]{provider.getProperty("label.period"), ""}, 2);
        ttable.addItem(new Object[]{provider.getProperty("label.style"), ""}, 3);
        final int[] nextItem = {4};
        stats.get("article").forEach((s, aLong) -> {
            ttable.addItem(new Object[]{s, Long.toString(aLong)}, nextItem[0]);
            ttable.setParent(nextItem[0], 0);
            nextItem[0]++;
        });
        stats.get("status").forEach((s, aLong) -> {
            ttable.addItem(new Object[]{s, Long.toString(aLong)}, nextItem[0]);
            ttable.setParent(nextItem[0], 1);
            nextItem[0]++;
        });
        stats.get("style").forEach((s, aLong) -> {
            ttable.addItem(new Object[]{s, Long.toString(aLong)}, nextItem[0]);
            ttable.setParent(nextItem[0], 2);
            nextItem[0]++;
        });
        stats.get("period").forEach((s, aLong) -> {
            ttable.addItem(new Object[]{s, Long.toString(aLong)}, nextItem[0]);
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

        grid.getColumn("left").setHeaderCaption("label.left.context");
        grid.getColumn("word").setHeaderCaption("label.searched.word");
        grid.getColumn("right").setHeaderCaption("label.right.context");
        grid.getColumn("publicationDate").setHeaderCaption("label.publication.date");
        grid.getColumn("journalTitle").setHeaderCaption("label.journal.title");

        grid.getColumn("sentence").setHidden(true);
        grid.getColumn("lemma").setHidden(true);
        grid.getColumn("status").setHidden(true);
        grid.getColumn("style").setHidden(true);
        grid.getColumn("period").setHidden(true);
        grid.getColumn("articleTitle").setHidden(true);

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
