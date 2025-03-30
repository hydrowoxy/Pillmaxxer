import { get, post, postFiles } from "./fetch"

const API_URL = process.env.EXPO_PUBLIC_API_URL ?? "localhost:8080" // for FE testing

// THIS IS JUST A DUMMY GET
interface dummyGetProps {
  params: { postId?: string }
}

export const dummyGet = async ({ params }: dummyGetProps) => {
  const data = await get({
    url: "https://jsonplaceholder.typicode.com/comments",
    params: params,
  })
  return data
}

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
