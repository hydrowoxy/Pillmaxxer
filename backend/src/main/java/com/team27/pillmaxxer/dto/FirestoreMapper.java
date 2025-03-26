package com.team27.pillmaxxer.dto;

import com.google.cloud.firestore.DocumentSnapshot;

public interface FirestoreMapper<D, M> {
    // Convert domain model to DTO
    D toDto(M model);

    // Convert DTO to domain model
    M toDomainModel(D dto);

    // Convert Firestore document to DTO
    D fromFirestoreDocument(DocumentSnapshot document);
}
