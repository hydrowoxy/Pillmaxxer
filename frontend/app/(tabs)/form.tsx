import React, { useState } from "react"
import {
  View,
  Text,
  TextInput,
  StyleSheet,
  TouchableOpacity,
} from "react-native"
import { router, useLocalSearchParams } from "expo-router"

const MedicationFormScreen = () => {
  const { autofill } = useLocalSearchParams<{ autofill: string }>()
  const medication = autofill ? JSON.parse(autofill) : {}

  const [name, setName] = useState(medication?.name)
  const [dosage, setDosage] = useState(medication?.dosage)
  const [frequency, setFrequency] = useState(medication?.frequency)

  const handleSave = () => {
    const updatedMedication = { name, dosage, frequency }
    console.log("Updated Medication:", updatedMedication)
    router.push("/(tabs)")
  }

  return (
    <View style={styles.container}>
      <Text style={styles.title}>Confirm your prescription.</Text>
      <Text style={styles.subtitle}>
        We've filled out what we can parse from your image. Fill out the rest,
        or change anything that doesn't seem quite right!
      </Text>

      <Text style={styles.label}>Medication Name</Text>
      <TextInput value={name} onChangeText={setName} style={styles.input} />

      <Text style={styles.label}>Dosage (mg)</Text>
      <TextInput value={dosage} onChangeText={setDosage} style={styles.input} />

      <Text style={styles.label}>Frequency</Text>
      <TextInput
        value={frequency}
        onChangeText={setFrequency}
        style={styles.input}
      />

      <TouchableOpacity style={[styles.button]} onPress={handleSave}>
        <Text style={styles.buttonText}>Looks good to me!</Text>
      </TouchableOpacity>
    </View>
  )
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    justifyContent: "center",
    alignItems: "center",
    backgroundColor: "#f9f9f9",
    padding: 36,
    gap: 8,
    textTransform: "uppercase",
  },
  title: {
    fontSize: 24,
    fontWeight: "600",
    color: "#333",
    alignSelf: "flex-start",
    marginBottom: 16,
  },
  subtitle: {
    color: "#333",
    alignSelf: "flex-start",
    marginBottom: 16,
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
  },
  button: {
    marginTop: 20,
    backgroundColor: "#0066cc",
    paddingVertical: 12,
    paddingHorizontal: 24,
    borderRadius: 8,
  },
  buttonText: {
    color: "#fff",
    fontSize: 14,
    fontWeight: "500",
  },
})

export default MedicationFormScreen
