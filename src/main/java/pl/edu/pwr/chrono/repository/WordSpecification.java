package pl.edu.pwr.chrono.repository;

import org.springframework.data.jpa.domain.Specification;
import pl.edu.pwr.chrono.domain.Word;
import pl.edu.pwr.chrono.readmodel.dto.QuantitativeAnalysisDTO;

import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by tnaskret on 08.02.16.
 */
public class WordSpecification {

    public static Specification<Word> filter(final List<Integer> ids, final QuantitativeAnalysisDTO dto){

        return (root, query, cb) -> {

            List<Predicate> criteriaList = new ArrayList<>();
            if(dto.getNoun()){
                criteriaList.add(isNoun().toPredicate(root,query,cb));
            }
            if(dto.getAdjective()){
                criteriaList.add(isAdj().toPredicate(root,query,cb));
            }
            if(dto.getAdverb()){
                criteriaList.add(isAdv().toPredicate(root,query,cb));
            }
            if (dto.getVerb()){
                criteriaList.add(isVerb().toPredicate(root,query,cb));
            }
            List<Predicate> cl = new ArrayList<>();
            Predicate p1 =  inIdList(ids).toPredicate(root, query, cb);
            cl.add(p1);
            if(criteriaList.size() > 0) {
                Predicate p2 = cb.or(criteriaList.toArray(new Predicate[0]));
                cl.add(p2);
            }
            return cb.and(cl.toArray(new Predicate[0]));
        };
    }

    public static Specification<Word> inIdList(Collection<Integer> ids){
        return (root, query, cb) -> {
            Predicate predicate  = root.get("id").in(ids);
            return predicate;
        };
    }

    public static Specification<Word> isVerb(){
        return (root, query, cb) -> {
            Predicate predicate  = cb.equal(root.get("pos_alias"), "verb");
            return predicate;
        };
    }

    public static Specification<Word> isNoun(){
        return (root, query, cb) -> {
            Predicate predicate  = cb.equal(root.get("pos_alias"), "noun");
            return predicate;
        };
    }

    public static Specification<Word> isAdj(){
        return (root, query, cb) -> {
            Predicate predicate  = cb.equal(root.get("pos_alias"), "adj");
            return predicate;
        };
    }

    public static Specification<Word> isAdv(){
        return (root, query, cb) -> {
            Predicate predicate  = cb.equal(root.get("pos_alias"), "adv");
            return predicate;
        };
    }

}
