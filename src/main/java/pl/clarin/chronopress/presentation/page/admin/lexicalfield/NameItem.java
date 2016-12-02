/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.clarin.chronopress.presentation.page.admin.lexicalfield;

import lombok.Data;

@Data
public class NameItem {

    private Long id;
    private String name;

    public NameItem() {
    }

    public NameItem(Long id, String name) {
        this.id = id;
        this.name = name;
    }
}
