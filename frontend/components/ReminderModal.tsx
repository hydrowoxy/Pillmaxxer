import React, { useEffect, useRef } from 'react';
import {
  Modal,
  View,
  Text,
  Pressable,
  StyleSheet,
  Animated,
  TouchableWithoutFeedback,
} from 'react-native';
import { Reminder } from '@/types/Types';
import { useRouter } from 'expo-router';

interface ReminderModalProps {
  isVisible: boolean;
  setIsVisible: (isVisible: boolean) => void;
  reminder: Reminder | null;
}

const ReminderModal = ({ isVisible, setIsVisible, reminder }: ReminderModalProps) => {
  const slideAnim = useRef(new Animated.Value(0)).current;
  const modalHeight = 250;
  const router = useRouter();

  useEffect(() => {
    if (isVisible) {
      Animated.timing(slideAnim, {
        toValue: 1,
        duration: 500,
        useNativeDriver: true,
      }).start();
    } else {
      Animated.timing(slideAnim, {
        toValue: 0,
        duration: 500,
        useNativeDriver: true,
      }).start();
    }
  }, [isVisible, slideAnim]);

  const closeModal = () => {
    setIsVisible(false);
  };

  const logTimingInfo = () => {
    if (!reminder) return; // Ensure reminder exists before logging
    const now = new Date();
    const reminderTimeParts = reminder.scheduledDose.timeOfDay.split(':');
    const reminderDateParts = reminder.date.split('-');

    const reminderTime = new Date(
      parseInt(reminderDateParts[0], 10),
      parseInt(reminderDateParts[1], 10) - 1,
      parseInt(reminderDateParts[2], 10),
      parseInt(reminderTimeParts[0], 10),
      parseInt(reminderTimeParts[1], 10),
      parseInt(reminderTimeParts[2], 10)
    );
    console.log(reminderTime.getTime(), now.getTime());
    const timeDifference = reminderTime.getTime() - now.getTime();
    console.log("TIME INTERVAL BETWEEN SCHEDULED TIME AND ACTUAL REMINDER RECEIVED " + timeDifference);
  };

  const handleOverlayPress = () => {
    closeModal();
  };

  const handleGoToSchedule = () => {
    closeModal();
    router.push('/medication-schedule');
  };

  const translateY = slideAnim.interpolate({
    inputRange: [0, 1],
    outputRange: [modalHeight, 0],
  });

  if (!reminder) return null;

  // Log timing info before rendering modal
  logTimingInfo();

  return (
    <Modal visible={isVisible} transparent={true} animationType="none">
      <View style={styles.container}>
        <TouchableWithoutFeedback onPress={handleOverlayPress}>
          <View style={styles.modalOverlay} />
        </TouchableWithoutFeedback>

        <Animated.View style={[styles.modalContainer, { transform: [{ translateY }] }]}>
          <Text style={styles.modalTitle}>Medication Reminder</Text>
          <Text style={styles.modalText}>Date: {reminder.date}</Text>
          <Text style={styles.modalText}>Time: {reminder.scheduledDose.timeOfDay}</Text>
          <Text style={styles.modalText}>Medications: {reminder.scheduledDose.medications.map(med => med.medicationName).join(', ')}</Text>

          <View style={styles.modalButtonsContainer}>
            <Pressable onPress={handleGoToSchedule} style={styles.scheduleButton}>
              <Text style={styles.scheduleButtonText}>Go to Schedule</Text>
            </Pressable>
            <Pressable onPress={closeModal} style={styles.closeButton}>
              <Text style={styles.closeButtonText}>Close</Text>
            </Pressable>
          </View>
        </Animated.View>
      </View>
    </Modal>
  );
};

const styles = StyleSheet.create({
  container: {
    flex: 1,
    justifyContent: 'flex-end',
  },
  modalOverlay: {
    position: 'absolute',
    top: 0,
    left: 0,
    right: 0,
    bottom: 0,
    backgroundColor: 'rgba(0, 0, 0, 0.5)',
  },
  modalContainer: {
    backgroundColor: 'white',
    borderTopLeftRadius: 20,
    borderTopRightRadius: 20,
    padding: 24,
    paddingBottom: 32,
    alignItems: 'center',
    shadowColor: '#000',
    shadowOffset: { width: 0, height: -2 },
    shadowOpacity: 0.1,
    shadowRadius: 5,
    elevation: 5,
  },
  modalTitle: {
    fontSize: 18,
    fontWeight: '600',
    marginBottom: 12,
    color: 'black',
    textAlign: 'center',
  },
  modalText: {
    fontSize: 16,
    color: 'black',
    marginBottom: 10,
    textAlign: 'center',
  },
  modalButtonsContainer: {
    flexDirection: 'row',
    justifyContent: 'space-around',
    width: '100%',
    paddingHorizontal: 4,
  },
  scheduleButton: {
    paddingVertical: 12,
    paddingHorizontal: 20,
    borderRadius: 24,
    backgroundColor: 'blue',
    justifyContent: 'center',
    alignItems: 'center',
  },
  scheduleButtonText: {
    color: 'white',
    fontSize: 16,
    fontWeight: '500',
    textAlign: 'center',
  },
  closeButton: {
    paddingVertical: 12,
    paddingHorizontal: 20,
    borderRadius: 24,
    backgroundColor: 'white',
    justifyContent: 'center',
    alignItems: 'center',
  },
  closeButtonText: {
    color: 'black',
    fontSize: 16,
    fontWeight: '500',
    textAlign: 'center',
  },
});

export default ReminderModal;