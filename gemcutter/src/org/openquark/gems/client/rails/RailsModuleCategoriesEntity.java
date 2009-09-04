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
@Table(catalog = "pipes_development", name = "module_categories")
public class RailsModuleCategoriesEntity {
    private int id;

    @Id
    @Column(name = "id", nullable = false, length = 10)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    private int pipeModuleId;

    @Basic
    @Column(name = "pipe_module_id", nullable = true, length = 10)
    public int getPipeModuleId() {
        return pipeModuleId;
    }

    public void setPipeModuleId(int id) {
        this.pipeModuleId = id;
    }

    private int categoryId;

    @Basic
    @Column(name = "category_id", nullable = true, length = 10)
    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int id) {
        this.categoryId = id;
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

}
