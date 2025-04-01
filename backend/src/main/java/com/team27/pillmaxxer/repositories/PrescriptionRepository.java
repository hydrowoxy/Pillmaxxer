package com.team27.pillmaxxer.repositories;

import com.google.cloud.firestore.*;
import com.team27.pillmaxxer.model.Prescription;
import com.team27.pillmaxxer.dto.PrescriptionMapper;
import com.team27.pillmaxxer.dto.PrescriptionDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

/*
 * Repository for managing Prescription entities in Firestore.
 * Provides custom query methods for finding prescriptions by userId, medicationId, and date range.
 */
@Repository
public class PrescriptionRepository implements FirestoreRepository<Prescription, String> {
    private static final String COLLECTION_NAME = "prescriptions";

    private final Firestore firestore;
    private final PrescriptionMapper mapper;

    @Autowired
    public PrescriptionRepository(Firestore firestore, PrescriptionMapper mapper) {
        this.firestore = firestore;
        this.mapper = mapper;
    }

    @Override
    public Prescription save(Prescription entity) throws ExecutionException, InterruptedException {

        PrescriptionDto dto = mapper.toDto(entity);

        if (dto.getId() == null) {
            dto.setId(UUID.randomUUID().toString());
        }

        firestore.collection(COLLECTION_NAME)
                .document(dto.getId())
                .set(dto.toFirestoreMap())
                .get();

        return mapper.toDomainModel(dto);
    }

    @Override
    public Optional<Prescription> findById(String id) throws ExecutionException, InterruptedException {
        DocumentSnapshot document = firestore.collection(COLLECTION_NAME)
                .document(id)
                .get()
                .get();

        if (!document.exists()) {
            return Optional.empty();
        }

        return Optional.of(mapper.toDomainModel(mapper.fromFirestoreDocument(document)));
    }

    @Override
    public List<Prescription> findAll() throws ExecutionException, InterruptedException {
        return firestore.collection(COLLECTION_NAME)
                .get()
                .get()
                .getDocuments()
                .stream()
                .map(mapper::fromFirestoreDocument)
                .map(mapper::toDomainModel)
                .collect(Collectors.toList());
    }

    @Override
    public List<Prescription> findByQuery(Query query) throws ExecutionException, InterruptedException {
        return query.get()
                .get()
                .getDocuments()
                .stream()
                .map(mapper::fromFirestoreDocument)
                .map(mapper::toDomainModel)
                .collect(Collectors.toList());
    }

    @Override
    public void delete(String id) throws ExecutionException, InterruptedException {
        firestore.collection(COLLECTION_NAME)
                .document(id)
                .delete()
                .get();
    }

    // Custom query methods
    public List<Prescription> findByuserId(String userId) throws ExecutionException, InterruptedException {
        Query query = firestore.collection(COLLECTION_NAME)
                .whereEqualTo("userId", userId);
        return findByQuery(query);
    }

    public List<Prescription> findActiveByuserId(String userId) throws ExecutionException, InterruptedException {
        Query query = firestore.collection(COLLECTION_NAME)
                .whereEqualTo("userId", userId)
                .whereEqualTo("active", true);
        return findByQuery(query);
    }

    public List<Prescription> findByMedicationId(String medicationId) throws ExecutionException, InterruptedException {
        Query query = firestore.collection(COLLECTION_NAME)
                .whereEqualTo("medicationId", medicationId);
        return findByQuery(query);
    }

    public List<Prescription> findByDateRange(LocalDate startDate, LocalDate endDate)
            throws ExecutionException, InterruptedException {
        Query query = firestore.collection(COLLECTION_NAME)
                .whereGreaterThanOrEqualTo("endDate", startDate.toString())
                .whereLessThanOrEqualTo("startDate", endDate.toString());
        return findByQuery(query);
    }
}
