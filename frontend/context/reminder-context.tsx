import React, { createContext, useState, useEffect, useContext } from 'react';
import { getReminder } from '@/api/general';
import { useAuth } from '@/context/auth-context';
import { Reminder } from '@/types/Reminder';

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
    const now = new Date();
    const reminderTime = new Date(reminder.date + "T" + reminder.scheduledDose[0].timeOfDay + ":00"); // Assuming timeOfDay is in HH:mm format
    const timeDifference = reminderTime.getTime() - now.getTime();

    if (timeDifference >= -5000 && timeDifference <= 5000) { // within 5 seconds
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