export const isImageUri = (uri: string) => {
  return /\.(png|jpg|jpeg|gif|bmp|webp)$/i.test(uri)
}
