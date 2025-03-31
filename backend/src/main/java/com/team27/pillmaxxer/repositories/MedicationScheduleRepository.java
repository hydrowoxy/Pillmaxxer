package com.team27.pillmaxxer.repositories;

import com.google.cloud.firestore.*;
import com.team27.pillmaxxer.model.MedicationSchedule;
import com.team27.pillmaxxer.dto.MedicationScheduleMapper;
import com.team27.pillmaxxer.dto.MedicationScheduleDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

/**
 * Repository for managing MedicationSchedule entities in Firestore.
 * Provides custom query methods for finding schedules by date, oatientId and
 * date range.
 */
@Repository
public class MedicationScheduleRepository implements FirestoreRepository<MedicationSchedule, String> {
    private static final String COLLECTION_NAME = "medicationSchedules";

    private final Firestore firestore;
    private final MedicationScheduleMapper mapper;

    @Autowired
    public MedicationScheduleRepository(Firestore firestore, MedicationScheduleMapper mapper) {
        this.firestore = firestore;
        this.mapper = mapper;
    }

    @Override
    public MedicationSchedule save(MedicationSchedule entity) throws ExecutionException, InterruptedException {
        MedicationScheduleDto dto = mapper.toDto(entity);

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
    public Optional<MedicationSchedule> findById(String id) throws ExecutionException, InterruptedException {
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
    public List<MedicationSchedule> findAll() throws ExecutionException, InterruptedException {
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
    public List<MedicationSchedule> findByQuery(Query query) throws ExecutionException, InterruptedException {
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

    public MedicationSchedule findByPatientId(String patientId) throws ExecutionException, InterruptedException {
        Query query = firestore.collection(COLLECTION_NAME)
                .whereEqualTo("patientId", patientId);

        List<MedicationSchedule> schedules = findByQuery(query);
        if (schedules.isEmpty()) {
            return null;
        }

        return schedules.get(0);
    }
}