package com.team27.pillmaxxer.dto;

import com.google.cloud.firestore.DocumentSnapshot;

/**
 * Defines a contract for mapping between domain models and DTOs (Data Transfer
 * Objects)
 * with Firestore document conversion capabilities.
 * 
 * Generic type parameters:
 * 
 * @param <D> The DTO (Data Transfer Object) type
 * @param <M> The domain Model type
 */
public interface FirestoreMapper<D, M> {

    D toDto(M model);

    M toDomainModel(D dto);

    D fromFirestoreDocument(DocumentSnapshot document);
}