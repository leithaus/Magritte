package org.openquark.gems.client.rails;

import javax.persistence.*;
import java.sql.Timestamp;

/**
 * Created by IntelliJ IDEA.
 * User: christopherrude
 * Date: Apr 20, 2008
 * Time: 10:16:47 AM
 * To change this template use File | Settings | File Templates.
 */
@Entity
@Table(catalog = "pipes_development", name = "categories")
public class RailsCategoriesEntity {
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

    private String source_text;

    @Basic
    @Column(name = "source_text")
    public String getSource_text() {
        return source_text;
    }

    public void setSource_text(String source_text) {
        this.source_text = source_text;
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
/*
    private Set<RailsPipeModulesEntity> modules;

    @OneToMany(mappedBy = "category")
    public Set<RailsPipeModulesEntity> getModules() {
        return modules;
    }

    public void setModules(Set<RailsPipeModulesEntity> modules) {
        this.modules = modules;
    }*/
}
