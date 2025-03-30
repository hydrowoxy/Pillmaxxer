import { postFiles } from "./fetch"

const API_URL = process.env.EXPO_PUBLIC_API_URL ?? "http://localhost:8080" // for testing, when env not loaded

interface postImageUploadProps {
  params: {
    imageFile: string
  }
}

export const postImageUpload = async ({ params }: postImageUploadProps) => {
  const data = await postFiles({
    url: `${API_URL}/api/image-scan/upload`,
    body: params,
  })
  return data
}
