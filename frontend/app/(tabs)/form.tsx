import React, { useState } from 'react';
import { View, Text, TextInput, Button, StyleSheet, Alert, ScrollView, TouchableOpacity } from 'react-native';
import { useRouter } from 'expo-router';
import { addScheduleItem } from '@/context/schedule-context';

export default function Form() {
  const router = useRouter();
  
  const [drugName, setDrugName] = useState('');
  const [timeOfDay, setTimeOfDay] = useState('');
  const [quantity, setQuantity] = useState('');
  const [dosage, setDosage] = useState('');

  const handleSubmit = () => {
    if (!drugName || !timeOfDay || !quantity || !dosage) {
      Alert.alert('Error', 'Please fill out all fields.');
      return;
    }

    const newItem = {
      id: Date.now().toString(),
      drugName,
      timeOfDay,
      quantity,
      dosage,
    };

    console.log("Adding new schedule item:", newItem);
    addScheduleItem(newItem);
    Alert.alert('Success', 'Drug scheduled successfully!');
    router.push('/(tabs)/schedule'); 
  };

  const goBack = () => {
    router.back();
  };

  return (
    <ScrollView contentContainerStyle={styles.container}>
      <View style={styles.headerRow}>
        <TouchableOpacity onPress={goBack}>
          <Text style={styles.backArrow}>←</Text>
        </TouchableOpacity>
        <Text style={styles.header}>Schedule a Drug</Text>
      </View>

      <TextInput
        style={styles.input}
        placeholder="Drug Name"
        placeholderTextColor="#333"
        value={drugName}
        onChangeText={setDrugName}
      />

      <TextInput
        style={styles.input}
        placeholder="Time of Day (e.g., 08:00 AM)"
        placeholderTextColor="#333"
        value={timeOfDay}
        onChangeText={setTimeOfDay}
      />

      <TextInput
        style={styles.input}
        placeholder="Quantity"
        placeholderTextColor="#333"
        value={quantity}
        onChangeText={setQuantity}
        keyboardType="numeric"
      />

      <TextInput
        style={styles.input}
        placeholder="Dosage (e.g., 50mg)"
        placeholderTextColor="#333"
        value={dosage}
        onChangeText={setDosage}
      />

      <Button title="Submit" onPress={handleSubmit} />
    </ScrollView>
  );
}

const styles = StyleSheet.create({
  container: {
    flexGrow: 1,
    backgroundColor: '#fff',
    padding: 20,
    paddingTop: 50,
  },
  headerRow: {
    flexDirection: 'row',
    alignItems: 'center',
    marginBottom: 20,
  },
  backArrow: {
    fontSize: 24,
    color: '#007AFF',
    marginRight: 10,
  },
  header: {
    flex: 1,
    fontSize: 24,
    fontWeight: 'bold',
    color: '#333',
    textAlign: 'center',
  },
  input: {
    borderColor: '#ccc',
    borderWidth: 1,
    borderRadius: 8,
    padding: 10,
    marginBottom: 15,
    color: '#333',
  },
});
