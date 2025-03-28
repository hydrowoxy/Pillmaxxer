import { get, post } from "./util"

const API_URL = process.env.EXPO_API_URL

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
    imageUri: string
  }
}

export const postImageUpload = async ({ params }: postImageUploadProps) => {
  const img = await fetch(params.imageUri)
  const blob = await img.blob()

  const formData = new FormData()
  formData.append("image", blob, "image/jpeg")

  const data = await post({
    url: `${API_URL}/api/image-scan/upload`,
    body: params,
    formData: true,
  })

  return data
}
