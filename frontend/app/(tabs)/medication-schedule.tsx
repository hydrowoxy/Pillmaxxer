import React, { useState, useEffect } from 'react';
import {
  View,
  Text,
  StyleSheet,
  ScrollView,
  SafeAreaView,
} from 'react-native';
import { getMedicationSchedule, checkDrugInteractions } from '../../api/general'; 
import { MedicationSchedule, DrugInteractionResponse, DailySchedule } from '../../types/Types'; 
import { useAuth } from '@/context/auth-context';

const MedicationScheduleScreen = () => {
  const [schedule, setSchedule] = useState<MedicationSchedule | null>(null);
  const [loading, setLoading] = useState<boolean>(true);
  const [error, setError] = useState<string | null>(null);
  const [drugInteractionsResponse, setDrugInteractionsResponse] = useState<DrugInteractionResponse | null>(null);

  const { user, userId } = useAuth();

  useEffect(() => {
    const loadScheduleAndCheckInteractions = async () => {
      setLoading(true);
      setError(null);
      setDrugInteractionsResponse(null);
      try {
        const today = new Date().toISOString().slice(0, 10); // Get today's date in YYYY-MM-DD format
        console.log("Fetching Schedule for userId:", userId, "Date:", today);
        const fetchedSchedule = await getMedicationSchedule(userId!, today);
        console.log("Fetched Schedule:", fetchedSchedule);

        getDrugInteractionsResponse(fetchedSchedule);

        if (fetchedSchedule) {
          setSchedule({
            userId: userId!,
            dailySchedules: [fetchedSchedule],
          });
        } else {
          setSchedule(null);
        }
      } catch (e: any) {
        setError(e.message || 'Failed to load schedule or check for drug interactions.');
      } finally {
        setLoading(false);
      }
    };

    loadScheduleAndCheckInteractions();
  }, [userId]);

  if (loading) {
    return (
      <SafeAreaView style={styles.container}>
        <Text>Loading schedule and checking for interactions...</Text>
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

  const getDrugInteractionsResponse = async (fetchedSchedule: DailySchedule) => {
    const scheduleObject: MedicationSchedule = {
      userId: userId!,
      dailySchedules: [],
    };

    if (fetchedSchedule) {
      scheduleObject.dailySchedules.push(fetchedSchedule);
    }
    console.log("Checking drug interactions for schedule:", scheduleObject);
    const interactions = await checkDrugInteractions(userId!, scheduleObject); 
    console.log("Drug Interactions found", interactions);
    setDrugInteractionsResponse(interactions);
  }

  return (
    <SafeAreaView style={styles.container}>
      <ScrollView>
        {drugInteractionsResponse && drugInteractionsResponse.interactionsList && drugInteractionsResponse.interactionsList.length > 0 && (
          <View style={styles.interactionContainer}>
            <Text style={styles.interactionTitle}>Drug Interactions Detected:</Text>
            {drugInteractionsResponse.interactionsList.map((interaction, index) => (
              <View key={index} style={styles.singleInteraction}>
                <Text style={styles.interactionPair}>
                  {interaction.drugA} & {interaction.drugB}
                </Text>
                <Text style={styles.interactionLevel}>Level: {interaction.level}</Text>
              </View>
            ))}
          </View>
        )}

        {drugInteractionsResponse && drugInteractionsResponse.interactionsList && drugInteractionsResponse.interactionsList.length === 0 && (
          <View style={styles.interactionContainer}>
            <Text style={styles.interactionTitle}>Drug Interactions:</Text>
            <Text style={styles.noInteraction}>No significant drug interactions found.</Text>
          </View>
        )}

        {!schedule || !schedule.dailySchedules || schedule.dailySchedules.length === 0 ? (
          <Text>No schedule found for today.</Text>
        ) : (
          schedule.dailySchedules.map((dailySchedule: any, index: any) => (
            <View key={index} style={styles.dailyScheduleContainer}>
              <Text style={styles.dateText}>
                {dailySchedule.date}
              </Text>
              {dailySchedule.scheduledDoses.map((dose: any, doseIndex: any) => (
                <View key={doseIndex} style={styles.doseContainer}>
                  <Text style={styles.timeText}>
                    {dose.timeOfDay}
                  </Text>
                  {dose.medications.map((medication: any, medIndex: any) => (
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
          ))
        )}
      </ScrollView>
    </SafeAreaView>
  );
};

const styles = StyleSheet.create({
  container: {
    flex: 1,
    padding: 20,
  },
  interactionContainer: {
    marginBottom: 20,
    borderWidth: 1,
    borderColor: '#ff9999',
    backgroundColor: '#ffe6e6',
    padding: 15,
    borderRadius: 5,
  },
  interactionTitle: {
    fontSize: 16,
    fontWeight: 'bold',
    marginBottom: 10,
    color: '#cc0000',
  },
  singleInteraction: {
    marginBottom: 8,
    padding: 8,
    backgroundColor: '#fff',
    borderRadius: 3,
    borderColor: '#ffcccc',
    borderWidth: 1,
  },
  interactionPair: {
    fontWeight: 'bold',
    marginBottom: 3,
  },
  interactionLevel: {
    fontSize: 14,
  },
  noInteraction: {
    fontStyle: 'italic',
    color: '#339933',
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