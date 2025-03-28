export const isImageUri = (uri: string) => {
  console.log("is uri?" + /\.(png|jpg|jpeg|gif|bmp|webp)$/i.test(uri))
  return /\.(png|jpg|jpeg|gif|bmp|webp)$/i.test(uri)
}

export const convertUriToBlob = async (uri: string) => {
  const img = await fetch(uri)
  const blob = await img.blob()
  return blob
  //   const [header, base64String] = uri.split(",")
  //   const mime = header.match(/:(.*?);/)?.[1]
  //   const binaryString = atob(base64String)

  //   const uint8Array = new Uint8Array(binaryString.length)
  //   for (let i = 0; i < binaryString.length; i++) {
  //     uint8Array[i] = binaryString.charCodeAt(i)
  //   }

  //   return new Blob([uint8Array], { type: mime })
}
