package com.team27.pillmaxxer.service;

import com.team27.pillmaxxer.model.MedicationSchedule;
import com.team27.pillmaxxer.model.Prescription;
import com.team27.pillmaxxer.repositories.MedicationScheduleRepository;
import com.team27.pillmaxxer.repositories.PrescriptionRepository;
import com.team27.pillmaxxer.TestUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutionException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class MedicationScheduleServiceTest {

    @Mock
    private PrescriptionRepository prescriptionRepository;

    @Mock
    private MedicationScheduleRepository scheduleRepository;

    @InjectMocks
    private MedicationScheduleService medicationScheduleService;

    private String patientId;
    private LocalDate testDate;

    @BeforeEach
    public void setUp() {
        patientId = "patient123";
        testDate = LocalDate.now();
    }

    @Test
    public void testPatientScheduleCreationFromPrescriptions() throws ExecutionException, InterruptedException {
        // Prepare mock prescriptions
        List<Prescription> mockPrescriptions = TestUtil.createMockPrescriptions(patientId, testDate);

        // Mock repository behaviors
        when(prescriptionRepository.findActiveByPatientId(patientId))
                .thenReturn(mockPrescriptions);

        when(scheduleRepository.findByPatientAndDate(patientId, testDate))
                .thenReturn(Optional.empty());

        // returns the first argument (the medication schedule)
        when(scheduleRepository.save(any(MedicationSchedule.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        // Execute the method
        MedicationSchedule schedule = medicationScheduleService.createPatientSchedule(patientId);

        // Verify interactions and assertions
        verify(prescriptionRepository).findActiveByPatientId(patientId);
        verify(scheduleRepository).save(any(MedicationSchedule.class));

        assertNotNull(schedule);
        assertEquals(patientId, schedule.getPatientId());
        assertEquals(testDate, schedule.getDate());
        assertNotNull(schedule.getScheduledDoses());
        assertFalse(schedule.getScheduledDoses().isEmpty());
        assertEquals(2, schedule.getScheduledDoses().size());
    }

    @Test
    public void testGenerateDailySchedule() throws ExecutionException, InterruptedException {
        // Prepare mock prescriptions
        List<Prescription> mockPrescriptions = TestUtil.createMockPrescriptions(patientId, testDate);

        // Mock repository behaviors
        when(prescriptionRepository.findActiveByPatientId(patientId))
                .thenReturn(mockPrescriptions);

        when(scheduleRepository.findByPatientAndDate(patientId, testDate))
                .thenReturn(Optional.empty());

        // Execute the method
        MedicationSchedule schedule = medicationScheduleService.generateDailySchedule(patientId, testDate);

        // Verify assertions
        assertNotNull(schedule);
        assertEquals(patientId, schedule.getPatientId());
        assertEquals(testDate, schedule.getDate());

        // Verify schedule generation for different frequencies
        assertEquals(2, schedule.getScheduledDoses().size());

        // Verify time slots
        assertTrue(schedule.getScheduledDoses().stream()
                .anyMatch(dose -> dose.getTimeOfDay().equals(LocalTime.of(9, 0))));
        assertTrue(schedule.getScheduledDoses().stream()
                .anyMatch(dose -> dose.getTimeOfDay().equals(LocalTime.of(21, 0))));
    }

    @Test
    public void testGetScheduleForDate() throws ExecutionException, InterruptedException {
        MedicationSchedule mockSchedule = new MedicationSchedule();
        mockSchedule.setPatientId(patientId);
        mockSchedule.setDate(testDate);

        // Mock repository behavior
        when(scheduleRepository.findByPatientAndDate(patientId, testDate))
                .thenReturn(Optional.of(mockSchedule));

        // Execute the method
        Optional<MedicationSchedule> schedule = medicationScheduleService.getScheduleForDate(patientId, testDate);

        // Verify assertions
        assertTrue(schedule.isPresent());
        assertEquals(patientId, schedule.get().getPatientId());
        assertEquals(testDate, schedule.get().getDate());
    }
}