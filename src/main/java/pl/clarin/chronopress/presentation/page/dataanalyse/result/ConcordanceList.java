package pl.clarin.chronopress.presentation.page.dataanalyse.result;

import com.google.gwt.thirdparty.guava.common.collect.Maps;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.data.util.converter.StringToDateConverter;
import com.vaadin.server.*;
import com.vaadin.shared.ui.grid.HeightMode;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import javax.enterprise.event.Event;
import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import org.vaadin.viritin.layouts.MVerticalLayout;
import pl.clarin.chronopress.business.property.boundary.DbPropertiesProvider;
import pl.clarin.chronopress.business.sample.boundary.SampleFacade;
import pl.clarin.chronopress.business.sample.entity.Sample;
import pl.clarin.chronopress.presentation.page.samplebrowser.SampleWindow;
import pl.clarin.chronopress.presentation.shered.dto.ConcordanceDTO;
import pl.clarin.chronopress.presentation.shered.theme.ChronoTheme;

@Slf4j
public class ConcordanceList implements CalculationResult {

    private TabSheet sheet = new TabSheet();
    private final Grid grid = new Grid();

    private VerticalLayout tree = new MVerticalLayout().withFullWidth();

    @Inject
    private DbPropertiesProvider provider;

    @Inject
    SampleFacade facade;

    @Inject
    SampleWindow window;

    @Inject
    Event<ShowSampleByIdEvent> showSample;

    private BeanItemContainer<ConcordanceDTO> container = new BeanItemContainer<>(ConcordanceDTO.class);

    private FileDownloader fileDownloader;
    private final Button downloadCSV = new Button(FontAwesome.DOWNLOAD);

    @PostConstruct
    public void init() {
        downloadCSV.setCaption(provider.getProperty("label.download"));
        initializeGrid();

        sheet.setWidth(100, Sizeable.Unit.PERCENTAGE);

        VerticalLayout wrapper = new VerticalLayout();
        wrapper.setCaption(provider.getProperty("label.concordance"));
        wrapper.addComponent(grid);

        tree.setCaption(provider.getProperty("label.stats"));

        HorizontalLayout btn = new HorizontalLayout();
        btn.addComponent(downloadCSV);
        btn.addStyleName(ChronoTheme.SMALL_MARGIN);
        downloadCSV.addStyleName(ValoTheme.BUTTON_TINY);

        wrapper.addComponent(btn);

        sheet.addComponents(wrapper, tree);
        sheet.setCaption(provider.getProperty("label.lexeme.concordance.list"));

    }

    @Override
    public String getType() {
        return "concordance";
    }

    @Override
    public Component showResult() {
        return sheet;
    }

    public Resource createExportContent(List<ConcordanceDTO> data) throws IOException {

        final String date = LocalDate.now().toString();
        java.io.File file = java.io.File.createTempFile("konkordancje-" + date, ".csv");
        file.deleteOnExit();
        FileWriter writer = new FileWriter(file);
        data.forEach(i -> {
            try {
                writer.append(i.getLeft() + ";" + i.getLemma() + ";" + i.getRight() + ";" + i.getPublicationDate() + ";" + i.getJournalTitle() + ";\n");
            } catch (IOException e) {
                log.debug("Export to csv", e);
            }
        });
        writer.flush();
        writer.close();
        return new FileResource(file);
    }

    public void showSampleWindow(Sample s) {
        window.setItem(s);
        UI.getCurrent().addWindow(window);
    }

    public void addData(List<ConcordanceDTO> data) {
        container.removeAllItems();
        container.addAll(data);
        TreeTable tt = buildStatTable(data);
        tt.setWidth(100, Sizeable.Unit.PERCENTAGE);
        tree.removeAllComponents();
        tree.addComponent(tt);

        try {
            fileDownloader = new FileDownloader(createExportContent(data));
        } catch (IOException e) {
            log.debug("Export to csv", e);
        }
        fileDownloader.extend(downloadCSV);
        sheet.setCaption(sheet.getCaption() + " (" + data.size() + ")");
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
        ttable.addItem(new Object[]{provider.getProperty("label.style"), ""}, 2);
        ttable.addItem(new Object[]{provider.getProperty("label.period"), ""}, 3);

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
        grid.setSelectionMode(Grid.SelectionMode.SINGLE);
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
            if ("left".equals(cell.getPropertyId())) {
                return "leftcol";
            }
            if ("word".equals(cell.getPropertyId())) {
                return "centercol";
            }
            return null;
        });

        grid.getColumn("left").setMaximumWidth(380);
        grid.getColumn("right").setMaximumWidth(380);
        grid.getColumn("publicationDate").setMaximumWidth(100);
        grid.addItemClickListener(event -> {
            ConcordanceDTO dto = container.getItem(event.getItemId()).getBean();
            //showSample.fire(new ShowSampleByIdEvent(dto.getTextId(), dto.getLemma()));
            Sample s = facade.findById(dto.getTextId());
            showSampleWindow(s);
        });
    }

    public Grid getGrid() {
        return grid;
    }

    public Map<String, Map<String, Long>> calculateConcordanceStats(final List<ConcordanceDTO> list) {
        Map<String, Map<String, Long>> stats = Maps.newHashMap();

        Map<String, Long> articleTitles = list.stream()
                .filter(i -> i.getArticleTitle() != null)
                .collect(Collectors.groupingBy(ConcordanceDTO::getArticleTitle, Collectors.counting()));
        Map<String, Long> status = list.stream()
                .filter(i -> i.getStatus() != null)
                .collect(Collectors.groupingBy(ConcordanceDTO::getStatus, Collectors.counting()));
        Map<String, Long> style = list.stream()
                .filter(i -> i.getStyle() != null)
                .collect(Collectors.groupingBy(ConcordanceDTO::getStyle, Collectors.counting()));
        Map<String, Long> period = list.stream()
                .filter(i -> i.getPeriod() != null)
                .collect(Collectors.groupingBy(ConcordanceDTO::getPeriod, Collectors.counting()));

        stats.put("article", articleTitles);
        stats.put("status", status);
        stats.put("style", style);
        stats.put("period", period);

        return stats;
    }

}
