/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.clarin.chronopress.business.lexicalfield.entity;

import java.io.Serializable;
import java.util.Set;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Table;
import lombok.Data;
import pl.clarin.chronopress.business.shered.Identifiable;

@Entity
@Table(name = "lexical_field")
@Data
public class LexicalField implements Serializable, Identifiable<Long> {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

    @Column(name = "group_name")
    private String groupName;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(
            name = "lexical_names",
            joinColumns = @JoinColumn(name = "lexical_field_id")
    )
    private Set<String> lexicalnames;

}