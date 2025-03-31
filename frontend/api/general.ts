import { get } from "./fetch"

const API_URL = process.env.EXPO_PUBLIC_API_URL ?? "http://localhost:8080" // for testing, when env not loaded

export const getHealth = async () => {
  const data = await get({
    url: `${API_URL}/api/health`,
  })
  return data
}
