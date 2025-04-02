import { Reminder, ReminderResponse } from "@/types/Reminder";
import { get, post } from "./fetch"

import axios from 'axios';

const API_BASE_URL = process.env.EXPO_PUBLIC_API_URL; 

// Configure axios instance
const api = axios.create({
  baseURL: API_BASE_URL,
  timeout: 15000, // 15 second timeout
  headers: {
    'Content-Type': 'application/json',
  },
});

/**
 * Registers a new patient with the backend including Expo push token
 */
export const registerPatient = async (patientData: {
  email: string;
  password: string;
  firstName: string;
  lastName: string;
  phoneNumber: string;
  deviceToken: string[];
}) => {
  try {
    console.log(patientData);
    // Register patient with push token
    const response = await api.post('/patients/register', patientData);

    return response.data;
  } catch (error) {
    throw error;
  }
};

// export const getHealth = async () => {
//   const data = await get({
//     url: `${API_URL}/api/health`,
//   })
//   return data
// }

export const getPatientData = async (userId: string) => {
  console.log("Fetching patient data for userId:", userId);
  try {
    const response = await api.get(`/patients/${userId}`);
    return response.data;
  } catch (error) {
    console.error("Error fetching patient data:", error);
    throw error;
  }
};

export const getReminder = async (userId: string) : Promise<ReminderResponse> => {
  console.log("Fetching reminder for userId: ", userId);
  const data = await api.get(`/patients/${userId}/reminders`);
  const reminderData: ReminderResponse = data.data; 
  console.log("Fetched reminder data: ", reminderData);
  return reminderData;
};


