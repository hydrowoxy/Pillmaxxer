import React, { useState, useEffect } from 'react';
import { 
  View, 
  Text, 
  StyleSheet, 
  SafeAreaView, 
  TouchableOpacity,
  StatusBar,
  ScrollView
} from 'react-native';
import { getPatientData } from '../../api/general';
import { useAuth } from '@/context/auth-context';

const ProfileScreen = () => {
  const [patientData, setPatientData] = useState<any>(null);
  const { user, signOut } = useAuth(); // Get user and logout function

  useEffect(() => {
    const fetchPatientData = async () => {
      if (user && user.uid) {
        try {
          const data = await getPatientData(user.uid);
          setPatientData(data);
        } catch (error) {
          console.error("Failed to fetch patient data:", error);
        }
      } else {
        setPatientData(null);
      }
    };

    fetchPatientData();
  }, [user]);

  const handleLogout = async () => {
    try {
      await signOut();
    } catch (error) {
      console.error("Logout failed:", error);
    }
  };

  if (!user) {
    return (
      <SafeAreaView>
        <View style={styles.container}>
          <Text>Please sign in to view your profile.</Text>
        </View>
      </SafeAreaView>
    );
  }

  if (!patientData) {
    return (
      <View style={styles.container}>
        <Text>Loading profile data...</Text>
      </View>
    );
  }

  return (
    <SafeAreaView style={styles.container}>
      <StatusBar barStyle="dark-content" />
      <ScrollView>
        <Text>Email: {patientData.email}</Text>
        <Text>First Name: {patientData.firstName}</Text>
        <Text>Last Name: {patientData.lastName}</Text>
        <Text>Phone Number: {patientData.phoneNumber}</Text>
        <TouchableOpacity style={styles.logoutButton} onPress={handleLogout}>
          <Text style={styles.logoutButtonText}>Logout</Text>
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
  logoutButton: {
    backgroundColor: '#e74c3c', // Red color for logout
    padding: 15,
    borderRadius: 5,
    alignItems: 'center',
    marginTop: 20,
  },
  logoutButtonText: {
    color: 'white',
    fontWeight: 'bold',
  },
});

export default ProfileScreen;