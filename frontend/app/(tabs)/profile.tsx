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

const primaryBlue = '#156fe9'; // Define the primary blue color

const ProfileScreen = () => {
  const [patientData, setPatientData] = useState<any>(null);
  const { user, signOut } = useAuth();

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
      <SafeAreaView style={styles.blueContainer}>
        <View style={styles.centeredInnerContainer}>
          <Text style={styles.whiteBody}>Please sign in to view your profile.</Text>
        </View>
      </SafeAreaView>
    );
  }

  if (!patientData) {
    return (
      <SafeAreaView style={styles.blueContainer}>
        <View style={styles.centeredInnerContainer}>
          <Text style={styles.whiteBody}>Loading profile data...</Text>
        </View>
      </SafeAreaView>
    );
  }

  return (
    <SafeAreaView style={styles.blueContainer}>
      <StatusBar barStyle="light-content" backgroundColor={primaryBlue} />
      <View style={styles.scrollContainer}>
        <ScrollView contentContainerStyle={styles.centeredInnerContainer}>
          <Text style={styles.whiteTitle}>Your Profile</Text>
          <View style={styles.infoContainer}>
            <Text style={styles.whiteLabel}>Email:</Text>
            <Text style={styles.whiteBody}>{patientData.email}</Text>
          </View>
          <View style={styles.infoContainer}>
            <Text style={styles.whiteLabel}>First Name:</Text>
            <Text style={styles.whiteBody}>{patientData.firstName}</Text>
          </View>
          <View style={styles.infoContainer}>
            <Text style={styles.whiteLabel}>Last Name:</Text>
            <Text style={styles.whiteBody}>{patientData.lastName}</Text>
          </View>
          <View style={styles.infoContainer}>
            <Text style={styles.whiteLabel}>Phone Number:</Text>
            <Text style={styles.whiteBody}>{patientData.phoneNumber}</Text>
          </View>
        </ScrollView>
      </View>
      <View style={styles.bottomButtonContainer}>
        <TouchableOpacity style={styles.logoutButton} onPress={handleLogout}>
          <Text style={styles.logoutButtonText}>Logout</Text>
        </TouchableOpacity>
      </View>
    </SafeAreaView>
  );
};

const styles = StyleSheet.create({
  blueContainer: {
    flex: 1,
    backgroundColor: primaryBlue,
  },
  scrollContainer: {
    flex: 1, // Take up most of the vertical space
  },
  centeredInnerContainer: {
    padding: 24,
    width: '100%',
    alignItems: 'flex-start',
  },
  whiteTitle: {
    fontSize: 24,
    fontWeight: '600',
    color: 'white',
    marginBottom: 20,
  },
  infoContainer: {
    flexDirection: 'row',
    alignItems: 'center',
    marginBottom: 15,
    width: '100%',
  },
  whiteLabel: {
    fontWeight: 'bold',
    color: 'white',
    marginRight: 10,
    width: 120,
  },
  whiteBody: {
    fontSize: 16,
    color: 'white',
    flexShrink: 1,
  },
  bottomButtonContainer: {
    backgroundColor: primaryBlue, // Ensure the button container has the same background
    paddingVertical: 15,
    paddingHorizontal: 20,
    borderTopWidth: StyleSheet.hairlineWidth, // Subtle separator
    borderTopColor: 'rgba(255, 255, 255, 0.3)',
  },
  logoutButton: {
    backgroundColor: primaryBlue,
    padding: 12,
    borderRadius: 8,
    borderWidth: 1,
    borderColor: 'white',
    alignItems: 'center',
    width: '100%',
  },
  logoutButtonText: {
    color: 'white',
    fontWeight: 'bold',
    fontSize: 16,
  },
});

export default ProfileScreen;