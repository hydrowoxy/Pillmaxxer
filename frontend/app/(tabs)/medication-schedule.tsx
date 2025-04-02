import React, { useState, useEffect } from 'react';
import {
  View,
  Text,
  StyleSheet,
  ScrollView,
  SafeAreaView,
} from 'react-native';

interface MedicationScheduleScreenProps {
  scheduleId: string; // Or however you're passing the schedule ID
  fetchSchedule: (scheduleId: string) => Promise<any | null>; // Function to fetch the schedule
}

const MedicationScheduleScreen: React.FC<MedicationScheduleScreenProps> = ({ scheduleId, fetchSchedule }) => {
  const [schedule, setSchedule] = useState<any | null>(null);
  const [loading, setLoading] = useState<boolean>(true);
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    const loadSchedule = async () => {
      setLoading(true);
      setError(null);
      try {
        //const fetchedSchedule = await fetchSchedule(scheduleId);
        //setSchedule(fetchedSchedule);
      } catch (e: any) {
        setError(e.message || 'Failed to load schedule.');
      } finally {
        setLoading(false);
      }
    };

    loadSchedule();
  }, [scheduleId, fetchSchedule]);

  if (loading) {
    return (
      <SafeAreaView style={styles.container}>
        <Text>Loading schedule...</Text>
      </SafeAreaView>
    );
  }

  if (error) {
    return (
      <SafeAreaView style={styles.container}>
        <Text style={styles.errorText}>{error}</Text>
      </SafeAreaView>
    );
  }

  if (!schedule || !schedule.dailySchedules || schedule.dailySchedules.length === 0) {
    return (
      <SafeAreaView style={styles.container}>
        <Text>No schedule found.</Text>
      </SafeAreaView>
    );
  }

  return (
    <SafeAreaView style={styles.container}>
      <ScrollView>
        {schedule.dailySchedules.map((dailySchedule:any, index:any) => (
          <View key={index} style={styles.dailyScheduleContainer}>
            <Text style={styles.dateText}>
              {dailySchedule.date}
            </Text>
            {dailySchedule.scheduledDoses.map((dose:any, doseIndex:any) => (
              <View key={doseIndex} style={styles.doseContainer}>
                <Text style={styles.timeText}>
                  {dose.timeOfDay}
                </Text>
                {dose.medications.map((medication:any, medIndex:any) => (
                  <View key={medIndex} style={styles.medicationContainer}>
                    <Text style={styles.medicationName}>
                      {medication.medicationName}
                    </Text>
                    <Text>Dosage: {medication.dosage}</Text>
                    <Text>Instructions: {medication.instructions}</Text>
                  </View>
                ))}
              </View>
            ))}
          </View>
        ))}
      </ScrollView>
    </SafeAreaView>
  );
};

const styles = StyleSheet.create({
  container: {
    flex: 1,
    padding: 20,
  },
  dailyScheduleContainer: {
    marginBottom: 20,
    borderWidth: 1,
    borderColor: '#ccc',
    padding: 10,
    borderRadius: 5,
  },
  dateText: {
    fontSize: 18,
    fontWeight: 'bold',
    marginBottom: 10,
  },
  doseContainer: {
    marginBottom: 10,
    padding: 10,
    borderWidth: 1,
    borderColor: '#eee',
    borderRadius: 5,
  },
  timeText: {
    fontWeight: 'bold',
    marginBottom: 5,
  },
  medicationContainer: {
    marginLeft: 10,
    marginBottom: 5,
  },
  medicationName: {
    fontWeight: 'bold',
  },
  errorText: {
    color: 'red',
  },
});

export default MedicationScheduleScreen;