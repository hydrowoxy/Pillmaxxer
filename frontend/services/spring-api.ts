/**
 * API client for communicating with the Spring Boot backend
 */

import axios from 'axios';

const API_BASE_URL = 'http://192.168.2.34:8080/api'; // Replace X with your ip address

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

/**
 * Fetches patient data
 */
export const getPatientData = async (patientId: string) => {
  try {
    const response = await api.get(`/patients/${patientId}`);
    return response.data;
  } catch (error) {
    throw error;
  }
};

export default api;