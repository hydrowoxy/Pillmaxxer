import { render, userEvent, waitFor } from "@testing-library/react-native"
import * as ImagePicker from "expo-image-picker"
import ScanImage from "@/app/(tabs)/scan"
import { router } from "expo-router"

it("uploads an image and receives a response within 3 seconds", async () => {
  // mock image upload content
  jest.spyOn(ImagePicker, "requestCameraPermissionsAsync").mockResolvedValue({
    canAskAgain: true,
    expires: "never",
    granted: true,
    status: ImagePicker.PermissionStatus.GRANTED,
  })

  const testImage: ImagePicker.ImagePickerAsset = {
    uri: "file:///C:/Users/alina/Downloads/ocr-test-img.jpg",
    width: 350,
    height: 350,
    mimeType: "image/jpeg",
  }

  jest.spyOn(ImagePicker, "launchCameraAsync").mockResolvedValue({
    canceled: false,
    assets: [testImage],
  })

  // mock router reroute
  const routerSpy = jest.spyOn(router, "push").mockImplementation(jest.fn())

  // run UI interaction
  const user = userEvent.setup()
  const { getByTestId } = render(<ScanImage />)

  const takeButton = getByTestId("take-image-button")
  await user.press(takeButton)
  await waitFor(() => getByTestId("upload-image-button"))
  const uploadButton = getByTestId("upload-image-button")

  // this is the sequence we want to capture and calculate the time of
  const startTime: number = new Date().getTime()
  await user.press(uploadButton)
  await waitFor(() => expect(routerSpy).toHaveBeenCalled())
  const endTime: number = new Date().getTime()

  // calculate time difference
  const timeDifference: number = endTime - startTime
  console.log("time difference: ", timeDifference, "ms")

  expect(timeDifference).toBeLessThanOrEqual(3000)
})
