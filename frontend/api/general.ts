import { get, post } from "./fetch"

const API_URL = process.env.EXPO_PUBLIC_API_URL ?? "http://localhost:8080" // for testing, when env not loaded

export const getHealth = async () => {
  const data = await get({
    url: `${API_URL}/api/health`,
  })
  return data
}

export const getPatientData = async (patientId: string) => {
  const data = await get({
    url: `${API_URL}/api/patients/${patientId}`,
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
  const data = await post({
    url: `${API_URL}/api/patients/register`,
    body: patientData,
  })
  return data
};


