/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.clarin.chronopress.business.propername.boundary;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.NoResultException;
import lombok.extern.slf4j.Slf4j;
import pl.clarin.chronopress.business.importer.entity.ProperNameXml;
import pl.clarin.chronopress.business.propername.entity.ProperName;
import pl.clarin.chronopress.business.sample.boundary.SentenceProperNameRepository;
import pl.clarin.chronopress.business.sample.entity.Sentence;
import pl.clarin.chronopress.business.sample.entity.SentenceProperName;

@Stateless
@Slf4j
public class ProperNameFacade {

    @Inject
    ProperNameRepository properNameRepository;

    @Inject
    SentenceProperNameRepository sentenceProperNameRepository;

    public void saveSentenceProperName(List<ProperNameXml> list, Sentence s, Map<String, ProperName> nam, Map<String, ProperName> nam_geo) {
        List<ProperName> properNames = new ArrayList<>();

        list.stream().map((pn) -> {
            ProperName proper;
            if (pn.getType().equals("nam")) {
                if (!nam.containsKey(pn.getOrth())) {
                    proper = findByOrthAndType(pn.getOrth(), pn.getType());
                    if (proper == null) {
                        proper = properNameRepository.save(new ProperName(pn.getOrth(), pn.getBase(), pn.getType()));
                    }
                    nam.put(pn.getOrth(), proper);
                } else {
                    proper = nam.get(pn.getOrth());
                }
            } else if (!nam_geo.containsKey(pn.getOrth())) {
                proper = findByOrthAndType(pn.getOrth(), pn.getType());
                if (proper == null) {
                    proper = properNameRepository.save(new ProperName(pn.getOrth(), pn.getBase(), pn.getType()));
                }
                nam_geo.put(pn.getOrth(), proper);
            } else {
                proper = nam_geo.get(pn.getOrth());
            }
            return proper;
        }).forEach((proper) -> {
            properNames.add(proper);
        });

        Map<Long, List<ProperName>> gruped = properNames.stream().collect(Collectors.groupingBy((properName) -> properName.getId()));

        gruped.forEach((k, v) -> {
            SentenceProperName spn = new SentenceProperName();
            spn.setOccurenceCount(v.size());
            spn.setSentence(s);
            spn.setProperName(v.get(0));
            sentenceProperNameRepository.saveAndFlush(spn);
        });
    }

    public ProperName save(ProperName pn) {
        return properNameRepository.saveAndFlushAndRefresh(pn);
    }

    public SentenceProperName findSentenceProperNameByIds(Long sentenceId, Long properId) {
        try {
            return sentenceProperNameRepository.findBySentenceAndProperName(sentenceId, properId);
        } catch (NoResultException ex) {
            // log.info("Not Found", ex);
        }
        return null;
    }

    public ProperName findByOrthAndType(String orth, String type) {
        try {
            return properNameRepository.findByOrthAndType(orth, type);
        } catch (NoResultException ex) {
            //  log.info("Not Found", ex);
        }
        return null;
    }

    public List<ProperName> findNotProcessedGeolocation() {
        return properNameRepository.findGeoloctionProperNamesNotProcessed();
    }
     public List<ProperName> findAllGeolocation() {
        return properNameRepository.findGeoloctionProperNames();
    }
}
