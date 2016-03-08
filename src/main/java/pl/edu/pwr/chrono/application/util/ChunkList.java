package pl.edu.pwr.chrono.application.util;

import com.google.common.collect.Lists;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement
public class ChunkList {


    private List<Chunk> chunk = Lists.newArrayList();

    @XmlElement
    public List<Chunk> getChunk() {
        return chunk;
    }

    public void setChunks(List<Chunk> chunks) {
        this.chunk = chunks;
    }

    public void addChunk(Chunk ch) {
        chunk.add(ch);
    }
}