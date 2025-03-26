package com.team27.pillmaxxer.repositories;

import com.google.cloud.firestore.Query;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutionException;

/**
 * Generic repository interface for Firestore operations.
 * Provides basic CRUD operations and query capabilities for Firestore
 * documents.
 *
 * @param <T>  The entity type this repository manages
 * @param <ID> The type of the entity's identifier - generally a String or UUID
 */
public interface FirestoreRepository<T, ID> {

    T save(T entity) throws ExecutionException, InterruptedException;

    Optional<T> findById(ID id) throws ExecutionException, InterruptedException;

    List<T> findAll() throws ExecutionException, InterruptedException;

    List<T> findByQuery(Query query) throws ExecutionException, InterruptedException;

    void delete(ID id) throws ExecutionException, InterruptedException;
}