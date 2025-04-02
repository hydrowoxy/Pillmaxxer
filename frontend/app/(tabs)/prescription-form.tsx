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
import DateTimePicker from "@react-native-community/datetimepicker" // For date pickers
import { useNavigation } from "@react-navigation/native" // If using React Navigation

const PrescriptionFormScreen = () => {
  const [medicationName, setMedicationName] = useState("")
  const [dosage, setDosage] = useState("")
  const [startDate, setStartDate] = useState(new Date())
  const [endDate, setEndDate] = useState(new Date())
  const [instructions, setInstructions] = useState("")
  const [quantity, setQuantity] = useState("")
  const [frequency, setFrequency] = useState("")

  const [showStartDatePicker, setShowStartDatePicker] = useState(false)
  const [showEndDatePicker, setShowEndDatePicker] = useState(false)

  const navigation = useNavigation() // If using React Navigation

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

  const handleSubmit = () => {
    const prescription = {
      medicationName,
      dosage,
      startDate: startDate.toISOString().split("T")[0], // Format to YYYY-MM-DD
      endDate: endDate.toISOString().split("T")[0],
      instructions,
      quantity,
      frequency,
    }
    console.log(prescription)
    // Here you would send the prescription data to your backend or store it locally.
    // navigation.navigate('PrescriptionConfirmation', { prescription }); // Example Navigation
  }

  return (
    <SafeAreaView style={styles.container}>
      <ScrollView contentContainerStyle={styles.innerContainer}>
        <Text style={styles.title}>Track a new prescription.</Text>
        <Text style={styles.subtitle}>
          Fill out as much as you know and we'll take care of the rest.
        </Text>

        <Text style={styles.label}>Medication Name</Text>
        <TextInput
          style={styles.input}
          value={medicationName}
          onChangeText={setMedicationName}
        />

        <Text style={styles.label}>Dosage (mg)</Text>
        <TextInput
          style={styles.input}
          value={dosage}
          onChangeText={setDosage}
          keyboardType="numeric"
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

        <Text style={styles.label}>Quantity (pills)</Text>
        <TextInput
          style={styles.input}
          value={quantity}
          onChangeText={setQuantity}
          keyboardType="numeric"
        />

        <Text style={styles.label}>Frequency (times/day)</Text>
        <TextInput
          style={styles.input}
          value={frequency}
          onChangeText={setFrequency}
          keyboardType="numeric"
        />

        <TouchableOpacity style={styles.button} onPress={handleSubmit}>
          <Text style={styles.buttonText}>Submit</Text>
        </TouchableOpacity>
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

export default PrescriptionFormScreen
