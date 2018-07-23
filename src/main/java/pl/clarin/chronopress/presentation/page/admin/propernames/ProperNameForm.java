/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.clarin.chronopress.presentation.page.admin.propernames;

import com.vaadin.data.fieldgroup.BeanFieldGroup;
import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.data.fieldgroup.PropertyId;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.TextField;
import com.vaadin.ui.themes.ValoTheme;
import javax.annotation.PostConstruct;
import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import org.vaadin.viritin.fields.MTextField;
import org.vaadin.viritin.layouts.MFormLayout;
import org.vaadin.viritin.layouts.MVerticalLayout;
import pl.clarin.chronopress.business.propername.entity.ProperName;
import pl.clarin.chronopress.business.property.boundary.DbPropertiesProvider;
import pl.clarin.chronopress.presentation.shered.theme.ChronoTheme;

@Slf4j
public class ProperNameForm extends CustomComponent {

    @PropertyId("orth")
    private final TextField orth = new TextField();

    @PropertyId("base")
    private final TextField base = new MTextField();

    @PropertyId("type")
    private final TextField type = new TextField();

    @PropertyId("userCorrection")
    private final TextField userCorrection = new TextField();

    @PropertyId("nameOnMap")
    private final TextField nameOnMap = new TextField();

    @PropertyId("lat")
    private final TextField lat = new TextField();

    @PropertyId("lon")
    private final TextField lon = new TextField();

    @PropertyId("processed")
    private final CheckBox processed = new CheckBox();

    private final BeanFieldGroup<ProperName> binder = new BeanFieldGroup<>(ProperName.class);

    @Inject
    DbPropertiesProvider provider;

    @PostConstruct
    public void init() {
        binder.bindMemberFields(this);

        orth.setCaption(provider.getProperty("label.orth"));
        base.setCaption(provider.getProperty("label.base"));
        userCorrection.setCaption(provider.getProperty("label.user.correction"));
        userCorrection.setNullRepresentation("");
        type.setCaption(provider.getProperty("label.type"));
        nameOnMap.setCaption(provider.getProperty("label.name.on.map"));
        nameOnMap.setNullRepresentation("");
        lat.setCaption(provider.getProperty("label.lat"));
        lon.setCaption(provider.getProperty("label.lon"));
        processed.setCaption(provider.getProperty("label.perocessed"));
        
        MFormLayout form = new MFormLayout(orth, base, userCorrection, type, nameOnMap, lat, lon, processed)
                .withStyleName(ChronoTheme.COMPACT_FORM, ValoTheme.FORMLAYOUT_LIGHT)
                .withFullWidth();

        MVerticalLayout layout = new MVerticalLayout()
                .with(form)
                .withFullHeight()
                .withFullWidth();

        setCompositionRoot(layout);
    }

    public ProperName getProperName() {
        try {
            binder.commit();
            ProperName names = binder.getItemDataSource().getBean();
            return names;
        } catch (FieldGroup.CommitException e) {
            log.debug("Validation failed", e);
        }
        return binder.getItemDataSource().getBean();
    }

    public void setPropername(ProperName name) {
        binder.setItemDataSource(name);
    }

    public void cleanUp() {
        binder.discard();
        binder.clear();
    }
}
