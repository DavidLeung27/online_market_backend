package com.onlinemarket.server.product;

import java.sql.Timestamp;
import java.util.Set;

import org.hibernate.search.engine.backend.types.Projectable;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.FullTextField;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.Indexed;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.IndexedEmbedded;

import com.onlinemarket.server.productCategory.Category;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Indexed
@Table(name = "product")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer product_id;

    @Column(nullable = false)
    @FullTextField(projectable = Projectable.YES)
    private String productName;

    @Column(nullable = false)
    private Float price;

    @Column(name = "image", columnDefinition = "LONGBLOB")
    private byte[] image;

    private Integer hitRate = 0;

    @Column(nullable = false)
    @ManyToMany
    @IndexedEmbedded
    @JoinTable(name = "Product_Category_Table", joinColumns = @JoinColumn(name = "product_id"), inverseJoinColumns = @JoinColumn(name = "category_id"))
    private Set<Category> linkedCategory;

    @Column(nullable = false)
    private Timestamp createdTime;

    @Column(nullable = false)
    private Timestamp updatedTime;
}
