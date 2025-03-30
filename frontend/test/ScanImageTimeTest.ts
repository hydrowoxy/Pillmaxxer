// import { render, fireEvent, waitFor } from "@testing-library/react-native"
// import * as ImagePicker from "expo-image-picker"
// import ScanImage from "@/app/(tabs)/scan-image"

// test("uploads an actual image and calculates response time", async () => {
//   // const { getByTestId } = render(<ScanImage />)

//   // mock camera request
//   ;(ImagePicker.requestCameraPermissionsAsync as jest.Mock).mockResolvedValue({
//     status: "granted",
//   })

//   const testImage: ImagePicker.ImagePickerAsset = {
//     uri: "file:///path/to/your/test-image.jpg",
//     width: 350,
//     height: 350,
//     mimeType: "image/jpeg",
//   }

//   // mock upload test image
//   jest.spyOn(ImagePicker, "launchImageLibraryAsync").mockResolvedValue({
//     canceled: false,
//     assets: [testImage],
//   })

//   const uploadButton = getByTestId("upload-image-button")

//   const startTime: number = new Date().getTime()
//   fireEvent.press(uploadButton)
//   console.log("well, i did SOMETHING at " + startTime)

//   //   const endTime: number = new Date().getTime()

//   //   const timeDifference: number = endTime - startTime
//   //   console.log("Time difference: ", timeDifference, "ms")

//   //   expect(timeDifference).toBeGreaterThan(0)
//   //   expect(responseBody.success).toBe(true)
// })
