import React, { useState } from "react";
import {
  View,
  Text,
  TextInput,
  StyleSheet,
  TouchableOpacity,
} from "react-native";
import { router, useLocalSearchParams } from "expo-router";
import { useReminder } from "../../context/reminder-context";
import { createPrescription, createMedicationSchedule } from "../../api/general";
import { useAuth } from "@/context/auth-context";
import { Prescription } from "@/types/Types";

const MedicationFormScreen = () => {
  const { autofill } = useLocalSearchParams<{ autofill: string }>();
  const medication = autofill ? JSON.parse(autofill) : {};
  const { userId } = useAuth();
  const { fetchReminder } = useReminder();

  const [medicationName, setMedicationName] = useState(medication?.name || "");
  const [dosage, setDosage] = useState(medication?.dosage || "");
  const [frequency, setFrequency] = useState(medication?.frequency || "");
  const [startDate, setStartDate] = useState("");
  const [endDate, setEndDate] = useState("");
  const [instructions, setInstructions] = useState("");
  const [quantity, setQuantity] = useState("");
  const [submissionError, setSubmissionError] = useState<string | null>(null);
  const [loading, setLoading] = useState<boolean>(false);
  const [message, setMessage] = useState<string | null>(null);

  const handleSave = async () => {
    if (!userId) {
      console.error("User ID not available.");
      setSubmissionError("User ID not available.");
      setMessage("User ID not available.");
      return;
    }

    setLoading(true);
    setSubmissionError(null);
    setMessage("Creating prescription...");

    const prescriptionData: Prescription = {
      userId,
      medicationName,
      dosage,
      startDate,
      endDate,
      instructions,
      quantity,
      frequency,
    };

    try {
      console.log("Creating prescription with data:", prescriptionData);
      const createdPrescription = await createPrescription(userId, prescriptionData);
      console.log("Prescription created successfully:", createdPrescription);
      setMessage("Prescription created successfully. Creating schedule...");

      // Add a 1 second buffer
      await new Promise((resolve) => setTimeout(resolve, 2000));

      console.log("Creating a new schedule for this user: ", userId);
      const createdSchedule = await createMedicationSchedule(userId);
      console.log("Schedule created successfully:", createdSchedule);
      setMessage("Schedule created successfully. Updating reminders...");

      // Add a 1 second buffer
      await new Promise((resolve) => setTimeout(resolve, 2000));

      await fetchReminder(); // Fetch reminders again to update the context
      setMessage("Prescription and schedule updated!");

      setTimeout(() => {
        router.push("/(tabs)/medication-schedule");
      }, 2000); // Redirect after a short delay
    } catch (error: any) {
      console.error("Failed to create prescription:", error);
      setSubmissionError(
        error?.response?.data?.message ||
          error.message ||
          "Failed to create prescription."
      );
      setMessage(
        error?.response?.data?.message ||
          error.message ||
          "Failed to create prescription."
      );
    } finally {
      setLoading(false);
    }
  };

  return (
    <View style={styles.container}>
      <Text style={styles.title}>Submit your prescription data</Text>

      {message && (
        <Text style={{ color: "blue", marginBottom: 10 }}>
          {message}
        </Text>
      )}
      {submissionError && (
        <Text style={{ color: "red", marginBottom: 10 }}>
          Error: {submissionError}
        </Text>
      )}

      <Text style={styles.label}>Medication Name</Text>
      <TextInput
        value={medicationName}
        onChangeText={setMedicationName}
        style={styles.input}
      />

      <Text style={styles.label}>Dosage (e.g., 500mg, 50 mL)</Text>
      <TextInput value={dosage} onChangeText={setDosage} style={styles.input} />

      <Text style={styles.label}>Frequency (e.g., Once daily, Twice a day)</Text>
      <TextInput
        value={frequency}
        onChangeText={setFrequency}
        style={styles.input}
      />

      <Text style={styles.label}>Start Date (YYYY-MM-DD)</Text>
      <TextInput
        value={startDate}
        onChangeText={setStartDate}
        style={styles.input}
        placeholder="YYYY-MM-DD"
      />

      <Text style={styles.label}>End Date (YYYY-MM-DD)</Text>
      <TextInput
        value={endDate}
        onChangeText={setEndDate}
        style={styles.input}
        placeholder="YYYY-MM-DD"
      />

      <Text style={styles.label}>Instructions (e.g., Take with food)</Text>
      <TextInput
        value={instructions}
        onChangeText={setInstructions}
        style={styles.input}
      />

      <Text style={styles.label}>Quantity (e.g., 2 tablets, 1 tablespoon)</Text>
      <TextInput
        value={quantity}
        onChangeText={setQuantity}
        style={styles.input}
      />

      <TouchableOpacity
        style={[styles.button, loading && styles.buttonDisabled]}
        onPress={handleSave}
        disabled={loading}
      >
        <Text style={styles.buttonText}>
          {loading ? "Saving..." : "Looks good to me!"}
        </Text>
      </TouchableOpacity>
    </View>
  );
};

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
    marginTop: 10,
    marginBottom: 5,
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
    backgroundColor: "#156fe9",
    paddingVertical: 12,
    paddingHorizontal: 30,
    borderRadius: 30,
  },
  buttonDisabled: {
    backgroundColor: "#ccc",
  },
  buttonText: {
    color: "#fff",
    fontSize: 14,
    fontWeight: "500",
  },
})

export default MedicationFormScreen;
