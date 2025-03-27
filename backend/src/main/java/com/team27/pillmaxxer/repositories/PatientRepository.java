package com.team27.pillmaxxer.repositories;

import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.Query;
import com.team27.pillmaxxer.model.Patient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutionException;

@Repository
public class PatientRepository implements FirestoreRepository<Patient, String> {

    private static final String COLLECTION_NAME = "patients";

    private final Firestore firestore;

    @Autowired
    public PatientRepository(Firestore firestore) {
        this.firestore = firestore;
    }

    @Override
    public Patient save(Patient entity) throws ExecutionException, InterruptedException {
        firestore.collection(COLLECTION_NAME)
                .document(entity.getPatientId())
                .set(entity)
                .get();
        return entity;
    }

    @Override
    public Optional<Patient> findById(String id) throws ExecutionException, InterruptedException {
        return Optional.ofNullable(
                firestore.collection(COLLECTION_NAME)
                        .document(id)
                        .get()
                        .get()
                        .toObject(Patient.class));
    }

    @Override
    public List<Patient> findAll() throws ExecutionException, InterruptedException {
        return firestore.collection(COLLECTION_NAME)
                .get()
                .get()
                .getDocuments()
                .stream()
                .map(document -> document.toObject(Patient.class))
                .toList();
    }

    @Override
    public List<Patient> findByQuery(Query query) throws ExecutionException, InterruptedException {
        return query.get()
                .get()
                .getDocuments()
                .stream()
                .map(document -> document.toObject(Patient.class))
                .toList();
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