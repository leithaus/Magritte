package org.openquark.gems.client.rails;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Set;

/**
 * Created by IntelliJ IDEA.
 * User: christopherrude
 * Date: Apr 20, 2008
 * Time: 10:16:49 AM
 * To change this template use File | Settings | File Templates.
 */
@Entity
@Table(catalog = "pipes_development", name = "pipe_modules")
public class RailsPipeModulesEntity {
    private int id;

    @Id
    @Column(name = "id", nullable = false, length = 10)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    private String name;

    @Basic
    @Column(name = "name")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    private String type;

    @Basic
    @Column(name = "type")
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    private String description;

    @Basic
    @Column(name = "description")
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    private String ui;

    @Basic
    @Column(name = "ui", length = 65535)
    public String getUi() {
        return ui;
    }

    public void setUi(String ui) {
        this.ui = ui;
    }

    private Timestamp createdAt;

    @Basic
    @Column(name = "created_at", length = 0)
    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    private Timestamp updatedAt;

    @Basic
    @Column(name = "updated_at", length = 0)
    public Timestamp getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Timestamp updatedAt) {
        this.updatedAt = updatedAt;
    }

    private String help;

    @Basic
    @Column(name = "help", length = 65535)
    public String getHelp() {
        return help;
    }

    public void setHelp(String help) {
        this.help = help;
    }

    private RailsCategoriesEntity category;

    @ManyToOne
    @JoinTable(catalog = "pipes_development", name = "module_categories", joinColumns = @JoinColumn(name = "pipe_module_id", referencedColumnName = "id"), inverseJoinColumns = @JoinColumn(name = "category_id", referencedColumnName = "id"))
    public RailsCategoriesEntity getCategory() {
        return category;
    }

    public void setCategory(RailsCategoriesEntity category) {
        this.category = category;
    }

    private Set<RailsTerminalsEntity> in_terminals;

    @ManyToMany
    @JoinTable(catalog = "pipes_development", name = "input_terminals", joinColumns = @JoinColumn(name = "pipe_module_id", referencedColumnName = "id"), inverseJoinColumns = @JoinColumn(name = "terminal_id", referencedColumnName = "id"))
    public Set<RailsTerminalsEntity> getIn_terminals() {
        return in_terminals;
    }

    public void setIn_terminals(Set<RailsTerminalsEntity> in_terminals) {
        this.in_terminals = in_terminals;
    }

    private Set<RailsTerminalsEntity> out_terminals;

    @ManyToMany
    @JoinTable(catalog = "pipes_development", name = "output_terminals", joinColumns = @JoinColumn(name = "pipe_module_id", referencedColumnName = "id"), inverseJoinColumns = @JoinColumn(name = "terminal_id", referencedColumnName = "id"))
    public Set<RailsTerminalsEntity> getOut_terminals() {
        return out_terminals;
    }

    public void setOut_terminals(Set<RailsTerminalsEntity> out_terminals) {
        this.out_terminals = out_terminals;
    }
}
