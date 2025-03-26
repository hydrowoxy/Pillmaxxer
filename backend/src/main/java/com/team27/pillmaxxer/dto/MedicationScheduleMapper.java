package com.team27.pillmaxxer.dto;

import com.google.cloud.firestore.DocumentSnapshot;
import com.team27.pillmaxxer.model.MedicationSchedule;
import org.springframework.stereotype.Component;

import java.time.LocalTime;
import java.util.stream.Collectors;

/*
 * This class maps MedicationSchedule objects to MedicationScheduleDto objects and vice versa.
 * It also maps Firestore documents to MedicationScheduleDto objects using the methods defined in MedicationScheduleDto.
 */
@Component
public class MedicationScheduleMapper implements FirestoreMapper<MedicationScheduleDto, MedicationSchedule> {

    @Override
    public MedicationScheduleDto toDto(MedicationSchedule model) {
        MedicationScheduleDto dto = new MedicationScheduleDto();
        dto.setId(model.getId());
        dto.setPatientId(model.getPatientId());
        dto.setDate(model.getDate());

        dto.setScheduledDoses(model.getScheduledDoses().stream()
                .map(MedicationScheduleDto.ScheduledDoseDto::fromDomain)
                .collect(Collectors.toList()));

        return dto;
    }

    @Override
    public MedicationSchedule toDomainModel(MedicationScheduleDto dto) {
        MedicationSchedule model = new MedicationSchedule();
        model.setId(dto.getId());
        model.setPatientId(dto.getPatientId());
        model.setDate(dto.getDate());

        model.setScheduledDoses(dto.getScheduledDoses().stream()
                .map(sd -> new MedicationSchedule.ScheduledDose(
                        LocalTime.parse(sd.getTimeOfDay()),
                        sd.getMedications().stream()
                                .map(m -> new MedicationSchedule.MedicationDose(
                                        m.getMedicationId(),
                                        m.getMedicationName(),
                                        m.getDosage(),
                                        m.getInstructions(),
                                        m.getPrescriptionId()))
                                .collect(Collectors.toList())))
                .collect(Collectors.toList()));

        return model;
    }

    @Override
    public MedicationScheduleDto fromFirestoreDocument(DocumentSnapshot document) {
        if (!document.exists()) {
            return null;
        }
        return MedicationScheduleDto.fromFirestoreMap(document.getData());
    }
}