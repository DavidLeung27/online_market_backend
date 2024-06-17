package com.onlinemarket.server.productCategory;

import java.util.Set;

import org.checkerframework.common.aliasing.qual.Unique;
import org.hibernate.search.engine.backend.types.Projectable;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.FullTextField;

import com.onlinemarket.server.product.Product;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "category")
public class Category {
    @Id
    @GeneratedValue
    private Integer category_id;

    @Column(nullable = false)
    @FullTextField(projectable = Projectable.YES)
    private String category;

    @ManyToMany(mappedBy = "linkedCategory", cascade = CascadeType.ALL)
    Set<Product> linkedProduct;

}
