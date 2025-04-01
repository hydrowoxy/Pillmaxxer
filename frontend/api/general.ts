import { get, post } from "./fetch"

const API_URL = process.env.EXPO_PUBLIC_API_URL ?? "http://localhost:8080" // for testing, when env not loaded

export const getHealth = async () => {
  const data = await get({
    url: `${API_URL}/api/health`,
  })
  return data
}

export const getPatientData = async (userId: string) => {
  console.log("Fetching patient data for userId:", userId);
  const data = await get({
    url: `${API_URL}/api/patients/${userId}`,
  })
  return data
}

export const registerPatient = async (patientData: {
  email: string;
  password: string;
  firstName: string;
  lastName: string;
  phoneNumber: string;
  deviceToken: string[];
}) => {
  console.log("Registering patient with data:", patientData);
  const data = await post({
    url: `${API_URL}/api/patients/register`,
    body: patientData,
  })
  console.log("Patient registered:", data);
  return data
};

export const getReminder = async (userId: string) => {
  console.log("Fetching reminder for userId:", userId);
  const data = await get({
    url: `${API_URL}/api/patients/${userId}/reminders`,
  });
  console.log("Fetched reminder data:", data);
  return data;
};


