import React, { useState, useEffect } from "react"
import { View, Text, StyleSheet, ScrollView, SafeAreaView } from "react-native"
import { getMedicationSchedule, checkDrugInteractions } from "../../api/general"
import {
  MedicationSchedule,
  DrugInteractionResponse,
  DailySchedule,
} from "../../types/Types"
import { useAuth } from "@/context/auth-context"

const MedicationScheduleScreen = () => {
  const [schedule, setSchedule] = useState<MedicationSchedule | null>(null)
  const [loading, setLoading] = useState<boolean>(true)
  const [error, setError] = useState<string | null>(null)
  const [drugInteractionsResponse, setDrugInteractionsResponse] =
    useState<DrugInteractionResponse | null>(null)

  const { user, userId } = useAuth()

  const getDrugInteractionsResponse = async (
    fetchedSchedule: DailySchedule
  ) => {
    const scheduleObject: MedicationSchedule = {
      userId: userId!,
      dailySchedules: [],
    }

    if (fetchedSchedule) {
      scheduleObject.dailySchedules.push(fetchedSchedule)
    }
    console.log("Checking drug interactions for schedule:", scheduleObject)
    const interactions = await checkDrugInteractions(userId!, scheduleObject)
    console.log("Drug Interactions found", interactions)
    setDrugInteractionsResponse(interactions)
  }

  useEffect(() => {
    const loadScheduleAndCheckInteractions = async () => {
      setLoading(true)
      setError(null)
      setDrugInteractionsResponse(null)
      try {
        const today = new Date().toISOString().slice(0, 10) // Get today's date in YYYY-MM-DD format
        console.log("Fetching Schedule for userId:", userId, "Date:", today)
        const fetchedSchedule = await getMedicationSchedule(userId!, today)
        console.log("Fetched Schedule:", fetchedSchedule)

        getDrugInteractionsResponse(fetchedSchedule)

        if (fetchedSchedule) {
          setSchedule({
            userId: userId!,
            dailySchedules: [fetchedSchedule],
          })
        } else {
          setSchedule(null)
        }
      } catch (e: any) {
        setError(
          e.message || "Failed to load schedule or check for drug interactions."
        )
      } finally {
        setLoading(false)
      }
    }

    loadScheduleAndCheckInteractions()
  }, [userId])

  if (loading) {
    return (
      <SafeAreaView style={styles.container}>
        <Text>Loading schedule and checking for interactions...</Text>
      </SafeAreaView>
    )
  }

  if (error) {
    return (
      <SafeAreaView style={styles.container}>
        <Text style={styles.errorText}>{error}</Text>
      </SafeAreaView>
    )
  }

  return (
    <SafeAreaView style={styles.container}>
      <ScrollView contentContainerStyle={styles.innerContainer}>
        <Text style={styles.title}>Your road to pillmaxxing.</Text>
        {drugInteractionsResponse &&
          drugInteractionsResponse.interactionsList &&
          drugInteractionsResponse.interactionsList.length > 0 && (
            <View style={styles.interactionContainer}>
              <Text style={styles.interactionTitle}>
                Drug Interactions Detected!
              </Text>
              {drugInteractionsResponse.interactionsList.map(
                (interaction, index) => (
                  <View key={index} style={styles.singleInteraction}>
                    <Text style={styles.interactionPair}>
                      {interaction.drugA} & {interaction.drugB}
                    </Text>
                    <Text style={styles.interactionLevel}>
                      Level: {interaction.level}
                    </Text>
                  </View>
                )
              )}
            </View>
          )}

        {drugInteractionsResponse &&
          drugInteractionsResponse.interactionsList &&
          drugInteractionsResponse.interactionsList.length === 0 && (
            <View style={styles.interactionContainer}>
              <Text style={styles.noInteraction}>
                No significant or dangerous drug interactions found. Yay!
              </Text>
            </View>
          )}

        {!schedule ||
        !schedule.dailySchedules ||
        schedule.dailySchedules.length === 0 ? (
          <Text>No schedule found for today! You're off the hook for now.</Text>
        ) : (
          schedule.dailySchedules.map((dailySchedule: any, index: any) => (
            <View key={index} style={styles.dailyScheduleContainer}>
              <Text style={styles.dateText}>{dailySchedule.date}</Text>
              {dailySchedule.scheduledDoses.map((dose: any, doseIndex: any) => (
                <View key={doseIndex} style={styles.doseContainer}>
                  <Text style={styles.timeText}>
                    {dose.timeOfDay.substring(0, 5)}
                  </Text>
                  {dose.medications.map((medication: any, medIndex: any) => (
                    <View key={medIndex} style={styles.medicationContainer}>
                      <Text style={styles.medicationName}>
                        {medication.medicationName}
                      </Text>
                      <Text style={styles.body}>
                        Dosage: {medication.dosage}
                      </Text>
                      <Text style={styles.body}>
                        Instructions: {medication.instructions}
                      </Text>
                    </View>
                  ))}
                </View>
              ))}
            </View>
          ))
        )}
      </ScrollView>
    </SafeAreaView>
  )
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    justifyContent: "center",
    alignItems: "center",
    backgroundColor: "#f9f9f9",
  },
  innerContainer: {
    alignItems: "flex-start",
    justifyContent: "flex-start",
    padding: 24,
  },
  title: {
    fontSize: 24,
    fontWeight: "600",
    color: "#333",
    alignSelf: "flex-start",
    marginBottom: 12,
  },
  interactionContainer: {
    marginBottom: 20,
    borderWidth: 1,
    borderColor: "#ddd",
    padding: 12,
    borderRadius: 5,
    width: "100%",
  },
  interactionTitle: {
    fontSize: 16,
    fontWeight: "bold",
    marginBottom: 10,
    color: "#cc0000",
  },
  singleInteraction: {
    marginBottom: 8,
    padding: 8,
    backgroundColor: "#fff",
    borderRadius: 4,
    borderColor: "#ddd",
    borderWidth: 1,
    width: "100%",
  },
  interactionPair: {
    fontWeight: "bold",
    marginBottom: 3,
  },
  interactionLevel: {
    fontSize: 14,
  },
  noInteraction: {
    fontStyle: "italic",
    color: "#156fe9",
  },
  dailyScheduleContainer: {
    marginBottom: 20,
    borderWidth: 1,
    borderColor: "#ddd",
    padding: 10,
    borderRadius: 4,
    width: "100%",
    gap: 8,
  },
  dateText: {
    fontSize: 18,
    fontWeight: "bold",
    marginBottom: 8,
  },
  doseContainer: {
    padding: 10,
    borderRadius: 4,
    color: "white",
    backgroundColor: "#156fe9",
  },
  timeText: {
    fontWeight: "bold",
    fontStyle: "italic",
    marginBottom: 5,
    color: "white",
  },
  medicationContainer: {
    marginBottom: 5,
    color: "white",
  },
  body: {
    color: "white",
  },
  medicationName: {
    fontWeight: "bold",
    color: "white",
  },
  errorText: {
    color: "red",
  },
})

export default MedicationScheduleScreen
