package pl.edu.pwr.chrono.application.util;


import com.google.common.collect.Maps;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static javax.xml.bind.Marshaller.JAXB_FORMATTED_OUTPUT;

public class UCExportToCLL {

    public void process(List<WordToCllDTO> list) {

        if (list.size() > 0) {
            ChunkList cl = new ChunkList();

            Chunk c = new Chunk();

            Map<Integer, List<WordToCllDTO>> map = Maps.newHashMap();
            list.forEach(i -> {
                if (!map.containsKey(i.getSentenceId())) {
                    map.put(i.getSentenceId(), new ArrayList<>());
                }
                map.get(i.getSentenceId()).add(i);
            });

            map.forEach((k, v) -> {
                Sentence s = new Sentence(Integer.toString(k));
                v.forEach(i -> {
                    s.addToken(i.getOrth(), i.getBase(), i.getCtag());
                });
                c.addSentence(s);
            });


            cl.addChunk(c);

            File file = new File("/home/tnaskret/ccl/plik-" + list.get(0).getSentenceId() + ".ccl");
            JAXBContext jaxbContext = null;
            try {
                jaxbContext = JAXBContext.newInstance(ChunkList.class);
                Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
                jaxbMarshaller.setProperty(JAXB_FORMATTED_OUTPUT, true);
                jaxbMarshaller.marshal(cl, file);
            } catch (JAXBException e) {
                e.printStackTrace();
            }
        }
    }
}
