package com.team27.pillmaxxer.repositories;

import com.google.cloud.firestore.*;
import com.team27.pillmaxxer.model.Reminder;
import com.team27.pillmaxxer.dto.ReminderMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

/**
 * Repository for managing Reminder entities in Firestore.
 */
@Repository
public class ReminderRepository implements FirestoreRepository<Reminder, String> {

    private static final String COLLECTION_NAME = "reminders";

    private final Firestore firestore;
    private final ReminderMapper mapper;

    @Autowired
    public ReminderRepository(Firestore firestore, ReminderMapper mapper) {
        this.firestore = firestore;
        this.mapper = mapper;
    }

    @Override
    public Reminder save(Reminder entity) throws ExecutionException, InterruptedException {

        if (entity.getId() == null) {
            entity.setId(UUID.randomUUID().toString());
        }

        firestore.collection(COLLECTION_NAME)
                .document(entity.getId())
                .set(mapper.toFirestoreMap(entity))
                .get();

        return entity;
    }

    @Override
    public Optional<Reminder> findById(String id) throws ExecutionException, InterruptedException {
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
    public List<Reminder> findAll() throws ExecutionException, InterruptedException {
        return firestore.collection(COLLECTION_NAME)
                .get()
                .get()
                .getDocuments()
                .stream()
                .map(mapper::fromFirestoreDocument)
                .collect(Collectors.toList());
    }

    @Override
    public List<Reminder> findByQuery(Query query) throws ExecutionException, InterruptedException {
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

    public List<Reminder> findByuserId(String userId) throws ExecutionException, InterruptedException {
        Query query = firestore.collection(COLLECTION_NAME)
                .whereEqualTo("userId", userId);

        return findByQuery(query);
    }
}
