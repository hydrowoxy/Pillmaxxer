package com.team27.pillmaxxer.repositories;

import com.google.cloud.firestore.*;
import com.team27.pillmaxxer.model.Medication;
import com.team27.pillmaxxer.dto.MedicationDto;
import com.team27.pillmaxxer.dto.MedicationMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

@Repository
public class MedicationRepository implements FirestoreRepository<Medication, String> {
    private static final String COLLECTION_NAME = "medications";

    private final Firestore firestore;
    private final MedicationMapper mapper;

    @Autowired
    public MedicationRepository(Firestore firestore, MedicationMapper mapper) {
        this.firestore = firestore;
        this.mapper = mapper;
    }

    @Override
    public Medication save(Medication entity) throws ExecutionException, InterruptedException {
        MedicationDto dto = mapper.toDto(entity);

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
    public Optional<Medication> findById(String id) throws ExecutionException, InterruptedException {
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
    public List<Medication> findAll() throws ExecutionException, InterruptedException {
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
    public List<Medication> findByQuery(Query query) throws ExecutionException, InterruptedException {
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
    public Optional<Medication> findByName(String name) throws ExecutionException, InterruptedException {
        Query query = firestore.collection(COLLECTION_NAME)
                .whereEqualTo("name", name)
                .limit(1);

        List<Medication> results = findByQuery(query);
        return results.isEmpty() ? Optional.empty() : Optional.of(results.get(0));
    }

    public List<Medication> findByNameContaining(String namePart) throws ExecutionException, InterruptedException {
        // Firestore doesn't support native string contains queries,
        // so we implement a prefix search as a workaround
        Query query = firestore.collection(COLLECTION_NAME)
                .whereGreaterThanOrEqualTo("name", namePart)
                .whereLessThanOrEqualTo("name", namePart + "\uf8ff");

        return findByQuery(query);
    }
}
