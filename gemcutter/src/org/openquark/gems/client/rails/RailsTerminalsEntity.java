package org.openquark.gems.client.rails;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Set;

/**
 * Created by IntelliJ IDEA.
 * User: christopherrude
 * Date: Apr 20, 2008
 * Time: 10:16:50 AM
 * To change this template use File | Settings | File Templates.
 */
@Entity
@Table(catalog = "pipes_development", name = "terminals")
public class RailsTerminalsEntity {
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

    private Set<RailsPipeModulesEntity> in_modules;

    @ManyToMany(mappedBy = "in_terminals")
    public Set<RailsPipeModulesEntity> getIn_modules() {
        return in_modules;
    }

    public void setIn_modules(Set<RailsPipeModulesEntity> in_modules) {
        this.in_modules = in_modules;
    }

    private Set<RailsPipeModulesEntity> out_modules;

    @ManyToMany(mappedBy = "out_terminals")
    public Set<RailsPipeModulesEntity> getOut_modules() {
        return out_modules;
    }

    public void setOut_modules(Set<RailsPipeModulesEntity> out_modules) {
        this.out_modules = out_modules;
    }
}
