// package com.team27.pillmaxxer.service;

// import com.team27.pillmaxxer.model.MedicationSchedule;
// import com.team27.pillmaxxer.model.Prescription;
// import com.team27.pillmaxxer.repositories.MedicationScheduleRepository;
// import com.team27.pillmaxxer.repositories.PrescriptionRepository;
// import com.team27.pillmaxxer.TestUtil;
// import org.junit.jupiter.api.BeforeEach;
// import org.junit.jupiter.api.Test;
// import org.junit.jupiter.api.extension.ExtendWith;
// import org.mockito.InjectMocks;
// import org.mockito.Mock;
// import org.mockito.junit.jupiter.MockitoExtension;

// import java.time.LocalDate;
// import java.util.ArrayList;
// import java.util.List;
// import java.util.Optional;
// import java.util.concurrent.ExecutionException;

// import static org.junit.jupiter.api.Assertions.*;
// import static org.mockito.ArgumentMatchers.any;
// import static org.mockito.Mockito.*;

// @ExtendWith(MockitoExtension.class)
// public class MedicationScheduleServiceTest {

//         @Mock
//         private PrescriptionRepository prescriptionRepository;

//         @Mock
//         private MedicationScheduleRepository scheduleRepository;

//         @InjectMocks
//         private MedicationScheduleService medicationScheduleService;

//         private String userId;
//         private LocalDate testDate;
//         private List<Prescription> mockPrescriptions;

//         @BeforeEach
//         public void setUp() {
//                 userId = "patient123";
//                 testDate = LocalDate.now();
//                 mockPrescriptions = TestUtil.createMockPrescriptions(userId, testDate);
//         }

//         @Test
//         public void testPatientScheduleCreationFromPrescriptions() throws ExecutionException, InterruptedException {
//                 // Mock repository behaviors
//                 when(prescriptionRepository.findActiveByuserId(userId))
//                                 .thenReturn(mockPrescriptions);

//                 when(scheduleRepository.findByuserId(userId))
//                                 .thenReturn(null); // Simulate no existing schedule

//                 // returns the first argument (the medication schedule)
//                 when(scheduleRepository.save(any(MedicationSchedule.class)))
//                                 .thenAnswer(invocation -> invocation.getArgument(0));

//                 // Execute the method
//                 MedicationSchedule schedule = medicationScheduleService.createPatientSchedule(userId);

//                 // Verify interactions and assertions
//                 verify(prescriptionRepository).findActiveByuserId(userId);
//                 verify(scheduleRepository).save(any(MedicationSchedule.class));

//                 assertNotNull(schedule);
//                 assertEquals(userId, schedule.getUserId());
//                 assertNotNull(schedule.getDailySchedules());
//                 assertFalse(schedule.getDailySchedules().isEmpty());
//                 assertTrue(schedule.getDailySchedules().stream().anyMatch(ds -> ds.getDate().equals(testDate)));
//                 assertEquals(2, schedule.getDailySchedules().get(0).getScheduledDoses().size());
//         }

//         @Test
//         public void testGetScheduleForDate() throws ExecutionException, InterruptedException {
//                 MedicationSchedule mockSchedule = new MedicationSchedule();
//                 mockSchedule.setUserId(userId);
//                 mockSchedule.setDailySchedules(
//                                 List.of(new MedicationSchedule.DailySchedule(testDate, new ArrayList<>())));

//                 // Mock repository behavior
//                 when(scheduleRepository.findByuserId(userId))
//                                 .thenReturn(mockSchedule);

//                 // Execute the method
//                 Optional<MedicationSchedule.DailySchedule> dailySchedule = medicationScheduleService
//                                 .getScheduleForDate(userId, testDate);

//                 // Verify assertions
//                 assertTrue(dailySchedule.isPresent());
//                 assertEquals(testDate, dailySchedule.get().getDate());
//         }

//         @Test
//         public void testGetScheduleForDateRange() throws ExecutionException, InterruptedException {
//                 LocalDate startDate = testDate.minusDays(1);
//                 LocalDate endDate = testDate.plusDays(1);

//                 MedicationSchedule mockSchedule = new MedicationSchedule();
//                 mockSchedule.setUserId(userId);
//                 mockSchedule.setDailySchedules(List.of(
//                                 new MedicationSchedule.DailySchedule(startDate, new ArrayList<>()),
//                                 new MedicationSchedule.DailySchedule(testDate, new ArrayList<>()),
//                                 new MedicationSchedule.DailySchedule(endDate, new ArrayList<>()),
//                                 new MedicationSchedule.DailySchedule(endDate.plusDays(1), new ArrayList<>())));

//                 // Mock repository behavior
//                 when(scheduleRepository.findByuserId(userId))
//                                 .thenReturn(mockSchedule);

//                 // Execute the method
//                 Optional<MedicationSchedule> schedule = medicationScheduleService.getScheduleForDateRange(userId,
//                                 startDate, endDate);

//                 // Verify assertions
//                 assertTrue(schedule.isPresent());
//                 assertNotNull(schedule.get().getDailySchedules());
//                 assertEquals(3, schedule.get().getDailySchedules().size());
//                 assertTrue(schedule.get().getDailySchedules().stream()
//                                 .allMatch(ds -> !ds.getDate().isBefore(startDate) && !ds.getDate().isAfter(endDate)));
//         }
// }