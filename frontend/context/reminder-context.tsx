import React, { createContext, useState, useEffect, useContext } from 'react';
import { getReminder } from '@/api/general';
import { useAuth } from '@/context/auth-context';
import { Reminder } from '@/types/Types';

interface ReminderContextType {
  reminder: Reminder | null;
  fetchReminder: () => Promise<void>;
  isReminderModalVisible: boolean;
  reminderModalContent: Reminder | null; // Content of the modal
  setReminderModalVisible: (visible: boolean) => void;
}

const ReminderContext = createContext<ReminderContextType | undefined>(undefined);

export const ReminderProvider: React.FC<{ children: React.ReactNode }> = ({ children }) => {
  const [reminder, setReminder] = useState<Reminder | null>(null);
  const [pollInterval, setPollInterval] = useState<number>(3600000); // 1 hour polling as default
  const [isReminderModalVisible, setReminderModalVisible] = useState<boolean>(false);
  const [reminderModalContent, setReminderModalContent] = useState<any | null>(null);
  const { userId } = useAuth();

  const fetchReminder = async () => {
    try {
      if (userId) {
        console.log(`Fetching reminder for userId: ${userId}`);
        const data = await getReminder(userId);
        setReminder(data.reminder);
        setPollInterval(data.pollInterval || 3600000); // Update poll interval if provided
        checkReminder(data.reminder);
      }
    } catch (error) {
      console.error('Error fetching reminders:', error);
    }
  };

  const checkReminder = (reminder: Reminder) => {
    console.log("Checking reminder:", reminder);
    const now = new Date();
    // Assuming reminder.scheduledDose.timeOfDay is in HH:mm:ss format
    const reminderTimeParts = reminder.scheduledDose.timeOfDay.split(':');
    const reminderDateParts = reminder.date.split('-');

    // Create a new Date object with the reminder's date and time
    const reminderTime = new Date(
      parseInt(reminderDateParts[0], 10), // Year
      parseInt(reminderDateParts[1], 10) - 1, // Month (0-indexed)
      parseInt(reminderDateParts[2], 10), // Day
      parseInt(reminderTimeParts[0], 10), // Hours
      parseInt(reminderTimeParts[1], 10), // Minutes
      parseInt(reminderTimeParts[2], 10)  // Seconds
    );
    console.log(reminderTime.getTime(), now.getTime());
    const timeDifference = reminderTime.getTime() - now.getTime();
    console.log("Time difference for next reminder "+ timeDifference);
    if (timeDifference >= -5000 && timeDifference <= 5000) { // within 5 seconds
      console.log("Reminder is within 5 seconds of the current time.");
      setReminderModalContent(reminder);
      setReminderModalVisible(true);
    }
  };

  useEffect(() => {
    fetchReminder(); // Initial fetch

    // this function calls the fetchReminder function every pollInterval milliseconds
    const intervalId = setInterval(fetchReminder, pollInterval);

    return () => clearInterval(intervalId);
  }, [userId, pollInterval]);

  const value: ReminderContextType = {
    reminder,
    fetchReminder,
    isReminderModalVisible,
    reminderModalContent,
    setReminderModalVisible,
  };

  return <ReminderContext.Provider value={value}>{children}</ReminderContext.Provider>;
};

export const useReminder = () => {
  const context = useContext(ReminderContext);
  if (!context) {
    throw new Error('useReminder must be used within a ReminderProvider');
  }
  return context;
};