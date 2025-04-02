import React, { useState } from 'react';
import {
  View,
  Text,
  TextInput,
  TouchableOpacity,
  StyleSheet,
  ScrollView,
  SafeAreaView,
  Platform,
} from 'react-native';
import DateTimePicker from '@react-native-community/datetimepicker'; // For date pickers
import { useNavigation } from '@react-navigation/native'; // If using React Navigation

const PrescriptionFormScreen = () => {
  const [medicationName, setMedicationName] = useState('');
  const [dosage, setDosage] = useState('');
  const [startDate, setStartDate] = useState(new Date());
  const [endDate, setEndDate] = useState(new Date());
  const [instructions, setInstructions] = useState('');
  const [quantity, setQuantity] = useState('');
  const [frequency, setFrequency] = useState('');

  const [showStartDatePicker, setShowStartDatePicker] = useState(false);
  const [showEndDatePicker, setShowEndDatePicker] = useState(false);

  const navigation = useNavigation(); // If using React Navigation

  const handleStartDateChange = (event: any, selectedDate: Date | undefined) => {
    const currentDate = selectedDate || startDate;
    setShowStartDatePicker(Platform.OS === 'ios'); // Hide picker on iOS
    setStartDate(currentDate);
  };

  const handleEndDateChange = (event: any, selectedDate: Date | undefined) => {
    const currentDate = selectedDate || endDate;
    setShowEndDatePicker(Platform.OS === 'ios'); // Hide picker on iOS
    setEndDate(currentDate);
  };

  const handleSubmit = () => {
    const prescription = {
      medicationName,
      dosage,
      startDate: startDate.toISOString().split('T')[0], // Format to YYYY-MM-DD
      endDate: endDate.toISOString().split('T')[0],
      instructions,
      quantity,
      frequency,
    };
    console.log(prescription);
    // Here you would send the prescription data to your backend or store it locally.
    // navigation.navigate('PrescriptionConfirmation', { prescription }); // Example Navigation
  };

  return (
    <SafeAreaView style={styles.container}>
      <ScrollView>
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
        <TouchableOpacity style={styles.datePicker} onPress={() => setShowStartDatePicker(true)}>
          <Text>{startDate.toISOString().split('T')[0]}</Text>
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
        <TouchableOpacity style={styles.datePicker} onPress={() => setShowEndDatePicker(true)}>
          <Text>{endDate.toISOString().split('T')[0]}</Text>
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
  );
};

const styles = StyleSheet.create({
  container: {
    flex: 1,
    padding: 20,
  },
  label: {
    fontSize: 16,
    marginTop: 10,
  },
  input: {
    height: 40,
    borderColor: 'gray',
    borderWidth: 1,
    marginTop: 5,
    padding: 10,
  },
  datePicker: {
    height: 40,
    borderColor: 'gray',
    borderWidth: 1,
    marginTop: 5,
    padding: 10,
    justifyContent: 'center',
  },
  button: {
    backgroundColor: 'blue',
    padding: 15,
    borderRadius: 5,
    alignItems: 'center',
    marginTop: 20,
  },
  buttonText: {
    color: 'white',
    fontSize: 16,
  },
});

export default PrescriptionFormScreen;