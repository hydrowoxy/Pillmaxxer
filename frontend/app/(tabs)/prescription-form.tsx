import React, { useState } from "react"
import {
  View,
  Text,
  TextInput,
  TouchableOpacity,
  StyleSheet,
  ScrollView,
  SafeAreaView,
  Platform,
} from "react-native"
import DateTimePicker from "@react-native-community/datetimepicker"
import { router, useLocalSearchParams } from "expo-router"
import { useReminder } from "../../context/reminder-context"
import { createPrescription, createMedicationSchedule } from "../../api/general"
import { useAuth } from "@/context/auth-context"
import { Prescription } from "@/types/Types"

const MedicationFormScreen = () => {
  const { autofill } = useLocalSearchParams<{ autofill: string }>()
  const medication = autofill ? JSON.parse(autofill) : {}

  const { userId } = useAuth()
  const { fetchReminder } = useReminder()

  const [medicationName, setMedicationName] = useState(
    medication?.medicationName || ""
  )
  const [dosage, setDosage] = useState(medication?.dosage || "")
  const [frequency, setFrequency] = useState(medication?.frequency || "")
  const [startDate, setStartDate] = useState(new Date())
  const [endDate, setEndDate] = useState(new Date())
  const [instructions, setInstructions] = useState("")
  const [quantity, setQuantity] = useState(medication?.quantity || "")
  const [submissionError, setSubmissionError] = useState<string | null>(null)
  const [loading, setLoading] = useState<boolean>(false)
  const [message, setMessage] = useState<string | null>(null)

  const [showStartDatePicker, setShowStartDatePicker] = useState(false)
  const [showEndDatePicker, setShowEndDatePicker] = useState(false)

  const handleStartDateChange = (
    event: any,
    selectedDate: Date | undefined
  ) => {
    const currentDate = selectedDate || startDate
    setShowStartDatePicker(Platform.OS === "ios") // Hide picker on iOS
    setStartDate(currentDate)
  }

  const handleEndDateChange = (event: any, selectedDate: Date | undefined) => {
    const currentDate = selectedDate || endDate
    setShowEndDatePicker(Platform.OS === "ios") // Hide picker on iOS
    setEndDate(currentDate)
  }

  const handleSave = async () => {
    if (!userId) {
      console.error("User ID not available.")
      setSubmissionError("User ID not available.")
      setMessage("User ID not available.")
      return
    }

    setLoading(true)
    setSubmissionError(null)
    setMessage("Creating prescription...")

    const prescriptionData: Prescription = {
      userId,
      medicationName,
      dosage,
      startDate: startDate.toISOString().split("T")[0], // Format to YYYY-MM-DD
      endDate: endDate.toISOString().split("T")[0],
      instructions,
      quantity,
      frequency,
    }

    try {
      console.log("Creating prescription with data:", prescriptionData)
      const createdPrescription = await createPrescription(
        userId,
        prescriptionData
      )
      console.log("Prescription created successfully:", createdPrescription)
      setMessage("Prescription created successfully. Creating schedule...")

      // Add a 1 second buffer
      await new Promise((resolve) => setTimeout(resolve, 2000))

      console.log("Creating a new schedule for this user: ", userId)
      const createdSchedule = await createMedicationSchedule(userId)
      console.log("Schedule created successfully:", createdSchedule)
      setMessage("Schedule created successfully. Updating reminders...")

      // Add a 1 second buffer
      await new Promise((resolve) => setTimeout(resolve, 2000))

      await fetchReminder() // Fetch reminders again to update the context
      setMessage("Prescription and schedule updated!")

      setTimeout(() => {
        router.push("/(tabs)/medication-schedule")
      }, 2000) // Redirect after a short delay
    } catch (error: any) {
      console.error("Failed to create prescription:", error)
      setSubmissionError(
        error?.response?.data?.message ||
          error.message ||
          "Failed to create prescription."
      )
      setMessage(
        error?.response?.data?.message ||
          error.message ||
          "Failed to create prescription."
      )
    } finally {
      setLoading(false)
    }
  }

  return (
    <SafeAreaView style={styles.container}>
      <ScrollView contentContainerStyle={styles.innerContainer}>
        <Text style={styles.title}>Track a new prescription.</Text>
        <Text style={styles.subtitle}>
          {autofill
            ? "Fill out all the fields below!"
            : "We've filled out what we can parse from your image. Fill out the rest, or change anything that doesn't seem quite right!"}
        </Text>

        <Text style={styles.label}>Medication Name</Text>
        <TextInput
          style={styles.input}
          value={medicationName}
          onChangeText={setMedicationName}
        />

        <Text style={styles.label}>Dosage (e.g., mg, mL)</Text>
        <TextInput
          style={styles.input}
          value={dosage}
          onChangeText={setDosage}
        />

        <Text style={styles.label}>Start Date</Text>
        <TouchableOpacity
          style={styles.datePicker}
          onPress={() => setShowStartDatePicker(true)}
        >
          <Text>{startDate.toISOString().split("T")[0]}</Text>
        </TouchableOpacity>
        {showStartDatePicker && (
          <DateTimePicker
            value={startDate}
            mode="date"
            display="default"
            onChange={handleStartDateChange}
          />
        )}

        <Text style={styles.label}>End Date</Text>
        <TouchableOpacity
          style={styles.datePicker}
          onPress={() => setShowEndDatePicker(true)}
        >
          <Text>{endDate.toISOString().split("T")[0]}</Text>
        </TouchableOpacity>
        {showEndDatePicker && (
          <DateTimePicker
            value={endDate}
            mode="date"
            display="default"
            onChange={handleEndDateChange}
          />
        )}

        <Text style={styles.label}>Instructions</Text>
        <TextInput
          style={styles.input}
          value={instructions}
          onChangeText={setInstructions}
          multiline
        />

        <Text style={styles.label}>Quantity (e.g., # tablets or tbsp)</Text>
        <TextInput
          style={styles.input}
          value={quantity}
          onChangeText={setQuantity}
        />

        <Text style={styles.label}>Frequency (e.g., once or twice daily)</Text>
        <TextInput
          style={styles.input}
          value={frequency}
          onChangeText={setFrequency}
        />

        <TouchableOpacity style={styles.button} onPress={handleSave}>
          <Text style={styles.buttonText}>Looks good to me!</Text>
        </TouchableOpacity>

        {message && (
          <Text style={{ color: "blue", marginVertical: 10 }}>{message}</Text>
        )}
        {submissionError && (
          <Text style={{ color: "red", marginVertical: 10 }}>
            Error: {submissionError}
          </Text>
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
    alignItems: "center",
    justifyContent: "flex-start",
    padding: 36,
  },
  title: {
    fontSize: 24,
    fontWeight: "600",
    color: "#333",
    alignSelf: "flex-start",
    marginBottom: 12,
  },
  subtitle: {
    color: "#333",
    alignSelf: "flex-start",
    marginBottom: 24,
  },
  label: {
    fontWeight: "bold",
    alignSelf: "flex-start",
  },
  input: {
    width: "100%",
    borderWidth: 1,
    padding: 8,
    borderColor: "#ddd",
    borderRadius: 8,
    marginBottom: 16,
  },
  datePicker: {
    height: 40,
    borderWidth: 1,
    padding: 8,
    borderColor: "#ddd",
    marginTop: 5,
    justifyContent: "center",
    marginBottom: 16,
  },
  button: {
    paddingVertical: 12,
    paddingHorizontal: 30,
    backgroundColor: "#156fe9",
    borderRadius: 30,
  },
  buttonText: {
    fontWeight: "600",
    color: "#fff",
    textAlign: "center",
  },
})

export default MedicationFormScreen
