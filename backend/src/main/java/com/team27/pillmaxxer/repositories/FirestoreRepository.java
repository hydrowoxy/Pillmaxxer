package com.team27.pillmaxxer.repositories;

import com.google.cloud.firestore.Query;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutionException;

public interface FirestoreRepository<T, ID> {
    T save(T entity) throws ExecutionException, InterruptedException;

    Optional<T> findById(ID id) throws ExecutionException, InterruptedException;

    List<T> findAll() throws ExecutionException, InterruptedException;

    List<T> findByQuery(Query query) throws ExecutionException, InterruptedException;

    void delete(ID id) throws ExecutionException, InterruptedException;
}
