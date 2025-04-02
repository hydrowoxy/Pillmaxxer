export const getFileType = (uri: string) => {
  const filename = uri.split("/").pop() || ""
  const match = /\.(\w+)$/.exec(filename)
  const type = match ? `image/${match[1]}` : `image`
  return type
}

export const getLocalFileBlob = async (path: string) => {
  return "This is not available on mobile. You are probably in the wrong environment."
}
