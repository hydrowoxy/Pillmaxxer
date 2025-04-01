package com.team27.pillmaxxer.repositories;

import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.Query;
import com.team27.pillmaxxer.dto.PatientMapper;
import com.team27.pillmaxxer.model.Patient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

@Repository
public class PatientRepository implements FirestoreRepository<Patient, String> {

    private static final String COLLECTION_NAME = "patients";

    private final Firestore firestore;
    private final PatientMapper mapper;

    @Autowired
    public PatientRepository(Firestore firestore, PatientMapper mapper) {
        this.firestore = firestore;
        this.mapper = mapper;
    }

    @Override
    public Patient save(Patient entity) throws ExecutionException, InterruptedException {
        firestore.collection(COLLECTION_NAME)
                .document(entity.getUserId())
                .set(mapper.toFirestoreMap(entity))
                .get();
        return entity;
    }

    @Override
    public Optional<Patient> findById(String id) throws ExecutionException, InterruptedException {
        DocumentSnapshot document = firestore.collection(COLLECTION_NAME)
                .document(id)
                .get()
                .get();

        if (!document.exists()) {
            return Optional.empty();
        }

        return Optional.of(mapper.fromFirestoreDocument(document));
    }

    @Override
    public List<Patient> findAll() throws ExecutionException, InterruptedException {
        return firestore.collection(COLLECTION_NAME)
                .get()
                .get()
                .getDocuments()
                .stream()
                .map(mapper::fromFirestoreDocument)
                .collect(Collectors.toList());
    }

    @Override
    public List<Patient> findByQuery(Query query) throws ExecutionException, InterruptedException {
        return query.get()
                .get()
                .getDocuments()
                .stream()
                .map(mapper::fromFirestoreDocument)
                .collect(Collectors.toList());
    }

    @Override
    public void delete(String id) throws ExecutionException, InterruptedException {
        firestore.collection(COLLECTION_NAME)
                .document(id)
                .delete()
                .get();
    }

    // Patient-specific queries
    public Optional<Patient> findByUserId(String userId) throws ExecutionException, InterruptedException {
        Query query = firestore.collection(COLLECTION_NAME)
                .whereEqualTo("userId", userId)
                .limit(1);

        List<Patient> patients = findByQuery(query);
        return patients.isEmpty() ? Optional.empty() : Optional.of(patients.get(0));
    }
}