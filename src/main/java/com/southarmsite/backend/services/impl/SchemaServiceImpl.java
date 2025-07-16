package com.southarmsite.backend.services.impl;

import com.southarmsite.backend.services.SchemaService;
import jakarta.persistence.Column;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Table;
import jakarta.persistence.metamodel.Attribute;
import jakarta.persistence.metamodel.EntityType;
import jakarta.persistence.metamodel.SingularAttribute;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
public class SchemaServiceImpl implements SchemaService {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public String getSchemaAsString() {
        StringBuilder schema = new StringBuilder();
        Set<EntityType<?>> entities = entityManager.getMetamodel().getEntities();

        for (EntityType<?> entity : entities) {
            String tableName = getTableName(entity);
            schema.append("Table: ").append(tableName).append("\n");
            Set<? extends Attribute<?, ?>> attributes = entity.getAttributes();
            List<String> columns = new ArrayList<>();

            for (Attribute<?, ?> attribute : attributes) {
                String columnInfo = getColumnInfo(attribute);
                columns.add(columnInfo);
            }
            schema.append("Columns: ").append(String.join(", ", columns)).append("\n");

            // Add relationships
            String relationships = getRelationships(entity);
            if (!relationships.isEmpty()) {
                schema.append("Relationships: ").append(relationships).append("\n");
            }

            schema.append("\n");
        }
        System.out.println(schema.toString());

        return schema.toString();
    }

    private String getTableName(EntityType<?> entity) {
        Class<?> javaType = entity.getJavaType();
        if (javaType.isAnnotationPresent(Table.class)) {
            Table tableAnnotation = javaType.getAnnotation(Table.class);
            if (!tableAnnotation.name().isEmpty()) {
                return tableAnnotation.name();
            }
        }

        return camelToSnakeCase(entity.getName());
    }

    private String getColumnInfo(Attribute<?, ?> attribute) {
        String columnName = attribute.getName();
        String javaTypeName = attribute.getJavaType().getSimpleName();
        String sqlDataType = "";

        try {
            Field field = attribute.getJavaMember() instanceof Field ?
                    (Field) attribute.getJavaMember() : null;

            // Prioritize @Column or @JoinColumn name if present
            if (field != null) {
                if (field.isAnnotationPresent(Column.class)) {
                    Column columnAnnotation = field.getAnnotation(Column.class);
                    if (!columnAnnotation.name().isEmpty()) {
                        columnName = columnAnnotation.name();
                    }
                } else if (field.isAnnotationPresent(jakarta.persistence.JoinColumn.class)) {
                    jakarta.persistence.JoinColumn joinColumn = field.getAnnotation(jakarta.persistence.JoinColumn.class);
                    if (!joinColumn.name().isEmpty()) {
                        columnName = joinColumn.name();
                    }
                    // --- FIX STARTS HERE ---
                    // For a JoinColumn, the actual SQL type is determined by the
                    // primary key of the target entity.
                    EntityType<?> targetEntityType = entityManager.getMetamodel().entity(attribute.getJavaType());
                    SingularAttribute<?, ?> idAttribute = targetEntityType.getId(targetEntityType.getIdType().getJavaType());
                    sqlDataType = mapJavaTypeToSQL(idAttribute.getJavaType().getSimpleName());
                    // --- FIX ENDS HERE ---
                }
            }

            // If not a JoinColumn, map based on its own Java type
            if (sqlDataType.isEmpty()) { // Only map if it hasn't been set by JoinColumn logic
                sqlDataType = mapJavaTypeToSQL(javaTypeName);
            }


        } catch (Exception e) {
            // Log the exception, don't just swallow
            // System.err.println("Error getting column info for " + attribute.getName() + ": " + e.getMessage());
            // Fallback in case of error
            sqlDataType = mapJavaTypeToSQL(javaTypeName);
        }

        return columnName + " (" + sqlDataType + ")";
    }

    private String getRelationships(EntityType<?> entity) {
        List<String> relationships = new ArrayList<>();
        for (Attribute<?, ?> attribute : entity.getAttributes()) {
            Attribute.PersistentAttributeType type = attribute.getPersistentAttributeType();
            String name = attribute.getName();
            String targetEntity = attribute.getJavaType().getSimpleName();

            switch (type) {
                case MANY_TO_ONE ->
                        relationships.add(name + " -> (ManyToOne) " + targetEntity);

                case ONE_TO_MANY ->
                        relationships.add(name + " <- (OneToMany) " + targetEntity);

                case ONE_TO_ONE ->
                        relationships.add(name + " <-> (OneToOne) " + targetEntity);

                case MANY_TO_MANY ->
                        relationships.add(name + " <-> (ManyToMany) " + targetEntity);


                default -> {
                }
            }
        }

        return String.join(", ", relationships);
    }


    private String mapJavaTypeToSQL(String javaType) {
        return switch (javaType.toLowerCase()) {
            case "string" -> "VARCHAR";
            case "integer", "int" -> "INTEGER";
            case "long" -> "BIGINT";
            case "double" -> "DOUBLE PRECISION";
            case "float" -> "REAL";
            case "boolean" -> "BOOLEAN";
            case "localdate" -> "DATE";
            case "localdatetime" -> "TIMESTAMP";
            case "bigdecimal" -> "DECIMAL";
            default -> "VARCHAR";
        };
    }


    private String camelToSnakeCase(String camelCase) {
        return camelCase.replaceAll("([a-z])([A-Z])", "$1_$2").toLowerCase();
    }

}
